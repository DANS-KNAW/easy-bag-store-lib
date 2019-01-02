package nl.knaw.dans.lib.bagstore

import java.nio.file.Paths
import java.util.UUID

import scala.util.Success

class RegularFileItemExistsSpec extends ReadWriteTestSupportFixture {
  private val staging = (testDir / "staging") createDirectories()
  private val bagStore = new BagStoreImpl(testResources / "bag-stores" / "three-revisions", staging)

  "exists" should "return true for a regular file that exists given location" in {
    RegularFileItem(BagItem(bagStore, UUID.fromString("00000000-0000-0000-0000-00000001")), Paths.get("bagit.txt")).exists should be(Success(true))
  }

}
