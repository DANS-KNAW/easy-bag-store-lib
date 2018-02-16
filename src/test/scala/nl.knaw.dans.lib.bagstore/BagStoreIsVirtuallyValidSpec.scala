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

import better.files.File

import scala.util.Success

class BagStoreIsVirtuallyValidSpec extends ReadWriteTestSupportFixture {
  import scala.language.postfixOps
  private val staging = testDir / "staging" createDirectory()

  "isVirtuallyValid" should "return true for valid bag with empty fetch.txt" in {
    val emptyBaseDir = testDir / "empty-bag-store" createDirectory()
    val bagStore = BagStore(emptyBaseDir, staging)
    val vv1 = testResources / "bags" / "virtually-valid1"

    val result = bagStore.isVirtuallyValid(vv1)
    result shouldBe a[Success[_]]
    inside(result) {
      case Success(r) => r should be(Right(()))
    }
  }

  it should "return true for revision 1 in bag-store: 'three revisions'" in {
    val bagStore = BagStore(testResources / "bag-stores" / "three-revisions", staging)
    val result = bagStore.isVirtuallyValid(testResources / "bag-stores/three-revisions/00/000000000000000000000000000001/bag-revision-1")
    result shouldBe a[Success[_]]
    inside(result) {
      case Success(r) => r should be(Right(()))
    }
  }

  it should "return true for revision 2 in bag-store: 'three revisions'" in {
    val bagStore = BagStore(testResources / "bag-stores" / "three-revisions", staging)
    val result = bagStore.isVirtuallyValid(testResources / "bag-stores/three-revisions/00/000000000000000000000000000002/bag-revision-2")
    result shouldBe a[Success[_]]
    inside(result) {
      case Success(r) => r should be(Right(()))
    }
  }

  it should "return true for revision 3 in bag-store: 'three revisions'" in {
    val bagStore = BagStore(testResources / "bag-stores" / "three-revisions", staging)
    val result = bagStore.isVirtuallyValid(testResources / "bag-stores/three-revisions/00/000000000000000000000000000003/bag-revision-3")
    result shouldBe a[Success[_]]
    inside(result) {
      case Success(r) => r should be(Right(()))
    }
  }
}
