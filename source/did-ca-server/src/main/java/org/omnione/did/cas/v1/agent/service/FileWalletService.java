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

import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.base.property.WalletProperty;
import org.omnione.did.base.util.BaseWalletUtil;
import org.omnione.did.wallet.exception.WalletException;
import org.omnione.did.wallet.key.WalletManagerInterface;
import org.springframework.stereotype.Service;
import org.omnione.did.base.exception.ErrorCode;

/**
 * The FileWalletService class provides methods for connecting to a file wallet and generating compact signatures.
 * It is designed to facilitate the connection to a file wallet and the generation of compact signatures for DID operations.
 */
@Service
@Slf4j
public class FileWalletService {
    private final WalletProperty walletProperty;
    private final WalletManagerInterface walletManager;

    public FileWalletService(WalletProperty walletProperty) {
        this.walletProperty = walletProperty;
        this.walletManager = BaseWalletUtil.getFileWalletManager();
    }

    /**
     * Connect to the file wallet.
     * This method connects to the file wallet using the wallet file path and password specified in the application properties.
     *
     * @throws OpenDidException if the connection to the wallet fails
     */
    public void connectToWallet() {
        try {
            walletManager.connect(walletProperty.getFilePath(), walletProperty.getPassword().toCharArray());
        } catch (WalletException e) {
            log.error("Failed to connect to wallet: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.WALLET_CONNECTION_FAILED);
        }
    }

    /**
     * Generate a compact signature for the given plain text using the specified key ID.
     *
     * @param keyId the key ID to use for signing
     * @param plainText the plain text to sign
     * @return the compact signature
     * @throws OpenDidException if the signature generation fails
     */
    public byte[] generateCompactSignature(String keyId, String plainText) {
        try {
            if (!walletManager.isConnect()) {
                log.info("Wallet manager disConnect. Connecting to wallet...");
                connectToWallet();
            }

            byte[] signature = BaseWalletUtil.generateCompactSignature(walletManager, keyId, plainText);
            log.info("Compact signature generated for keyId: {}", keyId);
            return signature;
        } catch (OpenDidException e) {
            throw e;
        } catch (Exception e) {
            throw new OpenDidException(ErrorCode.WALLET_SIGNATURE_GENERATION_FAILED);
        }
    }

}
