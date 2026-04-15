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
import org.omnione.did.cas.v1.agent.dto.user.CheckUserRegStatusReqDto;
import org.omnione.did.cas.v1.agent.dto.user.CheckUserRegStatusResDto;
import org.omnione.did.cas.v1.agent.dto.user.SigninReqDto;
import org.omnione.did.cas.v1.agent.dto.user.SignupReqDto;
import org.omnione.did.cas.v1.agent.service.TokenService;
import org.omnione.did.cas.v1.agent.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller handling user registration and authentication endpoints.
 * Base path: /cas/api/v1/user
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = UrlConstant.Cas.AGENT_V1 + UrlConstant.Cas.USER)
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;

    /**
     * Checks the user registration status for the given loginId + walletId combination.
     * Returns one of: NEW, CURRENT, OTHER_DEVICE, USED_BY_OTHER
     */
    @PostMapping(value = UrlConstant.Cas.CHECK_REGISTRATION_STATUS)
    @ResponseBody
    public CheckUserRegStatusResDto checkRegistrationStatus(
            @RequestBody @Valid CheckUserRegStatusReqDto reqDto) {
        return userService.checkRegistrationStatus(reqDto);
    }

    /**
     * Registers a new user (status must be NEW).
     * Returns issued OP JWT token pair.
     */
    @PostMapping(value = UrlConstant.Cas.SIGNUP)
    @ResponseBody
    public CasToken signup(@RequestBody @Valid SignupReqDto reqDto) {
        return userService.signup(reqDto);
    }

    /**
     * Authenticates an existing user (status must be CURRENT or OTHER_DEVICE).
     * For OTHER_DEVICE, the existing device record is replaced after password verification.
     * Returns issued OP JWT token pair.
     */
    @PostMapping(value = UrlConstant.Cas.SIGNIN)
    @ResponseBody
    public CasToken signin(@RequestBody @Valid SigninReqDto reqDto) {
        return userService.signin(reqDto);
    }

    /**
     * Signs out the user by revoking their OP token.
     * Requires a valid Bearer token in the Authorization header.
     */
    @PostMapping(value = UrlConstant.Cas.SIGNOUT)
    public void signout(@RequestHeader("Authorization") String authorizationHeader) {
        String pii = extractAndValidatePii(authorizationHeader);
        userService.signout(pii);
    }

    /**
     * Withdraws (deregisters) the user: deletes User and UserPii records, revokes OP token.
     * Requires a valid Bearer token in the Authorization header.
     */
    @PostMapping(value = UrlConstant.Cas.WITHDRAW)
    public void withdraw(@RequestHeader("Authorization") String authorizationHeader) {
        String pii = extractAndValidatePii(authorizationHeader);
        userService.withdraw(pii);
    }

    /**
     * Extracts the Bearer token from the Authorization header, validates it via OP server,
     * and returns the sub (pii = SHA-256(loginId)).
     */
    private String extractAndValidatePii(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new OpenDidException(ErrorCode.TOKEN_FORMAT_INVALID);
        }
        String accessToken = authorizationHeader.substring(7);
        return tokenService.validateToken(accessToken);
    }
}
