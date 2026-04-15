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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.datamodel.enums.UserRegistrationStatus;
import org.omnione.did.base.db.domain.User;
import org.omnione.did.base.db.domain.UserPii;
import org.omnione.did.base.db.repository.UserPiiRepository;
import org.omnione.did.base.db.repository.UserRepository;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.base.property.OpProviderProperty;
import org.omnione.did.base.util.BaseDigestUtil;
import org.omnione.did.cas.v1.agent.dto.CasToken;
import org.omnione.did.cas.v1.agent.dto.user.CheckUserRegStatusReqDto;
import org.omnione.did.cas.v1.agent.dto.user.CheckUserRegStatusResDto;
import org.omnione.did.cas.v1.agent.dto.user.SigninReqDto;
import org.omnione.did.cas.v1.agent.dto.user.SignupReqDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HexFormat;

/**
 * Service handling user registration, authentication, signout, and withdrawal.
 * <p>
 * PII = HexFormat(SHA-256(loginId)) is stored in user_pii and used as the JWT sub.
 * Passwords are bcrypt-hashed before storage.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserPiiRepository userPiiRepository;
    private final TokenService tokenService;
    private final OpProviderProperty opProviderProperty;

    /**
     * Computes pii = HexFormat(SHA-256(loginId)).
     */
    private String computePii(String loginId) {
        byte[] hash = BaseDigestUtil.generateHash(loginId);
        return HexFormat.of().formatHex(hash);
    }

    /**
     * Checks the registration status for a given loginId + walletId combination.
     * <p>
     * Priority order (highest first):
     * <ol>
     *   <li>USED_BY_OTHER — walletId is registered to a different loginId (this device used by another user)</li>
     *   <li>OTHER_DEVICE  — loginId is registered to a different walletId (this account is on another device)</li>
     *   <li>CURRENT       — loginId and walletId are both registered together</li>
     *   <li>NEW           — neither loginId nor walletId is registered</li>
     * </ol>
     * When USED_BY_OTHER and OTHER_DEVICE conditions both apply simultaneously,
     * USED_BY_OTHER takes precedence for security reasons.
     *
     * @param reqDto loginId and walletId to check
     * @return CheckUserRegStatusResDto containing the resolved UserRegistrationStatus
     */
    public CheckUserRegStatusResDto checkRegistrationStatus(CheckUserRegStatusReqDto reqDto) {
        log.debug("=== Starting checkRegistrationStatus: loginId={}", reqDto.getLoginId());

        String loginId = reqDto.getLoginId();
        String walletId = reqDto.getWalletId();

        boolean loginIdExists = userRepository.existsByUserIdentifier(loginId);
        boolean walletIdExists = userRepository.existsByWalletId(walletId);
        boolean exactPairExists = userRepository.existsByUserIdentifierAndWalletId(loginId, walletId);

        UserRegistrationStatus status;

        if (walletIdExists && !exactPairExists) {
            // walletId belongs to a different loginId — this device is in use by another user
            status = UserRegistrationStatus.USED_BY_OTHER;
        } else if (loginIdExists && !exactPairExists) {
            // loginId belongs to a different walletId — this account is registered on another device
            status = UserRegistrationStatus.OTHER_DEVICE;
        } else if (exactPairExists) {
            status = UserRegistrationStatus.CURRENT;
        } else {
            status = UserRegistrationStatus.NEW;
        }

        log.debug("*** Finished checkRegistrationStatus: status={}", status);
        return CheckUserRegStatusResDto.builder()
                .userRegistrationStatus(status)
                .build();
    }

    /**
     * Registers a new user with the given loginId, password, and walletId.
     * <p>
     * Prerequisites: caller must have verified status is NEW.
     *
     * @param reqDto signup request containing loginId, password, walletId
     * @return issued CasToken (access_token, refresh_token)
     */
    @Transactional
    public CasToken signup(SignupReqDto reqDto) {
        log.debug("=== Starting signup: loginId={}", reqDto.getLoginId());

        String loginId = reqDto.getLoginId();
        String walletId = reqDto.getWalletId();

        // Re-verify status is still NEW at service layer
        UserRegistrationStatus status = checkRegistrationStatus(
                CheckUserRegStatusReqDto.builder()
                        .loginId(loginId)
                        .walletId(walletId)
                        .build()
        ).getUserRegistrationStatus();

        if (status != UserRegistrationStatus.NEW) {
            log.error("Signup rejected: status={}", status);
            throw new OpenDidException(ErrorCode.USER_REGISTRATION_STATUS_INVALID);
        }

        try {
            User user = User.builder()
                    .userIdentifier(loginId)
                    .walletId(walletId)
                    .build();
            userRepository.save(user);

            String pii = computePii(loginId);
            UserPii userPii = UserPii.builder()
                    .userId(loginId)
                    .pii(pii)
                    .build();
            userPiiRepository.save(userPii);

            CasToken token = tokenService.generateToken(pii, opProviderProperty.getUrl());
            log.debug("*** Finished signup ***");
            return token;
        } catch (OpenDidException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to process signup: {}", e.getMessage(), e);
            throw new OpenDidException(ErrorCode.FAILED_TO_SIGNUP);
        }
    }

    /**
     * Authenticates a user and issues a new OP token.
     * <p>
     * For OTHER_DEVICE status, the existing User record is deleted and re-created
     * with the new walletId. UserPii is kept as-is since the loginId (and therefore pii) is unchanged.
     *
     * @param reqDto signin request containing loginId, walletId
     * @return issued CasToken (access_token, refresh_token)
     */
    @Transactional
    public CasToken signin(SigninReqDto reqDto) {
        log.debug("=== Starting signin: loginId={}", reqDto.getLoginId());

        String loginId = reqDto.getLoginId();
        String walletId = reqDto.getWalletId();

        UserRegistrationStatus status = checkRegistrationStatus(
                CheckUserRegStatusReqDto.builder()
                        .loginId(loginId)
                        .walletId(walletId)
                        .build()
        ).getUserRegistrationStatus();

        if (status == UserRegistrationStatus.NEW || status == UserRegistrationStatus.USED_BY_OTHER) {
            log.error("Signin rejected: status={}", status);
            throw new OpenDidException(ErrorCode.USER_REGISTRATION_STATUS_INVALID);
        }

        try {
            if (status == UserRegistrationStatus.OTHER_DEVICE) {
                // Delete existing user record and re-register with new walletId.
                // flush() is required to force Hibernate to execute the DELETE before the INSERT
                // within the same transaction, avoiding a unique constraint violation on user_identifier.
                userRepository.deleteByUserIdentifier(loginId);
                userRepository.flush();
                User newUser = User.builder()
                        .userIdentifier(loginId)
                        .walletId(walletId)
                        .build();
                userRepository.save(newUser);
                // UserPii is unchanged — loginId (and pii) is the same
            }

            String pii = computePii(loginId);
            CasToken token = tokenService.generateToken(pii, opProviderProperty.getUrl());
            log.debug("*** Finished signin ***");
            return token;
        } catch (OpenDidException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to process signin: {}", e.getMessage(), e);
            throw new OpenDidException(ErrorCode.FAILED_TO_SIGNIN);
        }
    }

    /**
     * Signs out the user by revoking their OP token.
     * The user record is kept; only the token is revoked.
     *
     * @param pii SHA-256(loginId) extracted from the JWT sub claim by the controller
     */
    public void signout(String pii) {
        log.debug("=== Starting signout ===");
        try {
            tokenService.revokeToken(pii);
            log.debug("*** Finished signout ***");
        } catch (OpenDidException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to process signout: {}", e.getMessage(), e);
            throw new OpenDidException(ErrorCode.FAILED_TO_SIGNOUT);
        }
    }

    /**
     * Withdraws (deregisters) the user: deletes User and UserPii records,
     * then revokes the OP token.
     *
     * @param pii SHA-256(loginId) extracted from the JWT sub claim by the controller
     */
    @Transactional
    public void withdraw(String pii) {
        log.debug("=== Starting withdraw ===");
        try {
            UserPii userPii = userPiiRepository.findByPii(pii)
                    .orElseThrow(() -> new OpenDidException(ErrorCode.USER_PII_NOT_FOUND));

            String loginId = userPii.getUserId();

            userRepository.deleteByUserIdentifier(loginId);
            userPiiRepository.deleteByUserId(loginId);

            tokenService.revokeToken(pii);
            log.debug("*** Finished withdraw ***");
        } catch (OpenDidException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to process withdraw: {}", e.getMessage(), e);
            throw new OpenDidException(ErrorCode.FAILED_TO_WITHDRAW);
        }
    }
}
