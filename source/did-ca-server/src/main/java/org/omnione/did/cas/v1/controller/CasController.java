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

package org.omnione.did.cas.v1.controller;

import jakarta.validation.Valid;
import org.omnione.did.base.datamodel.data.*;
import org.omnione.did.base.constants.UrlConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.cas.v1.service.CasService;
import org.springframework.web.bind.annotation.*;


/**
 * The CasController class is a controller that handles the CAS request.
 * It provides endpoints for requesting a wallet token, attested app info, saving user info, and retrieving PII.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = UrlConstant.Cas.V1)
public class CasController {
    private final CasService casService;

    /**
     * Request a wallet token using the given wallet token seed.
     *
     * @param walletTokenSeed the wallet token seed
     * @return the wallet token data
     */
    @PostMapping(value = UrlConstant.Cas.REQUEST_WALLET_TOKEN_DATA)
    @ResponseBody
    public WalletTokenData requestWalletToken(@RequestBody @Valid WalletTokenSeed walletTokenSeed) {
        return casService.requestWalletToken(walletTokenSeed);
    }

    /**
     * Request attested app information using the given request for attested app information.
     *
     * @param reqAttestedAppInfo the request for attested app information
     * @return the attested app information
     */
    @PostMapping(value = UrlConstant.Cas.REQUEST_ATTESTED_APPINFO)
    public AttestedAppInfo requestAttestedAppInfo(@RequestBody @Valid ReqAttestedAppInfo reqAttestedAppInfo){
        return casService.requestAttestedAppInfo(reqAttestedAppInfo);
    }

    /**
     * Save the given user information to the database.
     *
     * @param saveUserInfoDto the user information to save
     */
    @PostMapping(value = UrlConstant.Cas.SAVE_USER_INFO)
    public void SaveUserInfo(@RequestBody SaveUserInfoDto saveUserInfoDto)  {
        casService.saveUserInfo(saveUserInfoDto);
    }

    /**
     * Retrieve the PII data for the given request.
     *
     * @param retrievePiiReqDto the request for PII data
     * @return the PII data
     */
    @PostMapping(value = UrlConstant.Cas.RETRIEVE_PII)
    public RetrievePiiResDto retrievePii(@RequestBody RetrievePiiReqDto retrievePiiReqDto) {
        return casService.retrievePii(retrievePiiReqDto);
    };
}
