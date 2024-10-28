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
import org.omnione.did.base.datamodel.enums.EccCurveType;
import org.omnione.did.base.datamodel.enums.SymmetricCipherType;
import org.omnione.did.base.datamodel.enums.SymmetricPaddingType;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.crypto.engines.CipherInfo;

import org.omnione.did.crypto.exception.CryptoException;
import org.omnione.did.crypto.keypair.KeyPairInterface;
import org.omnione.did.crypto.util.CryptoUtils;
import org.omnione.did.crypto.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Utility class for cryptographic operations.
 * This class provides methods for generating key pairs, nonces, shared secrets, and signatures,
 * as well as encrypting and decrypting data using symmetric and asymmetric encryption algorithms.
 */
@Slf4j
public class BaseCryptoUtil {

    /**
     * Generate a key pair.
     * The key pair is generated using the specified ECC curve type.
     *
     * @param eccCurveType The ECC curve type to use for key pair generation
     * @return The generated key pair
     * @throws OpenDidException if key pair generation fails
     */
    public static KeyPairInterface generateKeyPair(EccCurveType eccCurveType) {
        try {
            return CryptoUtils.generateKeyPair(eccCurveType.toOmnioneDidKeyType());
        } catch (Exception e) {
            log.error("Failed to generate key pair: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.KEY_PAIR_GENERATION_FAILED);
        }
    }


    /**
     * Generate a nonce.
     * The nonce is generated with the specified length.
     *
     * @param length The length of the nonce to generate
     * @return The generated nonce
     * @throws OpenDidException if nonce generation fails
     */
    public static byte[] generateNonce(int length) {
        try {
            return CryptoUtils.generateNonce(length);
        } catch (Exception e) {
            log.error("Failed to generate nonce: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.NONCE_GENERATION_FAILED);
        }
    }


    /**
     * Merge two nonces.
     * The nonces are merged by concatenating them together.
     *
     * @param clientNonce The client nonce
     * @param serverNonce The server nonce
     * @return The merged nonce
     * @throws OpenDidException if nonce merge fails
     */
    public static byte[] mergeNonce(byte[] clientNonce, byte[] serverNonce) {
        try {
            return DigestUtils.mergeNonce(clientNonce, serverNonce);
        } catch (Exception e) {
            log.error("Failed to merge nonce: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.NONCE_MERGE_FAILED);
        }
    }

    /**
     * Merge a nonce using client nonce and server nonce.
     *
     * @param encodedClientNonce Encoded client nonce
     * @param encodedServerNonce Encoded server nonce
     * @return Merged nonce
     */
    public static byte[] mergeNonce(String encodedClientNonce, String encodedServerNonce) {
        return mergeNonce(BaseMultibaseUtil.decode(encodedClientNonce), BaseMultibaseUtil.decode(encodedServerNonce));
    }



    /**
     * Generate a shared secret.
     * The shared secret is generated using the public key and private key.
     *
     * @param publicKey The public key
     * @param privateKey The private key
     * @param curveType The ECC curve type
     * @return The generated shared secret
     * @throws OpenDidException if shared secret generation fails
     */
    public static byte[] generateSharedSecret(byte[] publicKey, byte[] privateKey, EccCurveType curveType) {
        try {
            return CryptoUtils.generateSharedSecret(publicKey, privateKey, curveType.toOmnioneEccCurveType());
        } catch (CryptoException e) {
            log.error("Failed to generate shared secret: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.SESSION_KEY_GENERATION_FAILED);
        }
    }

    /**
     * Merge a shared secret and nonce.
     * The shared secret and nonce are merged by hashing them together.
     * The length of the result is determined by the symmetric cipher type.
     *
     * @param sharedSecret The shared secret
     * @param nonce The nonce
     * @param symmetricCipherType The symmetric cipher type
     * @return The merged shared secret and nonce
     * @throws OpenDidException if shared secret and nonce merge fails
     * @throws OpenDidException if invalid symmetric cipher type
     */
    public static byte[] mergeSharedSecretAndNonce(byte[] sharedSecret, byte[] nonce, SymmetricCipherType symmetricCipherType) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(sharedSecret, 0, sharedSecret.length);
            digest.update(nonce, 0, nonce.length);

            byte[] combinedResult = digest.digest();

            return switch (symmetricCipherType) {
                case AES_128_CBC, AES_128_ECB -> Arrays.copyOfRange(combinedResult, 0, 16);
                case AES_256_CBC, AES_256_ECB -> Arrays.copyOfRange(combinedResult, 0, 32);
                default -> throw new RuntimeException("Invalid symmetric cipher type: " + symmetricCipherType);
            };
        } catch (NoSuchAlgorithmException e) {
            log.error("Failed to merge shared secret and nonce: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.NONCE_AND_SHARED_SECRET_MERGE_FAILED);
        }
    }


    /**
     * Decrypt data.
     *
     * @param encrypteData The encrypted data to decrypt
     * @param key The key to use for decryption
     * @param iv The initial vector to use for decryption
     * @param symmetricCipherType The symmetric cipher type
     * @param symmetricPaddingType The symmetric padding type
     * @return The decrypted data
     */
    public static byte[] decrypt(String encrypteData, byte[] key, byte[] iv, SymmetricCipherType symmetricCipherType, SymmetricPaddingType symmetricPaddingType) {
        return decrypt(BaseMultibaseUtil.decode(encrypteData), key, iv, symmetricCipherType, symmetricPaddingType);
    }

    /**
     * Decrypt data.
     *
     * @param encryptData The encrypted data to decrypt
     * @param key The key to use for decryption
     * @param iv The initial vector to use for decryption
     * @param symmetricCipherType The symmetric cipher type
     * @param symmetricPaddingType The symmetric padding type
     * @return The decrypted data
     * @throws OpenDidException if decryption fails
     */
    public static byte[] decrypt(byte[] encryptData, byte[] key, byte[] iv, SymmetricCipherType symmetricCipherType, SymmetricPaddingType symmetricPaddingType) {
        try {
            CipherInfo cipherInfo = new CipherInfo(symmetricCipherType.toOmnioneSymmetricCipherType(), symmetricPaddingType.toOmnioneSymmetricPaddingType());
            return CryptoUtils.decrypt(encryptData, cipherInfo, key, iv);
        } catch (CryptoException e) {
            log.error("Failed to decrypt data: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.DECRYPTION_FAILED);
        }
    }

}
