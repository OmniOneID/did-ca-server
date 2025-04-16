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
import org.omnione.did.base.datamodel.data.AccEcdh;
import org.omnione.did.base.datamodel.data.Candidate;
import org.omnione.did.base.datamodel.data.DidAuth;
import org.omnione.did.base.datamodel.data.EcdhReqData;
import org.omnione.did.base.datamodel.enums.EccCurveType;
import org.omnione.did.base.datamodel.enums.SymmetricCipherType;
import org.omnione.did.base.db.constant.CasStatus;
import org.omnione.did.base.db.domain.Cas;
import org.omnione.did.base.db.domain.CertificateVc;
import org.omnione.did.base.db.repository.CasRepository;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.base.util.BaseCryptoUtil;
import org.omnione.did.base.util.BaseMultibaseUtil;
import org.omnione.did.base.util.RandomUtil;
import org.omnione.did.cas.v1.admin.service.query.CasQueryService;
import org.omnione.did.cas.v1.agent.api.dto.ConfirmEnrollEntityApiReqDto;
import org.omnione.did.cas.v1.agent.api.dto.ConfirmEnrollEntityApiResDto;
import org.omnione.did.cas.v1.agent.api.dto.ProposeEnrollEntityApiReqDto;
import org.omnione.did.cas.v1.agent.api.dto.ProposeEnrollEntityApiResDto;
import org.omnione.did.cas.v1.agent.api.dto.RequestEcdhApiReqDto;
import org.omnione.did.cas.v1.agent.api.dto.RequestEcdhApiResDto;
import org.omnione.did.cas.v1.agent.api.dto.RequestEnrollEntityApiReqDto;
import org.omnione.did.cas.v1.agent.api.dto.RequestEnrollEntityApiResDto;
import org.omnione.did.common.util.DateTimeUtil;
import org.omnione.did.crypto.exception.CryptoException;
import org.omnione.did.crypto.keypair.EcKeyPair;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.did.Proof;
import org.omnione.did.data.model.enums.did.ProofType;
import org.omnione.did.data.model.vc.VerifiableCredential;
import org.omnione.did.cas.v1.agent.api.EnrollFeign;
import org.omnione.did.cas.v1.agent.dto.EnrollEntityResDto;
import org.springframework.stereotype.Service;
import java.security.interfaces.ECPrivateKey;
import java.util.Arrays;

