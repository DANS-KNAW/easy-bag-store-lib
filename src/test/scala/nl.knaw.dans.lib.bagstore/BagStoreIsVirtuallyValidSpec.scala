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

    bagStore.isVirtuallyValid(vv1) shouldBe a[Success[_]]
  }


}
