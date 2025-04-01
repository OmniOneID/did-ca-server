/*
 * Copyright 2024 OmniOne.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.omnione.did.cas.v1.agent.service;

import org.omnione.did.data.model.did.DidDocument;

/**
 * The StorageService interface provides methods for storing and retrieving data from the CAS database.
 * It is designed to facilitate the retrieval and validation of data from the database,
 * ensuring that the data is accurate and up-to-date.
 */
public interface StorageService {
    /**
     * Register a DID document in the CAS database.
     * This method stores the provided DID document in the database, associating it with the specified role type.
     *
     * @param didKeyUrl  the DID key URL associated with the DID document
     * @return the stored DID document
     */
    DidDocument findDidDoc(String didKeyUrl);
}
