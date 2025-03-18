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

package org.omnione.did.cas.v1.agent.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.datamodel.data.AttestedAppInfo;
import org.omnione.did.base.datamodel.data.ReqAttestedAppInfo;
import org.omnione.did.base.datamodel.data.RetrievePiiReqDto;
import org.omnione.did.base.datamodel.data.RetrievePiiResDto;
import org.omnione.did.base.datamodel.data.SaveUserInfoDto;
import org.omnione.did.base.datamodel.data.WalletTokenData;
import org.omnione.did.base.datamodel.data.WalletTokenSeed;
import org.omnione.did.base.datamodel.enums.ProofPurpose;
import org.omnione.did.base.datamodel.enums.ProofType;
import org.omnione.did.base.db.domain.Cas;
import org.omnione.did.base.db.domain.UserPii;
import org.omnione.did.base.db.repository.UserPiiRepository;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.base.util.BaseCryptoUtil;
import org.omnione.did.base.util.BaseMultibaseUtil;
import org.omnione.did.cas.v1.admin.service.query.CasQueryService;
import org.omnione.did.common.util.DateTimeUtil;
import org.omnione.did.crypto.enums.DigestType;
import org.omnione.did.crypto.enums.MultiBaseType;
import org.omnione.did.crypto.exception.CryptoException;
import org.omnione.did.crypto.util.DigestUtils;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.did.Proof;
import org.omnione.did.data.model.provider.Provider;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * The CasServiceImpl class provides methods for requesting wallet tokens, attested app information, and saving user information.
 * It is designed to facilitate the generation of wallet tokens, attested app information, and the storage of user information.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Profile("!sample")
public class CasServiceImpl implements CasService {
    private final SignatureService signatureService;
    private final DidDocService didDocService;
    private final UserPiiRepository userPiiRepository;
    private final CasQueryService casQueryService;

