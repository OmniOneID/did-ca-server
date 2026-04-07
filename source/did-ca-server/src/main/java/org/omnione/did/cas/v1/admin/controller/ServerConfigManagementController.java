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

import org.omnione.did.base.constants.UrlConstant;
import org.omnione.did.cas.v1.admin.dto.serverconfig.ServerConfigDto;
import org.omnione.did.cas.v1.admin.dto.serverconfig.UpdateServerConfigReqDto;
import org.omnione.did.cas.v1.admin.dto.serverconfig.UpdateServerConfigResDto;
import org.omnione.did.cas.v1.admin.service.ServerConfigManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing server configurations in the Admin Console.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = UrlConstant.Cas.ADMIN_V1)
public class ServerConfigManagementController {

    private final ServerConfigManagementService serverConfigManagementService;

    @GetMapping(value = "/server-configs")
    public List<ServerConfigDto> getAllServerConfigs() {
        return serverConfigManagementService.getAllServerConfigs();
    }

    @GetMapping(value = "/server-configs/{configKey}")
    public ServerConfigDto getServerConfigByKey(@PathVariable String configKey) {
        return serverConfigManagementService.getServerConfigByKey(configKey);
    }

    @PutMapping(value = "/server-configs")
    public UpdateServerConfigResDto updateServerConfigs(
            @Valid @RequestBody UpdateServerConfigReqDto updateServerConfigReqDto) {
        return serverConfigManagementService.updateServerConfigs(updateServerConfigReqDto);
    }
}
