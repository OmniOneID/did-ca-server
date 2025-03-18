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
import org.omnione.did.base.datamodel.data.DidAuth;
import org.omnione.did.base.datamodel.data.EcdhReqData;
import org.omnione.did.base.datamodel.enums.ProofPurpose;
import org.omnione.did.base.db.domain.Cas;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.base.util.BaseCoreDidUtil;
import org.omnione.did.base.util.BaseMultibaseUtil;
import org.omnione.did.cas.v1.admin.service.query.CasQueryService;
import org.omnione.did.common.util.JsonUtil;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.did.Proof;
import org.omnione.did.data.model.did.VerificationMethod;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service for signing data.
 * This class provides methods for signing data and generating proofs.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SignatureService {
    private final DidDocService didDocService;
    private final FileWalletService fileWalletService;
    private final CasQueryService casQueryService;

    /**
     * Sign the given data set and generate a proof.
     *
     * @param dataSet the data set to sign
     * @return the proof object
     * @throws OpenDidException if the signature generation fails
     */
    public Proof signData(Map<String, Object> dataSet) {
        try {
            // Retrieve Origin Proof object.
            Proof originProof = (Proof)dataSet.get("proof");

            // Serialize and sort data set.
            String serializedJson = JsonUtil.serializeAndSort(dataSet);

            // Retrieve CAS.
            Cas existedCas = casQueryService.findCas();

            // Retrieve CAS DID Document.
            DidDocument casDidDocument = didDocService.getDidDocument(existedCas.getDid());

            // Sign data.
            String proofValue = signData(casDidDocument, serializedJson, ProofPurpose.fromDisplayName(originProof.getProofPurpose()));

            // Generate proof.
            Proof proof = new Proof();
            proof.setType(originProof.getType());
            proof.setCreated(originProof.getCreated());
            proof.setVerificationMethod(originProof.getVerificationMethod());
            proof.setProofPurpose(originProof.getProofPurpose());
            proof.setProofValue(proofValue);
            return proof;
        } catch (Exception e) {
            throw new OpenDidException(ErrorCode.SIGNATURE_GENERATION_FAILED);
        }
    }

    /**
     * Sign the given data set and generate a proof.
     *
     * @param casDidDocument the DID Document to use for signing
     * @param signatureMessage the message to sign
     * @param proofPurpose the purpose of the proof
     * @return the signature encoded in base64
     * @throws OpenDidException if the signature generation fails
     */
    private String signData(DidDocument casDidDocument, String signatureMessage, ProofPurpose proofPurpose) {
        // Get the key ID
        VerificationMethod verificationMethod = BaseCoreDidUtil.getVerificationMethod(casDidDocument, proofPurpose.toKeyId());
        String keyId = verificationMethod.getId();

        //  Sign the message.
        byte[] signature = fileWalletService.generateCompactSignature(keyId, signatureMessage);

        return BaseMultibaseUtil.encode(signature);
    }

    /**
     * Get the verification method for the given DID Document and proof purpose.
     *
     * @param casDidDoc the DID Document to use
     * @param proofPurpose the purpose of the proof
     * @return the verification method
     */
    public String getVerificationMethod(DidDocument casDidDoc, ProofPurpose proofPurpose) {
        String version = casDidDoc.getVersionId();
        VerificationMethod verificationMethod = BaseCoreDidUtil.getVerificationMethod(casDidDoc, proofPurpose.toKeyId());

        return casDidDoc.getId() + "?versionId=" + version + "#" + verificationMethod.getId();
    }

    /**
     * Sign the given ECDH request and generate a proof.
     * @param walletDidDocument the DID Document of the wallet
     * @param signatureObject the ECDH request to sign
     * @return the signed ECDH request
     * @throws OpenDidException if the signature generation fails
     */
    public EcdhReqData signEcdhReq(DidDocument walletDidDocument, EcdhReqData signatureObject) {
        try {
            // Serialize and sort data set.
            String serializedJson = JsonUtil.serializeAndSort(signatureObject);

            // Sign data.
            String proofValue = signData(walletDidDocument, serializedJson, ProofPurpose.fromDisplayName(signatureObject.getProof().getProofPurpose()));

            // Generate proof.
            Proof proof = new Proof();
            proof.setType(signatureObject.getProof().getType());
            proof.setCreated(signatureObject.getProof().getCreated());
            proof.setVerificationMethod(signatureObject.getProof().getVerificationMethod());
            proof.setProofPurpose(signatureObject.getProof().getProofPurpose());
            proof.setProofValue(proofValue);

            return EcdhReqData.builder()
                    .client(walletDidDocument.getId())
                    .clientNonce(signatureObject.getClientNonce())
                    .curve(signatureObject.getCurve())
                    .publicKey(signatureObject.getPublicKey())
                    .candidate(signatureObject.getCandidate())
                    .proof(proof)
                    .build();
        } catch (Exception e) {
            log.error("Error occurred while signing ECDH request: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.SIGNATURE_GENERATION_FAILED);
        }
    }

    /**
     * Sign the given DID Auth object and generate a proof.
     * @param casDidDocument the DID Document of the CAS
     * @param signatureObject the DID Auth object to sign
     * @return the signed DID Auth object
     * @throws OpenDidException if the signature generation fails
     */
    public DidAuth signDidAuth(DidDocument casDidDocument, DidAuth signatureObject) {
        try {
            // Serialize and sort data set.
            String serializedJson = JsonUtil.serializeAndSort(signatureObject);

            // Sign data.
            String proofValue = signData(casDidDocument, serializedJson, ProofPurpose.fromDisplayName(signatureObject.getProof().getProofPurpose()));

            // Generate proof.
            Proof proof = new Proof();
            proof.setType(signatureObject.getProof().getType());
            proof.setCreated(signatureObject.getProof().getCreated());
            proof.setVerificationMethod(signatureObject.getProof().getVerificationMethod());
            proof.setProofPurpose(signatureObject.getProof().getProofPurpose());
            proof.setProofValue(proofValue);

            return DidAuth.builder()
                    .authNonce(signatureObject.getAuthNonce())
                    .did(casDidDocument.getId())
                    .proof(proof)
                    .build();
        } catch (Exception e) {
            log.error("Error occurred while signing DID Auth: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.SIGNATURE_GENERATION_FAILED);
        }
    }
}
