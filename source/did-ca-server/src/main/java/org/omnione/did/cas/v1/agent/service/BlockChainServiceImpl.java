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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.ContractApi;
import org.omnione.did.ContractFactory;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.base.property.BlockchainProperty;
import org.omnione.did.data.model.did.DidDocAndStatus;
import org.omnione.did.data.model.did.DidDocument;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * The BlockChainServiceImpl class provides methods for registering and retrieving DID Documents.
 * It is designed to facilitate the storage and retrieval of DID Documents, ensuring that the data is accurate and up-to-date.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Profile("!repository")
public class BlockChainServiceImpl implements StorageService {

    private ContractApi contractApiInstance = null;
    private final BlockchainProperty blockchainProperty;

    /**
     * Initializes the blockchain connection.
     *
     * @return a ContractApi instance.
     */
    public ContractApi initBlockChain() {
        return ContractFactory.FABRIC.create(blockchainProperty.getFilePath());
    }

    /**
     * Resets the ContractApi instance.
     * Use this method to reinitialize the blockchain connection.
     */
    public ContractApi getContractApiInstance() {
        if (contractApiInstance == null) {
            synchronized (BlockChainServiceImpl.class) {
                if (contractApiInstance == null) {
                    contractApiInstance = initBlockChain();
                }
            }
        }
        return contractApiInstance;
    }

    /**
     * Register the given DID Document with the blockchain.
     *
     * @param didKeyUrl the DID key URL
     */
    @Override
    public DidDocument findDidDoc(String didKeyUrl) {
        try {
            ContractApi contractApi = getContractApiInstance();
            DidDocAndStatus didDocAndStatus = (DidDocAndStatus) contractApi.getDidDoc(didKeyUrl);

            return didDocAndStatus.getDocument();
        } catch (Exception e) {
            log.error("Failed to find DID Document: " + e.getMessage());
            throw new OpenDidException(ErrorCode.DID_DOCUMENT_RETRIEVAL_FAILED);
        }
    }
}
