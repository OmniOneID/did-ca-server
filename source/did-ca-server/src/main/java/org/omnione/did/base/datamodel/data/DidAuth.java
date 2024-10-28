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
import org.omnione.did.data.model.did.Proof;

/**
 * Represents a DID authentication structure.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DidAuth {
    /**
     * The DID associate with this authentication.
     * This field cannot be null.
     */
    @NotNull(message = "didAuth.id cannot be null")
    private String did;

    /**
     * The authentication nonce.
     * This field cannot be null.
     */
    @NotNull(message = "didAuth.authNonce cannot be null")
    private String authNonce;

    /**
     * The cryptographic proof associated with this DID authentication.
     * This field must be valid.
     */
    @Valid
    private Proof proof;
}