package nl.knaw.dans.lib.bagstore

import java.nio.file.Path

import better.files.File
import gov.loc.repository.bagit.domain.Bag
import gov.loc.repository.bagit.exceptions._
import gov.loc.repository.bagit.reader.BagReader
import gov.loc.repository.bagit.verify.BagVerifier
import nl.knaw.dans.lib.bagstore.BagInspector.bagReader

import collection.JavaConverters._
import scala.util.Try

object BagInspector {
  private val bagReader = new BagReader
  private val bagVerifier = new BagVerifier()

  private def verifyValid(bag: Bag): Try[Either[String, Unit]] = Try {
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
 * Inspects the contents of a bag.
 *
 * @param bagFile the bag to inspect.
 */
case class BagInspector(bagFile: File) {

  private lazy val maybeBag: Try[Bag] = Try {
    bagReader.read(bagFile.path)
  }

  /**
   * Verifies if the bag is valid, according to the BagIt specs. If the verfication process succeeded
   * the result is a `Right` (meaning the bag is valid) or a `Left` (meaning it is non-valid). If it is
   * a `Left`, the results contains the reason why the bag is non-valid.
   *
   * @return if successful the
   */
  def verifyValid: Try[Either[String, Unit]] = {
    for {
      bag <- maybeBag
      result <- BagInspector.verifyValid(bag)
    } yield result
  }

  def getFetchItems: Try[Map[Path, FetchItem]] = {
    for {
      bag <- maybeBag
      // TODO: make sure fi.path is always relative
      items <- Try  { bag.getItemsToFetch.asScala.map(fi => (fi.path, FetchItem(fi.url.toURI, fi.length, fi.path))).toMap }
    } yield items
  }
}
