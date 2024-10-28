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

package org.omnione.did.base.datamodel.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.omnione.did.base.datamodel.enums.EccCurveType;
import org.omnione.did.data.model.DataObject;
import org.omnione.did.data.model.did.Proof;
import org.omnione.did.data.model.util.json.GsonWrapper;

/**
 * Represents ECDH request data, extending the IWObject class.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class EcdhReqData extends DataObject {
    /**
     * The client identifier.
     * This field cannot be null.
     */
    @NotNull(message = "reqEcdh.client cannot be null")
    private String client;

    /**
     * The client nonce.
     * This field cannot be null.
     */
    @NotNull(message = "reqEcdh.clientNonce cannot be null")
    private String clientNonce;

    /**
     * The ECC curve type.
     * This field cannot be null.
     */
    @NotNull(message = "reqEcdh.curve cannot be null")
    private EccCurveType curve;

    /**
     * The public key for ECDH.
     * This field cannot be null.
     */
    @NotNull(message = "reqEcdh.publicKey cannot be null")
    private String publicKey;

    /**
     * The candidate information.
     * This field must be valid.
     */
    @Valid
    private Candidate candidate;

    /**
     * The cryptographic proof associated with this ECDH request.
     * This field must be valid.
     */
    @Valid
    private Proof proof;

    /**
     * Deserializes JSON string into EcdhReqData object.
     *
     * @param s The JSON string to deserialize.
     */
    @Override
    public void fromJson(String s) {
        GsonWrapper gson = new GsonWrapper();
        EcdhReqData reqData = gson.fromJson(s, this.getClass());

        this.client = reqData.client;
        this.clientNonce = reqData.clientNonce;
        this.curve = reqData.curve;
        this.publicKey = reqData.publicKey;
        this.candidate = reqData.candidate;
        this.proof = reqData.proof;
    }
}