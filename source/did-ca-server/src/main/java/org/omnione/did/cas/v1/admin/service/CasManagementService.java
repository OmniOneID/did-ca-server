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

package org.omnione.did.cas.v1.admin.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.omnione.did.base.db.constant.CasStatus;
import org.omnione.did.base.db.domain.Cas;
import org.omnione.did.cas.v1.admin.dto.admin.GetCasInfoReqDto;
import org.omnione.did.cas.v1.common.service.query.CasQueryService;
import org.omnione.did.cas.v1.agent.service.StorageService;
import org.omnione.did.data.model.did.DidDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Transactional
@RequiredArgsConstructor
@Service
public class CasManagementService {
    private final CasQueryService casQueryService;
    private final StorageService storageService;

    @RequestMapping(value = "/ca/info", method = RequestMethod.GET)
    public GetCasInfoReqDto getCasInfo() {
        Cas existingCas = casQueryService.findCas();

        if (existingCas.getStatus() != CasStatus.ACTIVATE) {
            return GetCasInfoReqDto.fromEntity(existingCas);
        }

        DidDocument didDocument = storageService.findDidDoc(existingCas.getDid());
        return GetCasInfoReqDto.fromEntity(existingCas, didDocument);
    }
}
