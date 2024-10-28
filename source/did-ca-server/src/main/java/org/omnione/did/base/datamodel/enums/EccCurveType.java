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
import org.omnione.did.crypto.enums.DidKeyType;

public enum EccCurveType {
    @SerializedName("Secp256k1")
    SECP_256_K1("Secp256k1"),
    @SerializedName("Secp256r1")
    SECP_256_R1("Secp256r1");

    private final String displayName;

    EccCurveType(String displayName) {
        this.displayName = displayName;
    }
    @Override
    @JsonValue
    public String toString() {
        return displayName;
    }

    public org.omnione.did.crypto.enums.EccCurveType toOmnioneEccCurveType() {
        switch (this) {
            case SECP_256_K1:
                return org.omnione.did.crypto.enums.EccCurveType.Secp256k1;
            case SECP_256_R1:
                return org.omnione.did.crypto.enums.EccCurveType.Secp256r1;
            default:
                throw new RuntimeException("Invalid ECC curve type");
        }
    }

    public DidKeyType toOmnioneDidKeyType() {
        switch (this) {
            case SECP_256_K1:
                return DidKeyType.SECP256K1_VERIFICATION_KEY_2018;
            case SECP_256_R1:
                return DidKeyType.SECP256R1_VERIFICATION_KEY_2018;
            default:
                return DidKeyType.RSA_VERIFICATION_KEY_2018;
        }
    }
}
