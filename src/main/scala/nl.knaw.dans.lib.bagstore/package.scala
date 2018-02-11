package nl.knaw.dans.lib

import java.nio.file.{ Path, Paths }
import java.util.UUID

import better.files.File

package object bagstore {
  case class CorruptBagStoreException(details: String) extends Exception(s"Corrupt bag store: $details")



  private val uuidLength = 32

  /**
   * A slash pattern is defined by the resulting path's component sizes.
   */
  case class SlashPattern(componentSizes: Int*) {
    require(componentSizes.forall(_ > 0), "Component sizes must be positive")
    require(componentSizes.forall(_ < uuidLength + 1), s"Component sizes must be at most the size of a UUID string ($uuidLength)")
    require(componentSizes.sum == uuidLength, s"Sum of the component sizes must be $uuidLength")

    /**
     * Apply the slash pattern to the given UUID.
     *
     * @param uuid the uuid
     * @return the resulting path
     */
    def applyTo(uuid: UUID): Path = {
      val (head :: tail, _) = componentSizes.foldLeft((List.empty[String], uuid.toString.filterNot(_ == '-'))) {
        case ((acc, rest), size) =>
          val (next, newRest) = rest.splitAt(size)
          (acc :+ next, newRest)
      }
      Paths.get(head, tail: _*)
    }
  }
}
