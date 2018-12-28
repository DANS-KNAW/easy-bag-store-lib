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

import java.nio.file.attribute.PosixFilePermissions

import scala.collection.JavaConverters._
import scala.util.{ Failure, Success }

class BagStoreAddToPrefilledBagStore extends ReadWriteTestSupportFixture {
  private val stagingDir = (testDir / "staging").createDirectories()
  testResources / "bag-stores" / "three-revisions" copyToDirectory testDir
  private val bagStore = BagStore(testDir / "three-revisions", stagingDir,
    bagDirPermissions = PosixFilePermissions.fromString("rwx------").asScala.toSet,
    bagFilePermissions = PosixFilePermissions.fromString("rwx------").asScala.toSet)

  "add" should "accept an incomplete, unserialized bag that is virtually-valid" in {
    /*
     * A bag that is only virtually valid, but not valid according to the BagIt specs, should still be successfully added to the bag store.
     */
    bagStore.add(testResources / "bags" / "bag-revision-4") shouldBe a[Success[_]]
  }

  it should "reject a bag that includes a fetch item which overwrites a physically present file" in {
    /*
     * The input bag contains a fetch item that is to be put at relative path data/p, but a file already exists there.
     */
    val result = bagStore.add(testResources / "bags" / "fetchitem-overwrites-file")
    inside(result) {
      case Failure(e) =>
        e shouldBe a[IllegalArgumentException]
        e.getMessage should include("existing file")
    }
  }

  it should "reject a bag that includes a fetch item which overwrites another fetch item" in {
    /*
     * The input bag contains a fetch item that is to be put at relative path data/p, but a file already exists there.
     */
    val result = bagStore.add(testResources / "bags" / "fetchitem-overwrites-fetchitem")
    inside(result) {
      case Failure(e) =>
        e shouldBe a[IllegalArgumentException]
        e.getMessage should include("Conflicting fetch items")
    }
  }

}
