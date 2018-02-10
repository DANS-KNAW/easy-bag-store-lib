package nl.knaw.dans.lib.bagstore

import java.util.UUID

class SlashPatternSpec extends TestSupportFixture {
  val zeros = "00000000-0000-0000-0000-000000000000"

  "Constructor" should "not accept negative component sizes" in {
    val thrown = the[IllegalArgumentException] thrownBy SlashPattern(34,-1,1)
    thrown.getMessage should include("positive")
  }

  it should "not accept zero as a component size" in {
    val thrown = the[IllegalArgumentException] thrownBy SlashPattern(31,0,1)
    thrown.getMessage should include("positive")
  }

  it should "not accept component sizes that do not sum to 32" in {
    val thrown = the[IllegalArgumentException] thrownBy SlashPattern(30,1)
    thrown.getMessage should include("32")
  }

  it should "accept 32 components of 1" in {
    val pattern = SlashPattern(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1)
    pattern shouldBe a[SlashPattern]
  }

  it should "accept 1 components of 32" in {
    val pattern = SlashPattern(32)
    pattern shouldBe a[SlashPattern]
  }

  it should "something in between" in {
    val pattern = SlashPattern(2,30)
    pattern shouldBe a[SlashPattern]
  }

  "applyTo" should "divide up the UUID correctly" in {
    val pattern = SlashPattern(2,30)
    pattern applyTo UUID.fromString(zeros) should be("0" * 2 + "/" + "0" * 30)
  }


}
