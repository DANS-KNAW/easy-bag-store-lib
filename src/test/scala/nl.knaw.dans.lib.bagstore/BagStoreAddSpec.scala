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
import java.util.UUID

import scala.collection.JavaConverters._
import scala.util.{ Failure, Success }

class BagStoreAddSpec extends ReadWriteTestSupportFixture {
  private val baseDir = (testDir / "bag-store").createDirectories()
  private val stagingDir = (testDir / "staging").createDirectories()
  private val bagStore = new BagStore(baseDir, stagingDir,
    /*
     * Set to something unusual, so that we can effectively test it, but not something that prevents
     * our user from deleting the data afterwards (e.g., during mvn clean).
     */
    bagDirPermissions = PosixFilePermissions.fromString("rwx-w----").asScala.toSet,
    bagFilePermissions = PosixFilePermissions.fromString("rwx-w--w-").asScala.toSet)

  "add" should "create exact copy of valid bag in bag store" in {
    val uuid = UUID.randomUUID()
    val bagToAdd = testResources / "bags" / "small"
    val expectedLocationInBagStore = bagStore.baseDir / bagStore.slashPattern.applyTo(uuid).toString / "small"

    val result = bagStore.add(bagToAdd, Some(uuid))
    result shouldBe a[Success[_]]
    inside(result) {
      case Success(item: BagItem) =>
        val location = item.getLocation.get
        location should be(expectedLocationInBagStore)
        filesEqual(bagToAdd, location) shouldBe Success(())
      case _ => fail(s"Unexpected type of result: $result")
    }
  }

  it should "not accept an invalid, unserialized bag" in {
    bagStore.add(testResources / "bags" / "invalid") should matchPattern { case Failure(NonVirutallyValidBagException(_)) => }
  }

  it should "accept an incomplete, unserialized bag that is virtually-valid" in {
    testResources / "bag-stores" / "three-revisions" copyToDirectory  testDir
    val bagStoreThreeRevisions = BagStore(testDir / "three-revisions", stagingDir,
      bagDirPermissions = PosixFilePermissions.fromString("rwx------").asScala.toSet,
      bagFilePermissions = PosixFilePermissions.fromString("rwx------").asScala.toSet)
    bagStoreThreeRevisions.add(testResources / "bags" / "bag-revision-4") shouldBe a[Success[_]]
  }

  it should "remove parent directory in staging directory after adding with staging" in {}

  it should "NOT remove parent directory in staging directory after adding with move" in {}

  it should "set bag directory permission as specified in BagStore constructor" in {}

  it should "set bag file permissions as specified in BagStore contstructor" in {}

  it should "set serialized bag permissions to the values specified for bag dir permissions" in {}
}
