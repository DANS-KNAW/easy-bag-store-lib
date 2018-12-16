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

import java.net.URI
import java.nio.file._
import java.util.UUID

import better.files.File

import scala.util.Try

// TODO: Add Jekyll-github pages
// TODO: Add manual page with code examples
// TODO: Figure out how to use from Java-code (wrappers needed?)
// TODO: Update tutorial to CentOS7

package object bagstore {
  case class CorruptBagStoreException(details: String) extends Exception(s"Corrupt bag store: $details")
  case class NoSuchItemException(details: String) extends Exception(s"No such item in bag store: $details")
  case class BagReaderException(bagFile: File, cause: Throwable) extends Exception(s"Could not read bag at: $bagFile: ${ cause.getMessage }", cause)

  case class FetchItem(uri: URI, size: Long, path: Path) {
    require(!path.isAbsolute, "path must be relative")
  }

  /**
   * A slash pattern is defined by the resulting path's component sizes.
   */
  case class SlashPattern(componentSizes: Int*) {
    val uuidLength = 32
    require(componentSizes.forall(_ > 0), "Component sizes must be positive")
    require(componentSizes.forall(_ < uuidLength + 1), s"Component sizes must be at most the size of a UUID string ($uuidLength)")
    require(componentSizes.sum == uuidLength, s"Sum of the component sizes must be $uuidLength")

    /**
     * Apply the slash pattern to the given UUID.
     *
     * @param uuid the uuid
     * @return the resulting path
     */
    def applyTo(uuid: UUID): Path = {
      val (head :: tail, _) = componentSizes.foldLeft((List.empty[String], uuid.toString.filterNot(_ == '-'))) {
        case ((acc, rest), size) =>
          val (next, newRest) = rest.splitAt(size)
          (acc :+ next, newRest)
      }
      Paths.get(head, tail: _*)
    }
  }

  def getAncestors(f: File, c: List[File] = List[File]()): List[File] = {
    if (f.parent == null) c
    else f :: getAncestors(f.parent, c)
  }

  /**
   * Creates a shallow copy of `src` at `target`, consisting of the same directory structure as under `src`, but instead of files, symbolic
   * links to the source files. The source directory must contain only directories and regular files, no other types of objects. It must also
   * not contain symbolic links.
   *
   * @param src    the source
   * @param target the target to create
   * @return
   */
  def symLinkCopy(src: File, target: File): Try[File] = Try {
    import scala.language.postfixOps
    src.walk().foreach {
      f =>
        if (f isSymbolicLink) throw new IllegalArgumentException(s"Found symbolic link in input: $f")
        val t = target / src.path.relativize(f).toString
        if (f.isRegularFile) t symbolicLinkTo f
        else if (f.isDirectory) t.createDirectory()
        else throw new IllegalArgumentException(s"Encountered an object in src that is neither a regular file nor a directory: $f")
    }
    target
  }
}
