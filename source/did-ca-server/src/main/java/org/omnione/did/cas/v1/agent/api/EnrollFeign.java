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

package org.omnione.did.cas.v1.agent.api;

import org.omnione.did.cas.v1.agent.api.dto.ConfirmEnrollEntityApiReqDto;
import org.omnione.did.cas.v1.agent.api.dto.ConfirmEnrollEntityApiResDto;
import org.omnione.did.cas.v1.agent.api.dto.ProposeEnrollEntityApiReqDto;
import org.omnione.did.cas.v1.agent.api.dto.ProposeEnrollEntityApiResDto;
import org.omnione.did.cas.v1.agent.api.dto.RequestEcdhApiReqDto;
import org.omnione.did.cas.v1.agent.api.dto.RequestEcdhApiResDto;
import org.omnione.did.cas.v1.agent.api.dto.RequestEnrollEntityApiReqDto;
import org.omnione.did.cas.v1.agent.api.dto.RequestEnrollEntityApiResDto;
import org.omnione.did.cas.v1.agent.api.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * The EnrollFeign interface is a Feign client that provides endpoints for enrolling an entity.
 * It is used to communicate with the TAS service.
 */
@FeignClient(value = "Tas", url = "${tas.url}", path = "/api/v1")
public interface EnrollFeign {
    /**
     * Request to enroll an entity.
     *
     * @param request the request to enroll an entity
     * @return the response to enrolling an entity
     */
    @PostMapping("/request-enroll-entity")
    RequestEnrollEntityApiResDto requestEnrollEntityApi(@RequestBody RequestEnrollEntityApiReqDto request);

    /**
     * request to ecdh.
     * @param request the request to ecdh
     * @return the response to ecdh
     */
    @PostMapping("/request-ecdh")
    RequestEcdhApiResDto requestEcdh(@RequestBody RequestEcdhApiReqDto request);

    /**
     * propose to enroll an entity.
     * @param request the request to propose to enroll an entity
     * @return the response to propose to enroll an entity
     */
    @PostMapping("/propose-enroll-entity")
    ProposeEnrollEntityApiResDto proposeEnrollEntityApi(@RequestBody ProposeEnrollEntityApiReqDto request);

    /**
     * Confirm to enroll an entity.
     *
     * @param request the request to enroll an entity
     * @return the response to enrolling an entity
     */
    @PostMapping("/confirm-enroll-entity")
    ConfirmEnrollEntityApiResDto confirmEnrollEntityApi(@RequestBody ConfirmEnrollEntityApiReqDto request);
}
