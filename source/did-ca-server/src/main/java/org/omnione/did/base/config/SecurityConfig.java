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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * The SecurityConfig class provides methods for configuring security settings.
 * This class configures the security settings for the application, such as CSRF, form login, HTTP basic authentication,
 * and custom authorization for specific endpoints.
 */
@RequiredArgsConstructor
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    static {
    }

    /**
     * Configures the security filter chain that applies to all HTTP requests.
     * This method disables CSRF protection, basic authentication, form login, and logout functionalities,
     * and it customizes authorization for specific endpoints.
     *
     * @param httpSecurity the HttpSecurity object used to configure web-based security
     * @return the configured SecurityFilterChain instance
     * @throws Exception if an error occurs while configuring security
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
            throws Exception {

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(this::authorizeHttpRequestsCustomizer)
                .build();
    }

    /**
     * Customizes authorization for HTTP requests.
     * This method configures which requests are permitted and which require authorization.
     *
     * @param configurer the AuthorizationManagerRequestMatcherRegistry used to configure authorization rules
     */
    private void authorizeHttpRequestsCustomizer(AuthorizeHttpRequestsConfigurer<HttpSecurity>
                                                         .AuthorizationManagerRequestMatcherRegistry configurer) {
        allowedUrlsConfigurer(configurer);
        configurer.anyRequest().permitAll();
    }

    /**
     * Configures allowed URLs for unauthenticated access.
     * Specific API endpoints can be configured here to be accessible without authentication.
     *
     * The following configuration is commented out because it is an example and may not be required in all environments.
     * Uncomment and modify as needed based on the specific security requirements of your application.
     *
     * @param configurer the AuthorizationManagerRequestMatcherRegistry used to configure URL-specific authorization rules
     */
    private void allowedUrlsConfigurer(AuthorizeHttpRequestsConfigurer<HttpSecurity>
                                               .AuthorizationManagerRequestMatcherRegistry configurer) {

    }

}