/**
 * Service for enrolling entity.
 * This class provides methods for enrolling entity.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class EnrollEntityService {
    private final CasRepository casRepository;
    private final EnrollFeign enrollFeign;
    private final CertificateVcQueryService certificateVcQueryService;
    private final DidDocService didDocService;
    private final SignatureService signatureService;
    private final CasQueryService casQueryService;

    /**
     * Enroll entity.
     * This method enrolls an entity with the CAS and returns the result as an EnrollEntityResDto object.
     *
     * @return the result of the entity enrollment
     * @throws OpenDidException if the entity enrollment fails
     */
    public EnrollEntityResDto enrollEntity() {
        try {
            log.debug("=== Starting Enroll Entity ===");

            // Retrieve CAS.
            Cas existedCas = casQueryService.findCas();

            // Retrieve CAS DID Document.
            log.debug("\t--> Retrieve CAS DID Document");
            DidDocument casDidDocument = didDocService.getDidDocument(existedCas.getDid());

            // Send propose-enroll-entity.
            log.debug("\t--> Send propose-enroll-entity");
            ProposeEnrollEntityApiResDto proposeResponse = proposeEnrollEntity();
            String txId = proposeResponse.getTxId();
            String authNonce = proposeResponse.getAuthNonce();

            // Send request-ecdh
            log.debug("\t--> Send request-ecdh");
            EccCurveType eccCurveType = EccCurveType.SECP_256_R1;
            log.debug("\t\t--> generate Tmp Keypair");
            EcKeyPair ecKeyPair = (EcKeyPair) BaseCryptoUtil.generateKeyPair(eccCurveType);
            log.debug("\t\t--> generate ReqEcdh");
            String clientNonce = BaseMultibaseUtil.encode(BaseCryptoUtil.generateNonce(16));
            EcdhReqData reqData = generateReqData(ecKeyPair, eccCurveType, clientNonce, casDidDocument);
            log.debug("reqData = " + reqData);
            log.debug("\t\t--> request ECDH");
            RequestEcdhApiResDto ecdhResponse = requestEcdh(txId, reqData);

            // Send request-enroll-entity
            log.debug("\t--> Send request-enroll-entity");
            log.debug("\t\t--> generate DID Auth");
            DidAuth didAuth = generateDidAuth(authNonce, casDidDocument);
            log.debug("\t\t--> request Enroll Entity");
            RequestEnrollEntityApiResDto enrollEntityResponse = requestEnrollEntity(txId, didAuth);
            log.debug("\t\t--> decrypt VC");
            VerifiableCredential vc = decryptVc((ECPrivateKey) ecKeyPair.getPrivateKey(),
                    ecdhResponse.getAccEcdh(), enrollEntityResponse, clientNonce);

            // Send confirm-enroll-entity
            log.debug("\t--> Send confirm-enroll-entity");
            ConfirmEnrollEntityApiResDto confirmResponse = confirmEnrollEntity(txId, vc.getId());

            // Insert certificate VC.
            log.debug("\t\t--> Insert certificate VC");
            certificateVcQueryService.save(CertificateVc.builder()
                    .vc(vc.toJson())
                    .build());

            // Update CAS status.
            log.debug("\t\t--> Update CAS status");
            existedCas.setStatus(CasStatus.ACTIVATE);
            casRepository.save(existedCas);

            log.debug("*** Finished enrollEntity ***");

            return EnrollEntityResDto.builder()
                    .build();
        } catch (OpenDidException e) {
            log.error("Error occurred while enrolling entity: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while enrolling entity: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.FAILED_TO_ISSUE_CERTIFICATE_VC);
        }
    }

    /**
     * Propose enroll entity.
     * This method sends a propose-enroll-entity request to the CAS and returns the response as a ProposeEnrollEntityApiResDto object.
     *
     * @return the response to the propose-enroll-entity request
     */
    private ProposeEnrollEntityApiResDto proposeEnrollEntity() {
        ProposeEnrollEntityApiReqDto request = ProposeEnrollEntityApiReqDto.builder()
                .id(RandomUtil.generateMessageId())
                .build();
        return enrollFeign.proposeEnrollEntityApi(request);
    }

    /**
     * Request ECDH.
     * This method sends a request-ecdh request to the CAS and returns the response as a RequestEcdhApiResDto object.
     *
     * @param txId the transaction ID
     * @param reqEcdh the ECDH request data
     * @return the response to the request-ecdh request
     */
    private RequestEcdhApiResDto requestEcdh(String txId, EcdhReqData reqEcdh) {
        RequestEcdhApiReqDto request = RequestEcdhApiReqDto.builder()
                .id(RandomUtil.generateMessageId())
                .txId(txId)
                .reqEcdh(reqEcdh)
                .build();
        return enrollFeign.requestEcdh(request);
    }

    /**
     * Generate request data.
     * This method generates the request data for the ECDH request.
     *
     * @param publicKey the public key
     * @param curveType the ECC curve type
     * @param clientNonce the client nonce
     * @param casDidDocument the CAS DID document
     * @return the generated request data
     */
    private EcdhReqData generateReqData(EcKeyPair publicKey, EccCurveType curveType, String clientNonce, DidDocument casDidDocument) {
        try {
            // Generate Candidate object.
            Candidate candidate = Candidate.builder()
                    .ciphers(Arrays.asList(SymmetricCipherType.values()))
                    .build();

            // Generate Proof object.
            Proof proof = new Proof();
            proof.setType(ProofType.SECP256R1_SIGNATURE_2018.getRawValue());
            proof.setProofPurpose(org.omnione.did.base.datamodel.enums.ProofPurpose.KEY_AGREEMENT.toString());
            proof.setCreated(DateTimeUtil.getCurrentUTCTimeString());
            proof.setVerificationMethod(signatureService.getVerificationMethod(casDidDocument, org.omnione.did.base.datamodel.enums.ProofPurpose.KEY_AGREEMENT));

            // Generate Signature Object. (except ProofValue)
            EcdhReqData signatureObject = EcdhReqData.builder()
                    .client(casDidDocument.getId())
                    .clientNonce(clientNonce)
                    .curve(curveType)
                    .publicKey(publicKey.getBase58CompreessPubKey())
                    .candidate(candidate)
                    .proof(proof)
                    .build();

            return signatureService.signEcdhReq(casDidDocument, signatureObject);
        } catch (CryptoException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Request enroll entity.
     * This method sends a request-enroll-entity request to the CAS and returns the response as a RequestEnrollEntityApiResDto object.
     *
     * @param txId the transaction ID
     * @param didAuth the DID Auth object
     * @return the response to the request-enroll-entity request
     */
    private RequestEnrollEntityApiResDto requestEnrollEntity(String txId, DidAuth didAuth) {
        RequestEnrollEntityApiReqDto request = RequestEnrollEntityApiReqDto.builder()
                .id(RandomUtil.generateMessageId())
                .txId(txId)
                .didAuth(didAuth)
                .build();

        return enrollFeign.requestEnrollEntityApi(request);
    }

    /**
     * Generate DID Auth.
     * This method generates the DID Auth object for the request-enroll-entity request.
     *
     * @param authNonce the authentication nonce
     * @param casDidDocument the CAS DID document
     * @return the generated DID Auth object
     */
    private DidAuth generateDidAuth(String authNonce, DidDocument casDidDocument) {
        // Generate Proof object.
        Proof proof = new Proof();
        proof.setType(ProofType.SECP256R1_SIGNATURE_2018.getRawValue());
        proof.setProofPurpose(org.omnione.did.base.datamodel.enums.ProofPurpose.AUTHENTICATION.toString());
        proof.setCreated(DateTimeUtil.getCurrentUTCTimeString());
        proof.setVerificationMethod(signatureService.getVerificationMethod(casDidDocument, org.omnione.did.base.datamodel.enums.ProofPurpose.AUTHENTICATION));

        // Generate Signature Object. (except ProofValue)
        DidAuth signatureObject = DidAuth.builder()
                .authNonce(authNonce)
                .did(casDidDocument.getId())
                .proof(proof)
                .build();

        return signatureService.signDidAuth(casDidDocument, signatureObject);
    }

    /**
     * Decrypt VC.
     * This method decrypts the Verifiable Credential received from the CAS.
     *
     * @param privateKey the private key
     * @param accEcdh the account ECDH data
     * @param enrollEntityResponse the response to the request-enroll-entity request
     * @param clientNonce the client nonce
     * @return the decrypted Verifiable Credential
     */
    private VerifiableCredential decryptVc(ECPrivateKey privateKey, AccEcdh accEcdh, RequestEnrollEntityApiResDto enrollEntityResponse, String clientNonce) {
        byte[] compressedPublicKey = BaseMultibaseUtil.decode(accEcdh.getPublicKey());
        byte[] sharedSecret = BaseCryptoUtil.generateSharedSecret(compressedPublicKey, privateKey.getEncoded(), EccCurveType.SECP_256_R1);
        byte[] mergeNonce = BaseCryptoUtil.mergeNonce(clientNonce, accEcdh.getServerNonce());
        byte[] mergeSharedSecretAndNonce = BaseCryptoUtil.mergeSharedSecretAndNonce(sharedSecret, mergeNonce, accEcdh.getCipher());

        byte[] iv = BaseMultibaseUtil.decode(enrollEntityResponse.getIv());

        byte[] decrypt = BaseCryptoUtil.decrypt(
                enrollEntityResponse.getEncVc(),
                mergeSharedSecretAndNonce,
                iv,
                accEcdh.getCipher(),
                accEcdh.getPadding()
        );

        String jsonVc = new String(decrypt);
        VerifiableCredential vc = new VerifiableCredential();
        vc.fromJson(jsonVc);

        return vc;
    }

    /**
     * Confirm enroll entity.
     * This method sends a confirm-enroll-entity request to the CAS and returns the response as a ConfirmEnrollEntityApiResDto object.
     *
     * @param txId the transaction ID
     * @param vcId the Verifiable Credential ID
     * @return the response to the confirm-enroll-entity request
     */
    private ConfirmEnrollEntityApiResDto confirmEnrollEntity(String txId, String vcId) {
        ConfirmEnrollEntityApiReqDto request = ConfirmEnrollEntityApiReqDto.builder()
                .id(RandomUtil.generateMessageId())
                .txId(txId)
                .vcId(vcId)
                .build();

        return enrollFeign.confirmEnrollEntityApi(request);
    }
}
