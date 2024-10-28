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

package org.omnione.did.base.datamodel.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;

import java.util.Arrays;

public enum SymmetricCipherType {
    @SerializedName("AES-128-CBC")
    AES_128_CBC("AES-128-CBC"),
    @SerializedName("AES-128-ECB")
    AES_128_ECB("AES-128-ECB"),
    @SerializedName("AES-256-CBC")
    AES_256_CBC("AES-256-CBC"),
    @SerializedName("AES-256-ECB")
    AES_256_ECB("AES-256-ECB");
    private final String displayName;

    SymmetricCipherType(String displayName) {
        this.displayName = displayName;
    }
    @Override
    @JsonValue
    public String toString() {
        return displayName;
    }

    public static SymmetricCipherType fromDisplayName(String displayName) {
        return Arrays.stream(SymmetricCipherType.values())
                .filter(type -> type.displayName.equals(displayName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant with displayName " + displayName));
    }

    public org.omnione.did.crypto.enums.SymmetricCipherType toOmnioneSymmetricCipherType() {
        return switch (this) {
            case AES_128_CBC -> org.omnione.did.crypto.enums.SymmetricCipherType.AES_128_CBC;
            case AES_128_ECB -> org.omnione.did.crypto.enums.SymmetricCipherType.AES_128_ECB;
            case AES_256_CBC -> org.omnione.did.crypto.enums.SymmetricCipherType.AES_256_CBC;
            case AES_256_ECB -> org.omnione.did.crypto.enums.SymmetricCipherType.AES_256_ECB;
        };
    }
}
