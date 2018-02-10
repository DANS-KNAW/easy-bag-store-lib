package nl.knaw.dans.lib.bagstore

import java.util.UUID

import better.files.File

import scala.util.Try

/**
 * A Bag that is stored in a BagStore.
 */
class BagItem(bagStore: BagStore, uuid: UUID) extends Item {
  override def getId: ItemId = BagId(uuid)

  override def getLocation: File = {
    // TODO: get the container location; then get the only file in it (= the bag)
    bagStore.baseDir `/` uuid.toString
  }

  /**
   * A Bag is virtually-valid if:
   *
   * - it is [valid], or
   * - it is incomplete, but contains a [`fetch.txt`] file and can be made valid by fetching the files
   *   listed in it and removing `fetch.txt` and its corresponding checksums from the Bag. If
   *   local-file-uris are used, they must reference RegularFiles in the same BagStore.
   *
   * BagItems must *always* be virtually-valid. Otherwise the BagStore is corrupt.
   *
   * @return
   */
  def isVirtuallyValid: Try[Boolean] = {


    ???
  }
}
