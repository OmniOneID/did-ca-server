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
package org.omnione.did.cas.v1.admin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.db.domain.Admin;
import org.omnione.did.base.db.domain.AdminPasswordPolicy;
import org.omnione.did.base.db.repository.AdminPasswordPolicyRepository;
import org.omnione.did.base.db.repository.AdminRepository;
import org.omnione.did.cas.v1.admin.dto.admin.AdminDto;
import org.omnione.did.cas.v1.admin.dto.admin.RequestAdminLoginReqDto;
import org.omnione.did.cas.v1.admin.service.query.AdminQueryService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SessionService {
    private final AdminQueryService adminQueryService;
    private final AdminPasswordPolicyRepository adminPasswordPolicyRepository;
    private final AdminRepository adminRepository;

    public AdminDto requestAdminLogin(RequestAdminLoginReqDto requestAdminLoginReqDto) {
        Admin admin = adminQueryService.findByLoginIdAndLoginPassword(requestAdminLoginReqDto.getLoginId(), requestAdminLoginReqDto.getLoginPassword());

        boolean isPasswordExpired = checkPasswordExpired(admin);
        if (isPasswordExpired && !admin.getRequirePasswordReset()) {
            admin.setRequirePasswordReset(true);
            admin.setPasswordResetReason(org.omnione.did.base.db.constant.PasswordResetReason.EXPIRED);
            adminRepository.save(admin);
        }

        AdminDto dto = AdminDto.fromAdmin(admin);
        return AdminDto.builder()
                .id(dto.getId())
                .loginId(dto.getLoginId())
                .name(dto.getName())
                .email(dto.getEmail())
                .emailVerified(dto.getEmailVerified())
                .requirePasswordReset(dto.getRequirePasswordReset())
                .role(dto.getRole())
                .createdBy(dto.getCreatedBy())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .passwordResetReason(dto.getPasswordResetReason())
                .isPasswordExpired(isPasswordExpired)
                .build();
    }

    private boolean checkPasswordExpired(Admin admin) {
        Optional<AdminPasswordPolicy> policyOpt = adminPasswordPolicyRepository.findTop1ByOrderByIdAsc();
        if (policyOpt.isEmpty()) {
            return false;
        }
        AdminPasswordPolicy policy = policyOpt.get();
        Instant lastChanged = admin.getLastPasswordChangedAt();
        if (lastChanged == null) {
            return false;
        }
        long daysSinceChange = ChronoUnit.DAYS.between(lastChanged, Instant.now());
        return daysSinceChange >= policy.getPasswordExpiryDays();
    }
}
