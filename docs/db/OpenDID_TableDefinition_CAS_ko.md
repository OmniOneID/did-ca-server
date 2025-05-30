물론이죠! 요청하신 대로 **테이블 내부 값(컬럼명, 타입, 설명 등)은 영문으로 그대로 두고**, 나머지 설명 문장과 제목 부분만 한글로 번역했습니다:

---

# Open DID CAS 데이터베이스 테이블 정의

* 날짜: 2025-03-31
* 버전: v1.0.1 (dev)

## 목차

* [Open DID CAS 데이터베이스 테이블 정의](#open-did-cas-데이터베이스-테이블-정의)

  * [목차](#목차)
  * [1. 개요](#1-개요)
    * [1.1 ERD](#11-erd)
  * [2. 테이블 정의](#2-테이블-정의)
    * [2.1. User PII](#21-user-pii)
    * [2.2. Certificate VC](#22-certificate-vc)
    * [2.3. CAS](#23-cas)
    * [2.4. Admin](#24-admin)

## 1. 개요

이 문서는 CA 서버에서 사용하는 데이터베이스 테이블의 구조를 정의합니다. 각 테이블의 필드 속성, 관계, 데이터 흐름을 설명하며, 시스템 개발 및 유지보수의 필수 참고 자료로 활용됩니다.

### 1.1 ERD

[ERD](https://www.erdcloud.com/d/5tR4y8YGnkSzeheBN) 사이트에 접속하여 다이어그램을 확인하세요. 이 다이어그램은 CA 서버 데이터베이스의 테이블 간 관계, 주요 속성, 기본 키 및 외래 키 관계를 시각적으로 보여줍니다.

## 2. 테이블 정의

### 2.1. User PII

이 테이블은 사용자 PII (개인 식별 정보) 데이터를 저장합니다.

| Key | Column Name | Data Type | Length | Nullable | Default | Description  |
| --- | ----------- | --------- | ------ | -------- | ------- | ------------ |
| PK  | id          | BIGINT    |        | NO       | N/A     | id           |
|     | user\_id    | VARCHAR   | 50     | NO       | N/A     | user id      |
|     | pii         | VARCHAR   | 64     | NO       | N/A     | pii          |
|     | created\_at | TIMESTAMP |        | NO       | now()   | created date |
|     | updated\_at | TIMESTAMP |        | YES      | N/A     | updated date |

### 2.2. Certificate VC

이 테이블은 Certificate VC (검증 가능한 자격 증명) 정보를 저장합니다.

| Key | Column Name | Data Type | Length | Nullable | Default | Description  |
| --- | ----------- | --------- | ------ | -------- | ------- | ------------ |
| PK  | id          | BIGINT    |        | NO       | N/A     | id           |
|     | vc          | TEXT      |        | NO       | N/A     | vc           |
|     | created\_at | TIMESTAMP |        | NO       | now()   | created date |
|     | updated\_at | TIMESTAMP |        | YES      | N/A     | updated date |

### 2.3. CAS

이 테이블은 CA 서버의 정보를 저장합니다.

| Key | Column Name      | Data Type | Length | Nullable | Default | Description             |
| --- | ---------------- | --------- | ------ | -------- | ------- | ----------------------- |
| PK  | id               | BIGINT    |        | NO       | N/A     | id                      |
|     | did              | VARCHAR   | 200    | NO       | N/A     | did                     |
|     | name             | VARCHAR   | 200    | NO       | N/A     | name                    |
|     | status           | VARCHAR   | 50     | NO       | N/A     | cas status              |
|     | server\_url      | VARCHAR   | 2000   | YES      | N/A     | URL of the CAS server   |
|     | certificate\_url | VARCHAR   | 2000   | YES      | N/A     | URL for CAS certificate |
|     | created\_at      | TIMESTAMP |        | NO       | now()   | created date            |
|     | updated\_at      | TIMESTAMP |        | YES      | N/A     | updated date            |

### 2.4. Admin

이 테이블은 관리자 계정 정보를 저장합니다.

| Key | Column Name              | Data Type | Length | Nullable | Default | Description                        |
| --- | ------------------------ | --------- | ------ | -------- | ------- | ---------------------------------- |
| PK  | id                       | BIGINT    |        | NO       | N/A     | id                                 |
|     | login\_id                | VARCHAR   | 50     | NO       | N/A     | login ID                           |
|     | login\_password          | VARCHAR   | 64     | NO       | N/A     | hashed login password              |
|     | name                     | VARCHAR   | 200    | YES      | N/A     | administrator name                 |
|     | email                    | VARCHAR   | 100    | YES      | N/A     | email address                      |
|     | email\_verified          | BOOLEAN   |        | YES      | false   | whether email is verified          |
|     | require\_password\_reset | BOOLEAN   |        | NO       | true    | whether password reset is required |
|     | role                     | VARCHAR   | 50     | NO       | N/A     | administrator role                 |
|     | created\_by              | VARCHAR   | 50     | NO       | N/A     | creator's login ID                 |
|     | created\_at              | TIMESTAMP |        | NO       | now()   | created date                       |
|     | updated\_at              | TIMESTAMP |        | YES      | N/A     | updated date                       |

---
