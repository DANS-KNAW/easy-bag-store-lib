/**
 * Copyright (C) 2018 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.lib.bagstore

import java.nio.file.Paths
import java.util.UUID

class SlashPatternSpec extends ReadOnlyTestSupportFixture {
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

  "applyTo" should "divide up the UUID correctly (1)" in {
    val pattern = SlashPattern(2,30)
    pattern applyTo UUID.fromString(zeros) should be(Paths.get("0" * 2, "0" * 30))
  }

  it should "divide up the UUID correctly (2)" in {
    val pattern = SlashPattern(4,4,4,4,16)
    pattern applyTo UUID.fromString(zeros) should be(Paths.get("0" * 4, "0" * 4, "0" * 4, "0" * 4, "0" * 16))
  }
}
