package nl.knaw.dans.lib.bagstore

import scala.util.{ Failure, Success }

/**
 * This class tests a test utility function.
 */
class FilesEqualSpec extends ReadOnlyTestSupportFixture {

  "filesEqual" should "return success for identical files" in {
    val identical = testResources / "filesEqual" / "identical"
    filesEqual(identical / "file1.txt", identical / "file2.txt") shouldBe a[Success[_]]
  }

  it should "return failure for files with different content" in {
    val different = testResources / "filesEqual" / "different"
    val result = filesEqual(different / "file1.txt", different / "file2.txt")
    result shouldBe a[Failure[_]]
    inside(result) {
      case Failure(e) => e.getMessage should include("content")
    }
  }

  it should "return success for copies of the same directory" in {
    val identicalDirs = testResources / "filesEqual" / "identical-dirs"
    filesEqual(identicalDirs / "top1", identicalDirs / "top2") shouldBe a[Success[_]]
  }

  it should "return failure for directories in one of which a file has been renamed" in {
    val differentDirs = testResources / "filesEqual" / "different-dirs-filename"
    val result = filesEqual(differentDirs / "top1", differentDirs / "top2")
    result shouldBe a[Failure[_]]
    inside(result) {
      case Failure(e) => e.getMessage should include("names")
    }
  }

  it should "return failure for directories in one of which a file has different content" in {
    val differentDirs = testResources / "filesEqual" / "different-dirs-content"
    val result = filesEqual(differentDirs / "top1", differentDirs / "top2")
    result shouldBe a[Failure[_]]
    inside(result) {
      case Failure(e) => e.getMessage should include("content")
    }
  }
}
