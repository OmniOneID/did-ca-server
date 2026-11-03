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

- 작성일: 2026-11-03
- 버전: v3.0.0

| 버전 | 일자       | 변경사항                 |
|---------|------------|-------------------------|
| v1.0.0  | 2024-09-03 | 최초 작성         |
| v3.0.0  | 2026-11-03 | 사용자·지갑·관리자·CAS·외부 서버 연동·토큰·서버 설정 에러 추가 |

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

### 설명
```
CAS 백엔드를 위한 에러코드입니다. 코드와 메시지 쌍을 가지고 있습니다.
코드는 SCRVCFA로 시작합니다.
```

### 선언
```java
public class ErrorResponse {
    private final String code;
    private final String description;
}
```

### 속성

| 이름        | 타입   | 설명                        | **필수/선택** | **참고** |
|-------------|--------|-----------------------------|---------------|----------|
| code        | String | 오류 코드. SCRVCFA로 시작합니다 | 필수       |          | 
| message     | String | 오류 설명                  | 필수       |          | 

# Error Code

## 1-1. General (001xx)

| 오류 코드   | Error Message                                  | 설명 | 필요 조치                                |
|--------------|------------------------------------------------|------|------------------------------------------|
| SCRVCFA00100 | Unable to process the request.                 | -    | 요청 형식과 내용을 확인하세요.             |
| SCRVCFA00101 | Failed to encode data.                         | -    | 인코딩 프로세스와 입력 데이터를 확인하세요.        |
| SCRVCFA00102 | Failed to decode data: incorrect encoding.     | -    | 인코딩된 데이터와 디코딩 방법을 확인하세요.        |

<br>

## 1-2. Cryptography and Security (002xx)

| 오류 코드   | Error Message                                  | 설명 | 필요 조치                                |
|--------------|------------------------------------------------|------|------------------------------------------|
| SCRVCFA00200 | Failed to encrypt PII.                         | -    | 암호화 알고리즘과 입력 데이터를 확인하세요.     |
| SCRVCFA00201 | Failed to generate signature.                  | -    | 서명 생성 프로세스와 키를 확인하세요.  |
| SCRVCFA00202 | Failed to generate key pair.                   | -    | 키 생성 알고리즘과 매개변수를 확인하세요. |
| SCRVCFA00203 | Failed to generate nonce.                      | -    | 논스 생성 프로세스를 확인하세요.               |
| SCRVCFA00204 | Failed to merge nonce.                         | -    | 논스 병합 알고리즘을 확인하세요.                 |
| SCRVCFA00205 | Failed to generate session key.                | -    | 세션 키 생성 프로세스를 확인하세요.         |
| SCRVCFA00206 | Failed to merge nonce and shared secret.       | -    | 논스와 공유 비밀의 병합 알고리즘을 확인하세요.  |
| SCRVCFA00207 | Failed to decrypt data.                        | -    | 복호화 프로세스와 키를 확인하세요.            |
| SCRVCFA00208 | Failed to generate hash value.                 | -    | 해시 알고리즘과 입력 데이터를 확인하세요.        |

<br>

## 1-3. DID Related (003xx)

| 오류 코드   | Error Message                                  | 설명 | 필요 조치                                |
|--------------|------------------------------------------------|------|------------------------------------------|
| SCRVCFA00300 | Failed to retrieve DID Document.               | -    | DID와 문서 검색 프로세스를 확인하세요.     |

<br>

## 1-4. User and Data (004xx)

| 오류 코드   | Error Message                                  | 설명 | 필요 조치                                |
|--------------|------------------------------------------------|------|------------------------------------------|
| SCRVCFA00400 | User PII not found.                            | -    | 사용자 데이터와 검색 프로세스를 확인하세요.         |
| SCRVCFA00401 | Tas Certificate VC data not found.             | -    | 인증서 데이터와 저장소를 확인하세요.           |
| SCRVCFA00402 | User not found. | - | 사용자가 등록되어 있는지 확인하세요. |
| SCRVCFA00403 | User already exists. | - | 기존 사용자를 쓰거나 다른 사용자 아이디를 쓰세요. |
| SCRVCFA00404 | WalletId is already registered to another user. | - | 지갑 아이디를 확인하세요. 다른 사용자에게 이미 연결되어 있습니다. |
| SCRVCFA00405 | Password does not match. | - | 비밀번호를 확인하세요. |
| SCRVCFA00406 | Invalid user registration status for this operation. | - | API를 호출하기 전에 사용자 등록 상태를 확인하세요. |
| SCRVCFA00407 | Failed to process signup. | - | 가입 요청과 서버 로그를 확인하세요. |
| SCRVCFA00408 | Failed to process signin. | - | 로그인 요청과 서버 로그를 확인하세요. |
| SCRVCFA00409 | Failed to process signout. | - | 로그아웃 요청과 서버 로그를 확인하세요. |
| SCRVCFA00410 | Failed to process withdraw. | - | 탈퇴 요청과 서버 로그를 확인하세요. |

<br>

## 1-5. Wallet (005xx)

| 오류 코드   | Error Message                                  | 설명 | 필요 조치                                |
|--------------|------------------------------------------------|------|------------------------------------------|
| SCRVCFA00500 | Failed to connect to wallet.                   | -    | 지갑 연결 설정과 네트워크를 확인하세요.  |
| SCRVCFA00501 | Failed to generate wallet signature.           | -    | 지갑 서명 생성 프로세스를 확인하세요.    |
| SCRVCFA00502 | Failed to get File Wallet Manager              | -    | 파일 지갑 매니저 초기화를 확인하세요.      |
| SCRVCFA00503 | Failed to create wallet: wallet already exists. | - | 기존 지갑 파일을 쓰거나, 지운 뒤 새로 만드세요. |
| SCRVCFA00504 | Failed to generate keys: key already exists. | - | 기존 키를 쓰거나 다른 키 아이디를 쓰세요. |
| SCRVCFA00505 | Failed to load key element. | - | 지갑 파일과 키 아이디를 확인하세요. |

