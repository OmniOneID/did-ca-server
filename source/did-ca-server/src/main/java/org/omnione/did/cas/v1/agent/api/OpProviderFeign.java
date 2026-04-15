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

package org.omnione.did.cas.v1.agent.api;

import org.omnione.did.base.constants.UrlConstant;
import org.omnione.did.cas.v1.agent.api.dto.OpRevokeTokenReqDto;
import org.omnione.did.cas.v1.agent.api.dto.OpRevokeTokenResDto;
import org.omnione.did.cas.v1.agent.api.dto.OpValidateTokenReqDto;
import org.omnione.did.cas.v1.agent.api.dto.OpValidateTokenResDto;
import org.omnione.did.cas.v1.agent.api.dto.RequestOpTokenReqDto;
import org.omnione.did.cas.v1.agent.dto.CasToken;
import org.omnione.did.cas.v1.agent.dto.authenticate.RefreshTokenReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "OpProvider", url = "${op-provider.url}" + UrlConstant.Op.V1)
public interface OpProviderFeign {

    @PostMapping(value = UrlConstant.Op.ISSUE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    CasToken issueToken(@RequestBody RequestOpTokenReqDto requestDto);

    @PostMapping(value = UrlConstant.Op.REFRESH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    CasToken refreshToken(@RequestBody RefreshTokenReqDto requestDto);

    @PostMapping(value = UrlConstant.Op.VALIDATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    OpValidateTokenResDto validateToken(@RequestBody OpValidateTokenReqDto requestDto);

    @PostMapping(value = UrlConstant.Op.REVOKE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    OpRevokeTokenResDto revokeTokenBySubject(@RequestBody OpRevokeTokenReqDto requestDto);
}
