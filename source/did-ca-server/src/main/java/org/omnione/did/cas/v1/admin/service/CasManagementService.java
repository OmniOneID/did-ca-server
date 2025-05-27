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


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.constants.UrlConstant.Tas;
import org.omnione.did.base.db.constant.CasStatus;
import org.omnione.did.base.db.domain.Cas;
import org.omnione.did.base.db.domain.CertificateVc;
import org.omnione.did.base.db.domain.EntityDidDocument;
import org.omnione.did.base.db.repository.CasRepository;
import org.omnione.did.base.db.repository.DidDocumentRepository;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.base.property.TasProperty;
import org.omnione.did.base.response.ErrorResponse;
import org.omnione.did.base.util.BaseCoreDidUtil;
import org.omnione.did.base.util.BaseMultibaseUtil;
import org.omnione.did.cas.v1.admin.constant.EntityStatus;
import org.omnione.did.cas.v1.admin.dto.cas.CaInfoResDto;
import org.omnione.did.cas.v1.admin.dto.cas.RegisterCaInfoReqDto;
import org.omnione.did.cas.v1.admin.dto.cas.RegisterDidToTaReqDto;
import org.omnione.did.cas.v1.admin.dto.cas.RequestEntityStatusResDto;
import org.omnione.did.cas.v1.admin.dto.cas.RequestRegisterDidReqDto;
import org.omnione.did.cas.v1.admin.dto.cas.SendCertificateVcReqDto;
import org.omnione.did.cas.v1.admin.dto.cas.SendEntityInfoReqDto;
import org.omnione.did.cas.v1.admin.service.query.CasQueryService;
import org.omnione.did.cas.v1.admin.service.query.DidDocumentQueryService;
import org.omnione.did.cas.v1.agent.service.CertificateVcQueryService;
import org.omnione.did.cas.v1.agent.service.EnrollEntityService;
import org.omnione.did.cas.v1.agent.service.FileWalletService;
import org.omnione.did.cas.v1.agent.service.StorageService;
import org.omnione.did.cas.v1.common.dto.EmptyResDto;
import org.omnione.did.common.exception.HttpClientException;
import org.omnione.did.common.util.HttpClientUtil;
import org.omnione.did.common.util.JsonUtil;
import org.omnione.did.core.data.rest.DidKeyInfo;
import org.omnione.did.core.manager.DidManager;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.enums.did.ProofPurpose;
import org.omnione.did.data.model.enums.vc.RoleType;
import org.omnione.did.data.model.vc.VerifiableCredential;
import org.omnione.did.wallet.key.WalletManagerInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class CasManagementService {
    private final CasQueryService casQueryService;
    private final StorageService storageService;
    private final CertificateVcQueryService certificateVcQueryService;
    private final CasRepository casRepository;
    private final FileWalletService fileWalletService;
    private final JsonParseService jsonParseService;
    private final TasProperty tasProperty;
    private final DidDocumentQueryService didDocumentQueryService;
    private final DidDocumentRepository didDocumentRepository;
    private final EnrollEntityService entollEntityService;

    /**
     * Get CAS information.
     *
     * @return CAS information
     */
    public CaInfoResDto getCasInfo() {
        log.debug("=== Starting getCasInfo ===");

        // Finding CAS
        log.debug("\t--> Finding CAS");
        Cas existingCas = casQueryService.findCasOrNull();

        // If CAS is not registered, return empty CAS info
        // or if CAS status is DID_DOCUMENT_REQUIRED, return only cas info. (not did document)
        if (existingCas == null || existingCas.getStatus() == CasStatus.DID_DOCUMENT_REQUIRED) {
            log.debug("\t--> CAS is not registered or status is DID_DOCUMENT_REQUIRED");
            return CaInfoResDto.fromEntity(existingCas);
        }

        // If CAS status is DID_DOCUMENT_REQUESTED, fetch the DID Document from DB
        if (existingCas.getStatus() == CasStatus.DID_DOCUMENT_REQUESTED) {
            log.debug("\t--> Fetching DID Document from DB");
            EntityDidDocument entityDidDocument = didDocumentQueryService.findDidDocument();
            DidDocument didDocument = new DidDocument();
            didDocument.fromJson(entityDidDocument.getDidDocument());

            return CaInfoResDto.fromEntity(existingCas, didDocument);
        }

        // If CAS didocument is already registered, fetch it from storage
        DidDocument didDocument = storageService.findDidDoc(existingCas.getDid());

        log.debug("=== Finished getCasInfo ===");
        return CaInfoResDto.fromEntity(existingCas, didDocument);
    }

    /**
     * Stores the Certificate VC issued by the TAS.
     * This functionality is intended for testing purposes only.
     *
     * @param sendCertificateVcReqDto Request data transfer object
     * @return Empty response DTO
     */
    public EmptyResDto createCertificateVc(SendCertificateVcReqDto sendCertificateVcReqDto) {
        log.debug("=== Starting createCertificateVc ===");

        // Decode the Certificate VC
        byte[] decodedVc = BaseMultibaseUtil.decode(sendCertificateVcReqDto.getCertificateVc());
        log.debug("Decoded VC: {}", new String(decodedVc));

        // Save the Certificate VC to the database
        log.debug("\t--> Saving Certificate VC to the database");
        certificateVcQueryService.save(CertificateVc.builder()
                .vc(new String(decodedVc))
                .build());

        log.debug("=== Finished createCertificateVc ===");

        return new EmptyResDto();
    }

    /**
     * Stores the entity information created by the TA.
     * This functionality is intended for testing purposes only.
     *
     * @param sendEntityInfoReqDto Request data transfer object
     * @return Empty response DTO
     */
    public EmptyResDto updateEntityInfo(SendEntityInfoReqDto sendEntityInfoReqDto) {
        log.debug("=== Starting updateEntityInfo ===");

        // Finding CAS
        log.debug("\t--> Finding CAS");
        Cas existedCas = casQueryService.findCasOrNull();

        // If CAS is not registered, create a new CAS entry
        if (existedCas == null) {
            log.debug("\t--> CAS is not registered. Creating a new CAS entry");
            casRepository.save(Cas.builder()
                    .name(sendEntityInfoReqDto.getName())
                    .did(sendEntityInfoReqDto.getDid())
                    .status(CasStatus.ACTIVATE)
                    .serverUrl(sendEntityInfoReqDto.getServerUrl())
                    .certificateUrl(sendEntityInfoReqDto.getCertificateUrl())
                    .build());
        }
        // If CAS is already registered, update the existing CAS entry
        else {
            log.debug("\t--> CAS is already registered. Updating existing CAS entry");
            existedCas.setName(sendEntityInfoReqDto.getName());
            existedCas.setDid(sendEntityInfoReqDto.getDid());
            existedCas.setServerUrl(sendEntityInfoReqDto.getServerUrl());
            existedCas.setCertificateUrl(sendEntityInfoReqDto.getCertificateUrl());
            existedCas.setStatus(CasStatus.ACTIVATE);
            casRepository.save(existedCas);
        }

        log.debug("=== Finished updateEntityInfo ===");

        return new EmptyResDto();
    }

    /**
     * Register CA information.
     *
     * @param registerCaInfoReqDto Request data transfer object
     * @return CAS information
     */
    public CaInfoResDto registerCaInfo(RegisterCaInfoReqDto registerCaInfoReqDto) {
        log.debug("=== Starting registerCaInfo ===");

        Cas cas = casQueryService.findCasOrNull();
        log.debug("\t--> Found CAS: {}", cas);

        if (cas == null) {
            log.debug("\t--> CAS is not registered yet. Proceeding with new registration.");
            cas = Cas.builder()
                    .name(registerCaInfoReqDto.getName())
                    .serverUrl(registerCaInfoReqDto.getServerUrl())
                    .certificateUrl(registerCaInfoReqDto.getServerUrl() + "/api/v1/certificate-vc")
                    .status(CasStatus.DID_DOCUMENT_REQUIRED)
                    .build();
            casRepository.save(cas);

            log.debug("=== Finished registerCaInfo ===");
            return buildCasInfoResponse(cas);
        }

        if (cas.getStatus() == CasStatus.ACTIVATE) {
            log.error("CAS is already registered");
            throw new OpenDidException(ErrorCode.CAS_ALREADY_REGISTERED);
        }

        log.debug("\t--> Updating CAS information");
        cas.setName(registerCaInfoReqDto.getName());
        cas.setServerUrl(registerCaInfoReqDto.getServerUrl());
        cas.setCertificateUrl(registerCaInfoReqDto.getServerUrl() + "/api/v1/certificate-vc");
        casRepository.save(cas);

        log.debug("=== Finished registerCaInfo ===");

        return buildCasInfoResponse(cas);
    }

    /**
     * Builds the CAS information response.
     *
     * @param cas CAS entity
     * @return CAS information response DTO
     */
    private CaInfoResDto buildCasInfoResponse(Cas cas) {
        if (cas.getStatus() == CasStatus.DID_DOCUMENT_REQUIRED
            || cas.getStatus() == CasStatus.DID_DOCUMENT_REQUESTED) {
            return CaInfoResDto.fromEntity(cas);
        }

        log.debug("\t--> Finding TAS DID Document");
        DidDocument didDocument = storageService.findDidDoc(cas.getDid());
        return CaInfoResDto.fromEntity(cas, didDocument);
    }

    /**
     * Register CA DID Document automatically.
     *
     * This method creates a wallet, generates keys, and creates a DID Document.
     *
     * @return Map containing the generated DID Document
     */
    public Map<String, Object> registerCaDidDocumentAuto() {
        log.debug("=== Starting registerCaDidDocumentAuto ===");

        // Finding CAS
        log.debug("\t--> Finding CAS");
        Cas existedCas = casQueryService.findCas();
        log.debug("\t--> Found CAS: {}", existedCas);

        // Check CAS status
        if (existedCas.getStatus() != CasStatus.DID_DOCUMENT_REQUIRED) {
            if (existedCas.getStatus() == CasStatus.DID_DOCUMENT_REQUESTED) {
                log.error("CAS DID Document is already requested");
                throw new OpenDidException(ErrorCode.CAS_DID_DOCUMENT_ALREADY_REQUESTED);
            }
            log.error("CAS DID Document is already registered");
            throw new OpenDidException(ErrorCode.CAS_DID_DOCUMENT_ALREADY_REGISTERED);
        }

        // Step1: Create Wallet and keys
        WalletManagerInterface walletManager = initializeWalletWithKeys();

        // Step2: Create DID Document
        DidDocument didDocument = createDidDocumentAuto(walletManager);

        log.debug("=== Finished registerCaDidDocumentAuto ===");

        return jsonParseService.parseDidDocToMap(didDocument.toJson());
    }

    /*
     * Generate CA wallet and keys.
     */
    public WalletManagerInterface initializeWalletWithKeys() {
        return fileWalletService.initializeWalletWithKeys();
    }

    /**
     * Create DID Document automatically.
     *
     * This method creates a DID Document using the provided wallet manager.
     *
     * @param walletManager Wallet manager
     * @return Created DID Document
     */
    public DidDocument createDidDocumentAuto(WalletManagerInterface walletManager) {
        String did = "did:omn:cas";

        Map<String, List<ProofPurpose>> purposes = BaseCoreDidUtil.createDefaultProofPurposes();
        List<DidKeyInfo> keyInfos = BaseCoreDidUtil.getDidKeyInfosFromWallet(walletManager, did, purposes);

        DidManager didManager = new DidManager();
        DidDocument unsignedDoc = BaseCoreDidUtil.createDidDocument(didManager, did, did, keyInfos);

        List<String> signingKeys = BaseCoreDidUtil.getSigningKeyIds(purposes);
        DidDocument signedDoc = BaseCoreDidUtil.signAndAddProof(didManager, walletManager, signingKeys);

        return signedDoc;
    }

    /**
     * Request to register CAS DID Document.
     *
     * Note:
     * - Currently, there is no functionality to cancel a DID Document registration request once it has been sent to TAS.
     * - This cancellation feature is planned for future development.
     *
     * @param reqDto Request data transfer object
     * @return Empty response DTO
     */
    public EmptyResDto requestRegisterDid(RequestRegisterDidReqDto reqDto) {
        try {
            log.debug("=== Starting requestRegisterDid ===");

            Cas cas = casQueryService.findCas();
            log.debug("\t--> Found CAS: {}", cas);

            if (cas.getStatus() != CasStatus.DID_DOCUMENT_REQUIRED) {
                if (cas.getStatus() == CasStatus.DID_DOCUMENT_REQUESTED) {
                    log.error("CAS DID Document is already requested");
                    throw new OpenDidException(ErrorCode.CAS_DID_DOCUMENT_ALREADY_REQUESTED);
                }
                log.error("CAS DID Document is already registered");
                throw new OpenDidException(ErrorCode.CAS_DID_DOCUMENT_ALREADY_REGISTERED);
            }

            // Send the register DID request to TAS
            log.debug("\t--> Sending register DID request to TAS");
            EmptyResDto resDto = sendRegisterDid(cas, reqDto);

            // Update didDocument in the database
            log.debug("\t--> Updating DID Document in the database");
            EntityDidDocument entityDidDoc = didDocumentQueryService.findDidDocumentOrNull();
            if (entityDidDoc == null) {
                entityDidDoc = EntityDidDocument.builder()
                        .didDocument(reqDto.getDidDocument())
                        .build();
            } else {
                entityDidDoc.setDidDocument(reqDto.getDidDocument());
            }
            didDocumentRepository.save(entityDidDoc);

            // Update CAS status
            log.debug("\t--> Updating CAS did and status");
            cas.setStatus(CasStatus.DID_DOCUMENT_REQUESTED);
            cas.setDid(BaseCoreDidUtil.parseDid(reqDto.getDidDocument()));
            casRepository.save(cas);

            log.debug("=== Finished requestRegisterDid ===");

            return resDto;
        } catch (OpenDidException e) {
            log.error("Failed to register CAS DID Document: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to register CAS DID Document", e);
            throw new OpenDidException(ErrorCode.FAILED_TO_REGISTER_CAS_DID_DOCUMENT);
        }
    }

    private EmptyResDto sendRegisterDid(Cas cas, RequestRegisterDidReqDto requestRegisterDidReqDto) {
        String url = tasProperty.getUrl() + Tas.ADMIN_V1 + Tas.REGISTER_DID_PUBLIC;

        String encodedDidDocument = BaseMultibaseUtil.encode(requestRegisterDidReqDto.getDidDocument().getBytes());
        RegisterDidToTaReqDto registerDidToTaReqDto = RegisterDidToTaReqDto.builder()
                .didDoc(encodedDidDocument)
                .name(cas.getName())
                .serverUrl(cas.getServerUrl())
                .certificateUrl(cas.getCertificateUrl())
                .role(RoleType.APP_PROVIDER)
                .build();
        try {
            String request = JsonUtil.serializeToJson(registerDidToTaReqDto);
            return HttpClientUtil.postData(url, request, EmptyResDto.class);
        } catch (HttpClientException e) {
            log.error("HttpClientException occurred while sending register-did-public request: {}", e.getResponseBody(), e);
            ErrorResponse errorResponse = convertExternalErrorResponse(e.getResponseBody());
            throw new OpenDidException(errorResponse);
        } catch (Exception e) {
            log.error("An unknown error occurred while sending register-did-public request", e);
            throw new OpenDidException(ErrorCode.TAS_COMMUNICATION_ERROR);
        }
    }

    /**
     * Converts an external error response string to an ErrorResponse object.
     * This method attempts to parse the given JSON string into an ErrorResponse instance.
     *
     * @param resBody The JSON string representing the external error response
     * @return An ErrorResponse object parsed from the input string
     * @throws OpenDidException with ErrorCode.ISSUER_UNKNOWN_RESPONSE if parsing fails
     */
    private ErrorResponse convertExternalErrorResponse(String resBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(resBody, ErrorResponse.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse external error response: {}", resBody, e);
            throw new OpenDidException(ErrorCode.TAS_UNKNOWN_RESPONSE);
        }
    }

    /**
     * Request the status of the entity.
     *
     * @return RequestEntityStatusResDto containing the status information
     */
    public RequestEntityStatusResDto requestEntityStatus() {
        log.debug("=== Starting requestEntityStatus ===");

        // Finding CAS
        log.debug("\t--> Finding CAS");
        Cas exsitedCas = casQueryService.findCas();

        String did = exsitedCas.getDid();
        if (did == null) {
            EntityDidDocument entityDidDocument = didDocumentQueryService.findDidDocument();
            DidDocument didDocument = new DidDocument();
            didDocument.fromJson(entityDidDocument.getDidDocument());
            did = didDocument.getId();
        }

        // Sending request-status request to TAS
        log.debug("\t--> Sending request-status request to TAS");
        RequestEntityStatusResDto requestEntityStatusResDto = sendRequestEntityStatus(did);

        // Update CAS status based on the response
        if (requestEntityStatusResDto.getStatus() == EntityStatus.NOT_REGISTERED) {
            log.debug("\t--> TA has deleted the entity's registration request. Updating CAS status accordingly");
            exsitedCas.setStatus(CasStatus.DID_DOCUMENT_REQUIRED);
            casRepository.save(exsitedCas);
        } else if (requestEntityStatusResDto.getStatus() == EntityStatus.CERTIFICATE_VC_REQUIRED) {
            log.debug("\t--> TA has requested a certificate VC. Updating CAS status accordingly");
            exsitedCas.setStatus(CasStatus.CERTIFICATE_VC_REQUIRED);
            casRepository.save(exsitedCas);
        }

        log.debug("=== Finished requestEntityStatus ===");

        return requestEntityStatusResDto;
    }

    private RequestEntityStatusResDto sendRequestEntityStatus(String did) {
        String url = tasProperty.getUrl() + Tas.ADMIN_V1 + Tas.REQUEST_ENTITY_STATUS + "?did=" + did;

        try {
            return HttpClientUtil.getData(url, RequestEntityStatusResDto.class);
        } catch (HttpClientException e) {
            log.error("HttpClientException occurred while sending request-status request: {}", e.getResponseBody(), e);
            ErrorResponse errorResponse = convertExternalErrorResponse(e.getResponseBody());
            throw new OpenDidException(errorResponse);
        } catch (Exception e) {
            log.error("An unknown error occurred while sending request-status request", e);
            throw new OpenDidException(ErrorCode.TAS_COMMUNICATION_ERROR);
        }
    }

    public Map<String, Object> enrollEntity() {
        try {
            log.debug("=== Starting enrollEntity ===");
            // Register the entity
            log.debug("\t--> Registering the entity");
            entollEntityService.enrollEntity();

            // Finding Certificate VC
            log.debug("\t--> Finding Certificate VC");
            CertificateVc certificateVc = certificateVcQueryService.findCertificateVc();
            VerifiableCredential verifiableCredential = new VerifiableCredential();
            verifiableCredential.fromJson(certificateVc.getVc());

            log.debug("=== Finished enrollEntity ===");
            return jsonParseService.parseCertificateVcToMap(verifiableCredential.toJson());
        } catch(OpenDidException e) {
            log.error("An OpenDidException occurred while sending requestCertificateVc request", e);
            throw e;
        } catch (Exception e) {
            log.error("An unknown error occurred while sending requestCertificateVc request", e);
            throw new OpenDidException(ErrorCode.FAILED_TO_REQUEST_CERTIFICATE_VC);
        }
    }
}
