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
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.cas.v1.admin.dto.server.VerifyServerUrlReqDto;
import org.omnione.did.cas.v1.admin.dto.server.VerifyServerUrlResDto;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A utility class for checking if a URL is reachable.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UrlPingChecker {

    private final String HEALTH_CHECK_PATH = "/actuator/health";

    /**
     * Checks if the given URL is reachable.
     *
     * @param verifyServerUrlReqDto the URL to check
     * @return a response DTO indicating if the URL is reachable
     */
    public VerifyServerUrlResDto isUrlReachable(VerifyServerUrlReqDto verifyServerUrlReqDto) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(verifyServerUrlReqDto.getServerUrl() + HEALTH_CHECK_PATH);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.connect();

            int responseCode = connection.getResponseCode();

            if (responseCode >= 200 && responseCode < 400) {
                return VerifyServerUrlResDto.builder()
                        .isAvailable(true)
                        .build();
            } else {
                return VerifyServerUrlResDto.builder()
                        .isAvailable(false)
                        .build();
            }
        } catch (IOException e) {
            throw new OpenDidException(ErrorCode.URL_PING_ERROR);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
