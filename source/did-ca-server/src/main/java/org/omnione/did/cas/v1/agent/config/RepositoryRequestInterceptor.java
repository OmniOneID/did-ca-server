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
package org.omnione.did.cas.v1.agent.config;

import org.omnione.did.base.db.domain.ServerConfig;
import org.omnione.did.base.db.repository.ServerConfigRepository;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Request interceptor for Repository Feign client to add X-API-Key header.
 * This interceptor automatically adds the API key from server_config table
 * to all requests made by RepositoryFeign.
 */
@RequiredArgsConstructor
@Slf4j
public class RepositoryRequestInterceptor implements RequestInterceptor {

    private static final String API_KEY_HEADER = "X-API-Key";
    private static final String API_KEY_CONFIG_KEY = "API_KEY_LSS";

    private final ServerConfigRepository serverConfigRepository;

    @Override
    public void apply(RequestTemplate template) {
        try {
            log.debug("Adding X-API-Key header to Repository request: {}", template.url());

            Optional<ServerConfig> configOpt = serverConfigRepository.findByConfigKey(API_KEY_CONFIG_KEY);

            if (configOpt.isEmpty()) {
                log.error("API key configuration not found for key: {}", API_KEY_CONFIG_KEY);
                throw new OpenDidException(ErrorCode.SERVER_CONFIG_KEY_NOT_FOUND);
            }

            String apiKey = configOpt.get().getConfigValue();

            if (apiKey == null || apiKey.trim().isEmpty()) {
                log.error("API key value is empty for key: {}", API_KEY_CONFIG_KEY);
                throw new OpenDidException(ErrorCode.SERVER_CONFIG_KEY_NOT_FOUND);
            }

            template.header(API_KEY_HEADER, apiKey);

            log.debug("Successfully added X-API-Key header to Repository request");

        } catch (OpenDidException e) {
            log.error("Failed to add X-API-Key header to Repository request", e);
            throw e;
        }
    }
}
