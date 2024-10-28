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
import org.omnione.did.crypto.enums.DigestType;
import org.omnione.did.crypto.exception.CryptoException;
import org.omnione.did.crypto.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for generating hash values using various algorithms.
 * This class provides methods to generate SHA-256 hashes from byte arrays and strings.
 * It also allows generating hashes with specified algorithms.
 */
@Slf4j
public class BaseDigestUtil {

    /**
     * Generates a SHA-256 hash value from the given byte array input.
     *
     * <strong>Note:</strong> If the input is a string, it should be converted to a byte array using UTF-8 encoding
     * before being passed to this method.
     *
     * @param input The input byte array to hash. If the input is a string, convert it to a byte array using UTF-8 encoding.
     * @return The hash value as a byte array.
     * @throws OpenDidException if hash generation fails.
     */
    public static byte[] generateHash(byte[] input) {
        return generateHash(input, DigestType.SHA256);
    }

    /**
     * Generate a SHA-256 hash value from the given string input.
     * This method converts the input string to a byte array using UTF-8 encoding
     * before generating the hash value using the SHA-256 algorithm.
     *
     * @param input Input string to hash
     * @return Hash value as a byte array
     * @throws OpenDidException if hash generation fails
     */
    public static byte[] generateHash(String input) {
        return generateHash(input.getBytes(StandardCharsets.UTF_8), DigestType.SHA256);
    }

    /**
     * Generate a hash value from the given byte array input using the specified digest algorithm.
     * This method allows generating hash values using different digest algorithms.
     *
     * @param input Input byte array to hash
     * @param digestType Digest algorithm to use
     * @return Hash value as a byte array
     * @throws OpenDidException if hash generation fails
     */
    public static byte[] generateHash(byte[] input, DigestType digestType)  {
        try {
            return DigestUtils.getDigest(input, digestType);
        } catch (CryptoException e) {
            log.error("Failed to generate hash value.", e);
            throw new OpenDidException(ErrorCode.HASH_GENERATION_FAILED);
        }
    }
}
