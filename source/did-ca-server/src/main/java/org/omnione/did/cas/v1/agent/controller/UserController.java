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
import org.omnione.did.cas.v1.agent.dto.CasToken;
import org.omnione.did.cas.v1.agent.dto.user.CheckUserRegStatusReqDto;
import org.omnione.did.cas.v1.agent.dto.user.CheckUserRegStatusResDto;
import org.omnione.did.cas.v1.agent.dto.user.SigninReqDto;
import org.omnione.did.cas.v1.agent.dto.user.SignupReqDto;
import org.omnione.did.cas.v1.agent.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     * Registers a new user using the client-provided userId.
     */
    @PostMapping(value = UrlConstant.Cas.SIGNUP)
    @ResponseBody
    public void signup(@RequestBody @Valid SignupReqDto reqDto) {
        userService.signup(reqDto);
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
     * Signs out the user. No-op: session state is managed client-side.
     */
    @PostMapping(value = UrlConstant.Cas.SIGNOUT)
    public void signout() {
        userService.signout();
    }
}
