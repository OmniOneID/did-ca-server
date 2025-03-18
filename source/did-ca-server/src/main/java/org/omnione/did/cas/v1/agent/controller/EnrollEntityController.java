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

package org.omnione.did.cas.v1.agent.controller;

import lombok.RequiredArgsConstructor;
import org.omnione.did.base.constants.UrlConstant;
import org.omnione.did.cas.v1.agent.dto.EnrollEntityReqDto;
import org.omnione.did.cas.v1.agent.dto.EnrollEntityResDto;
import org.omnione.did.cas.v1.agent.service.EnrollEntityService;
import org.omnione.did.cas.v1.agent.service.VcService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The EnrollEntityController class is a controller that handles the enroll entity request.
 * It provides endpoints for enrolling an entity and requesting a certificate verifiable credential.
 *
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = UrlConstant.Cas.AGENT_V1)
public class EnrollEntityController {
    private final EnrollEntityService enrollEntityService;
    private final VcService vcService;

    /**
     * Enroll an entity.
     *
     * @param request the request to enroll an entity
     * @return the response to enrolling an entity
     */
    @PostMapping("/certificate-vc")
    public EnrollEntityResDto enrollEntity(@RequestBody EnrollEntityReqDto request) {
        return enrollEntityService.enrollEntity();
    }

    /**
     * Request a certificate verifiable credential.
     *
     * @return the certificate verifiable credential
     */
    @GetMapping("/certificate-vc")
    public String requestCertificateVc() {
        return vcService.requestCertificateVc();
    }
}
