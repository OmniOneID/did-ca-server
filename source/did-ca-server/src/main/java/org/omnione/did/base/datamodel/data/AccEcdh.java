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

import lombok.*;
import org.omnione.did.base.datamodel.enums.SymmetricCipherType;
import org.omnione.did.base.datamodel.enums.SymmetricPaddingType;
import org.omnione.did.data.model.did.Proof;

/**
 * Represents the Accept E2E (End-to-End) data structure.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AccEcdh {
    /**
     * The server that is accepting the ECDH key exchange.
     */
    private String server;
    /**
     * The server nonce used for the ECDH key exchange.
     */
    private String serverNonce;
    /**
     * The public key used for ECDH key exchange.
     */
    private String publicKey;
    /**
     * The cipher used for ECDH key exchange.
     */
    private SymmetricCipherType cipher;
    /**
     * The padding used for ECDH key exchange.
     */
    private SymmetricPaddingType padding;
    /**
     * The cryptographic proof associated with this ECDH key exchange.
     */
    private Proof proof;

}
