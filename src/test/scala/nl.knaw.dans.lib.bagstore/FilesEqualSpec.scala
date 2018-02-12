package nl.knaw.dans.lib.bagstore

import scala.util.{ Failure, Success }

/**
 * This class tests a test utility function.
 */
class FilesEqualSpec extends TestSupportFixture {

  "filesEqual" should "return success for identical files" in {
    val identical = testResources / "filesEqual" / "identical"
    filesEqual(identical / "file1.txt", identical / "file2.txt") shouldBe a[Success[_]]
  }

  it should "return failure for files with different content" in {
    val identical = testResources / "filesEqual" / "different"
    val result = filesEqual(identical / "file1.txt", identical / "file2.txt")
    result shouldBe a[Failure[_]]
    inside(result) {
      case Failure(e) => e.getMessage should include("content")
    }
  }

  it should "return success for copies of the same directory" in {


  }

  it should "return failure for directories in one of which a file has been renamed" in {

  }

  it should "return failure for directories in one of which a file has different content" in {


  }

}
