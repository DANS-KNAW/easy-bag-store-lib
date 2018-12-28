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

import java.util.UUID

import scala.util.{ Failure, Success }

class BagItemExistsSpec extends ReadWriteTestSupportFixture {
  private val staging = (testDir / "staging") createDirectories()
  private val bagStore = new BagStoreImpl(testResources / "bag-stores" / "three-revisions", staging)


  "exists" should "return true for a bag that exists in the bag store" in {
    BagItem(bagStore, UUID.fromString("00000000-0000-0000-0000-00000001")).exists should be(Success(true))
  }

  it should "return false for a bag that does not exist in the bag store" in {
    BagItem(bagStore, UUID.fromString("00000000-0000-0000-0000-00000000")).exists should be(Success(false))
  }

  it should "return a failure if a corrupt container directory is hit" in {
    /*
     * The container contains two directories (not really full-blown bags), which should be enough to trip up the function.
     */
    val bagStore = new BagStoreImpl(testResources / "bag-stores" / "corrupt", staging)
    val result = BagItem(bagStore, UUID.fromString("00000000-0000-0000-0000-00000000")).exists
    result shouldBe a[Failure[_]]
    inside(result) {
      case Failure(e) => e shouldBe a[CorruptBagStoreException]
    }
  }

}
