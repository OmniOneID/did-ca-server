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
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.db.constant.CasStatus;
import org.omnione.did.base.db.domain.Cas;
import org.omnione.did.base.db.domain.CertificateVc;
import org.omnione.did.base.db.repository.CasRepository;
import org.omnione.did.base.util.BaseMultibaseUtil;
import org.omnione.did.cas.v1.admin.dto.admin.GetCasInfoReqDto;
import org.omnione.did.cas.v1.admin.dto.cas.SendCertificateVcReqDto;
import org.omnione.did.cas.v1.admin.dto.cas.SendEntityInfoReqDto;
import org.omnione.did.cas.v1.admin.service.query.CasQueryService;
import org.omnione.did.cas.v1.agent.service.CertificateVcQueryService;
import org.omnione.did.cas.v1.agent.service.StorageService;
import org.omnione.did.cas.v1.common.dto.EmptyResDto;
import org.omnione.did.data.model.did.DidDocument;
import org.springframework.stereotype.Service;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class CasManagementService {
    private final CasQueryService casQueryService;
    private final StorageService storageService;
    private final CertificateVcQueryService certificateVcQueryService;
    private final CasRepository casRepository;

    public GetCasInfoReqDto getCasInfo() {
        Cas existingCas = casQueryService.findCas();

        if (existingCas.getStatus() != CasStatus.ACTIVATE) {
            return GetCasInfoReqDto.fromEntity(existingCas);
        }

        DidDocument didDocument = storageService.findDidDoc(existingCas.getDid());
        return GetCasInfoReqDto.fromEntity(existingCas, didDocument);
    }

    public EmptyResDto createCertificateVc(SendCertificateVcReqDto sendCertificateVcReqDto) {
        byte[] decodedVc = BaseMultibaseUtil.decode(sendCertificateVcReqDto.getCertificateVc());
        log.debug("Decoded VC: {}", new String(decodedVc));

        certificateVcQueryService.save(CertificateVc.builder()
                .vc(new String(decodedVc))
                .build());

        return new EmptyResDto();
    }

    public EmptyResDto updateEntityInfo(SendEntityInfoReqDto sendEntityInfoReqDto) {
        Cas existedCas = casQueryService.findCasOrNull();

        if (existedCas == null) {
            casRepository.save(Cas.builder()
                    .name(sendEntityInfoReqDto.getName())
                    .did(sendEntityInfoReqDto.getDid())
                    .status(CasStatus.ACTIVATE)
                    .serverUrl(sendEntityInfoReqDto.getServerUrl())
                    .certificateUrl(sendEntityInfoReqDto.getCertificateUrl())
                    .build());
        } else {
            existedCas.setName(sendEntityInfoReqDto.getName());
            existedCas.setDid(sendEntityInfoReqDto.getDid());
            existedCas.setServerUrl(sendEntityInfoReqDto.getServerUrl());
            existedCas.setCertificateUrl(sendEntityInfoReqDto.getCertificateUrl());
            existedCas.setStatus(CasStatus.ACTIVATE);
            casRepository.save(existedCas);
        }

        return new EmptyResDto();
    }
}
