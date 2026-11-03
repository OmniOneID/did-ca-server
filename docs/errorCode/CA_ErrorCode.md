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

- Date: 2026-11-03
- Version: v3.0.0

| Version | Date       | Changes                 |
|---------|------------|-------------------------|
| v1.0.0  | 2024-09-03 | Initial version         |
| v3.0.0  | 2026-11-03 | Added user, wallet, admin, CAS, external server, token, and server configuration errors |

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
  - [1-9. Admin (009xx)](#1-9-admin-009xx)
  - [1-10. CAS (010xx)](#1-10-cas-010xx)
  - [1-11. External Server Integration (011xx)](#1-11-external-server-integration-011xx)
  - [1-12. Token (012xx)](#1-12-token-012xx)
  - [1-13. Server Configuration (018xx)](#1-13-server-configuration-018xx)

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
| SCRVCFA00402 | User not found. | - | Check that the user is registered. |
| SCRVCFA00403 | User already exists. | - | Use the existing user or another user ID. |
| SCRVCFA00404 | WalletId is already registered to another user. | - | Check the wallet ID. It is already linked to another user. |
| SCRVCFA00405 | Password does not match. | - | Check the password. |
| SCRVCFA00406 | Invalid user registration status for this operation. | - | Check the user registration status before calling the API. |
| SCRVCFA00407 | Failed to process signup. | - | Check the signup request and the server logs. |
| SCRVCFA00408 | Failed to process signin. | - | Check the signin request and the server logs. |
| SCRVCFA00409 | Failed to process signout. | - | Check the signout request and the server logs. |
| SCRVCFA00410 | Failed to process withdraw. | - | Check the withdraw request and the server logs. |

<br>

## 1-5. Wallet (005xx)

| Error Code   | Error Message                                  | Description | Action Required                                |
|--------------|------------------------------------------------|-------------|------------------------------------------------|
| SCRVCFA00500 | Failed to connect to wallet.                   | -           | Check wallet connection settings and network.  |
| SCRVCFA00501 | Failed to generate wallet signature.           | -           | Verify wallet signature generation process.    |
| SCRVCFA00502 | Failed to get File Wallet Manager              | -           | Check File Wallet Manager initialization.      |
| SCRVCFA00503 | Failed to create wallet: wallet already exists. | - | Use the existing wallet file, or remove it before creating a new one. |
| SCRVCFA00504 | Failed to generate keys: key already exists. | - | Use the existing key or a different key ID. |
| SCRVCFA00505 | Failed to load key element. | - | Check the wallet file and the key ID. |

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

<br>

## 1-9. Admin (009xx)

| Error Code | Error Message | Description | Action Required |
|------------|---------------|-------------|-----------------|
| SCRVCFA00900 | Failed to find admin: admin is not registered. | - | Register the admin account. |
| SCRVCFA00901 | Failed to register admin: admin is already registered. | - | Use the existing admin account. |

<br>

## 1-10. CAS (010xx)

| Error Code | Error Message | Description | Action Required |
|------------|---------------|-------------|-----------------|
| SCRVCFA01000 | CAS is not registered. | - | Register the CAS information in the Admin Console. |
| SCRVCFA01001 | CAS is already registered. | - | Use the registered CAS information. |
| SCRVCFA01002 | Invalid DID Document. | - | Check the format and contents of the DID Document. |
| SCRVCFA01003 | Failed to register CAS DID Document: document is already registered. | - | The DID Document is already registered. Check its status. |
| SCRVCFA01004 | Failed to generate DID document. | - | Check the wallet keys used to generate the DID Document. |
| SCRVCFA01005 | Failed to register CAS DID Document. | - | Check the TA connection and the DID Document. |
| SCRVCFA01006 | Failed to find CAS DID Document: no registration request has been made. | - | Request DID Document registration first. |
| SCRVCFA01007 | Failed to register CAS DID Document: document is already requested. | - | Wait until the TA approves the registration request. |
| SCRVCFA01008 | Failed to process certificate VC: invalid JSON format. | - | Check the certificate VC sent by the TA. |

<br>

## 1-11. External Server Integration (011xx)

| Error Code | Error Message | Description | Action Required |
|------------|---------------|-------------|-----------------|
| SCRVCFA01100 | Failed to ping the URL. | - | Check that the URL is reachable. |
| SCRVCFA01101 | Failed to process response: received unknown data from the tas. | - | Check the response format and version of the TA. |
| SCRVCFA01102 | Failed to communicate with tas: unknown error occurred. | - | Check the TA connection and configuration. |

<br>

## 1-12. Token (012xx)

| Error Code | Error Message | Description | Action Required |
|------------|---------------|-------------|-----------------|
| SCRVCFA01200 | Connection with token server failed. | - | Check the connection to the token server. |
| SCRVCFA01201 | An error occurred while issuing the token. | - | Check the token request and the server logs. |
| SCRVCFA01202 | An error occurred while revoking the token. | - | Check the token and the server logs. |
| SCRVCFA01203 | An error occurred while validating the token. | - | Check that the token is valid and has not expired. |
| SCRVCFA01204 | An error occurred while refreshing the token. | - | Check the refresh token. |
| SCRVCFA01205 | Token format is invalid and does not conform to the expected specification. | - | Check the token format. |

<br>

## 1-13. Server Configuration (018xx)

| Error Code | Error Message | Description | Action Required |
|------------|---------------|-------------|-----------------|
| SCRVCFA01801 | Server configuration key not found. | - | Register the key (for example, API_KEY_LSS) in Server Configuration of the Admin Console. |
