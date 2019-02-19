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

class BagInspectorSpec extends ReadWriteTestSupportFixture {

  "getPathsToFetchItemsMap" should "return an empty map if no fetch.txt is present" in {
    val fetchItems = BagInspector(testResources / "bags" / "medium").getPathsToFetchItemsMap
    fetchItems shouldBe a[Success[_]]
    inside(fetchItems) {
      case Success(fis) => fis shouldBe empty
    }
  }

  it should "return an empty map if fetch.txt is empty" in {
    val fetchItems = BagInspector(testResources / "bags" / "empty-fetchtxt").getPathsToFetchItemsMap
    fetchItems shouldBe a[Success[_]]
    inside(fetchItems) {
      case Success(fis) => fis shouldBe empty
    }
  }

  it should "return a map with several items if several entries are present in fetch.txt" in {
    val fetchItems = BagInspector(testResources / "bags" / "bag-revision-4").getPathsToFetchItemsMap
    fetchItems shouldBe a[Success[_]]
    inside(fetchItems) {
      case Success(fis) =>
        fis should have size 3
        fis(Paths.get("data/x")).uri shouldBe new URI("http://localhost/00000000-0000-0000-0000-000000000002/data/x")
    }
  }

  "getPayloadManifestEntryPaths" should "return an empty set if the payload is empty" in {
    val emptyBag = testResources / "bags" / "empty" copyToDirectory testDir
    emptyBag / "data" createDirectories() // We cannot store empty directories in git, so we create it on the fly here.
    val paths = new BagInspector(emptyBag).getPayloadManifestEntryPaths
    paths shouldBe a[Success[_]]
    inside(paths) {
      case Success(ps) => ps should be(empty)
    }
  }

  it should "return all entries distributed over multiple manifests" in {
    /*
     * With BagIt v1.0 each payload manifest is required to contain entries for all payload files, so this test would not seem useful.
     * However, we will also need to keep supporting v0.97, as many bags are already stored in this format version.
     */
    val paths = new BagInspector(testResources / "bags" / "medium-bagit0.97").getPayloadManifestEntryPaths
    paths shouldBe a[Success[_]]
    inside(paths) {
      case Success(ps) => ps should have size 5
    }
  }

  it should "return a set with the size of the number of present + to-be-fetched files" in {
    val paths = new BagInspector(testResources / "bags" / "bag-revision-4").getPayloadManifestEntryPaths
    paths shouldBe a[Success[_]]
    inside(paths) {
      case Success(ps) => ps should have size 4
    }

  }
}
