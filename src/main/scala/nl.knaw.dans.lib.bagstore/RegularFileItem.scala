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

import better.files.File

import scala.language.postfixOps
import scala.util.{ Failure, Try }
import scala.util.matching.Regex

case class RegularFileItem(bagItem: BagItem, path: Path) extends FileItem(bagItem, path) {

  /**
   * Regular file items may be included by fetch-reference in a bag. This function returns to
   * location where the file data is actually stored.
   *
   * @return the real location of the file data
   */
  def getFileDataLocation: Try[File] = {
    for {
      bagLocation <- bagItem.getLocation
      fileDataLocation <- bagItem.bagStore.getFileDataLocation(bagLocation, path)
    } yield fileDataLocation
  }

  // TODO: Implement erase
  /**
   *
   *
   * @param authority
   * @param reason
   * @return
   */
  def erase(authority: String, reason: String): Try[Unit] = ???
}
