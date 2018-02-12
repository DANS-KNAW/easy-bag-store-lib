package nl.knaw.dans.lib.bagstore

import java.util.UUID

import scala.util.Success

class BagStoreAddSpec extends ReadWriteTestSupportFixture {
  private val baseDir = (testDir / "bag-store").createDirectories()
  private val stagingDir = (testDir / "staging").createDirectories()
  private val bagStore = new BagStore(baseDir, stagingDir)

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


}
