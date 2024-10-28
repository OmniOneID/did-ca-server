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
import lombok.experimental.SuperBuilder;
import org.omnione.did.data.model.did.Proof;
import org.omnione.did.data.model.provider.Provider;

/**
 * Represents a DID document.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class DidDoc {
    /**
     * The owner's DID document.
     * This field cannot be null.
     */
    @NotNull(message = "didDoc.ownerDidDoc cannot be null")
    private String ownerDidDoc;

    /**
     * The provider associated with this DID document.
     */
    private Provider provider;

    /**
     * The DID associate with this document.
     */
    private String did;

    /**
     * The cryptographic proof associated with this DID document.
     * This field must be valid and cannot be null.
     */
    @Valid
    @NotNull(message = "didDoc.proof cannot be null")
    private Proof proof;
}