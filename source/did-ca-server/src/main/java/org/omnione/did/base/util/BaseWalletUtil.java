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
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.wallet.enums.WalletEncryptType;
import org.omnione.did.wallet.exception.WalletException;
import org.omnione.did.wallet.key.WalletManagerFactory;
import org.omnione.did.wallet.key.WalletManagerFactory.WalletManagerType;
import org.omnione.did.wallet.key.WalletManagerInterface;
import org.omnione.did.wallet.key.data.CryptoKeyPairInfo;


import java.nio.charset.StandardCharsets;


/**
 * Utility class for wallet operations.
 * This class provides methods for creating, connecting to, and managing wallets,
 * as well as performing cryptographic operations using wallet keys.
 */
@Slf4j
public class BaseWalletUtil {

    /**
     * Creates a new file-based wallet at the specified path with the given password.
     * The wallet is encrypted using AES-256-CBC with PKCS5 padding.
     *
     * @param walletFilePath Path to the wallet file.
     * @param password Password to protect the wallet.
     * @throws OpenDidException if the wallet creation fails.
     */
    public static void createFileWallet(String walletFilePath, String password) {
        try {
            WalletManagerInterface walletManager = WalletManagerFactory.getWalletManager(WalletManagerFactory.WalletManagerType.FILE);
            walletManager.create(walletFilePath, password.toCharArray(), WalletEncryptType.AES_256_CBC_PKCS5Padding);
        } catch (Exception e) {
            log.error("Failed to create wallet: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.WALLET_CONNECTION_FAILED);
        }
    }
    /**
     * Retrieves an instance of a file-based wallet manager.
     * The wallet manager is responsible for handling cryptographic operations
     * and key management using the keys stored in a file-based wallet.
     *
     * This method attempts to get a wallet manager of type FILE using the
     * WalletManagerFactory. If the wallet manager cannot be retrieved, an
     * OpenDidException is thrown.
     *
     * @return WalletManagerInterface Instance of the file-based wallet manager
     * @throws OpenDidException if the wallet manager cannot be retrieved
     */
    public static WalletManagerInterface getFileWalletManager() {
        try {
            return WalletManagerFactory.getWalletManager(WalletManagerType.FILE);
        } catch (WalletException e) {
            log.error("Failed to get Wallet Manager: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.FAILED_TO_GET_FILE_WALLET_MANAGER);
        }
    }
    /**
     * Connects to an existing file-based wallet using the specified path and password.
     * The wallet manager returned can be used for cryptographic operations.
     *
     * @param walletFilePath Path to the wallet file.
     * @param password Password to unlock the wallet.
     * @return WalletManagerInterface instance for managing wallet operations.
     * @throws OpenDidException if the wallet connection fails.
     */
    public static WalletManagerInterface connectFileWallet(String walletFilePath, String password) {
        try {
            WalletManagerInterface walletManager = WalletManagerFactory.getWalletManager(WalletManagerType.FILE);
            walletManager.connect(walletFilePath, password.toCharArray());

            return walletManager;
        } catch (WalletException e) {
            log.error("Failed to connect wallet: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.WALLET_CONNECTION_FAILED);
        }
    }

    /**
     * Generates a new key pair in the wallet with the specified key ID.
     * The key pair is generated using the SECP256r1 algorithm.
     *
     * @param walletManager WalletManagerInterface instance for managing wallet operations.
     * @param keyId Key ID for the new key pair.
     * @throws OpenDidException if key pair generation fails.
     */
    public static void generateKeyPair(WalletManagerInterface walletManager, String keyId) {
        try {
            walletManager.generateRandomKey(keyId, CryptoKeyPairInfo.KeyAlgorithmType.SECP256r1);
        } catch (WalletException e) {
            log.error("Failed to generate random keys: {} ", e.getMessage());
            throw new OpenDidException(ErrorCode.KEY_PAIR_GENERATION_FAILED);
        }
    }

    /**
     * Generates a compact signature for the given plain text using the specified key ID in the wallet.
     * The plain text is first converted to a byte array using UTF-8 encoding before signing.
     *
     * @param walletManager WalletManagerInterface instance for managing wallet operations.
     * @param keyId Key ID of the key to use for signing.
     * @param plainText Plain text to sign.
     * @return Compact signature as a byte array.
     */
    public static byte[] generateCompactSignature(WalletManagerInterface walletManager, String keyId, String plainText) {
        return generateCompactSignature(walletManager, keyId, plainText.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a compact signature for the given data using the specified key ID in the wallet.
     *
     * @param walletManager WalletManagerInterface instance for managing wallet operations.
     * @param keyId Key ID of the key to use for signing.
     * @param plainText Data to sign as a byte array.
     * @return Compact signature as a byte array.
     * @throws OpenDidException if signature generation fails.
     */
    public static byte[] generateCompactSignature(WalletManagerInterface walletManager, String keyId, byte[] plainText) {
        try {
            return walletManager.generateCompactSignatureFromHash(keyId, BaseDigestUtil.generateHash(plainText));
        } catch (WalletException e) {
            log.error("Failed to generate compact signature: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.SIGNATURE_GENERATION_FAILED);
        }
    }
}
