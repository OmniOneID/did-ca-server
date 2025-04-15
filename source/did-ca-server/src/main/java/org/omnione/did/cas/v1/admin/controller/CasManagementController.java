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

package org.omnione.did.cas.v1.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.constants.UrlConstant;
import org.omnione.did.cas.v1.admin.dto.cas.CaInfoResDto;
import org.omnione.did.cas.v1.admin.dto.cas.RegisterCaInfoReqDto;
import org.omnione.did.cas.v1.admin.dto.cas.RequestEntityStatusResDto;
import org.omnione.did.cas.v1.admin.dto.cas.RequestRegisterDidReqDto;
import org.omnione.did.cas.v1.admin.dto.cas.SendCertificateVcReqDto;
import org.omnione.did.cas.v1.admin.dto.cas.SendEntityInfoReqDto;
import org.omnione.did.cas.v1.admin.service.CasManagementService;
import org.omnione.did.cas.v1.common.dto.EmptyResDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = UrlConstant.Cas.ADMIN_V1)
public class CasManagementController {
    private final CasManagementService casManagementService;

    @Operation(summary = "Get CAS Info", description = "get CAS Info")
    @GetMapping("/ca/info")
    public CaInfoResDto getCasInfo() {
        return casManagementService.getCasInfo();
    }

    @RequestMapping(value = "/certificate-vc", method = RequestMethod.POST)
    public EmptyResDto createCertificateVc(@RequestBody SendCertificateVcReqDto sendCertificateVcReqDto) {
        return casManagementService.createCertificateVc(sendCertificateVcReqDto);
    }

    @RequestMapping(value = "/entity-info", method = RequestMethod.POST)
    public EmptyResDto updateEntityInfo(@RequestBody SendEntityInfoReqDto sendEntityInfoReqDto) {
        return casManagementService.updateEntityInfo(sendEntityInfoReqDto);
    }

    @RequestMapping(value = "/ca/register-ca-info", method = RequestMethod.POST)
    public CaInfoResDto registerCaInfo(@RequestBody RegisterCaInfoReqDto registerCaInfoReqDto) {
        return casManagementService.registerCaInfo(registerCaInfoReqDto);
    }

    @RequestMapping(value = "/ca/generate-did-auto", method = RequestMethod.POST)
    public Map<String, Object> generateCaDidDocumentAuto() {
        return casManagementService.registerCaDidDocumentAuto();
    }

    @RequestMapping(value = "/ca/register-did", method = RequestMethod.POST)
    public EmptyResDto requestRegisterDid(@RequestBody RequestRegisterDidReqDto requestRegisterDidReqDto) {
        return casManagementService.requestRegisterDid(requestRegisterDidReqDto);
    }

    @GetMapping(value = "/ca/request-status")
    public RequestEntityStatusResDto requestEntityStatus() {
        return casManagementService.requestEntityStatus();
    }
}
