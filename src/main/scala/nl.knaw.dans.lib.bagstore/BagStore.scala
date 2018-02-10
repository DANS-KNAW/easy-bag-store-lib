package nl.knaw.dans.lib.bagstore

import java.io.OutputStream
import java.util.UUID

import better.files.File
import nl.knaw.dans.lib.bagstore.ArchiveStreamType.ArchiveStreamType

import scala.util.Try

class BagStore(baseDir: File) extends BagStoreOperations {
  override def add(bag: BagFile, optUuid: Option[UUID]): Try[BagId] = ???

  override def get(itemId: ItemId, target: BagFile, overwrite: Boolean): Try[Unit] = ???

  override def stream(itemId: ItemId, target: =>OutputStream, optArchiveStreamType: Option[ArchiveStreamType]): Try[Unit] = ???

  override def enumBags(includeActive: Boolean, includeInactive: Boolean): Try[Stream[Try[BagId]]] = ???

  override def enumFiles(bagId: BagId, includeRegularFiles: Boolean, includeDirectories: Boolean): Try[Stream[Try[FileId]]] = ???

  override def deactivate(bagId: BagId): Try[Unit] = ???

  override def reactivate(bag: BagId): Try[Unit] = ???

  override def erase(authority: String, reason: String, fileId: FileId*): Try[Unit] = ???
}
