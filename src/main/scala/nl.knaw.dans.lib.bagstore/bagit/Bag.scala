package nl.knaw.dans.lib.bagstore.bagit

import nl.knaw.dans.lib.bagstore.BagFile

import scala.util.Try

/**
 * Provides low-level access to a bag. Only operations that can be performed without information
 * about the bag store context are included in this class.
 *
 * @param bagFile
 */
class Bag(bagFile: BagFile) {

  /**
   *
   * @return
   */
  def isValid: Try[Boolean] = ???



}
