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

class BagStoreGetSpec extends ReadWriteTestSupportFixture {

  "get(bagId)" should "succeed if the bag exists in the bag store" in {

  }

  it should "return a failure if there is no bag with the given bag-id" in {

  }

  "get(regularFileId)" should "succeed if the file-item is found as an entry in the bag's manifests" in {

  }

  "get(directoryId)" should "succeed if the directory is found as a prefix in the bag's manifests" in {

  }

}
