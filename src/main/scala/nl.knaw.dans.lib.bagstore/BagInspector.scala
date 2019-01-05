/**
 * Copyright (C) 2018 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.lib.bagstore

import java.nio.file.Path
import java.util.concurrent.{ CountDownLatch, ExecutorService }
import java.util.{ ArrayList => JArrayList }

import better.files.File
import gov.loc.repository.bagit.domain.{ Bag, Manifest => BagitManifest }
import gov.loc.repository.bagit.exceptions._
import gov.loc.repository.bagit.reader.BagReader
import gov.loc.repository.bagit.verify.{ BagVerifier, CheckManifestHashesTask }
import nl.knaw.dans.lib.bagstore.BagInspector.bagReader
import nl.knaw.dans.lib.error._
import nl.knaw.dans.lib.logging.DebugEnhancedLogging

import scala.collection.JavaConverters._
import scala.collection.immutable.Stream.Empty
import scala.util.{ Failure, Success, Try }

private[bagstore] object BagInspector extends DebugEnhancedLogging {
  private val bagReader = new BagReader
  private val bagVerifier = new BagVerifier()

  private def verifyValid(bag: Bag): Try[Either[String, Unit]] = Try {
    trace(())
    bagVerifier.isValid(bag, false)
  }.map(_ => Right(()))
    .recover {
      /*
       * Any of these (unfortunately unrelated) exception types mean that the bag is non-valid. The reason is captured in the
       * exception. Any other (non-fatal) exception type means the verification process itself failed;
       * this should lead to a Failure. (Btw fatal errors will NOT be wrapped in a Failure by above Try block!)
       */
      case cause: MissingPayloadManifestException => Left(cause.getMessage)
      case cause: MissingBagitFileException => Left(cause.getMessage)
      case cause: MissingPayloadDirectoryException => Left(cause.getMessage)
      case cause: FileNotInPayloadDirectoryException => Left(cause.getMessage)
      case cause: MaliciousPathException => Left(cause.getMessage)
      case cause: CorruptChecksumException => Left(cause.getMessage)
      case cause: VerificationException => Left(cause.getMessage)
      case cause: UnsupportedAlgorithmException => Left(cause.getMessage)
      case cause: InvalidBagitFileFormatException => Left(cause.getMessage)
    }
}

/**
 * Inspects the contents of a bag. This is a helper class that contains low-level functions. It only deals with the
 * bag properties specified by the BagIt specs. Particularly, it does not know about the bag store in the context of which a bag
 * may be processed.
 *
 * @param bagFile the bag to inspect.
 */
private[bagstore] case class BagInspector(bagFile: File) {

  private lazy val triedBag: Try[Bag] = Try {
    bagReader.read(bagFile.path)
  }

  /**
   * Verifies that the bag is valid, according to the BagIt specs. If the verification process succeeded
   * the result is a `Right` (meaning the bag is valid) or a `Left` (meaning it is non-valid). If it is
   * a `Left`, the results contains the reason why the bag is non-valid. If the verification process itself
   * failed the result is a `Failure`
   *
   * @return if successful the result of the validation
   */
  def verifyValid: Try[Either[String, Unit]] = {
    for {
      bag <- triedBag
      result <- BagInspector.verifyValid(bag)
    } yield result
  }

  def hasValidTagManifests(bagDir: File): Try[Either[String, Unit]] = {
    def runTasks(tagManifest: BagitManifest)(executor: ExecutorService): Try[Boolean] = {
      val values = tagManifest.getFileToChecksumMap
      val exc = new JArrayList[Exception]()
      val latch = new CountDownLatch(values.size())

      for (entry <- values.entrySet().asScala) {
        executor.execute(new CheckManifestHashesTask(entry, tagManifest.getAlgorithm.getMessageDigestName, latch, exc))
      }

      latch.await()

      exc.asScala.toList match {
        case Nil => Success(true)
        case (_: CorruptChecksumException) :: _ => Success(false)
        case e :: _ => Failure(new VerificationException(e))
      }
    }

    triedBag
      .map(_.getTagManifests.asScala.toStream
        .map(
          manifest => (manifest, runTasks(manifest)(BagInspector.bagVerifier.getExecutor).unsafeGetOrThrow))
        .collect { case (manifest, false) => manifest.getAlgorithm.getMessageDigestName.toLowerCase } match {
        case Empty => Right(())
        case fails => Left("The following tagmanifests were invalid: " + fails.mkString("[", ", ", "]"))
      })
  }

  /**
   * Returns a map from bag relative path to [[FetchItem]] object. If there are multiple fetch items with the
   * same target path it is undetermined to which one the path will be mapped.
   *
   * @return the fetch items in this bag's `fetch.txt`
   */
  def getPathsToFetchItems: Try[Map[Path, FetchItem]] = {
    for {
      items <- getFetchItems
      pathToItem <- Try { items.map(fi => (fi.path, fi)).toMap }
    } yield pathToItem
  }

  /**
   * Returns a sequence of all the fetch items.
   *
   * @return a sequence of fetch items.
   */
  def getFetchItems: Try[Seq[FetchItem]] = {
    for {
      bag <- triedBag
      items = bag.getItemsToFetch.asScala.map(
        fi => FetchItem(fi.url.toURI, fi.length, bagFile.path.relativize(fi.path)))
    } yield items
  }

  def getBagFiles: Try[Seq[File]] = Try {
    // TODO: Need to close a stream here?
    bagFile.walk().toList
  }
}
