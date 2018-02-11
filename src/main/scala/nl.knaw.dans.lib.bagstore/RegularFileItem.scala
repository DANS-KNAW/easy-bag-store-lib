package nl.knaw.dans.lib.bagstore

import java.nio.file.Path

import better.files.File

import scala.util.Try

class RegularFileItem(bagItem: BagItem, path: Path) extends FileItem(bagItem, path) {

  /**
   * Regular file items may be included by fetch-reference in a bag. This function returns to
   * location where the file data is actually stored.
   *
   * @return the real location of the file data
   */
  def getFileDataLocation: Try[File] = ???


  /**
   *
   *
   * @param authority
   * @param reason
   * @return
   */
  def erase(authority: String, reason: String): Try[Unit] = ???
}
