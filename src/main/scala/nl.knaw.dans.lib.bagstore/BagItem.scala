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

import java.io.{ FileNotFoundException, InputStream }
import java.net.URI
import java.nio.file.{ NoSuchFileException, Path }
import java.util.UUID

import better.files.File

import scala.util.{ Failure, Success, Try }

/**
 * A Bag that is stored in a BagStore.
 */
case class BagItem (bagStore: BagStoreImpl, uuid: UUID) extends Item {
  private lazy val maybeInspector = getLocation.map(BagInspector(_))

  override def getId: ItemId = BagId(uuid)

  override def getLocation: Try[File] = Try {
    val container = bagStore.baseDir/bagStore.slashPattern.applyTo(uuid).toString
    if (container.notExists) throw NoSuchItemException("This bag item does not point to an existing bag")
    val files = container.list.toList
    if(files.isEmpty) throw CorruptBagStoreException(s"$uuid: empty container")
    else if (files.size > 1) throw CorruptBagStoreException(s"$uuid: more than one file in container")
    else files.head
  }

  override def exists: Try[Boolean] = {
    getLocation.map(_ => true).recoverWith {
      case _: NoSuchItemException => Success(false)
      case e => Failure(e)
    }
  }

  /**
   * Enumerates the Files in this Bag. The Files are returned as a stream of `Try[FileItem]`. If a Failure is encountered,
   * it means that the method failed to successfully retrieve all items. It will not try to retrieve any more items after
   * the failure and the result must be regarded as incomplete.
   *
   * <!-- nbsp below to fix Scaladoc issue -->
   * @param includeRegularFiles  &nbsp;whether to include RegularFiles.
   * @param includeDirectories  &nbsp;whether to include Directories.
   * @return a stream of Files.
   */
  def enum(includeRegularFiles: Boolean = true, includeDirectories: Boolean = false): Try[Stream[Try[FileItem]]] = ???


  def deactivate(): Try[Unit] = ???

  def reactivate(): Try[Unit] = ???

  /**
   * A Bag is virtually-valid if:
   *
   *  - it is valid, or
   *  - it is incomplete, but contains a fetch.txt file and can be made valid by fetching the files
   *    listed in it and removing fetch.txt and its corresponding checksums from the Bag. If
   *    local-file-uris are used, they must reference RegularFiles in the same BagStore.
   *
   * BagItems must ''always'' be virtually-valid. Otherwise the BagStore is corrupt.
   *
   * @return `Right(())` if the BagItem is virtually valid, `Left(msg)` otherwise, in which `msg` specifies the reason.
   */
  def isVirtuallyValid: Try[Either[String, Unit]] = {
    for {
      location <- getLocation
      result <- bagStore.isVirtuallyValid(location)
    } yield result
  }

  def getFetchUri(path: Path): Try[Option[URI]] = {
    for {
      inspector <- maybeInspector
      fetchItems <- inspector.getFetchItems
      optUri <- Try { fetchItems.get(path) }
    } yield optUri.map(_.uri)
  }
}
