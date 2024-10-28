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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.omnione.did.common.util.HexUtil;
import org.omnione.did.common.util.NonceGenerator;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

/**
 * Random Utility
 * This class provides methods to generate random values.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RandomUtil {

    /**
     * Generate a UUID.
     * @return UUID as a string
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
    /**
     * Generate a message ID.
     * @return Message ID as a string
     */
    public static String generateMessageId() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentDateAndTime = dateFormat.format(new Date());

        long currentTimeMillis = System.currentTimeMillis();
        long microsecond = (currentTimeMillis % 1000) * 1000;

        String randomHexString = generateUUID()
                .replace("-", "")
                .substring(0, 8);

        return currentDateAndTime +
                String.format("%06d", microsecond) +
                randomHexString;
    }


}
