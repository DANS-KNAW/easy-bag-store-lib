package nl.knaw.dans.lib.bagstore

import java.nio.file.Paths
import java.util.UUID

class ItemIdSpec extends ReadOnlyTestSupportFixture {

  "BagId.toString" should "return the same as wrapped UUID.toString" in {
    val uuid = UUID.randomUUID()
    BagId(uuid).toString shouldBe uuid.toString
  }

  "FileId.toString" should "return BagId/path" in {
    val uuid = UUID.randomUUID()
    FileId(uuid, Paths.get("some/path")).toString shouldBe s"$uuid/some/path"
  }

  it should "not accept an empty path" in {
    an[IllegalArgumentException] should be thrownBy FileId(UUID.randomUUID(), Paths.get(""))
  }

  it should "not accept an absolute path" in {
    an[IllegalArgumentException] should be thrownBy FileId(UUID.randomUUID(), Paths.get("/absolute"))
  }
}
