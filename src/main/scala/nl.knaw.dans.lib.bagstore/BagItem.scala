package nl.knaw.dans.lib.bagstore

import java.util.UUID

import better.files.File

import scala.util.Try

/**
 * A Bag that is stored in a BagStore.
 */
class BagItem(bagStore: BagStore, uuid: UUID) extends Item {
  override def getId: ItemId = BagId(uuid)

  override def getLocation: Try[File] = Try {
    val container = bagStore.baseDir/bagStore.slashPattern.applyTo(uuid).toString
    val files = container.list
    if (files.size != 1) throw CorruptBagStoreException(s"container with more than one file for $uuid")
    else files.toList.head
  }

  /**
   * Enumerates the items in this bag.
   *
   * @param includeRegularFiles
   * @param includeDirectories
   * @return
   */
  def enum(includeRegularFiles: Boolean = true, includeDirectories: Boolean = false): Try[Stream[Try[FileItem]]] = ???


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
