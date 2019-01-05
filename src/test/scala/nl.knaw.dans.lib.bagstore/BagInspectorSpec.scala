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

import java.net.URI
import java.nio.file.Paths

import scala.util.Success

class BagInspectorSpec extends ReadOnlyTestSupportFixture {

  "getPathsToFetchItems" should "return an empty map if no fetch.txt is present" in {
    val fetchItems = BagInspector(testResources / "bags" / "medium").getPathsToFetchItems
    fetchItems shouldBe a[Success[_]]
    inside(fetchItems) {
      case Success(fis) => fis shouldBe empty
    }
  }

  it should "return an empty map if fetch.txt is empty" in {
    val fetchItems = BagInspector(testResources / "bags" / "empty-fetchtxt").getPathsToFetchItems
    fetchItems shouldBe a[Success[_]]
    inside(fetchItems) {
      case Success(fis) => fis shouldBe empty
    }
  }

  it should "return a map with several items if several entries are present in fetch.txt" in {
    val fetchItems = BagInspector(testResources / "bags" / "bag-revision-4").getPathsToFetchItems
    fetchItems shouldBe a[Success[_]]
    inside(fetchItems) {
      case Success(fis) =>
        fis should have size 3
        fis(Paths.get("data/x")).uri shouldBe new URI("http://localhost/00000000-0000-0000-0000-000000000002/data/x")
    }
  }
}
