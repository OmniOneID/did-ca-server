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
package org.omnione.did.cas.v1.common.service.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.db.domain.Admin;
import org.omnione.did.base.db.repository.AdminRepository;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.cas.v1.admin.dto.admin.AdminDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminQueryService {
    private final AdminRepository adminRepository;

    public Admin findByLoginIdAndLoginPassword(String loginId, String loginPassword) {
        return adminRepository.findByLoginIdAndLoginPassword(loginId, loginPassword)
                .orElseThrow(() -> new OpenDidException(ErrorCode.ADMIN_INFO_NOT_FOUND));
    }

    public Page<AdminDto> searchAdminList(String searchKey, String searchValue, Pageable pageable) {
        Page<Admin> adminPage = adminRepository.searchAdmins(searchKey, searchValue, pageable);

        List<AdminDto> adminDtos = adminPage.getContent().stream()
                .map(AdminDto::fromAdmin)
                .collect(Collectors.toList());

        return new PageImpl<>(adminDtos, pageable, adminPage.getTotalElements());
    }

    public Admin findById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new OpenDidException(ErrorCode.ADMIN_INFO_NOT_FOUND));
    }

    public Admin findByLoginIdOrNull(String loginId) {
        return adminRepository.findByLoginId(loginId).orElse(null);
    }

    public long countByLoginId(String loginId) {
        return adminRepository.countByLoginId(loginId);
    }

    public Admin findByLoginId(String loginId) {
        return adminRepository.findByLoginId(loginId).orElseThrow(() -> new OpenDidException(ErrorCode.ADMIN_INFO_NOT_FOUND));
    }
}
