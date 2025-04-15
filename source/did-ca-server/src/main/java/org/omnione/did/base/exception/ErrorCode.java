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

package org.omnione.did.base.exception;

import lombok.Getter;

/**
 * Enumeration of error codes used in the DID Verifier system.
 * Each error code contains a unique identifier, a descriptive message, and an associated HTTP status code.
 *
 */
@Getter
public enum ErrorCode {
    // General Errors (100-199)
    REQUEST_BODY_UNREADABLE("SCRVCFA00100", "Unable to process the request.", 400),
    ENCODING_FAILED("SCRVCFA00101", "Failed to encode data.", 500),
    DECODING_FAILED("SCRVCFA00102", "Failed to decode data: incorrect encoding.", 400),

    // Cryptography and Security Errors (200-299)
    FAILED_TO_ENCRYPT_PII("SCRVCFA00200", "Failed to encrypt PII.", 500),
    SIGNATURE_GENERATION_FAILED("SCRVCFA00201", "Failed to generate signature.", 500),
    KEY_PAIR_GENERATION_FAILED("SCRVCFA00202", "Failed to generate key pair.", 500),
    NONCE_GENERATION_FAILED("SCRVCFA00203", "Failed to generate nonce.", 500),
    NONCE_MERGE_FAILED("SCRVCFA00204", "Failed to merge nonce.", 500),
    SESSION_KEY_GENERATION_FAILED("SCRVCFA00205", "Failed to generate session key.", 500),
    NONCE_AND_SHARED_SECRET_MERGE_FAILED("SCRVCFA00206", "Failed to merge nonce and shared secret.", 500),
    DECRYPTION_FAILED("SCRVCFA00207", "Failed to decrypt data.", 500),
    HASH_GENERATION_FAILED("SCRVCFA00208", "Failed to generate hash value.", 500),

    // DID Related Errors (300-399)
    DID_DOCUMENT_RETRIEVAL_FAILED("SCRVCFA00300", "Failed to retrieve DID Document.", 500),

    // User and Data Errors (400-499)
    USER_PII_NOT_FOUND("SCRVCFA00400", "User PII not found.", 400),
    CERTIFICATE_DATA_NOT_FOUND("SCRVCFA00401", "Tas Certificate VC data not found.", 500),

    // Wallet Errors (500-599)
    WALLET_CONNECTION_FAILED("SCRVCFA00500", "Failed to connect to wallet.", 500),
    WALLET_SIGNATURE_GENERATION_FAILED("SCRVCFA00501", "Failed to generate wallet signature.", 500),
    FAILED_TO_GET_FILE_WALLET_MANAGER("SCRVCFA00502", "Failed to get File Wallet Manager", 500),
    WALLET_ALREADY_EXISTS("SCRVCFA00503", "Failed to create wallet: wallet already exists.", 500),
    KEY_ALREADY_EXISTS("SCRVCFA00504", "Failed to generate keys: key already exists.", 500),
    FAILED_TO_LOAD_KEY_ELEMENT("SCRVCFA00505", "Failed to load key element.", 500),

    // Verification and Proof Errors (600-699)
    INVALID_PROOF_PURPOSE("SCRVCFA00600", "Invalid proof purpose.", 400),

    // Blockchain (700~799),
    BLOCKCHAIN_GET_DID_DOC_FAILED("SCRVCFA00701", "Failed to retrieve DID document on the blockchain.", 500),

    // API Process Errors (800~899)
    FAILED_TO_REQUEST_WALLET_TOKEN_DATA("SCRVCFA00800", "Failed to process the 'request-wallet-tokendata' API request.", 500),
    FAILED_TO_REQUEST_ATTESTED_APPINFO("SCRVCFA00801", "Failed to process the 'request-attested-appinfo' API request.", 500),
    FAILED_TO_SAVE_USER_INFO("SCRVCFA00802", "Failed to process the 'save-user-info' API request.", 500),
    FAILED_TO_RETRIEVE_PII("SCRVCFA00803", "Failed to process the 'retrieve-pii' API request.", 500),
    FAILED_TO_ISSUE_CERTIFICATE_VC("SCRVCFA00804", "Failed to process the 'issue_certificate-vc' API request.", 500),
    FAILED_TO_REQUEST_CERTIFICATE_VC("SCRVCFA00805", "Failed to process the 'request-certificate-vc' API request.", 500),

    // Admin-related errors (900~999)
    ADMIN_INFO_NOT_FOUND("SCRVCFA00900", "Failed to find admin: admin is not registered.", 400),
    ADMIN_ALREADY_EXISTS("SCRVCFA00901", "Failed to register admin: admin is already registered.", 400),

    // CAS-related errors (1000 ~ 1099)
    CAS_INFO_NOT_FOUND("SCRVCFA01000", "CAS is not registered.", 500),
    CAS_ALREADY_REGISTERED("SCRVCFA01001", "CAS is already registered.", 400),
    INVALID_DID_DOCUMENT("SCRVCFA01002", "Invalid DID Document.", 400),
    CAS_DID_DOCUMENT_ALREADY_REGISTERED("SCRVCFA01003", "Failed to register CAS DID Document: document is already registered.", 400),
    DIDDOC_GENERATION_FAILED("SCRVCFA01004", "Failed to generate DID document.", 500),
    FAILED_TO_REGISTER_CAS_DID_DOCUMENT("SCRVCFA01005", "Failed to register CAS DID Document.", 500),
    CAS_DID_DOCUMENT_NOT_FOUND("SCRVCFA01006", "Failed to find CAS DID Document: o registration request has been made.", 400),
    CAS_DID_DOCUMENT_ALREADY_REQUESTED("SCRVCFA01007", "Failed to register CAS DID Document: document is already requested.", 400),


    // External server integration errors (1100 ~ 1199)
    URL_PING_ERROR("SCRVCFA01100", "Failed to ping the URL.", 400),
    TAS_UNKNOWN_RESPONSE("SCRVCFA01101", "Failed to process response: received unknown data from the tas.", 500),
    TAS_COMMUNICATION_ERROR("SCRVCFA01102", "Failed to communicate with tas: unknown error occurred.", 500),
    ;

    private final String code;
    private final String message;
    private final int httpStatus;

    /**
     * Constructor for ErrorCode enum.
     *
     * @param code       Error Code
     * @param message    Error Message
     * @param httpStatus HTTP Status Code
     */
    ErrorCode(String code, String message, int httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    /**
     * Get the error code.
     *
     * @return Error Code
     */
    public static String getMessageByCode(String code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode.getMessage();
            }
        }
        return "Unknown error code: " + code;
    }

    /**
     * Get the error message.
     *
     * @return Error Message
     */
    @Override
    public String toString() {
        return String.format("ErrorCode{code='%s', message='%s', httpStatus=%d}", code, message, httpStatus);
    }
}
