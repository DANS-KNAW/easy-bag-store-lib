package nl.knaw.dans.lib.bagstore

import java.util.UUID

import better.files.File

import scala.util.Try

class BagStore(val baseDir: File, val slashPattern: SlashPattern) {


  def add(bag: File, optUuid: Option[UUID] = None): Try[BagItem] = {

    ???
  }

  def get(itemId: ItemId): Try[Item] = {

    ???
  }

  def get(bagId: BagId): Try[BagItem] = {

    ???
  }

  def get(fileId: FileId): Try[FileItem] = {

    ???
  }

  def get(regularFileItem: RegularFileItem): Try[RegularFileItem] = {

    ???
  }

  def get(directoryItem: DirectoryItem): Try[DirectoryItem] = {

    ???
  }

  def enum(includeActive: Boolean = true, includeInactive: Boolean = false): Try[Stream[Try[BagItem]]] = {


    ???
  }

  /**
   * Verifies this bag store. The following invariants are checked (in this order):
   *
   * 1. The slash-pattern is maintained throughout the bag store.
   * 2. Every container only has one file in it.
   * 3. All bags are virtually-valid.
   *
   * If any of these invariants is violated the bag store is declared corrupt. By default the function will
   * stop at the first violation, but this behavior can be overridden.
   *
   * Note that, depending on the size of the bag store, this function may take a long time to complete.
   *
   * @param failOnFirstViolation whether to stop checking after encountering the first violation
   *
   * @return
   */
  def verify(failOnFirstViolation: Boolean = true): Try[Unit] = {


    ???
  }


  /**
   * Determines whether `bag` is virtually-valid with respect to this bag store. The bag may or may not be
   * stored in the bag store currently.
   *
   * @param bag the bag to validate
   * @return if the bag is virtually-valid
   */
  def isVirtuallyValid(bag: File): Try[Unit] = {


    ???
  }

  /**
   * Removes from `bag` the payload files which are found in one of the reference bags and adds a fetch reference to
   * the file in that reference bag. The result is a smaller bag, which is still virtually valid relative to this
   * bag store.
   *
   * @param bag
   * @param refBagId
   */
  def prune(bag: File, refBagId: BagId*)


  /**
   *
   *
   * @param bag
   */
  def complete(bag: File)


}
