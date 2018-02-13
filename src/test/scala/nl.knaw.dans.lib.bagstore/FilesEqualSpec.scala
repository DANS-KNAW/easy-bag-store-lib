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

import scala.util.{ Failure, Success }

/**
 * This class tests a test utility function.
 */
class FilesEqualSpec extends ReadOnlyTestSupportFixture {

  "filesEqual" should "return success for identical files" in {
    val identical = testResources / "filesEqual" / "identical"
    filesEqual(identical / "file1.txt", identical / "file2.txt") shouldBe a[Success[_]]
  }

  it should "return failure for files with different content" in {
    val different = testResources / "filesEqual" / "different"
    val result = filesEqual(different / "file1.txt", different / "file2.txt")
    result shouldBe a[Failure[_]]
    inside(result) {
      case Failure(e) => e.getMessage should include("content")
    }
  }

  it should "return success for copies of the same directory" in {
    val identicalDirs = testResources / "filesEqual" / "identical-dirs"
    filesEqual(identicalDirs / "top1", identicalDirs / "top2") shouldBe a[Success[_]]
  }

  it should "return failure for directories in one of which a file has been renamed" in {
    val differentDirs = testResources / "filesEqual" / "different-dirs-filename"
    val result = filesEqual(differentDirs / "top1", differentDirs / "top2")
    result shouldBe a[Failure[_]]
    inside(result) {
      case Failure(e) => e.getMessage should include("names")
    }
  }

  it should "return failure for directories in one of which a file has different content" in {
    val differentDirs = testResources / "filesEqual" / "different-dirs-content"
    val result = filesEqual(differentDirs / "top1", differentDirs / "top2")
    result shouldBe a[Failure[_]]
    inside(result) {
      case Failure(e) => e.getMessage should include("content")
    }
  }
}
