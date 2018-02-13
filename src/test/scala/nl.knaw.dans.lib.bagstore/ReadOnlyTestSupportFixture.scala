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

import java.nio.file.{ Path, Paths }

import better.files.File
import org.scalatest.{ FlatSpec, Inside, Matchers, OneInstancePerTest }

import scala.util.{ Failure, Success, Try }

/**
 * Test support for tests that only need to read files, not write them.
 */
trait ReadOnlyTestSupportFixture extends FlatSpec with Matchers with Inside with OneInstancePerTest {
  protected val testResources: File = Paths.get("src/test/resources")

  def filesEqual(file1: File, file2: File): Try[Unit] = {
    val diffs = (pairWithRelativePath(file1) zip pairWithRelativePath(file2))
      .map {
        case ((p1, f1), (p2, f2)) =>
          (p1.toString, p2.toString, p1 == p2, f1.isDirectory && f2.isDirectory || f1.isSameContentAs(f2))
      }.filter {
      case (_, _, names, content) => !names || !content
    }

    if (diffs.isEmpty) Success(())
    else Failure(new IllegalStateException(s"Differences found:\n${layoutDifferences(file1, file2, diffs)}\n---\n\n"))
  }

  private def pairWithRelativePath(base: File): Seq[(Path, File)] = {
    base.walk().toList.map {
      f =>
        (base.toJava.toPath.relativize(f.toJava.toPath), f)
    }.sortBy { case (p, _) => p }
  }

  private def layoutDifferences(base1: File, base2: File, diffs: Seq[(String, String, Boolean, Boolean)]): String = {
    diffs.map {
      case (p1, p2, names, content) =>
        s" - $p1 <=> $p2: ${if(!names) "different names" else if (!content) "different content" else "???"}"
    }.mkString("\n")
  }
}
