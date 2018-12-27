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
import scala.util.{ Failure, Success, Try }
import better.files.Dsl._

class BagStoreAddSpec extends ReadWriteTestSupportFixture {
  private val baseDir = (testDir / "bag-store").createDirectories()
  private val stagingDir = (testDir / "staging").createDirectories()
  private val bagDirPermissions = "rwx-w----"
  private val bagFilePermissions = "rwx-w--w-"
  private val bagStore = new BagStore(baseDir, stagingDir,
    /*
     * Set to something unusual, so that we can effectively test it, but not something that prevents
     * our user from deleting the data afterwards (e.g., during mvn clean).
     */
    bagDirPermissions = PosixFilePermissions.fromString(bagDirPermissions).asScala.toSet,
    bagFilePermissions = PosixFilePermissions.fromString(bagFilePermissions).asScala.toSet)

  "add" should "create exact copy of valid bag in bag store" in {
    /*
     * The most basic case: just copying a directory into the bag store.
     */
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
    /*
     * The "add" method must guard the bag store against becoming corrupt.
     */
    bagStore.add(testResources / "bags" / "invalid") should matchPattern { case Failure(NonVirtuallyValidBagException(_)) => }
  }

  it should "remove parent directory in staging directory after rejecting invalid bag" in {
    /*
     * We don't want to leave temporary files or directories around, when the operation aborts because of invalid input.
     */
    bagStore.add(testResources / "bags" / "invalid")
    (testDir / "staging").list shouldBe empty
  }

  it should "remove parent directory in staging directory after adding with staging" in {
    /*
     * We don't want to leave temporary files or directories around after successfully completing the operation, either.
     */
    bagStore.add(testResources / "bags" / "small")
    (testDir / "staging").list shouldBe empty
  }

  it should "NOT remove parent directory (OUTSIDE staging directory) after adding with MOVE" in {
    /*
     * We also don't want to remove too much. When moving, the "staged" bag is the original bag in, say, the user's home directory.
     * The parent of the bag is then that home directory, which must of course not be deleted.
     */
    val userHome = mkdir(testDir / "userhome")
    testResources / "bags" / "small" copyToDirectory userHome
    bagStore.add(userHome / "small", move = true)
    userHome.toJava should exist
    userHome.list shouldBe empty
  }

  it should "accept an incomplete, unserialized bag that is virtually-valid" in {
    /*
     * A bag that is only virtually valid, but not valid according to the BagIt specs, should still be successfully added to the bag store.
     */
    testResources / "bag-stores" / "three-revisions" copyToDirectory testDir
    val bagStoreThreeRevisions = BagStore(testDir / "three-revisions", stagingDir,
      bagDirPermissions = PosixFilePermissions.fromString("rwx------").asScala.toSet,
      bagFilePermissions = PosixFilePermissions.fromString("rwx------").asScala.toSet)
    bagStoreThreeRevisions.add(testResources / "bags" / "bag-revision-4") shouldBe a[Success[_]]
  }

  it should "set bag directory and file permissions as specified in BagStore constructor" in {
    for {
      bagItem <- bagStore.add(testResources / "bags" / "medium")
      location <- bagItem.getLocation
      actualPermissions <- Try { location.walk().map(f => (f.isDirectory, f.permissionsAsString)).toList }
      _ <- Try {
        actualPermissions.map { case (isDir, perms) =>
          if (isDir) perms shouldBe bagDirPermissions
          else perms shouldBe bagFilePermissions
        }
      }
    } yield ()
  }
}