    /**
     * Request a wallet token using the given wallet token seed.
     *
     * @param walletTokenSeed the wallet token seed
     * @return the wallet token data
     * @throws OpenDidException if an error occurs while requesting the wallet token
     */
    @Override
    public WalletTokenData requestWalletToken(WalletTokenSeed walletTokenSeed)  {
        try {
            log.debug("=== Starting requestWalletToken ===");

            // Generate Wallet Token Data Set
            log.debug("\t--> Generating Wallet Token Data Set");
            Map<String, Object> dataSet = createDataSet(walletTokenSeed, generateSha256Pii(walletTokenSeed.getUserId()));

            // Sign Data
            log.debug("\t--> Signing Data");
            Proof proof = signatureService.signData(dataSet);

            log.debug("*** Finished requestWalletToken ***");

            return WalletTokenData.builder()
                    .seed(walletTokenSeed)
                    .sha256_pii((String) dataSet.get("sha256_pii"))
                    .provider((Provider) dataSet.get("provider"))
                    .nonce((String) dataSet.get("nonce"))
                    .proof(proof)
                    .build();
        } catch (OpenDidException e) {
            log.error("OpenDidException occurred while requesting wallet token: {}", e.getErrorCode().getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Exception occurred while requesting wallet token: {}", e.getMessage(), e);
            throw new OpenDidException(ErrorCode.FAILED_TO_REQUEST_WALLET_TOKEN_DATA);
        }
    }

    /**
     * Request attested app information using the given app ID.
     *
     * @param reqAppInfo the requested app information
     * @return the attested app information
     * @throws OpenDidException if an error occurs while requesting attested app information
     */
    @Override
    public AttestedAppInfo requestAttestedAppInfo(ReqAttestedAppInfo reqAppInfo)  {
        try {
            log.debug("=== Starting requestAttestedAppInfo ===");

            // Prepare Data Set (AttestedAppInfo except ProofValue)
            log.debug("\t--> Generating Data Set");
            Map<String, Object> dataSet = createDataSet(reqAppInfo.getAppId(), null);

            // Sign Data
            log.debug("\t--> Signing Data");
            Proof proof = signatureService.signData(dataSet);

            log.debug("*** Finished requestAttestedAppInfo ***");

            return AttestedAppInfo.builder()
                    .appId(reqAppInfo.getAppId())
                    .provider((Provider) dataSet.get("provider"))
                    .nonce((String) dataSet.get("nonce"))
                    .proof(proof)
                    .build();
        } catch (OpenDidException e) {
            log.error("OpenDidException occurred while requesting attested app info: {}", e.getErrorCode().getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Exception occurred while requesting attested app info: {}", e.getMessage(), e);
            throw new OpenDidException(ErrorCode.FAILED_TO_REQUEST_ATTESTED_APPINFO);
        }
    }

    /**
     * Save user information using the given user information.
     *
     * @param saveUserInfoDto the user information to save
     * @throws OpenDidException if an error occurs while saving user information
     */
    @Override
    public void saveUserInfo(SaveUserInfoDto saveUserInfoDto) {
        try {
            log.debug("=== Starting saveUserInfo ===");
            log.debug("\t--> Saving User PII");
            userPiiRepository.save(UserPii.builder()
                    .pii(saveUserInfoDto.getPii())
                    .userId(saveUserInfoDto.getUserId())
                    .build());
            log.debug("*** Finished saveUserInfo ***");
        } catch (OpenDidException e) {
            log.error("OpenDidException occurred while saving user info: {}", e.getErrorCode().getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Exception occurred while saving user info: {}", e.getMessage(), e);
            throw new OpenDidException(ErrorCode.FAILED_TO_SAVE_USER_INFO);
        }


    }

    /**
     * Retrieve PII using the given user ID.
     *
     * @param retrievePiiReqDto the user ID to retrieve PII
     * @return the retrieved PII
     * @throws OpenDidException if an error occurs while retrieving PII
     */
    @Override
    public RetrievePiiResDto retrievePii(RetrievePiiReqDto retrievePiiReqDto) {
        try {
            log.debug("=== Starting retrievePii ===");
            log.debug("\t--> Retrieving User PII");
            Optional<UserPii> byUserIdUserPii = userPiiRepository.findByUserId(retrievePiiReqDto.getUserId());
            if (byUserIdUserPii.isEmpty()) {
                throw new OpenDidException(ErrorCode.USER_PII_NOT_FOUND);
            }
            log.debug("*** Finished retrievePii ***");
            return  RetrievePiiResDto.builder()
                    .pii(byUserIdUserPii.get().getPii())
                    .build();
        } catch (OpenDidException e) {
            log.error("OpenDidException occurred while retrieving PII: {}", e.getErrorCode().getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Exception occurred while retrieving PII: {}", e.getMessage(), e);
            throw new OpenDidException(ErrorCode.FAILED_TO_RETRIEVE_PII);
        }

    }

    /**
     * Create a data set using the given object and SHA-256 PII.
     *
     * @param object the object to include in the data set
     * @param sha256Pii the SHA-256 PII to include in the data set
     * @return the created data set
     */
    private Map<String, Object> createDataSet(Object object, String sha256Pii) {
        // Retrieve CAS.
        Cas existedCas = casQueryService.findCas();

        Map<String, Object> dataSet = new HashMap<>();
        dataSet.put(object instanceof WalletTokenSeed ? "seed" : "appId", object);
        if (sha256Pii != null) {
            dataSet.put("sha256_pii", sha256Pii);
        }
        dataSet.put("provider", getProvider(existedCas));
        dataSet.put("nonce", generateNonce());
        dataSet.put("proof", preGenerateProof(existedCas));
        return dataSet;
    }

    /**
     * Generate a SHA-256 PII using the given user ID.
     *
     * @param userId walletTokenSeed the user ID to find the PII
     * @return the generated SHA-256 PII
     */
    private String generateSha256Pii(String userId) {
        Optional<UserPii> findUserPii = userPiiRepository.findByUserId(userId);
        try {
            if (findUserPii.isEmpty()) {
                throw new OpenDidException(ErrorCode.USER_PII_NOT_FOUND);
            }
            byte[] piiDigest = DigestUtils.getDigest(findUserPii.get().getPii().getBytes(), DigestType.SHA256);
            return BaseMultibaseUtil.encode(piiDigest, MultiBaseType.base58btc);
        } catch (CryptoException e) {
            log.error("CryptoException occurred while generating SHA-256 PII: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.FAILED_TO_ENCRYPT_PII);
        }
    }

    /**
     * Generate a nonce.
     *
     * @return the generated nonce
     */
    private Provider getProvider(Cas cas) {
        Provider provider = new Provider();
        provider.setCertVcRef(cas.getCertificateUrl());
        provider.setDid(cas.getDid());
        return provider;
    }

    /**
     * Generate a nonce.
     *
     * @return the generated nonce
     */
    private String generateNonce() {
        return BaseMultibaseUtil.encode(BaseCryptoUtil.generateNonce(16), MultiBaseType.base64);
    }

    /**
     * Pre-generate a proof.
     *
     * @return the pre-generated proof
     */
    private Proof preGenerateProof(Cas cas) {
        DidDocument casDidDocument = didDocService.getDidDocument(cas.getDid());
        Proof proof = new Proof();
        proof.setType(ProofType.SECP_256R1_SIGNATURE_2018.toString());
        proof.setCreated(DateTimeUtil.getCurrentUTCTimeString());
        proof.setVerificationMethod(signatureService.getVerificationMethod(casDidDocument, ProofPurpose.ASSERTION_METHOD));
        proof.setProofPurpose(ProofPurpose.ASSERTION_METHOD.toString());
        proof.setProofValue(null);
        return proof;
    }
}