<br>

## 1-6. Verification and Proof (006xx)

| 오류 코드   | Error Message                                  | 설명 | 필요 조치                                |
|--------------|------------------------------------------------|------|------------------------------------------|
| SCRVCFA00600 | Invalid proof purpose.                         | -    | 증명 목적과 형식을 확인하세요.               |

<br>

## 1-7. Blockchain (007xx)

| 오류 코드   | Error Message                                  | 설명 | 필요 조치                                |
|--------------|------------------------------------------------|------|------------------------------------------|
| SCRVCFA00701 | Failed to retrieve DID document on the blockchain. | -       | 블록체인 연결과 DID 유효성을 확인하세요.  |

<br>

## 1-8. API Process Errors (008xx)

| 오류 코드   | Error Message                                           | 설명 | 필요 조치                                |
|--------------|----------------------------------------------------------|------|------------------------------------------|
| SCRVCFA00800 | Failed to process the 'request-wallet-tokendata' API request. | -        | API 요청 형식과 매개변수를 확인하세요.       |
| SCRVCFA00801 | Failed to process the 'request-attested-appinfo' API request. | -        | API 요청과 앱 정보 데이터를 확인하세요.             |
| SCRVCFA00802 | Failed to process the 'save-user-info' API request.        | -           | 사용자 정보 데이터와 저장 프로세스를 확인하세요.      |
| SCRVCFA00803 | Failed to process the 'retrieve-pii' API request.          | -           | PII 검색 프로세스와 권한을 확인하세요.    |
| SCRVCFA00804 | Failed to process the 'issue_certificate-vc' API request.  | -           | 인증서 발급 프로세스와 데이터를 확인하세요.   |
| SCRVCFA00805 | Failed to process the 'request-certificate-vc' API request. | -          | 인증서 요청 형식과 유효성을 확인하세요.  |

<br>

## 1-9. Admin (009xx)

| 오류 코드 | Error Message | 설명 | 필요 조치 |
|------------|---------------|-------------|-----------------|
| SCRVCFA00900 | Failed to find admin: admin is not registered. | - | 관리자 계정을 등록하세요. |
| SCRVCFA00901 | Failed to register admin: admin is already registered. | - | 기존 관리자 계정을 쓰세요. |

<br>

## 1-10. CAS (010xx)

| 오류 코드 | Error Message | 설명 | 필요 조치 |
|------------|---------------|-------------|-----------------|
| SCRVCFA01000 | CAS is not registered. | - | 관리자 콘솔에서 CAS 정보를 등록하세요. |
| SCRVCFA01001 | CAS is already registered. | - | 등록된 CAS 정보를 쓰세요. |
| SCRVCFA01002 | Invalid DID Document. | - | DID Document의 형식과 내용을 확인하세요. |
| SCRVCFA01003 | Failed to register CAS DID Document: document is already registered. | - | 이미 등록된 DID Document입니다. 상태를 확인하세요. |
| SCRVCFA01004 | Failed to generate DID document. | - | DID Document 생성에 쓰는 지갑 키를 확인하세요. |
| SCRVCFA01005 | Failed to register CAS DID Document. | - | TA 연결과 DID Document를 확인하세요. |
| SCRVCFA01006 | Failed to find CAS DID Document: no registration request has been made. | - | 먼저 DID Document 등록을 요청하세요. |
| SCRVCFA01007 | Failed to register CAS DID Document: document is already requested. | - | TA가 등록 요청을 승인할 때까지 기다리세요. |
| SCRVCFA01008 | Failed to process certificate VC: invalid JSON format. | - | TA가 보낸 가입증명서 VC를 확인하세요. |

<br>

## 1-11. External Server Integration (011xx)

| 오류 코드 | Error Message | 설명 | 필요 조치 |
|------------|---------------|-------------|-----------------|
| SCRVCFA01100 | Failed to ping the URL. | - | URL에 접근할 수 있는지 확인하세요. |
| SCRVCFA01101 | Failed to process response: received unknown data from the tas. | - | TA의 응답 형식과 버전을 확인하세요. |
| SCRVCFA01102 | Failed to communicate with tas: unknown error occurred. | - | TA 연결과 설정을 확인하세요. |

<br>

## 1-12. Token (012xx)

| 오류 코드 | Error Message | 설명 | 필요 조치 |
|------------|---------------|-------------|-----------------|
| SCRVCFA01200 | Connection with token server failed. | - | 토큰 서버 연결을 확인하세요. |
| SCRVCFA01201 | An error occurred while issuing the token. | - | 토큰 요청과 서버 로그를 확인하세요. |
| SCRVCFA01202 | An error occurred while revoking the token. | - | 토큰과 서버 로그를 확인하세요. |
| SCRVCFA01203 | An error occurred while validating the token. | - | 토큰이 유효하고 만료되지 않았는지 확인하세요. |
| SCRVCFA01204 | An error occurred while refreshing the token. | - | refresh token을 확인하세요. |
| SCRVCFA01205 | Token format is invalid and does not conform to the expected specification. | - | 토큰 형식을 확인하세요. |

<br>

## 1-13. Server Configuration (018xx)

| 오류 코드 | Error Message | 설명 | 필요 조치 |
|------------|---------------|-------------|-----------------|
| SCRVCFA01801 | Server configuration key not found. | - | 관리자 콘솔의 Server Configuration에 해당 키(예: API_KEY_LSS)를 등록하세요. |
