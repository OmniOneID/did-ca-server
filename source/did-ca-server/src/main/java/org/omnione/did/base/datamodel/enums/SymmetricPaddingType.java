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

import java.util.Arrays;

public enum SymmetricPaddingType {
    @SerializedName("NOPAD")
    NOPAD("NOPAD"),
    @SerializedName("PKCS5")
    PKCS5("PKCS5");

    private final String displayName;

    SymmetricPaddingType(String displayName) {
        this.displayName = displayName;
    }
    @Override
    @JsonValue
    public String toString() {
        return displayName;
    }

    public static SymmetricPaddingType fromDisplayName(String displayName) {
        return Arrays.stream(SymmetricPaddingType.values())
                .filter(type -> type.displayName.equals(displayName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant with displayName " + displayName));
    }

    public org.omnione.did.crypto.enums.SymmetricPaddingType toOmnioneSymmetricPaddingType() {
        switch (this) {
            case NOPAD:
                return org.omnione.did.crypto.enums.SymmetricPaddingType.NOPAD;
            case PKCS5:
                return org.omnione.did.crypto.enums.SymmetricPaddingType.PKCS5;
            default:
                throw new RuntimeException("Invalid symmetric padding type");
        }
    }
}
