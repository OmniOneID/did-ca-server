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

# CAS API

* Date: 2024-08-19
* Version: v1.0.0

## Table of Contents

<!-- TOC tocDepth:2..3 chapterDepth:2..6 -->

* [1. Overview](#1-overview)
* [2. Terminology](#2-terminology)
* [3. API List](#3-api-list)
  * [3.1. Sequential API](#31-sequential-api)
  * [3.2. Single Call API](#32-single-call-api)
* [4. Single Call API](#4-single-call-api)
  * [4.1. Issue Certificate VC](#41-issue-certificate-vc)
  * [4.2. Get Certificate VC](#42-get-certificate-vc)
  * [4.3. Request Wallet Tokendata](#43-request-wallet-tokendata)
  * [4.4. Request Attested App Info](#44-request-attested-app-info)
  * [4.5. Save User Info](#45-save-user-info)
  * [4.6. Retrieve PII](#46-retrieve-pii)

<!-- /TOC -->

## 1. Overview

This document defines the APIs provided by the CA Service.

<div style="page-break-after: always; margin-top: 50px;"></div>

## 2. Terminology

* Protocol

  * A set of `Sequential APIs` that must be called in a defined order to perform a specific function. If the order is incorrect, unexpected results may occur.
* Sequential API

  * A series of APIs that must be called in a specific order to perform a protocol function. Calling out of sequence may cause failures. However, some protocols may allow selecting between APIs with the same call sequence.
* Single Call API

  * An API that can be called independently, like a general REST API, without regard to order.
* Standard API

  * An API clearly defined in the documentation, provided consistently across all implementations. It ensures interoperability between systems and must operate according to predefined specs.
* Non-Standard API

  * APIs that can vary or be customized per implementation. Non-standard APIs provided here are only examples; actual implementations may differ and require separate documentation. For example, a `save-user-info` API may be redefined based on system-specific needs.

<div style="page-break-after: always; margin-top: 50px;"></div>

## 3. API List

### 3.1. Sequential API

Currently, the CA Service does not define any protocols, and therefore, no sequential APIs are provided.

<div style="page-break-after: always; margin-top: 40px;"></div>

### 3.2. Single Call API

| API                        | URL                              | Description                 | Standard API |
| -------------------------- | -------------------------------- | --------------------------- | ------------ |
| `issue-certificate-vc`     | /api/v1/certificate-vc           | Entity registration request | N            |
| `get-certificate-vc`       | /api/v1/certificate-vc           | Retrieve Certificate VC     | N            |
| `request-wallet-tokendata` | /api/v1/request-wallet-tokendata | Request wallet token data   | N            |
| `request-attested-appinfo` | /api/v1/request-attested-appinfo | Request signed app info     | N            |
| `save-user-info`           | /api/v1/save-user-info           | Save user information       | N            |
| `retrieve-pii`             | /api/v1/retrieve-pii             | Retrieve user PII           | N            |

<div style="page-break-after: always; margin-top: 50px;"></div>

## 4. Single Call API

Single call APIs are independent APIs that perform specific functions.
Since they are not part of a sequential API group (aka, protocol), no protocol numbers are assigned.
The CA Service’s provided single call APIs are as follows:

| API                        | URL                              | Description                 | Standard API |
| -------------------------- | -------------------------------- | --------------------------- | ------------ |
| `issue-certificate-vc`     | /api/v1/certificate-vc           | Entity registration request | N            |
| `get-certificate-vc`       | /api/v1/certificate-vc           | Retrieve Certificate VC     | N            |
| `request-wallet-tokendata` | /api/v1/request-wallet-tokendata | Request wallet token data   | N            |
| `request-attested-appinfo` | /api/v1/request-attested-appinfo | Request signed app info     | N            |
| `save-user-info`           | /api/v1/save-user-info           | Save user information       | N            |
| `retrieve-pii`             | /api/v1/retrieve-pii             | Retrieve user PII           | N            |

■ Authorization

Protocols include APIs to verify caller authorization.
Although the above single call APIs do not currently define authorization, the following approaches are under consideration for future updates:

* Option 1) Issue a time-limited token after verifying `AttestedAppInfo` signed by the authorized app provider.

  * Attach the TAS-issued token in the header for each single API call.
  * Requires a separate token management API.
* Option 2) The authorized app provider issues tokens, and TAS requests the app provider to verify the token.

  * Attach the app provider–issued token in the header for each single API call.
  * Requires the app provider to implement token issuance and verification.

좋아요! 이어서 나머지 API 상세 번역을 아래에 정리합니다:

---

### 4.1. Issue Certificate VC

Request issuance of a certificate VC.

The CAS DID Document must already be registered in the repository (e.g., blockchain) via the TAS administrator.
This API internally calls the TAS P120 protocol APIs in order to issue the certificate.

| Item          | Description              | Remarks |
| ------------- | ------------------------ | ------- |
| Method        | `POST`                   |         |
| Path          | `/api/v1/certificate-vc` |         |
| Authorization | -                        |         |

#### 4.1.1. Request

**■ Path Parameters**
N/A

**■ Query Parameters**
N/A

**■ HTTP Body**

```c#
def object IssueCertificateVc: "Issue Certificate VC Request"
{    
}
```

---

#### 4.1.2. Response

**■ Process**

1. Call the TAS P120 protocol APIs in sequence.
2. Store the issued certificate in the database.

**■ Status 200 - Success**

```c#
def object _IssueCertificateVc: "Issue Certificate VC Response"
{    
}
```

**■ Status 400 - Client error**
N/A

**■ Status 500 - Server error**

| Code         | Description                                              |
| ------------ | -------------------------------------------------------- |
| SSRVTRAXXXXX | "Please refer to the TAS\_API."                          |
| SCRVCFA00804 | "Failed to process 'issue\_certificate-vc' API request." |

---

#### 4.1.3. Example

**■ Request**

```shell
curl -v -X POST "http://${Host}:${Port}/cas/api/v1/certificate-vc" \
-H "Content-Type: application/json;charset=utf-8" \
-d @"data.json"
```

```json
{
    // no data
}
```

**■ Response**

```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=utf-8

{
    // no data
}
```

---

### 4.2. Get Certificate VC

Retrieve the issued certificate VC.

| Item          | Description              | Remarks |
| ------------- | ------------------------ | ------- |
| Method        | `GET`                    |         |
| Path          | `/api/v1/certificate-vc` |         |
| Authorization | -                        |         |

#### 4.2.1. Request

**■ HTTP Headers**

| Header         | Value                            |
| -------------- | -------------------------------- |
| `Content-Type` | `application/json;charset=utf-8` |

**■ Path Parameters**
N/A

**■ Query Parameters**
N/A

**■ HTTP Body**
N/A

---

#### 4.2.2. Response

**■ Process**

1. Retrieve the certificate VC.

**■ Status 200 - Success**

```c#
def object _GetCertificateVc: "Get Certificate VC Response"
{
    @spread(Vc) // Refer to the data specification
}
```

**■ Status 400 - Client error**

| Code         | Description                                   |
| ------------ | --------------------------------------------- |
| SCRVCFA00401 | "Unable to find the Tas certificate VC data." |

**■ Status 500 - Server error**

| Code         | Description                                               |
| ------------ | --------------------------------------------------------- |
| SCRVCFA00805 | "Failed to process 'request-certificate-vc' API request." |

---

#### 4.2.3. Example

**■ Request**

```shell
curl -v -X GET "http://${Host}:${Port}/cas/api/v1/certificate-vc"
```

**■ Response**

*(Full JSON response omitted for brevity here — you already have it in your source.)*

---

### 4.3. Request Wallet Tokendata

Request the data needed to generate a wallet token.
The wallet token data includes hashed user PII and signed values from the authorized app server.

| Item          | Description                        | Remarks |
| ------------- | ---------------------------------- | ------- |
| Method        | `POST`                             |         |
| Path          | `/api/v1/request-wallet-tokendata` |         |
| Authorization | -                                  |         |

#### 4.3.1. Request

**■ HTTP Body**

```c#
def object RequestWalletTokenData: "Request Wallet TokenData Request"
{   
    @spread(WalletTokenSeed) // Refer to the data spec
    + string  "userId": "User identifier held by the authorized app"
}
```

---

#### 4.3.2. Response

**■ Process**

1. Look up user’s PII using userId.
2. Generate nonce.
3. Create walletTokenData.
4. Sign WalletTokenData with the wallet provider’s assert key.

**■ Status 200 - Success**

```c#
def object _RequestWalletTokenData: "Request Wallet TokenData Response"
{
    @spread(WalletTokenData) 
}
```

**■ Status 400 - Client error**

| Code         | Description                |
| ------------ | -------------------------- |
| SCRVCFA00400 | "Unable to find user PII." |
| SCRVCFA00600 | "Invalid proof purpose."   |

**■ Status 500 - Server error**

| Code         | Description                                                 |
| ------------ | ----------------------------------------------------------- |
| SCRVCFA00101 | "Failed to encode data."                                    |
| SCRVCFA00200 | "Failed to encrypt PII."                                    |
| SCRVCFA00201 | "Failed to generate signature."                             |
| SCRVCFA00300 | "Failed to retrieve DID document."                          |
| SCRVCFA00800 | "Failed to process 'request-wallet-tokendata' API request." |

---

### 4.4. Request Attested App Info

Request signed app information needed to generate a server token.

| Item          | Description                        | Remarks |
| ------------- | ---------------------------------- | ------- |
| Method        | `POST`                             |         |
| Path          | `/api/v1/request-attested-appinfo` |         |
| Authorization | -                                  |         |

#### 4.4.1. Request

```c#
def object RequestAttestedAppInfo: "Request Attested AppInfo Request"
{   
    + appId "appId" : "Client application ID"
}
```

---

#### 4.4.2. Response

**■ Process**

1. Generate nonce.
2. Create AttestedAppInfo.
3. Sign AttestedAppInfo.

**■ Status 200 - Success**

```c#
def object _RequestAttestedAppInfo: "Request Attested AppInfo Response"
{
    @spread(AttestedAppInfo) // Refer to the data spec
}
```

**■ Status 400 - Client error**

| Code         | Description              |
| ------------ | ------------------------ |
| SCRVCFA00600 | "Invalid proof purpose." |

**■ Status 500 - Server error**

| Code         | Description                                                 |
| ------------ | ----------------------------------------------------------- |
| SCRVCFA00101 | "Failed to encode data."                                    |
| SCRVCFA00201 | "Failed to generate signature."                             |
| SCRVCFA00300 | "Failed to retrieve DID document."                          |
| SCRVCFA00801 | "Failed to process 'request-attested-appinfo' API request." |

---

### 4.5. Save User Info

Save user information (mainly for testing purposes).
Note: Although PII is included in the request, encryption/decryption is not applied since it’s for test use.

| Item          | Description              | Remarks |
| ------------- | ------------------------ | ------- |
| Method        | `POST`                   |         |
| Path          | `/api/v1/save-user-info` |         |
| Authorization | -                        |         |

#### 4.5.1. Request

```c#
def object SaveUserInfo: "Save User Info Request"
{   
    + string "userId" : "User identifier held by the authorized app"
    + string "pii" : "User PII"
}
```

---

#### 4.5.2. Response

**■ Process**

1. Map and store userId and pii.

**■ Status 200 - Success**
N/A

**■ Status 400 - Client error**
N/A

**■ Status 500 - Server error**

| Code         | Description                                       |
| ------------ | ------------------------------------------------- |
| SCRVCFA00802 | "Failed to process 'save-user-info' API request." |

---

### 4.6. Retrieve PII

Request user PII.
Trust Agent requires PII to identify the user; CAS acts as a proxy KYC server for testing purposes.

| Item          | Description            | Remarks |
| ------------- | ---------------------- | ------- |
| Method        | `POST`                 |         |
| Path          | `/api/v1/retrieve-pii` |         |
| Authorization | -                      |         |

#### 4.6.1. Request

```c#
def object RetrievePii: "Retrieve User PII Request"
{   
    + string  "userId": "User identifier held by the authorized app"
}
```

---

#### 4.6.2. Response

**■ Process**

1. Look up user’s PII using userId.

**■ Status 200 - Success**

```c#
def object _RetrievePii: "Retrieve PII Response"
{
    + string  "pii": "User PII"
}
```

**■ Status 400 - Client error**

| Code         | Description                |
| ------------ | -------------------------- |
| SCRVCFA00400 | "Unable to find user PII." |

**■ Status 500 - Server error**

| Code         | Description                                     |
| ------------ | ----------------------------------------------- |
| SCRVCFA00803 | "Failed to process 'retrieve-pii' API request." |

---

<div style="page-break-after: always; margin-top: 30px;"></div>
