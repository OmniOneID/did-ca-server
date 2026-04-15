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

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.constants.UrlConstant;
import org.omnione.did.cas.v1.admin.dto.admin.AdminDto;
import org.omnione.did.cas.v1.admin.dto.admin.RequestAdminLoginReqDto;
import org.omnione.did.cas.v1.admin.service.JwtService;
import org.omnione.did.cas.v1.admin.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = UrlConstant.Cas.ADMIN_V1)
public class SessionController {

    private final SessionService sessionService;
    private final JwtService jwtService;

    /**
     * Admin login. Returns AdminDto fields (flat) + accessToken + refreshToken.
     * Frontend compatibility is maintained via @JsonUnwrapped on AdminDto.
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public LoginResDto requestAdminLogin(@Valid @RequestBody RequestAdminLoginReqDto requestAdminLoginReqDto) {
        AdminDto admin = sessionService.requestAdminLogin(requestAdminLoginReqDto);
        String accessToken = jwtService.createAccessToken(admin);
        String refreshToken = jwtService.createRefreshToken(admin.getId());
        return new LoginResDto(admin, accessToken, refreshToken);
    }

    /**
     * Issues a new access token using a valid refresh token.
     */
    @PostMapping(value = "/refresh-token")
    @ResponseBody
    public TokenResDto refreshToken(@RequestBody RefreshReqDto req) {
        var jws = jwtService.parse(req.refreshToken());
        if (!jwtService.isRefreshToken(jws)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }
        Long adminId = Long.valueOf(jws.getPayload().getSubject());
        AdminDto admin = sessionService.requestAdminLoginById(adminId);
        String newAccessToken = jwtService.createAccessToken(admin);
        return new TokenResDto(newAccessToken);
    }

    @Getter
    @AllArgsConstructor
    public static class LoginResDto {
        @JsonUnwrapped
        private final AdminDto admin;
        private final String accessToken;
        private final String refreshToken;
    }

    public record RefreshReqDto(String refreshToken) {}

    @Getter
    @AllArgsConstructor
    public static class TokenResDto {
        private final String accessToken;
    }
}
