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

import scala.util.Success

class RegularFileItemExistsSpec extends ReadWriteTestSupportFixture {
  private val staging = (testDir / "staging") createDirectories()
  private val bagStore = new BagStoreImpl(testResources / "bag-stores" / "three-revisions", staging)

  // TODO: unignore when exists is implemented
  "exists" should "return true for a regular file that exists given location" ignore  {
    RegularFileItem(BagItem(bagStore, UUID.fromString("00000000-0000-0000-0000-00000001")), Paths.get("bagit.txt")).exists should be(Success(true))
  }

}
