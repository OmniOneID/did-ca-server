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

package org.omnione.did.base.datamodel.enums;

/**
 * Enum representing the registration status of a user.
 *
 * <p>판별 우선순위: CURRENT → USED_BY_OTHER → OTHER_DEVICE → NEW
 *
 * <ul>
 *   <li>CURRENT      : 동일 userIdentifier + 동일 walletId 등록됨</li>
 *   <li>USED_BY_OTHER: 해당 walletId가 다른 사용자에게 등록됨</li>
 *   <li>OTHER_DEVICE : 동일 userIdentifier이나 다른 walletId (기기 변경)</li>
 *   <li>NEW          : 미등록 신규 사용자</li>
 * </ul>
 *
 * <p>USED_BY_OTHER와 OTHER_DEVICE는 동시에 충족될 수 있으며,
 * 이 경우 USED_BY_OTHER를 우선 반환하여 타인 기기를 통한 계정 탈취를 방지한다.
 */
public enum UserRegistrationStatus {
    CURRENT,
    USED_BY_OTHER,
    OTHER_DEVICE,
    NEW
}
