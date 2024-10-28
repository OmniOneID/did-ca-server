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

package org.omnione.did.cas.v1.service.sample;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.datamodel.data.*;
import org.omnione.did.base.db.repository.UserPiiRepository;
import org.omnione.did.cas.v1.service.CasService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.omnione.did.data.model.did.Proof;
import org.omnione.did.data.model.provider.Provider;

/**
 * The CasServiceSample class provides sample methods for requesting wallet tokens, attested app information, and saving user information.
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Profile("sample")
public class CasServiceSample implements CasService {
    @Override
    public WalletTokenData requestWalletToken(WalletTokenSeed walletTokenSeed) {

//        System.out.println("Wa" + TestUtils.generateUUID());
        /*
         * def object WalletTokenData: "wallet token data"
         * {
         * + WalletTokenSeed "seed" : "wallet token seed"
         * + multibase "sha256_pii": "multibase(sha256(personId)) - hashed PII"
         * + object "provider" : "월렛 사업자 정보"
         * {
         * + did "did" : "provider DID"
         * + url "certVcRef": "provider 가입증명서 VC URL"
         * }
         * + multibase "nonce": "provider nonce", byte_length(16)
         * + AssertProof "proof": "provider proof - 월렛 사업자 서명"
         * }
         */

        Provider provider = new Provider();
        provider.setCertVcRef("http://192.168.3.130:8090/tas/download/vc?name=tas");
        provider.setDid("did:raon:entity");
        Proof proof = new Proof();
        proof.setProofPurpose("assertionMethod");
        proof.setProofValue("1234567890");
        proof.setVerificationMethod("did:raon:tas?version=1#attested");
        proof.setType("Secp256k1Signature2018");
        proof.setCreated("2024-01-01T09:00:00Z");

        return WalletTokenData.builder()
                .seed(walletTokenSeed)
                .sha256_pii("2845bac0835ba292946e2476545dfec6cd82027ee91b1cfb5ae5b1edce9b9b74")
                .provider(provider)
                .nonce("zNgLzfu8edopNmKhmuGMv32")
                .proof(proof)
                .build();
    }

    @Override
    public AttestedAppInfo requestAttestedAppInfo(ReqAttestedAppInfo reqAttestedAppInfo) {
        /*
         * def object AttestedAppInfo: "attested app info"
         * {
         * + string "appId": "app ID"
         * + multibase "nonce": "provider nonce", byte_length(16)
         * + object "proof": "provider proof - 앱 사업자 서명"
         * {
         * + ProofPurpose "proofPurpose": "proof purpose"
         * + ProofType "type": "proof type"
         * + string "created": "created time"
         * + string "proofValue": "proof value"
         * + string "verificationMethod": "verification method"
         * }
         * + object "provider" : "앱 사업자 정보"
         * {
         * + did "did" : "provider DID"
         * + url "certVcRef": "provider 가입증명서 VC URL"
         * }
         * }
         */
        Provider provider = new Provider();
        provider.setCertVcRef("http://192.168.3.130:8090/tas/download/vc?name=tas");
        provider.setDid("did:raon:entity");

        Proof proof = new Proof();
        proof.setProofPurpose("assertionMethod");
        proof.setProofValue("1234567890");
        proof.setVerificationMethod("did:raon:tas?version=1#attested");
        proof.setType("Secp256k1Signature2018");
        proof.setCreated("2024-01-01T09:00:00Z");


        return AttestedAppInfo.builder()
                .appId(reqAttestedAppInfo.getAppId())
                .nonce("zNgLzfu8edopNmKhmuGMv32")
                .proof(proof)
                .provider(provider)
                .build();
    }

    @Override
    public void saveUserInfo(SaveUserInfoDto saveUserInfoDto) {

    }

    @Override
    public RetrievePiiResDto retrievePii(RetrievePiiReqDto retrievePiiReqDto) {
        return RetrievePiiResDto.builder()
                .pii("2845bac0835ba292946e2476545dfec6cd82027ee91b1cfb5ae5b1edce9b9b74")
                .build();
    }
}
