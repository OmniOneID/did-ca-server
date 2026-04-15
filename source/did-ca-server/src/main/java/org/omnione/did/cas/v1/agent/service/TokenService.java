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

package org.omnione.did.cas.v1.agent.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.base.property.TasProperty;
import org.omnione.did.cas.v1.agent.api.OpProviderFeign;
import org.omnione.did.cas.v1.agent.api.dto.OpRevokeTokenReqDto;
import org.omnione.did.cas.v1.agent.api.dto.OpRevokeTokenResDto;
import org.omnione.did.cas.v1.agent.api.dto.OpValidateTokenReqDto;
import org.omnione.did.cas.v1.agent.api.dto.OpValidateTokenResDto;
import org.omnione.did.cas.v1.agent.api.dto.RequestOpTokenReqDto;
import org.omnione.did.cas.v1.agent.dto.CasToken;
import org.omnione.did.cas.v1.agent.dto.authenticate.RefreshTokenReqDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for issuing and revoking OP server tokens.
 * sub = SHA-256(loginId) is passed as the token subject.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

    private final OpProviderFeign opProviderFeign;
    private final TasProperty tasProperty;

    /**
     * Issues an OP token for the given pii (SHA-256(loginId)).
     *
     * @param pii       SHA-256 hash of loginId, used as JWT subject
     * @param casDomain domain of the CAS server, included in audience
     * @return issued CasToken
     */
    public CasToken generateToken(String pii, String casDomain) {
        log.debug("=== Starting generateToken ===");
        RequestOpTokenReqDto request = RequestOpTokenReqDto.builder()
                .sub(pii)
                .aud(List.of(casDomain, tasProperty.getUrl()))
                .build();
        try {
            CasToken response = opProviderFeign.issueToken(request);
            log.debug("*** Finished generateToken ***");
            return response;
        } catch (FeignException e) {
            log.error("Connection with OP server failed: status={}, body={}", e.status(), e.contentUTF8());
            throw new OpenDidException(ErrorCode.TOKEN_SERVICE_CONNECT_FAIL);
        } catch (Exception e) {
            log.error("Exception occurred while generating token: {}", e.getMessage(), e);
            throw new OpenDidException(ErrorCode.TOKEN_GENERATE_FAIL);
        }
    }

    /**
     * Validates an access token via the OP server and returns the sub (pii).
     *
     * @param accessToken the JWT access token to validate
     * @return the sub claim (SHA-256(loginId)) from the validated token
     */
    public String validateToken(String accessToken) {
        log.debug("=== Starting validateToken ===");
        OpValidateTokenReqDto request = OpValidateTokenReqDto.builder()
                .token(accessToken)
                .build();
        try {
            OpValidateTokenResDto response = opProviderFeign.validateToken(request);
            if (!response.isValid()) {
                log.error("Token validation failed: reason={}", response.getReason());
                throw new OpenDidException(ErrorCode.TOKEN_VALIDATE_FAIL);
            }
            log.debug("*** Finished validateToken: sub={}", response.getSub());
            return response.getSub();
        } catch (OpenDidException e) {
            throw e;
        } catch (FeignException e) {
            if (e.status() == HttpStatus.BAD_REQUEST.value()) {
                log.error("Token format invalid: {}", e.contentUTF8());
                throw new OpenDidException(ErrorCode.TOKEN_FORMAT_INVALID);
            } else if (e.status() == HttpStatus.UNAUTHORIZED.value()) {
                log.error("Token validation rejected by OP server: {}", e.contentUTF8());
                throw new OpenDidException(ErrorCode.TOKEN_VALIDATE_FAIL);
            }
            log.error("Connection with OP server failed: status={}, body={}", e.status(), e.contentUTF8());
            throw new OpenDidException(ErrorCode.TOKEN_SERVICE_CONNECT_FAIL);
        } catch (Exception e) {
            log.error("Exception occurred while validating token: {}", e.getMessage(), e);
            throw new OpenDidException(ErrorCode.TOKEN_VALIDATE_FAIL);
        }
    }

    /**
     * Refreshes an access token via the OP server using a refresh token.
     *
     * @param refreshToken the refresh token DTO
     * @return new CasToken (access_token, refresh_token)
     */
    public CasToken refreshToken(RefreshTokenReqDto refreshToken) {
        log.debug("=== Starting refreshToken ===");
        try {
            CasToken response = opProviderFeign.refreshToken(refreshToken);
            log.debug("*** Finished refreshToken ***");
            return response;
        } catch (OpenDidException e) {
            throw e;
        } catch (FeignException e) {
            if (e.status() == HttpStatus.UNAUTHORIZED.value()) {
                log.error("Token refresh rejected by OP server: {}", e.contentUTF8());
                throw new OpenDidException(ErrorCode.TOKEN_REFRESH_FAIL);
            } else if (e.status() == HttpStatus.BAD_REQUEST.value()) {
                log.error("Token format invalid: {}", e.contentUTF8());
                throw new OpenDidException(ErrorCode.TOKEN_FORMAT_INVALID);
            }
            log.error("Connection with OP server failed: status={}, body={}", e.status(), e.contentUTF8());
            throw new OpenDidException(ErrorCode.TOKEN_SERVICE_CONNECT_FAIL);
        } catch (Exception e) {
            log.error("Exception occurred while refreshing token: {}", e.getMessage(), e);
            throw new OpenDidException(ErrorCode.TOKEN_REFRESH_FAIL);
        }
    }

    /**
     * Revokes all OP tokens for the given pii (SHA-256(loginId)).
     *
     * @param pii SHA-256 hash of loginId, used as JWT subject
     */
    public void revokeToken(String pii) {
        log.debug("=== Starting revokeToken ===");
        OpRevokeTokenReqDto request = OpRevokeTokenReqDto.builder()
                .subject(pii)
                .reason("user_requested")
                .build();
        try {
            OpRevokeTokenResDto response = opProviderFeign.revokeTokenBySubject(request);
            if (!response.isRevoked()) {
                log.error("Revoke token failed: message={}", response.getMessage());
                throw new OpenDidException(ErrorCode.TOKEN_REVOKE_FAIL);
            }
            log.debug("*** Finished revokeToken ***");
        } catch (OpenDidException e) {
            throw e;
        } catch (FeignException e) {
            log.error("Connection with OP server failed: status={}, body={}", e.status(), e.contentUTF8());
            throw new OpenDidException(ErrorCode.TOKEN_SERVICE_CONNECT_FAIL);
        } catch (Exception e) {
            log.error("Exception occurred while revoking token: {}", e.getMessage(), e);
            throw new OpenDidException(ErrorCode.TOKEN_REVOKE_FAIL);
        }
    }
}
