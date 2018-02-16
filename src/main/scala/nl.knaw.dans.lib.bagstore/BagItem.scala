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

import java.net.URI
import java.nio.file.Path
import java.util.UUID

import better.files.File

import scala.util.Try


/**
 * A Bag that is stored in a BagStore.
 */
case class BagItem(bagStore: BagStore, uuid: UUID) extends Item {
  private lazy val maybeInspector = getLocation.map(BagInspector(_))

  override def getId: ItemId = BagId(uuid)

  override def getLocation: Try[File] = Try {
    val container = bagStore.baseDir/bagStore.slashPattern.applyTo(uuid).toString
    val files = container.list.toList
    if(files.isEmpty) throw CorruptBagStoreException(s"$uuid: empty container")
    else if (files.size > 1) throw CorruptBagStoreException(s"$uuid: more than one file in container")
    else files.head
  }

  /**
   * Enumerates the items in this bag.
   *
   * @param includeRegularFiles
   * @param includeDirectories
   * @return
   */
  def enum(includeRegularFiles: Boolean = true, includeDirectories: Boolean = false): Try[Stream[Try[FileItem]]] = ???


  /**
   * A Bag is virtually-valid if:
   *
   * - it is [valid], or
   * - it is incomplete, but contains a [`fetch.txt`] file and can be made valid by fetching the files
   *   listed in it and removing `fetch.txt` and its corresponding checksums from the Bag. If
   *   local-file-uris are used, they must reference RegularFiles in the same BagStore.
   *
   * BagItems must *always* be virtually-valid. Otherwise the BagStore is corrupt.
   *
   * @return
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

// TODO: memoization of BagItems
