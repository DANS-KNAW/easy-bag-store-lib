package nl.knaw.dans.lib.bagstore

import java.nio.file.Path

import better.files.File

class RegularFileItem(bagItem: BagItem, path: Path) extends FileItem(bagItem, path) {

  /**
   *
   *
   * @return
   */
  def getFileDataLocation: File = ???


}
