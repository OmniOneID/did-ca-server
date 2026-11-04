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
import org.omnione.did.base.annotation.ArticleLog;
import org.omnione.did.base.constants.ActionType;
import org.omnione.did.base.constants.ApiType;
import org.omnione.did.base.constants.UrlConstant;
import org.omnione.did.cas.v1.agent.dto.user.SignupReqDto;
import org.omnione.did.cas.v1.agent.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller handling user registration endpoints.
 * Base path: /cas/api/v1/user
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = UrlConstant.Cas.AGENT_V1 + UrlConstant.Cas.USER)
public class UserController {

    private final UserService userService;

    /**
     * Registers a new user using the client-provided userId.
     */
    @PostMapping(value = UrlConstant.Cas.SIGNUP)
    @ResponseBody
    @ArticleLog(name = "[User] Sign Up", apiType = ApiType.AGENT, targetType = "USER", actionType = ActionType.CREATE)
    public void signup(@RequestBody @Valid SignupReqDto reqDto) {
        userService.signup(reqDto);
    }
}
