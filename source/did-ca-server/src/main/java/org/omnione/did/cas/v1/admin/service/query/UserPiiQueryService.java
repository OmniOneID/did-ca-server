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
package org.omnione.did.cas.v1.admin.service.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.db.domain.Admin;
import org.omnione.did.base.db.domain.UserPii;
import org.omnione.did.base.db.repository.UserPiiRepository;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.cas.v1.admin.dto.user.UserPiiDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserPiiQueryService {
    private final UserPiiRepository userPiiRepository;

    public Page<UserPiiDto> searchUserPiiList(String searchKey, String searchValue, Pageable pageable) {
        Page<UserPii> userPiiPage = userPiiRepository.searchUserPiis(searchKey, searchValue, pageable);

        List<UserPiiDto> userPiiDtoList = userPiiPage.getContent().stream()
                .map(UserPiiDto::fromUserPii)
                .collect(Collectors.toList());

        return new PageImpl<>(userPiiDtoList, pageable, userPiiPage.getTotalElements());
    }

    public UserPii findById(Long id) {
        return userPiiRepository.findById(id)
                .orElseThrow(() -> new OpenDidException(ErrorCode.USER_PII_NOT_FOUND));
    }
}
