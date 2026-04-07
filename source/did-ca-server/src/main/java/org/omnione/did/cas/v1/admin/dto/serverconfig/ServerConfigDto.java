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

import org.omnione.did.base.db.domain.ServerConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * DTO for server configuration data.
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ServerConfigDto {

    private final Long id;
    private final String configKey;
    private final String configValue;
    private final String description;
    private final String createdAt;
    private final String updatedAt;

    public static ServerConfigDto fromServerConfig(ServerConfig serverConfig) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return ServerConfigDto.builder()
                .id(serverConfig.getId())
                .configKey(serverConfig.getConfigKey())
                .configValue(serverConfig.getConfigValue())
                .description(serverConfig.getDescription())
                .createdAt(formatInstant(serverConfig.getCreatedAt(), formatter))
                .updatedAt(formatInstant(serverConfig.getUpdatedAt(), formatter))
                .build();
    }

    private static String formatInstant(Instant instant, DateTimeFormatter formatter) {
        if (instant == null) return null;
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(formatter);
    }
}
