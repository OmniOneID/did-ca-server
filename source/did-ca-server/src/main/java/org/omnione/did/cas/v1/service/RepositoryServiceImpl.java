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

package org.omnione.did.cas.v1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.base.util.BaseCoreDidUtil;
import org.omnione.did.base.util.BaseMultibaseUtil;
import org.omnione.did.cas.v1.api.RepositoryFeign;
import org.omnione.did.cas.v1.api.dto.DidDocApiResDto;
import org.omnione.did.common.util.DidUtil;
import org.omnione.did.core.manager.DidManager;
import org.omnione.did.data.model.did.DidDocument;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * The RepositoryServiceImpl class provides methods for retrieving DID documents from the repository.
 * It is designed to facilitate the retrieval of DID documents from the repository, ensuring that the data is accurate and up-to-date.
 */

@Service
@RequiredArgsConstructor
@Slf4j
@Primary
@Profile("repository")
public class RepositoryServiceImpl implements StorageService {
    private final RepositoryFeign repositoryFeign;

    /**
     * Retrieve a DID document from the repository.
     * This method retrieves the DID document associated with the specified DID key URL from the repository and returns it as a DidDocument object.
     *
     * @param didKeyUrl the DID key URL associated with the DID document
     * @return the retrieved DID document
     * @throws OpenDidException if the DID document cannot be retrieved
     */
    @Override
    public DidDocument findDidDoc(String didKeyUrl) {
        try {
            String did = DidUtil.extractDid(didKeyUrl);

            DidDocApiResDto didDocApiResDto = repositoryFeign.getDid(did);

            byte[] decodedDidDoc = BaseMultibaseUtil.decode(didDocApiResDto.getDidDoc());

            String didDocJson = new String(decodedDidDoc);
            DidManager didManager = BaseCoreDidUtil.parseDidDoc(didDocJson);

            return didManager.getDocument();
        } catch (Exception e) {
            log.error("Failed to find DID document.", e);
            throw new OpenDidException(ErrorCode.DID_DOCUMENT_RETRIEVAL_FAILED);
        }
    }
}
