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
import org.omnione.did.cas.v1.admin.dto.user.UserPiiDto;
import org.omnione.did.cas.v1.admin.service.query.UserPiiQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserManagementService {
    private final UserPiiQueryService userPiiQueryService;

    public Page<UserPiiDto> searchUsers(String searchKey, String searchValue, Pageable pageable) {
        return userPiiQueryService.searchUserPiiList(searchKey, searchValue, pageable);
    }

    public UserPiiDto findById(Long id) {
        return UserPiiDto.fromUserPii(userPiiQueryService.findById(id));
    }
}
