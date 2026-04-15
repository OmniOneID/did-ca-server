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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.constants.UrlConstant;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.cas.v1.agent.dto.CasToken;
import org.omnione.did.cas.v1.agent.dto.authenticate.RefreshTokenReqDto;
import org.omnione.did.cas.v1.agent.dto.authenticate.VerifyTokenResDto;
import org.omnione.did.cas.v1.agent.service.TokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller handling JWT token verification and refresh for the OP-issued tokens.
 * Base path: /cas/api/v1/jwt
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = UrlConstant.Cas.AGENT_V1 + UrlConstant.Cas.JWT)
public class AuthenticationController {

    private final TokenService tokenService;

    /**
     * Validates an OP access token via the OP server.
     * Requires a valid Bearer token in the Authorization header.
     *
     * @return VerifyTokenResDto containing the sub (SHA-256(loginId))
     */
    @PostMapping(value = UrlConstant.Cas.JWT_VERIFY)
    @ResponseBody
    public VerifyTokenResDto verify(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new OpenDidException(ErrorCode.TOKEN_FORMAT_INVALID);
        }
        String accessToken = authorizationHeader.substring(7);
        String sub = tokenService.validateToken(accessToken);
        return VerifyTokenResDto.builder().sub(sub).build();
    }

    /**
     * Refreshes an OP access token using the provided refresh token.
     *
     * @return new CasToken (access_token, refresh_token)
     */
    @PostMapping(value = UrlConstant.Cas.JWT_REFRESH)
    @ResponseBody
    public CasToken refresh(@RequestBody @Valid RefreshTokenReqDto reqDto) {
        return tokenService.refreshToken(reqDto);
    }
}
