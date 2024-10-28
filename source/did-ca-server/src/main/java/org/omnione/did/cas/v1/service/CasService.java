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

package org.omnione.did.cas.v1.service;

import org.omnione.did.base.datamodel.data.*;

/**
 * The CasService class provides methods for requesting wallet tokens, attested app information, and saving user information.
 * It is designed to facilitate the generation of wallet tokens, attested app information, and the storage of user information.
 */
public interface CasService {
    /**
     * Request a wallet token using the given wallet token seed.
     *
     * @param walletTokenSeed the wallet token seed
     * @return the wallet token data
     */
    WalletTokenData requestWalletToken(WalletTokenSeed walletTokenSeed);

    /**
     * Request attested app information using the given request for attested app information.
     *
     * @param reqAppInfo the request for attested app information
     * @return the attested app information
     */
    AttestedAppInfo requestAttestedAppInfo(ReqAttestedAppInfo reqAppInfo);

    /**
     * Save the given user information to the database.
     *
     * @param saveUserInfoDto the user information to save
     */
    void saveUserInfo(SaveUserInfoDto saveUserInfoDto);

    /**
     * Retrieve the PII data for the given request.
     *
     * @param retrievePiiReqDto the request for PII data
     * @return the PII data
     */
    RetrievePiiResDto retrievePii(RetrievePiiReqDto retrievePiiReqDto);
}
