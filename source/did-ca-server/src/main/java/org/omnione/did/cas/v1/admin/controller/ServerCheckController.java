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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.constants.UrlConstant;
import org.omnione.did.cas.v1.admin.dto.server.VerifyServerUrlReqDto;
import org.omnione.did.cas.v1.admin.dto.server.VerifyServerUrlResDto;
import org.omnione.did.cas.v1.admin.service.UrlPingChecker;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = UrlConstant.Cas.ADMIN_V1)
public class ServerCheckController {

    private final UrlPingChecker urlPingChecker;

    @RequestMapping(value = "/servers/ping", method = RequestMethod.POST)
    public VerifyServerUrlResDto verifyServerUrl(@RequestBody VerifyServerUrlReqDto verifyServerUrlReqDto) {
        return urlPingChecker.isUrlReachable(verifyServerUrlReqDto);
    }
}
