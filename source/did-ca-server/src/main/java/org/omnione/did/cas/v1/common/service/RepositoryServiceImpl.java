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

package org.omnione.did.cas.v1.common.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.base.util.BaseCoreDidUtil;
import org.omnione.did.base.util.BaseMultibaseUtil;
import org.omnione.did.cas.v1.agent.api.RepositoryFeign;
import org.omnione.did.cas.v1.agent.api.dto.DidDocApiResDto;
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
@Profile("lss")
public class RepositoryServiceImpl implements StorageService {
    private final RepositoryFeign repositoryFeign;
    /**
     * Finds a DID document by DID key URL.
     *
     * @param didKeyUrl The DID key URL of the document to find
     * @return The found DID document
     * @throws OpenDidException If the DID document cannot be found
     */
    @Override
    public DidDocument findDidDoc(String didKeyUrl) {
        try {
            String didDocument = repositoryFeign.getDid(didKeyUrl);

            DidManager didManager = BaseCoreDidUtil.parseDidDoc(didDocument);

            return didManager.getDocument();
        } catch (OpenDidException e) {
            log.error("Failed to find DID document.", e);
            throw e;
        } catch (FeignException e) {
            log.error("Failed to find DID document.", e);
            throw new OpenDidException(ErrorCode.BLOCKCHAIN_GET_DID_DOC_FAILED);
        } catch (Exception e) {
            log.error("Failed to find DID document.", e);
            throw new OpenDidException(ErrorCode.BLOCKCHAIN_GET_DID_DOC_FAILED);
        }
    }

}
