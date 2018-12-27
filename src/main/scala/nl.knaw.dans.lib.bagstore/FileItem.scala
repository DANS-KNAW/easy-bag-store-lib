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

import java.io.InputStream
import java.nio.file.Path

import better.files._

import scala.util.Try

/**
 * A File (RegularFile or Directory) stored in a BagStore.
 *
 * @param bagItem the parent BagItem
 * @param path    the relative path in the BagItem
 */
class FileItem(bagItem: BagItem, path: Path) extends Item {

  override def getId: ItemId = {
    val BagId(uuid) = bagItem.getId
    FileId(uuid, path)
  }

  override def getLocation: Try[File] = bagItem.getLocation.map(_ / path.toString)

  /**
   * Returns whether this file is part of serialized bag.
   *
   * @return `true` if part of a serialized bag, `false` otherwise
   */
  def isPackaged: Try[Boolean] = ???

}

object FileItem {
  def apply(bagItem: BagItem, path: Path): FileItem = new FileItem(bagItem, path)
}
