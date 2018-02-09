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
package nl.knaw.dans.lib

import java.nio.file.Path
import java.util.UUID

import better.files.File

package object bagstore {
  type BagFile = File
  type SlashPattern = Seq[Int]
  type Location = File

  sealed trait ItemId
  case class FileId(uuid: UUID, path: Path) extends ItemId {
    override def toString: String = ???
  }
  case class BagId(uuid: UUID) extends ItemId {
    override def toString: String = uuid.toString
  }

  object ArchiveStreamType extends Enumeration {
    type ArchiveStreamType = Value
    val TAR, ZIP = Value
  }
}
