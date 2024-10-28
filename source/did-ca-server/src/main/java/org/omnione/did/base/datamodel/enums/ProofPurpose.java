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

public enum ProofPurpose {
    @SerializedName("assertionMethod")
    ASSERTION_METHOD("assertionMethod"),
    @SerializedName("authentication")
    AUTHENTICATION("authentication"),
    @SerializedName("keyAgreement")
    KEY_AGREEMENT("keyAgreement"),
    @SerializedName("capabilityInvocation")
    CAPABILITY_INVOCATION("capabilityInvocation"),
    @SerializedName("capabilityDelegation")
    CAPABILITY_DELEGATION("capabilityDelegation");

    private final String displayName;

    ProofPurpose(String displayName) {
        this.displayName = displayName;
    }
    @Override
    @JsonValue
    public String toString() {
        return displayName;
    }

    public String toKeyId() {
        switch (this) {
            case ASSERTION_METHOD:
                return "assert";
            case AUTHENTICATION:
                return "auth";
            case KEY_AGREEMENT:
                return "keyagree";
            case CAPABILITY_INVOCATION:
                return "invoke";
            default:
                throw new OpenDidException(ErrorCode.INVALID_PROOF_PURPOSE);
        }
    }

    public static ProofPurpose fromDisplayName(String displayName) {
        for (ProofPurpose purpose : ProofPurpose.values()) {
            if (purpose.displayName.equalsIgnoreCase(displayName)) {
                return purpose;
            }
        }
        throw new OpenDidException(ErrorCode.INVALID_PROOF_PURPOSE);
    }
}
