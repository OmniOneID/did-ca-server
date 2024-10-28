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

package org.omnione.did.base.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The OpenApiConfig class provides configuration for OpenAPI documentation.
 * This class configures the OpenAPI specification for the application, including
 * details such as the API title, description, version, and license information.
 */
@RequiredArgsConstructor
@Configuration
public class OpenApiConfig {
    private final BuildProperties buildProperties;

    /**
     * Creates and configures an OpenAPI instance with the application-specific
     * information such as title, version, and license.
     *
     * @return the configured OpenAPI instance
     */
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(getInfo());
    }

    /**
     * Provides the API information including title, description, and version.
     *
     * @return an Info object containing API metadata
     */
    private Info getInfo() {
        return new Info()
                .title(buildProperties.getName() + " API")
                .description(buildProperties.getName())
                .version(buildProperties.getVersion());
//                .license(getLicense());
    }

    /**
     * Provides license information for the API.
     *
     * @TODO: Update this method once the license is finalized.
     * @return a License object with the license details
     */
    private License getLicense() {
        return new License().name("Apache 2.0")
                .url("https://github.com/OmniOneID");
    }

}
