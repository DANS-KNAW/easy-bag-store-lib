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
import java.util.UUID

import com.google.common.escape.Escaper
import com.google.common.net.UrlEscapers

import scala.collection.JavaConverters._

// TODO: Add constructors that take a string representation of the ItemId
sealed class ItemId
case class BagId(uuid: UUID) extends ItemId {
  override def toString: String = uuid.toString
}
class FileId(val uuid: UUID, val path: Path) extends ItemId {
  require(path.getFileName.toString.nonEmpty, "Path must not be empty")
  require(!path.isAbsolute, "Path must be relative")

  override def toString: String = {
    uuid.toString + "/" + path.asScala.map(_.toString).map(FileId.pathEscaper.escape).mkString("/")
  }
}

object FileId {
  private val pathEscaper: Escaper = UrlEscapers.urlPathSegmentEscaper()

  def apply(uuid: UUID, path: Path): FileId = new FileId(uuid, path)
}

case class RegularFileId(override val uuid: UUID, override val path: Path) extends FileId(uuid, path)

case class DirectoryId(override val uuid: UUID, override val path: Path) extends FileId(uuid, path)