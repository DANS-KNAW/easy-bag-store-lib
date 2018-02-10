package nl.knaw.dans.lib

import java.util.UUID

package object bagstore {
  private val uuidLength = 32

  /**
   * A slash-pattern is defined by the sizes of the components that it divide up its argument into.
   */
  case class SlashPattern(componentSizes: Int*) {
    require(componentSizes.forall(_ > 0), "Component sizes must be positive")
    require(componentSizes.forall(_ < uuidLength + 1), s"Component sizes must be at most the size of a UUID string ($uuidLength)")
    require(componentSizes.sum == uuidLength, s"Sum of the component sizes must be $uuidLength")

    def applyTo(uuid: UUID): String = {
      uuid.toString.filter(_ == '-')
    }
  }
}
