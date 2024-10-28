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

package org.omnione.did.base.constants;

/**
 * Represents URL constants for the Open DID servers.
 * This class organizes all the API endpoints used in the application, ensuring that URLs are consistent and easy to manage.
 */
public class UrlConstant {

    public static class Cas {
        public static final String V1 = "/cas/api/v1";
        public static final String REQUEST_WALLET_TOKEN_DATA = "/request-wallet-tokendata";
        public static final String REQUEST_ATTESTED_APPINFO = "/request-attested-appinfo";
        public static final String SAVE_USER_INFO = "/save-user-info";
        public static final String RETRIEVE_PII = "/retrieve-pii";
    }
}