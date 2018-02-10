package nl.knaw.dans.lib.bagstore

import better.files.File

/**
 * An item is a bag, directory or regular file in a bag store. An item *always* exists in the context
 * of a bag store.
 */
trait Item {

  /**
   * The item-id is the key you can use to look up this item in the bag store.
   *
   * @return the item-id
   */
  def getId: ItemId

  /**
   * The location on storage where the item resides. Note that in the case of regular files this may
   * be a virtual location.
   *
   * @return
   */
  def getLocation: File
}
