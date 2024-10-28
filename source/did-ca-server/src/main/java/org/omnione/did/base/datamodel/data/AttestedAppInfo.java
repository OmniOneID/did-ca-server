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
import org.omnione.did.data.model.did.Proof;
import org.omnione.did.data.model.provider.Provider;

/**
 * Represents the attested app information data structure.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AttestedAppInfo {
    /**
     * The application ID.
     */
    private String appId;
    /**
     * The application name.
     */
    private Provider provider;
    /**
     * The application version.
     */
    private String nonce;

    private Proof proof;
}
