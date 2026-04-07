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
package org.omnione.did.cas.v1.admin.dto.serverconfig;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for updating server configurations request.
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class UpdateServerConfigReqDto {

    @NotNull(message = "Configs list is required")
    @NotEmpty(message = "Configs list cannot be empty")
    @Valid
    private final List<ConfigItem> configs;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(force = true)
    public static class ConfigItem {

        @NotBlank(message = "Config key is required")
        @Size(max = 100, message = "Config key must not exceed 100 characters")
        private final String configKey;

        @NotNull(message = "Config value is required")
        private final String configValue;

        @Size(max = 200, message = "Description must not exceed 200 characters")
        private final String description;
    }
}
