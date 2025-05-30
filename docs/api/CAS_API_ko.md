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

CAS API
==

- 일자: 2024-08-19
- 버전: v1.0.0
  
목차
---
<!-- TOC tocDepth:2..3 chapterDepth:2..6 -->

- [1. 개요](#1-개요)
- [2. 용어 설명](#2-용어-설명)
- [3. API 목록](#3-api-목록)
    - [3.1. 순차 API](#31-순차-api)
    - [3.2. 단일호출 API](#32-단일호출-api)
- [4. 단일 호출 API](#4-단일-호출-api)
    - [4.1. Issue Certificate VC](#41-issue-certificate-vc)
    - [4.2. Get Certificate Vc](#42-get-certificate-vc)
    - [4.3. Request Wallet Tokendata](#43-request-wallet-tokendata)
    - [4.4. Request Attested App Info](#44-request-attested-app-info)
    - [4.5. Save User Info](#45-save-user-info)
    - [4.6. Retrieve PII](#46-retrieve-pii)

<!-- /TOC -->

## 1. 개요

본 문서는 CA Service가 제공하는 API를 정의한다.

<div style="page-break-after: always; margin-top: 50px;"></div>

## 2. 용어 설명
- 프로토콜 (Protocol)
  - 특정 기능을 수행하기 위해 정해진 순서에 따라 호출해야 하는 `순차 API`의 집합이다. API 호출 순서를 엄격히 따라야 하며, 순서가 잘못될 경우 예상하지 못한 결과가 발생할 수 있다.
- 순차 API (Sequential API)
  - 특정 기능(프로토콜)을 수행하기 위해 정해진 순서대로 호출하는 일련의 API를 말한다. 각 API는 순차적으로 호출되어야 하며, 순서가 잘못될 경우 제대로 동작하지 않을 수 있다.
  - 그러나 일부 프로토콜에서는 같은 호출 순서를 가진 API가 존재할 수 있으며, 이 경우 하나의 API를 선택하여 호출할 수 있다.
- 단일 호출 API (Single Call API)
  - 일반적인 REST API처럼 순서에 관계없이 독립적으로 호출 가능한 API를 의미한다.
- 표준 API (Standard API)
  - API 문서에서 명확하게 정의된 API로, 모든 구현체에서 일관된 방식으로 제공되어야 한다. 표준 API는 시스템 간 상호 운용성을 보장하며, 사전에 정의된 스펙에 따라 동작해야 한다.
- 비표준 API (Non-Standard API)
  - 구현체마다 필요에 따라 다르게 정의되거나 커스터마이징될 수 있는 API이다. 본 문서에서 제공하는 비표준 API는 한 가지 예시일 뿐이며, 각 구현체에 맞춰 다르게 구현될 수 있다. 이 경우, 구현체별 별도의 문서화가 필요하다.
  - 예를 들어, 사용자 정보 저장 기능은 시스템에 따라 구현 방법이 달라질 수 있으며, `save-user-info` API와 같은 비표준 API는 각 구현체에서 필요한 방식으로 재정의할 수 있다.

<div style="page-break-after: always; margin-top: 50px;"></div>

## 3. API 목록

### 3.1. 순차 API
CA Service는 현재 특정 기능을 수행하기 위한 프로토콜이 정의되어 있지 않으며, 따라서 순차 API도 제공하지 않는다.

<div style="page-break-after: always; margin-top: 40px;"></div>

### 3.2. 단일호출 API

| API                    | URL                              | Description           | 표준API |
| ---------------------- | -------------------------------- | --------------------- | ------- |
| `issue-certificate-vc` | /api/v1/certificate-vc       | Entity 등록 요청      | N       |
| `get-certificate-vc`   | /api/v1/certificate-vc       | 가입증명서 조회       | N       |
| `request-wallet-tokendata` | /api/v1/request-wallet-tokendata | 월렛 토큰 데이터 요청 | N       |
| `request-attested-appinfo` | /api/v1/request-attested-appinfo | 서명된 앱 정보 요청   | N       |
| `save-user-info`       | /api/v1/save-user-info       | 사용자 정보 저장      | N       |
| `retrieve-pii`         | /api/v1/retrieve-pii         | 사용자 PII 조회       | N       |


<div style="page-break-after: always; margin-top: 50px;"></div>

## 4. 단일 호출 API

단일 호출 API는 특정 기능을 수행하는 하나의 독립된 API이다.
따라서 순서대로 호출해야 하는 API의 집단인 순차 API(aka, 프로토콜)이 아니므로 프로토콜 번호가 부여되지 않는다.
CA Service가 제공하는 단일 호출 API 목록은 아래 표와 같다.

| API                    | URL                              | Description           | 표준API |
| ---------------------- | -------------------------------- | --------------------- | ------- |
| `issue-certificate-vc` | /api/v1/certificate-vc       | Entity 등록 요청      | N       |
| `get-certificate-vc`   | /api/v1/certificate-vc       | 가입증명서 조회       | N       |
| `request-wallet-tokendata` | /api/v1/request-wallet-tokendata | 월렛 토큰 데이터 요청 | N       |
| `request-attested-appinfo` | /api/v1/request-attested-appinfo | 서명된 앱 정보 요청   | N       |
| `save-user-info`       | /api/v1/save-user-info       | 사용자 정보 저장      | N       |
| `retrieve-pii`         | /api/v1/retrieve-pii         | 사용자 PII 조회       | N       |

■ Authorization

프로토콜에는 '호출자의 호출 권한을 확인'(authorization)하는 API가 포함되어 있다.
상기 목록의 단일 호출 API는 authroization에 대하여 정의하지 않았으나,
향후 다음의 방안을 고려하여 추가할 예정이다.

- 1안) 인가앱 사업자가 서명한 `AttestedAppInfo` 정보를 확인한 후 일정기간 사용이 가능한 토큰을 발급
    - 단일 API 호출 시 헤더에 TAS 발행 토큰을 첨부
    - 별도의 토큰 관리 API 필요
- 2안) 인가앱 사업자가 인가앱에 토큰을 발행하고 TAS가 인가앱 사업자에 토큰 검증을 요청
    - 단일 API 호출 시 헤더에 인가앱 사업자 발행 토큰을 첨부
    - 인가앱 사업자가 토큰을 발행하고 검증해주는 기능 구현 필요

### 4.1. Issue Certificate VC

가입증명서 발급을 요청한다.

CAS의 DID Document가 TAS 관리자를 통하여 저장소(예:블록체인)에 이미 등록되어 있어야 한다.
이 API에서는 TAS의 P120 프로토콜 API를 순서대로 호출하여 가입증명서를 발급 받는다.

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
def object IssueCertificateVc: "Issue Certificate VC 요청문"
{    
}
```

<div style="page-break-after: always; margin-top: 30px;"></div>

#### 4.1.2. Response

**■ Process**
1. TA P120 프로토콜의 API를 순서대로 호출
1. 발급받은 가입증명서를 DB에 저장

**■ Status 200 - Success**

```c#
def object _IssueCertificateVc: "Issue Certificate VC 응답문"
{    
}
```

**■ Status 400 - Client error**

N/A

**■ Status 500 - Server error**

| Code         | Description                                            |
| ------------ | ------------------------------------------------------ |
| SSRVTRAXXXXX | "TAS_API를 참고해주세요."                              |
| SCRVCFA00804 | "'issue_certificate-vc' API 요청 처리에 실패했습니다." |

<div style="page-break-after: always; margin-top: 30px;"></div>

#### 4.1.3. Example

**■ Request**

```shell
curl -v -X POST "http://${Host}:${Port}/cas/api/v1/certificate-vc" \
-H "Content-Type: application/json;charset=utf-8" \
-d @"data.json"
```

```json
{
    //no data
}
```

**■ Response**

```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=utf-8

{
    //no data
}
```

<div style="page-break-after: always; margin-top: 40px;"></div>

### 4.2. Get Certificate Vc

가입증명서를 조회한다.

| Item          | Description              | Remarks |
| ------------- | ------------------------ | ------- |
| Method        | `GET`                    |         |
| Path          | `/api/v1/certificate-vc` |         |
| Authorization | -                        |         |

#### 4.2.1. Request

**■ HTTP Headers**

| Header           | Value                            | Remarks |
| ---------------- | -------------------------------- | ------- |
| + `Content-Type` | `application/json;charset=utf-8` |         |     

**■ Path Parameters**

N/A

**■ Query Parameters**

N/A

**■ HTTP Body**

N/A

<div style="page-break-after: always; margin-top: 30px;"></div>

#### 4.2.2. Response

**■ Process**
1. 가입증명서 조회

**■ Status 200 - Success**

```c#
def object _GetCertificateVc: "Get Certificate VC 응답문"
{
    @spread(Vc) // 데이터 명세서 참고
}
```

**■ Status 400 - Client error**

| Code         | Description                                |
| ------------ | ------------------------------------------ |
| SCRVCFA00401 | "Tas 인증서 VC 데이터를 찾을 수 없습니다." |

**■ Status 500 - Server error**

| Code         | Description                                              |
| ------------ | -------------------------------------------------------- |
| SCRVCFA00805 | "'request-certificate-vc' API 요청 처리에 실패했습니다." |

<div style="page-break-after: always; margin-top: 30px;"></div>

#### 4.2.3. Example

**■ Request**

```shell
curl -v -X GET "http://${Host}:${Port}/cas/api/v1/certificate-vc"
```

**■ Response**

```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=utf-8

{
  "@context": [
    "https://www.w3.org/ns/credentials/v2"
  ],
  "credentialSchema": {
    "id": "http://127.0.0.130:8090/tas/api/v1/vc-schema?name=certificate",
    "type": "OsdSchemaCredential"
  },
  "credentialSubject": {
    "claims": [
      {
        "caption": "subject",
        "code": "org.opendid.v1.subject",
        "format": "plain",
        "hideValue": false,
        "type": "text",
        "value": "o=Cas"
      },
      {
        "caption": "role",
        "code": "org.opendid.v1.role",
        "format": "plain",
        "hideValue": false,
        "type": "text",
        "value": "AppProvider"
      }
    ],
    "id": "did:omn:cas"
  },
  "encoding": "UTF-8",
  "evidence": [
    {
      "attribute": {
        "licenseNumber": "1234567890"
      },
      "documentPresence": "Physical",
      "evidenceDocument": "BusinessLicense",
      "subjectPresence": "Physical",
      "type": "DocumentVerification",
      "verifier": "did:omn:tas"
    }
  ],
  "formatVersion": "1.0",
  "id": "e6fd95e1-dd15-4b3f-8d5e-58c1f78b479b",
  "issuanceDate": "2024-01-01T08:39:26Z",
  "issuer": {
    "id": "did:omn:tas",
    "name": "raonsecure"
  },
  "language": "ko",
  "proof": {
    "created": "2024-01-01T08:39:26Z",
    "proofPurpose": "assertionMethod",
    "proofValue": "mIMyew1G13Na6B/A6dGCSp/58iIdFGVhOdKGdP1XRpLetc0wBWRKNvxA53WLQWzMW7pvV0gZxZCABgB6fiISRz3Q",
    "proofValueList": [
      "mIM8vvPRwfmx9ZBM9EwSvZu7qD9llePjDBiC3CZ5jihMScasxMAIpDudq4ykxPMWoPdNcIE9sKGnvDRKuI/RnXdo",
      "mHxmZSJn6X+jIHnfz1htTE5zZSoHJeed7ZmfNNcZKqAeTIWj6vmHuSvDXNjlS/bNC47n32pD5SU6eb8sq8+FZCDs"
    ],
    "type": "Secp256r1Signature2018",
    "verificationMethod": "did:omn:tas?versionId=1#assert"
  },
  "type": [
    "VerifiableCredential",
    "CertificateVC"
  ],
  "validFrom": "2024-01-01T08:39:26Z",
  "validUntil": "2025-01-01T08:39:26Z"
}
```

<div style="page-break-after: always; margin-top: 40px;"></div>

### 4.3. Request Wallet Tokendata

월렛토큰을 만들기 위해 필요한 데이터를 요청한다.

월렛토큰 데이터에는 인가 앱이 보유한 사용자의 개인 식별 정보(PII)가 해시된 형태로 포함되며, 인가앱 서버에서 서명한 값도 포함된다. 인가앱은 로그인 등의 과정을 통해 서버와 세션을 맺어 userId를 획득한 뒤에만 API 호출이 가능하다.

| Item          | Description                    | Remarks |
| ------------- | ------------------------------ | ------- |
| Method        | `POST`                          |         |
| Path          | `/api/v1/request-wallet-tokendata` |         |
| Authorization | -                              |         |

#### 4.3.1. Request

**■ Path Parameters**

N/A

**■ Query Parameters**

N/A

**■ HTTP Body**

```c#
def object RequestWalletTokenData: "Request Wallet TokenData 요청문"
{   
    @spread(WalletTokenSeed) // 데이터 명세서 참고
    + string  "userId": "인가앱이 보유하고 있는 사용자의 식별자"
}
```

<div style="page-break-after: always; margin-top: 30px;"></div>

#### 4.3.2. Response

**■ Process**
1. useId로 사용자의 PII 조회
1. nonce 생성
1. walletTokenData 생성
1. wallet 사업자의 assert 키로 WalletTokenData 서명

**■ Status 200 - Success**

```c#
def object _RequestWalletTokenData: "Request Wallet TokenData 응답문"
{
    @spread(WalletTokenData) 
}
```

**■ Status 400 - Client error**

| Code         | Description                      |
| ------------ | -------------------------------- |
| SCRVCFA00400 | "사용자 PII를 찾을 수 없습니다." |
| SCRVCFA00600 | "유효하지 않은 증명 목적입니다." |

**■ Status 500 - Server error**

| Code         | Description                                                |
| ------------ | ---------------------------------------------------------- |
| SCRVCFA00101 | "데이터 인코딩에 실패했습니다."                            |
| SCRVCFA00200 | "PII 암호화에 실패했습니다."                               |
| SCRVCFA00201 | "서명 생성에 실패했습니다."                                |
| SCRVCFA00300 | "DID 문서 검색에 실패했습니다."                            |
| SCRVCFA00800 | "'request-wallet-tokendata' API 요청 처리에 실패했습니다." |

<div style="page-break-after: always; margin-top: 30px;"></div>

#### 4.3.3. Example

**■ Request**

```shell
curl -v -X POST "http://${Host}:${Port}/cas/api/v1/request-wallet-tokendata"
-H "Content-Type: application/json;charset=utf-8" \
-d @"data.json"
```

```json
{
  "purpose":8,
  "pkgName":"com.omnione.did",
  "nonce":"zNgLzfu8edopNmKhmuGMv32",
  "validUntil":"2024-01-01T09:00:00Z",
  "userId":"testUser"
}
```

**■ Response**

```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=utf-8

{
    "seed": {
        "purpose": 8,
        "pkgName": "com.omnione.did",
        "nonce": "zNgLzfu8edopNmKhmuGMv32",
        "validUntil": "2024-01-01T09:00:00Z",
        "userId": "testUser"
    },
    "sha256_pii": "2845bac0835ba292946e2476545dfec6cd82027ee91b1cfb5ae5b1edce9b9b74",
    "provider": {
        "did": "did:raon:entity",
        "certVcRef": "http://127.0.0.1:8090/tas/download/vc?name=tas"
    },
    "nonce": "zNgLzfu8edopNmKhmuGMv32",
    "proof": {
        "type": "Secp256k1Signature2018",
        "created": "2024-01-01T09:00:00Z",
        "verificationMethod": "did:raon:tas?version=1#attested",
        "proofPurpose": "assertionMethod",
        "proofValue": "1234567890"
    }
}
```

<div style="page-break-after: always; margin-top: 40px;"></div>

### 4.4. Request Attested App Info

서버 토큰을 생성하는 데 필요한 서명된 앱 정보를 요청한다.

반환되는 서명된 앱 정보에는 appId와 인가앱 서버가 서명한 값이 포함된다.
이 API는 인가앱이 인가앱 서버와 세션을 맺은 후에만 호출할 수 있으며, 세션 설정 과정은 이 문서의 범위에 포함되지 않는다.

| Item          | Description                    | Remarks |
| ------------- | ------------------------------ | ------- |
| Method        | `POST`                          |         |
| Path          | `/api/v1/request-attested-appinfo` |         |
| Authorization | -                              |         |


#### 4.4.1. Request

**■ Path Parameters**

N/A

**■ Query Parameters**

N/A

**■ HTTP Body**

```c#
def object RequestAttestdAppInfo: "Request Attested AppInfo 요청문"
{   
    + appId "appId" : "Client application id"
}
```

<div style="page-break-after: always; margin-top: 30px;"></div>

#### 4.4.2. Response

**■ Process**
1. nonce 생성
1. AttestedAppInfo 생성
1. AttestedAppInfo 서명

**■ Status 200 - Success**

```c#
def object _RequestAttestedAppInfo: "Request Attested AppInfo 응답문"
{
    @spread(AttestedAppInfo) // 데이터 명세서 참고
}
```

**■ Status 400 - Client error**

| Code         | Description                      |
| ------------ | -------------------------------- |
| SCRVCFA00600 | "유효하지 않은 증명 목적입니다." |

**■ Status 500 - Server error**

| Code         | Description                                                |
| ------------ | ---------------------------------------------------------- |
| SCRVCFA00101 | "데이터 인코딩에 실패했습니다."                            |
| SCRVCFA00201 | "서명 생성에 실패했습니다."                                |
| SCRVCFA00300 | "DID 문서 검색에 실패했습니다."                            |
| SCRVCFA00801 | "'request-attested-appinfo' API 요청 처리에 실패했습니다." |

<div style="page-break-after: always; margin-top: 30px;"></div>

#### 4.4.3. Example

**■ Request**

```shell
curl -v -X POST "http://${Host}:${Port}/cas/api/v1/request-attested-appinfo"
-H "Content-Type: application/json;charset=utf-8" \
-d @"data.json"
```

```json
{
  "appId":"com.opendid.did"
}
```

**■ Response**

```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=utf-8

{
    "appId": "com.opendid.did",
    "provider": {
        "did": "did:raon:entity",
        "certVcRef": "http://192.168.3.130:8090/tas/download/vc?name=tas"
    },
    "nonce": "zNgLzfu8edopNmKhmuGMv32",
    "proof": {
        "type": "Secp256k1Signature2018",
        "created": "2024-01-01T09:00:00Z",
        "verificationMethod": "did:raon:tas?version=1#attested",
        "proofPurpose": "assertionMethod",
        "proofValue": "1234567890"
    }
}
```

<div style="page-break-after: always; margin-top: 40px;"></div>

### 4.5. Save User Info

사용자 정보를 저장한다.

인가앱 서버는 사용자의 정보를 보유하고 있어야 하는데, 이 API는 테스트 목적으로 사용자의 정보를 저장하기 위해 필요하다.
요청 데이터에 PII가 포함되어 있지만, 테스트 목적의 API이므로 암복호화 처리를 하지 않는다.

| Item          | Description              | Remarks |
| ------------- | ------------------------ | ------- |
| Method        | `POST`                   |         |
| Path          | `/api/v1/save-user-info` |         |
| Authorization | -                        |         |

#### 4.5.1. Request

**■ Path Parameters**

N/A

**■ Query Parameters**

N/A

**■ HTTP Body**

```c#
def object SaveUserInfo: "Save User Info 요청문"
{   
    + string "userId" : "인가앱이 보유하고 있는 사용자의 식별자"
    + string "pii" : "사용자 PII"
}
```

<div style="page-break-after: always; margin-top: 30px;"></div>

#### 4.5.2. Response

**■ Process**
1. userId와 pii 매핑 저장

**■ Status 200 - Success**
N/A

**■ Status 400 - Client error**
N/A

**■ Status 500 - Server error**

| Code         | Description                                       |
| ------------ | ------------------------------------------------- |
| SCRVCFA00802 | "'save-user-info'  API 요청 처리에 실패했습니다." |

<div style="page-break-after: always; margin-top: 30px;"></div>

#### 4.5.3. Example

**■ Request**

```shell
curl -v -X POST "http://${Host}:${Port}/cas/api/v1/save-user-info" \
-H "Content-Type: application/json;charset=utf-8" \
-d @"data.json"
```

```json
{
    "userId":"testUser123",
    "pii":"2845bac0835ba292946e2476545dfec6cd82027ee91b1cfb5ae5b1edce9b9b74"
}
```

**■ Response**

```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=utf-8

{
    //no data
}
```

<div style="page-break-after: always; margin-top: 40px;"></div>

### 4.6. Retrieve PII

사용자의 PII를 요청한다.

Trust Agent는 사용자를 식별하기 위해서 PII가 필요하며, 이를 위해 CAS가 보유한 사용자의 PII를 임의로 요청한다.
즉, 테스트를 위해 CAS가 KYC 서버의 역할을 대신 수행한다.

| Item          | Description                | Remarks |
| ------------- | -------------------------- | ------- |
| Method        | `POST`                     |         |
| Path          | `/cas/api/v1/retrieve-pii` |         |
| Authorization | -                          |         |

#### 4.6.1. Request

**■ Path Parameters**

N/A

**■ Query Parameters**

N/A

**■ HTTP Body**

```c#
def object RetrievePii: "Retrieve User PII 요청문"
{   
    + string  "userId": "인가앱이 보유하고 있는 사용자의 식별자"
}
```

<div style="page-break-after: always; margin-top: 0px;"></div>

#### 4.6.2. Response

**■ Process**
1. useId로 사용자의 PII 조회

**■ Status 200 - Success**

```c#
def object _RetrievePii: "Retrieve PII 응답문"
{
    + string  "pii": "사용자 PII"
}
```
**■ Status 400 - Client error**

| Code         | Description                     |
| ------------ | ------------------------------- |
| SCRVCFA00400 | "사용자 PII를 찾을수 없습니다." |

**■ Status 500 - Server error**

| Code         | Description                                    |
| ------------ | ---------------------------------------------- |
| SCRVCFA00803 | "'retrieve-pii' API 요청 처리에 실패했습니다." |

<div style="page-break-after: always; margin-top: 30px;"></div>
