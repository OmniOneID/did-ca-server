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

public enum ProofType {
    @SerializedName("RsaSignature2018")
    RSA_SIGNATURE_2018("RsaSignature2018"),
    @SerializedName("Secp256k1Signature2018")
    SECP_256K1_SIGNATURE_2018("Secp256k1Signature2018"),
    @SerializedName("Secp256r1Signature2018")
    SECP_256R1_SIGNATURE_2018("Secp256r1Signature2018");

    private final String displayName;

    ProofType(String displayName) {
        this.displayName = displayName;
    }
    @Override
    @JsonValue
    public String toString() {
        return displayName;
    }
}
