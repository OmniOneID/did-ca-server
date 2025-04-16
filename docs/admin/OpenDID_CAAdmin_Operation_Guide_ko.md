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

Open DID CA Admin Operation Guide
==

- Date: 2025-03-31
- Version: v1.0.0

목차
==

- [1. 소개](#1-소개)
  - [1.1. 개요](#11-개요)
  - [1.2. Admin Console 정의](#12-admin-console-정의)
- [2. 기본 메뉴얼](#2-기본-메뉴얼)
  - [2.1. 로그인](#21-로그인)
  - [2.2. 메인 화면 구성](#22-메인-화면-구성)
  - [2.3. 메뉴 구성](#23-메뉴-구성)
  - [2.4. 비밀번호 변경 관리](#24-비밀번호-변경-관리)
- [3. 기능별 상세 메뉴얼](#3-기능별-상세-메뉴얼)
  - [3.1. CA Management](#31-ca-management)
    - [3.1.1 DID Document 조회](#311-did-document-조회)
  - [3.2. User Management](#32-user-management)
    - [3.2.1 사용자 목록 조회](#321-사용자-목록-조회)
    - [3.2.2 사용자 상세 정보 조회](#322-사용자-상세-정보-조회)
  - [3.3. Admin Management](#33-admin-management)
    - [3.3.1 Admin 목록 조회](#331-admin-목록-조회)
    - [3.3.2 Admin 등록](#332-admin-등록)

# 1. 소개

## 1.1. 개요

본 문서는 Open DID CA Admin Console의 사용 방법에 관한 가이드를 제공합니다. 관리자가 CA(Certificate Application) 시스템을 효율적으로 관리하고 운영할 수 있도록 기능 및 사용 방법을 단계별로 설명합니다.

OpenDID의 전체 설치에 대한 가이드는 [Open DID Installation Guide]를 참고해 주세요.

## 1.2. Admin Console 정의

CA Admin Console은 Open DID 시스템에서 인증서 발급, 사용자 관리, 관리자 권한 제어 등 CA 서버의 핵심 기능을 관리하기 위한 웹 기반 관리 도구입니다. 관리자는 이 콘솔을 통해 시스템의 전반적인 상태를 모니터링하고, 필요한 설정 및 작업을 수행할 수 있습니다.

CA Admin Console은 다음과 같은 주요 기능을 제공합니다:

- CA 기본 정보 관리
- 사용자 정보 조회
- 관리자 계정 관리

<br/>

# 2. 기본 메뉴얼

## 2.1. 로그인

Admin Console에 접속하려면 다음 단계를 따르세요:

1. 웹 브라우저를 열고 CA Admin Console URL에 접속합니다.

   ```
   http://<ca_domain>:<port>
   ```

2. 로그인 화면에서 관리자 계정의 이메일과 비밀번호를 입력합니다.
   - 기본 관리자 계정: <admin@opendid.omnione.net>
   - 초기 비밀번호: password (최초 로그인 시 변경 필요)

3. '로그인' 버튼을 클릭합니다.

> **참고**: 보안상의 이유로 최초 로그인 시에는 비밀번호 변경이 필요합니다.

## 2.2. 메인 화면 구성

로그인 후 표시되는 메인 화면은 다음과 같은 요소로 구성됩니다:

<img src="./images/main_screen.jpg" width="800"/>

| 번호 | 영역 | 설명 |
|------|------|------|
| 1 | 설정 버튼 | 'SETTING' 버튼을 클릭하면 현재 로그인한 계정의 비밀번호를 변경할 수 있는 화면으로 이동합니다. |
| 2 | 콘텐츠 헤더 | 현재 선택된 메뉴의 제목이 표시됩니다. 메뉴별로 해당 페이지 이름이 노출됩니다. |
| 3 | 사이드바 메뉴 | CA Management를 비롯한 주요 기능에 접근할 수 있는 메뉴가 나열되어 있습니다. |
| 4 | 사용자 정보 영역 | 현재 로그인한 관리자의 이메일(<admin@opendid.omnione.net>)과 로그아웃 버튼이 표시됩니다. |

<br/>

## 2.3. 메뉴 구성

Admin Console의 메뉴는 다음과 같이 구성되어 있습니다:

<img src="./images/menu_structure.jpg" width="800"/>

| 번호 | 기능 명칭 | 기능 설명 |
|------|-----------|-----------|
| 1 | **CA Management** | 서비스의 CA(Certificate Application) 관리를 할 수 있는 메뉴입니다. CA 관련 설정 및 관리 작업을 이곳에서 진행할 수 있습니다. |
| 2 | **User Management** | 시스템에 등록된 사용자 정보를 조회할 수 있는 메뉴입니다. 사용자의 개인 식별 정보(PII)를 확인할 수 있습니다. |
| 3 | **Admin Management** | 관리자 관련 설정을 관리하는 메뉴입니다. 관리자의 계정 및 권한을 조정할 수 있습니다. |

각 메뉴에 대한 상세 기능은 [3. 기능별 상세 메뉴얼](#3-기능별-상세-메뉴얼) 섹션에서 자세히 설명합니다.

## 2.4. 비밀번호 변경 관리

사용자 비밀번호 변경은 다음 단계를 통해 수행할 수 있습니다:

1. 헤더 영역의 'SETTING' 버튼을 클릭합니다.
2. 설정 메뉴에서 '비밀번호 변경'을 선택합니다.
3. 비밀번호 변경 화면에서:
   - 현재 비밀번호 입력
   - 새 비밀번호 입력
   - 새 비밀번호 확인 입력
4. '저장' 버튼을 클릭하여 변경 사항을 적용합니다.

> **참고**: 비밀번호는 8자 이상, 64자 이하입니다.

# 3. 기능별 상세 메뉴얼

이 장에서는 Open DID CA Admin Console의 주요 기능에 대한 상세 사용 방법을 안내합니다.

## 3.1. CA Management

CA Management 메뉴에서는 가입증명서로 등록된 CA(Certificate Application) 서버의 정보를 조회할 수 있습니다. 이 화면에서는 CA의 기본 정보가 표시되며, 수정이나 삭제는 불가능합니다.

<img src="./images/ca_management.png" width="800"/>

| 번호 | 항목 | 설명 |
|------|------|------|
| 1 | DID | CA의 고유 식별자입니다. 형식은 'did:omn:ca'와 같은 형태로 표시됩니다. |
| 2 | Name | CA의 이름입니다. VC 발급 시 표시되는 이름입니다. |
| 3 | Status | CA의 활성화 상태를 나타냅니다. ACTIVATE 또는 DEACTIVATE 상태가 표시됩니다. |
| 4 | URL | CA 서비스의 기본 URL 주소입니다. |
| 5 | Certificate URL | CA의 가입증명서를 확인할 수 있는 URL 주소입니다. |
| 6 | Registered At | CA가 등록된 날짜와 시간을 표시합니다. |
| 7 | VIEW DID DOCUMENT | DID 문서를 확인할 수 있는 버튼입니다. 클릭 시 팝업 형태로 블록체인에 등록된 DID 문서 정보가 표시됩니다. |
| 8 | DID Document 내용 | VIEW DID DOCUMENT 버튼을 클릭했을 때 표시되는 DID Document의 내용입니다. JSON 형식으로 CA의 DID 정보, controller, 생성일시, 검증 방법 등이 포함됩니다. |

### 3.1.1. DID Document 조회

DID Document를 조회하려면 다음 단계를 따르세요:

1. CA Management 화면에서 'VIEW DID DOCUMENT' 버튼을 클릭합니다.
2. 팝업 창이 열리며 블록체인에 등록된 DID Document의 전체 내용이 JSON 형식으로 표시됩니다.
3. 팝업 창을 닫으려면 창 외부를 클릭합니다.

<br/>

## 3.2. User Management

User Management 메뉴는 시스템에 등록된 사용자 정보를 조회하는 기능을 제공합니다. 이 기능을 통해 관리자는
사용자의 개인 식별 정보(PII)를 확인할 수 있습니다.

### 3.2.1. 사용자 목록 조회

<img src="./images/user_management.jpg" width="800"/>

User Management 화면은 다음과 같은 주요 정보를 표시합니다:

| 번호 | 항목 | 설명 |
|------|------|------|
| 1 | ID | 사용자의 고유 식별자입니다. 클릭하면 상세 정보를 볼 수 있습니다. |
| 2 | PII | 사용자의 개인 식별 정보입니다. |
| 3 | Registered At | 사용자가 등록된 날짜와 시간을 표시합니다. |
| 4 | Updated At | 사용자 정보가 마지막으로 업데이트된 날짜와 시간을 표시합니다. |
| 5 | 페이지네이션 | 사용자 목록의 페이지를 관리합니다. 한 페이지당 표시할 행 수와 현재 페이지 정보를 확인할 수 있습니다. |

> **참고**: 사용자 목록 화면에서는 읽기만 가능하며, 사용자 정보의 추가, 수정, 삭제 기능은 제공되지 않습니다.

### 3.2.2. 사용자 상세 정보 조회

사용자 목록에서 사용자 ID를 클릭하면 해당 사용자의 상세 정보를 조회할 수 있습니다.

<img src="./images/user_detail.png" width="800"/>

사용자 상세 정보 화면에는 다음과 같은 정보가 표시됩니다:

| 섹션 | 포함 정보 |
|------|-----------|
| **기본 정보** | - ID<br>- PII<br>- Registered At<br>- Updated At |

> **중요**: 개인 식별 정보(PII)는 현재 시스템상 Demo Server를 통해 입력받은 임의의 값입니다. CA서버는 사용자 정보를 갖고 있는 설정을 위해 해당 조회 기능을 제공합니다.

을 적용합니다.
## 3.3. Admin Management

`Admin Management` 메뉴는 CA Admin Console에 접근할 수 있는 관리자 계정을 관리하는 기능입니다.  

CA 서버를 설치하면 기본적으로 `admin@opendid.omnione.net` 계정이 ROOT 권한으로 자동 생성됩니다.  
이 계정은 시스템 내 유일한 ROOT 계정이며, 삭제할 수 없습니다.

관리자 계정은 **ROOT**와 **Normal Admin** 두 가지 권한 유형으로 구분됩니다.  
ROOT 계정은 `Admin Management` 메뉴에서 모든 기능을 수행할 수 있으며, Normal Admin은 일반적인 조회 기능만 가능합니다.

---
> **참고:** 현재는 ROOT 계정과 Normal Admin 계정 간의 권한 차이는  
> `Admin Management` 메뉴에서 표시되는 버튼의 차이(Root만 REGISTER / DELETE / CHANGE PASSWORD 가능) 외에는 없습니다.  
> 그 외 시스템의 다른 메뉴에 대한 접근 권한이나 기능 제한은 아직 적용되어 있지 않습니다.
---


<br/>

## 3.3.1 Admin 목록 조회


`Admin Management` 메뉴에 진입하면 등록된 관리자 계정들의 목록이 테이블 형태로 표시됩니다.

<img src="./images/admin-management.png" width="800"/>


| 번호 | 항목                    | 설명                                                             |
| ---- | ----------------------- | ---------------------------------------------------------------- |
| 1    | **REGISTER 버튼**       | 새로운 관리자 계정을 등록할 수 있는 등록 페이지로 이동합니다.       |
| 2    | **DELETE 버튼**         | 선택한 관리자 계정을 삭제합니다. (ROOT 관리자만 가능)              |
| 3    | **CHANGE PASSWORD 버튼** | 선택한 관리자 계정의 비밀번호를 변경할 수 있습니다.                |
| 4    | **ID**                  | 등록된 관리자 계정의 이메일 ID입니다.                              |
| 5    | **Role**                | 해당 관리자 계정의 역할(Role)입니다. (예: ROOT, Normal Admin 등)   |
| 6    | **Registered At**       | 해당 계정이 최초 등록된 일시입니다.                               |
| 7    | **Updated At**          | 마지막으로 수정된 일시입니다.     

<br/>

## 3.3.2. Admin 등록

`Admin Management` 화면에서 **REGISTER** 버튼을 클릭하면, 아래와 같은 등록 화면으로 이동합니다.

<img src="./images/admin-registration.png" width="600"/>

| 번호 | 항목                        | 설명                                                                |
| ---- | --------------------------- | ------------------------------------------------------------------- |
| 1    | **ID**                      | 등록할 관리자 계정의 ID입니다. 이메일 형식을 사용해야 합니다.         |
| 2    | **Check Availability 버튼** | 입력한 ID가 중복되지 않는지 확인합니다.                             |
| 3    | **Role**                    | 등록할 관리자 계정의 권한을 선택합니다. (예: Normal Admin)           |
| 4    | **Password**                | 로그인 시 사용할 비밀번호를 입력합니다.                              |
| 5    | **Re-enter Password**       | 비밀번호를 한 번 더 입력하여 일치 여부를 확인합니다.                |
| 6    | **REGISTER 버튼**           | 입력한 정보를 바탕으로 관리자 계정을 등록합니다.                    |
| 7    | **RESET 버튼**              | 모든 입력값을 초기화합니다.                                         |
| 8    | **CANCEL 버튼**             | 등록을 취소하고 이전 화면으로 돌아갑니다.                           |



[Open DID Installation Guide]: https://github.com/OmniOneID/did-release/blob/main/release-V1.0.0.0/OepnDID_Installation_Guide-V1.0.0.0.md


