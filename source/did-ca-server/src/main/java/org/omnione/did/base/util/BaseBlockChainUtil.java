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

package org.omnione.did.base.util;

import lombok.extern.slf4j.Slf4j;
import org.omnione.did.ContractApi;
import org.omnione.did.ContractFactory;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.data.model.did.DidDocAndStatus;
import org.omnione.exception.BlockChainException;

/**
 * Utility class for blockchain operations.
 * This class provides methods to interact with the blockchain, including
 * initializing the blockchain, registering and retrieving DID documents,
 * and managing VC metadata and status.
 */
@Slf4j
public class BaseBlockChainUtil {

    private static ContractApi contractApiInstance = getContractApiInstance();

    /**
     * Initializes the blockchain connection.
     *
     * @return a ContractApi instance.
     * @throws OpenDidException if blockchain initialization fails.
     */
    public static ContractApi initBlockChain() {
        return ContractFactory.FABRIC.create("properties/blockchain.properties");
    }

    /**
     * Resets the ContractApi instance.
     * Use this method to reinitialize the blockchain connection.
     */
    public static ContractApi getContractApiInstance() {
        if (contractApiInstance == null) {
            synchronized (BaseBlockChainUtil.class) {
                if (contractApiInstance == null) {
                    contractApiInstance = initBlockChain();
                }
            }
        }
        return contractApiInstance;
    }


    /**
     * Retrieves a DID document and its status from the blockchain.
     *
     * @param didKeyUrl the DID key URL to search for.
     * @return the DID document and its status.
     * @throws OpenDidException if the DID document cannot be found.
     */
    public static DidDocAndStatus findDidDocument(String didKeyUrl) {
        try {
            ContractApi contractApi = getContractApiInstance();
            return (DidDocAndStatus) contractApi.getDidDoc(didKeyUrl);
        } catch (BlockChainException e) {
            log.error("Failed to get DID Document: " + e.getMessage());
            throw new OpenDidException(ErrorCode.BLOCKCHAIN_GET_DID_DOC_FAILED);
        }
    }


}
