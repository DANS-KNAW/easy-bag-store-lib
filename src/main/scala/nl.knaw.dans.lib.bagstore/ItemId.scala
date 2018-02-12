package nl.knaw.dans.lib.bagstore

import java.nio.file.Path
import java.util.UUID

import com.google.common.escape.Escaper
import com.google.common.net.UrlEscapers

import scala.collection.JavaConverters._

sealed class ItemId
case class BagId(uuid: UUID) extends ItemId {
  override def toString: String = uuid.toString
}
case class FileId(uuid: UUID, path: Path) extends ItemId {
  require(path.getFileName.toString.nonEmpty, "Path must not be empty")
  require(!path.isAbsolute, "Path must be relative")

  override def toString: String = {
    uuid.toString + "/" + path.asScala.map(_.toString).map(FileId.pathEscaper.escape).mkString("/")
  }
}

object FileId {
  private val pathEscaper: Escaper = UrlEscapers.urlPathSegmentEscaper()
}

object ItemId {




}