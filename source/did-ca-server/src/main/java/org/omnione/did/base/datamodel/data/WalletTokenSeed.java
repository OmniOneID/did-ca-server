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

import jakarta.validation.constraints.NotNull;
import org.omnione.did.base.datamodel.enums.WalletTokenPurpose;
import lombok.*;

/**
 * Represents the seed data for a wallet token.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class WalletTokenSeed {
    /**
     * The purpose of the wallet token.
     * This field cannot be null.
     */
    @NotNull(message = "purpose is required")
    WalletTokenPurpose purpose;

    /**
     * The package name associated with the wallet token.
     * This field cannot be null.
     */
    @NotNull(message = "pkgName is required")
    String pkgName;

    /**
     * A nonce value for the wallet token seed.
     * This field cannot be null.
     */
    @NotNull(message = "nonce is required")
    String nonce;

    /**
     * The valid until date for the wallet token seed.
     * This field cannot be null.
     */
    @NotNull(message = "validUntil is required")
    String validUntil;

    /**
     * The user ID associated with the wallet token seed.
     */
    String userId;
}
