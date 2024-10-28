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
import org.omnione.did.base.validation.NonEmptyList;

import java.util.List;

/**
 * Represents a candidate with a list of supported symmetric cipher types.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Candidate {
    /**
     * List of supported symmetric cipher types.
     * This list must not be empty.
     */
    @NonEmptyList(message = "candidate.ciphers must not be empty")
    private List<SymmetricCipherType> ciphers;
}