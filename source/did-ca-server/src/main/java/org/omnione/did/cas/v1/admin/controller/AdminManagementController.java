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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.constants.UrlConstant;
import org.omnione.did.cas.v1.admin.dto.admin.AdminDto;
import org.omnione.did.cas.v1.admin.dto.admin.RegisterAdminReqDto;
import org.omnione.did.cas.v1.admin.dto.admin.ResetPasswordByRootReqDto;
import org.omnione.did.cas.v1.admin.dto.admin.ResetPasswordReqDto;
import org.omnione.did.cas.v1.admin.dto.admin.VerifyAdminIdUniqueResDto;
import org.omnione.did.cas.v1.admin.service.AdminManagementService;
import org.omnione.did.cas.v1.common.dto.EmptyResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = UrlConstant.Cas.ADMIN_V1)
public class AdminManagementController {
    private final AdminManagementService adminManagementService;

    @PostMapping(value = "/admins/reset-password")
    @ResponseBody
    public AdminDto resetPassword(@Valid @RequestBody ResetPasswordReqDto resetPasswordReqDto) {
        return adminManagementService.resetPassword(resetPasswordReqDto);
    }

    @GetMapping(value = "/admins/list")
    public Page<AdminDto> searchAdmins(String searchKey, String searchValue, Pageable pageable) {
        return adminManagementService.searchAdmins(searchKey, searchValue, pageable);
    }

    @GetMapping(value = "/admins")
    public AdminDto getAdmin(@RequestParam Long id) {
        return adminManagementService.findById(id);
    }

    @PostMapping(value = "/admins")
    public EmptyResDto registerAdmin(@RequestBody RegisterAdminReqDto registerAdminReqDto) {
        return adminManagementService.registerAdmin(registerAdminReqDto);
    }

    @GetMapping(value = "/admins/check-admin-id")
    public VerifyAdminIdUniqueResDto verifyAdminIdUnique(@RequestParam String loginId) {
        return adminManagementService.verifyAdminIdUnique(loginId);
    }

    @RequestMapping(value = "/admins", method = RequestMethod.DELETE)
    public EmptyResDto deleteAdmin(@RequestParam Long id) {
        return adminManagementService.deleteAdmin(id);
    }

    @PostMapping(value = "/admins/root/reset-password")
    @ResponseBody
    public EmptyResDto resetPasswordByRoot(@RequestBody ResetPasswordByRootReqDto resetPasswordByRootReqDto) {
        return adminManagementService.resetPasswordByRoot(resetPasswordByRootReqDto);
    }

}
