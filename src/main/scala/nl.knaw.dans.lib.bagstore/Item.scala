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

import better.files.File

import scala.util.Try

/**
 * An item is a bag, directory or regular file in a bag store. An item *always* exists in the context
 * of a bag store.
 */
trait Item {

  /**
   * The item-id is the key you can use to look up this item in the bag store.
   *
   * @return the item-id
   */
  def getId: ItemId

  /**
   * The location on storage where the item resides. Note that in the case of regular files this may
   * be a virtual location.
   *
   * @return
   */
  def getLocation: Try[File]
}
