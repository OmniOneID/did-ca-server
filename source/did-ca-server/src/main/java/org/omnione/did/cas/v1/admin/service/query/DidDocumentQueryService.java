/*
 * Copyright 2025 OmniOne.
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
package org.omnione.did.cas.v1.admin.service.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.db.domain.EntityDidDocument;
import org.omnione.did.base.db.repository.DidDocumentRepository;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DidDocumentQueryService {
    private final DidDocumentRepository didDocumentRepository;

    public EntityDidDocument findDidDocument() {
        return didDocumentRepository.findTop1ByOrderByIdDesc()
                .orElseThrow(() -> new OpenDidException(ErrorCode.CAS_DID_DOCUMENT_NOT_FOUND));
    }

    public EntityDidDocument findDidDocumentOrNull() {
        return didDocumentRepository.findTop1ByOrderByIdDesc().orElse(null);
    }
}
