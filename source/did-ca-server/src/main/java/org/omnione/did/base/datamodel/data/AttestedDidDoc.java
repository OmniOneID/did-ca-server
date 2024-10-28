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

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Represents an attested DID document, extending the base DidDoc class.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@JsonPropertyOrder({ "walletId", "ownerDidDoc", "provider", "nonce", "proof" })
public class AttestedDidDoc extends DidDoc {
    /**
     * The ID of the wallet associated with this attested DID document.
     */
    private String walletId;

    /**
     * A nonce value for the attested DID document.
     */
    private String nonce;
}