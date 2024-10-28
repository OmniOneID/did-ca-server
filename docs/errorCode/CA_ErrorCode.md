---
puppeteer:
    pdf:
        format: A4
        displayHeaderFooter: true
        landscape: false
        scale: 0.8
        margin:
            top: 1.2cm
            right: 1cm
            bottom: 1cm
            left: 1cm
    image:
        quality: 100
        fullPage: false
---

CA Server Error
==

- Date: 2024-09-03
- Version: v1.0.0

| Version | Date       | Changes                 |
|---------|------------|-------------------------|
| v1.0.0  | 2024-09-03 | Initial version         |

<div style="page-break-after: always;"></div>

# CA Server Error

# Table of Contents
- [Model](#model)
  - [Error Response](#error-response)
- [Error Code](#error-code)
  - [1-1. General (001xx)](#1-1-general-001xx)
  - [1-2. Cryptography and Security (002xx)](#1-2-cryptography-and-security-002xx)
  - [1-3. DID Related (003xx)](#1-3-did-related-003xx)
  - [1-4. User and Data (004xx)](#1-4-user-and-data-004xx)
  - [1-5. Wallet (005xx)](#1-5-wallet-005xx)
  - [1-6. Verification and Proof (006xx)](#1-6-verification-and-proof-006xx)
  - [1-7. Blockchain (007xx)](#1-7-blockchain-007xx)
  - [1-8. API Process Errors (008xx)](#1-8-api-process-errors-008xx)

# Model
## Error Response

### Description
```
Error struct for CAS Backend. It has code and message pair.
Code starts with SCRVCFA.
```

### Declaration
```java
public class ErrorResponse {
    private final String code;
    private final String description;
}
```

### Property

| Name        | Type   | Description                        | **M/O** | **Note** |
|-------------|--------|------------------------------------|---------| -------- |
| code        | String | Error code. It starts with SCRVCFA | M       |          | 
| message     | String | Error description                  | M       |          | 

# Error Code

## 1-1. General (001xx)

| Error Code   | Error Message                                  | Description | Action Required                                |
|--------------|------------------------------------------------|-------------|------------------------------------------------|
| SCRVCFA00100 | Unable to process the request.                 | -           | Verify request format and content.             |
| SCRVCFA00101 | Failed to encode data.                         | -           | Verify encoding process and input data.        |
| SCRVCFA00102 | Failed to decode data: incorrect encoding.     | -           | Check encoded data and decoding method.        |

<br>

## 1-2. Cryptography and Security (002xx)

| Error Code   | Error Message                                  | Description | Action Required                                |
|--------------|------------------------------------------------|-------------|------------------------------------------------|
| SCRVCFA00200 | Failed to encrypt PII.                         | -           | Check encryption algorithm and input data.     |
| SCRVCFA00201 | Failed to generate signature.                  | -           | Verify signature generation process and keys.  |
| SCRVCFA00202 | Failed to generate key pair.                   | -           | Check key generation algorithm and parameters. |
| SCRVCFA00203 | Failed to generate nonce.                      | -           | Verify nonce generation process.               |
| SCRVCFA00204 | Failed to merge nonce.                         | -           | Check nonce merging algorithm.                 |
| SCRVCFA00205 | Failed to generate session key.                | -           | Verify session key generation process.         |
| SCRVCFA00206 | Failed to merge nonce and shared secret.       | -           | Check merging algorithm for nonce and secret.  |
| SCRVCFA00207 | Failed to decrypt data.                        | -           | Verify decryption process and keys.            |
| SCRVCFA00208 | Failed to generate hash value.                 | -           | Check hashing algorithm and input data.        |

<br>

## 1-3. DID Related (003xx)

| Error Code   | Error Message                                  | Description | Action Required                                |
|--------------|------------------------------------------------|-------------|------------------------------------------------|
| SCRVCFA00300 | Failed to retrieve DID Document.               | -           | Verify DID and document retrieval process.     |

<br>

## 1-4. User and Data (004xx)

| Error Code   | Error Message                                  | Description | Action Required                                |
|--------------|------------------------------------------------|-------------|------------------------------------------------|
| SCRVCFA00400 | User PII not found.                            | -           | Check user data and retrieval process.         |
| SCRVCFA00401 | Tas Certificate VC data not found.             | -           | Verify certificate data and storage.           |

<br>

## 1-5. Wallet (005xx)

| Error Code   | Error Message                                  | Description | Action Required                                |
|--------------|------------------------------------------------|-------------|------------------------------------------------|
| SCRVCFA00500 | Failed to connect to wallet.                   | -           | Check wallet connection settings and network.  |
| SCRVCFA00501 | Failed to generate wallet signature.           | -           | Verify wallet signature generation process.    |
| SCRVCFA00502 | Failed to get File Wallet Manager              | -           | Check File Wallet Manager initialization.      |

<br>

## 1-6. Verification and Proof (006xx)

| Error Code   | Error Message                                  | Description | Action Required                                |
|--------------|------------------------------------------------|-------------|------------------------------------------------|
| SCRVCFA00600 | Invalid proof purpose.                         | -           | Verify proof purpose and format.               |

<br>

## 1-7. Blockchain (007xx)

| Error Code   | Error Message                                  | Description | Action Required                                |
|--------------|------------------------------------------------|-------------|------------------------------------------------|
| SCRVCFA00701 | Failed to retrieve DID document on the blockchain. | -       | Check blockchain connection and DID validity.  |

<br>

## 1-8. API Process Errors (008xx)

| Error Code   | Error Message                                           | Description | Action Required                                |
|--------------|----------------------------------------------------------|-------------|------------------------------------------------|
| SCRVCFA00800 | Failed to process the 'request-wallet-tokendata' API request. | -        | Verify API request format and parameters.       |
| SCRVCFA00801 | Failed to process the 'request-attested-appinfo' API request. | -        | Check API request and appinfo data.             |
| SCRVCFA00802 | Failed to process the 'save-user-info' API request.        | -           | Verify user info data and storage process.      |
| SCRVCFA00803 | Failed to process the 'retrieve-pii' API request.          | -           | Check PII retrieval process and permissions.    |
| SCRVCFA00804 | Failed to process the 'issue_certificate-vc' API request.  | -           | Verify certificate issuance process and data.   |
| SCRVCFA00805 | Failed to process the 'request-certificate-vc' API request. | -          | Check certificate request format and validity.  |