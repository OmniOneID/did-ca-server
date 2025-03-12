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
import org.omnione.did.base.db.repository.AdminRepository;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.cas.v1.admin.dto.admin.AdminDto;
import org.omnione.did.cas.v1.admin.dto.admin.RegisterAdminReqDto;
import org.omnione.did.cas.v1.admin.dto.admin.ResetPasswordByRootReqDto;
import org.omnione.did.cas.v1.admin.dto.admin.ResetPasswordReqDto;
import org.omnione.did.cas.v1.admin.dto.admin.VerifyAdminIdUniqueResDto;
import org.omnione.did.cas.v1.admin.service.query.AdminQueryService;
import org.omnione.did.cas.v1.common.dto.admin.test.EmptyResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminManagementService {
    private final AdminQueryService adminQueryService;
    private final AdminRepository adminRepository;

    public AdminDto resetPassword(ResetPasswordReqDto resetPasswordReqDto) {
        Admin admin = adminQueryService.findByLoginIdAndLoginPassword(resetPasswordReqDto.getLoginId(), resetPasswordReqDto.getOldPassword());
        admin.setLoginPassword(resetPasswordReqDto.getNewPassword());
        admin.setRequirePasswordReset(false);

        return AdminDto.fromAdmin(adminRepository.save(admin));
    }

    public Page<AdminDto> searchAdmins(String searchKey, String searchValue, Pageable pageable) {
        return adminQueryService.searchAdminList(searchKey, searchValue, pageable);
    }

    public AdminDto findById(Long id) {
        return AdminDto.fromAdmin(adminQueryService.findById(id));
    }

    public EmptyResDto registerAdmin(RegisterAdminReqDto registerAdminReqDto) {
        Admin existingAdmin = adminQueryService.findByLoginIdOrNull(registerAdminReqDto.getLoginId());
        if (existingAdmin != null) {
            throw new OpenDidException(ErrorCode.ADMIN_ALREADY_EXISTS);
        }

        // @TODO: Check if the role is valid
        // @TODO: createBy should be the logged in user
        Admin admin = Admin.builder()
                .loginId(registerAdminReqDto.getLoginId())
                .role(registerAdminReqDto.getRole())
                .loginPassword(registerAdminReqDto.getLoginPassword())
                .requirePasswordReset(true)
                .emailVerified(false)
                .createdBy("SYSTEM")
                .build();

        adminRepository.save(admin);

        return new EmptyResDto();
    }

    public VerifyAdminIdUniqueResDto verifyAdminIdUnique(String loginId) {
        long count = adminQueryService.countByLoginId(loginId);
        return VerifyAdminIdUniqueResDto.builder()
                .isUnique(count == 0)
                .build();
    }

    public EmptyResDto deleteAdmin(Long id) {
        adminQueryService.findById(id);
        adminRepository.deleteById(id);
        return new EmptyResDto();
    }

    public EmptyResDto resetPasswordByRoot(ResetPasswordByRootReqDto resetPasswordByRootReqDto) {
        Admin admin = adminQueryService.findByLoginId(resetPasswordByRootReqDto.getLoginId());
        admin.setLoginPassword(resetPasswordByRootReqDto.getNewPassword());
        admin.setRequirePasswordReset(true);

        adminRepository.save(admin);
        return new EmptyResDto();
    }
}
