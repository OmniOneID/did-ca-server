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

# Open DID CA Server Installation Guide

- Date: 2025-05-29
- Version: v2.0.0

## 목차

- [Open DID CA Server Installation Guide](#open-did-ca-server-installation-guide)
  - [목차](#목차)
- [1. 소개](#1-소개)
  - [1.1. 개요](#11-개요)
  - [1.2. CA 서버 정의](#12-ca-서버-정의)
  - [1.3. 시스템 요구 사항](#13-시스템-요구-사항)
- [2. 사전 준비 사항](#2-사전-준비-사항)
  - [2.1. Git 설치](#21-git-설치)
  - [2.2. PostgreSQL 설치](#22-postgresql-설치)
  - [2.3. Node.js 설치](#23-nodejs-설치)
- [3. GitHub에서 소스 코드 복제하기](#3-github에서-소스-코드-복제하기)
  - [3.1. 소스코드 복제](#31-소스코드-복제)
  - [3.2. 디렉토리 구조](#32-디렉토리-구조)
- [4. 서버 구동 방법](#4-서버-구동-방법)
  - [4.1. IDE로 구동하기 (Gradle 및 React 프로젝트 실행)](#41-ide로-구동하기-gradle-및-react-프로젝트-실행)
    - [4.1.1. IntelliJ IDEA에서 백엔드(Spring Boot) 실행](#411-intellij-idea에서-백엔드spring-boot-실행)
      - [4.1.1.1. IntelliJ IDEA 설치](#4111-intellij-idea-설치)
      - [4.1.1.2. 프로젝트 열기](#4112-프로젝트-열기)
      - [4.1.1.3. Gradle 빌드](#4113-gradle-빌드)
      - [4.1.1.4. 서버 실행](#4114-서버-실행)
      - [4.1.1.5. 데이터베이스 설치](#4115-데이터베이스-설치)
      - [4.1.1.6. 서버 설정](#4116-서버-설정)
  - [4.2. 콘솔 명령어로 구동하기](#42-콘솔-명령어로-구동하기)
    - [4.2.1. Gradle 빌드 명령어](#421-gradle-빌드-명령어)
    - [4.2.2. 서버 구동 방법](#422-서버-구동-방법)
    - [4.2.3. 데이터베이스 설치](#423-데이터베이스-설치)
    - [4.2.4. 서버 설정 방법](#424-서버-설정-방법)
  - [4.3. Docker로 구동하기](#43-docker로-구동하기)
- [5. 설정 가이드](#5-설정-가이드)
  - [5.1. application.yml](#51-applicationyml)
    - [5.1.1. Spring 기본 설정](#511-spring-기본-설정)
    - [5.1.2. Jackson 기본 설정](#512-jackson-기본-설정)
    - [5.1.3. 서버 설정](#513-서버-설정)
    - [5.1.4. TA(Trust Agent) 설정](#514-tatrust-agent-설정)
  - [5.2. database.yml](#52-databaseyml)
    - [5.2.1. Spring Liquibase 설정](#521-spring-liquibase-설정)
    - [5.2.2. 데이터소스 설정](#522-데이터소스-설정)
    - [5.2.3. JPA 설정](#523-jpa-설정)
  - [5.3. application-logging.yml](#53-application-loggingyml)
    - [5.3.1. 로깅 설정](#531-로깅-설정)
  - [5.4. application-spring-docs.yml](#54-application-spring-docsyml)
  - [5.5. applicaiton-wallet.yml](#55-applicaiton-walletyml)
  - [5.6. applicaiton-cas.yml](#56-applicaiton-casyml)
  - [5.7. blockchain.properties](#57-blockchainproperties)
    - [5.7.1. 블록체인 연동 설정](#571-블록체인-연동-설정)
      - [EVM Network Configuration](#evm-network-configuration)
      - [EVM Contract Configuration](#evm-contract-configuration)
- [6. 프로파일 설정 및 사용](#6-프로파일-설정-및-사용)
  - [6.1. 프로파일 개요 (`sample`, `dev`)](#61-프로파일-개요-sample-dev)
    - [6.1.1. `sample` 프로파일](#611-sample-프로파일)
    - [6.1.2. `dev` 프로파일](#612-dev-프로파일)
  - [6.2. 프로파일 설정 방법](#62-프로파일-설정-방법)
    - [6.2.1. IDE를 사용한 서버 구동 시](#621-ide를-사용한-서버-구동-시)
    - [6.2.2. 콘솔 명령어를 사용한 서버 구동 시](#622-콘솔-명령어를-사용한-서버-구동-시)
    - [6.2.3. Docker를 사용한 서버 구동 시](#623-docker를-사용한-서버-구동-시)
- [7. Docker로 빌드 후 구동하기](#7-docker로-빌드-후-구동하기)
  - [7.1. Docker 이미지 빌드 방법 (`Dockerfile` 기반)](#71-docker-이미지-빌드-방법-dockerfile-기반)
    - [7.1.1. Docker 이미지 빌드](#711-docker-이미지-빌드)
  - [7.2. Docker Compose를 이용한 구동](#72-docker-compose를-이용한-구동)
    - [7.2.1. 디렉토리 및 설정 파일 준비](#721-디렉토리-및-설정-파일-준비)
      - [1. docker-compose 디렉토리 및 config 디렉토리 생성](#1-docker-compose-디렉토리-및-config-디렉토리-생성)
      - [2. 설정 파일(yml)들을 config 디렉토리로 복사](#2-설정-파일yml들을-config-디렉토리로-복사)
      - [3. blockchain.properties 파일 수정](#3-blockchainproperties-파일-수정)
      - [4. application-database.yml 파일 수정](#4-application-databaseyml-파일-수정)
    - [7.2.2. `docker-compose.yml` 파일 생성](#722-docker-composeyml-파일-생성)
    - [7.2.3. 컨테이너 실행](#723-컨테이너-실행)
- [8. Docker PostgreSQL 설치하기](#8-docker-postgresql-설치하기)
  - [8.1. Docker Compose를 이용한 PostgreSQL 설치](#81-docker-compose를-이용한-postgresql-설치)
  - [8.2. PostgreSQL 컨테이너 실행](#82-postgresql-컨테이너-실행)

# 1. 소개

## 1.1. 개요
본 문서는 Open DID CA(Certificate Application) 서버의 설치, 설정 및 구동 방법에 대한 가이드를 제공합니다. CA 서버는 Spring Boot 기반의 백엔드와 React 기반의 Admin console 프론트엔드로 구성되어 있으며, Gradle 빌드를 통해 통합 배포가 가능합니다. 설치 과정, 환경 설정, Docker 실행 방법, 프로파일 설정까지 단계별로 설명되어 있어, 사용자가 효율적으로 서버를 설치하고 실행할 수 있도록 안내합니다.

- OpenDID의 전체 설치에 대한 가이드는 [Open DID Installation Guide]를 참고해 주세요.
- Admin console에 대한 가이드는 [Open DID Admin Console Guide]를 참고해 주세요.

<br/>

## 1.2. CA 서버 정의

CA 서버는 인가 앱 서버로, 인가 앱을 Open DID 내에서 사용할 수 있도록 보증하는 역할을 합니다.

서버내 모든 문서에서는 Certified App Server(이하 '' 또는 'CA-Server'로 약칭)를 문맥과 사용방법에 따라 혼용하여 사용하고 있습니다. 이는 모두 동일한 시스템을 의미합니다.

<br/>

## 1.3. 시스템 요구 사항
- **Java 21** 이상
- **Gradle 7.0** 이상
- **Docker** 및 **Docker Compose** (Docker 사용 시)
- 최소 **2GB RAM** 및 **10GB 디스크 공간**

<br/>

# 2. 사전 준비 사항

이 장에서는 Open DID 프로젝트의 구성요소를 설치하기 전, 사전에 필요한 준비 항목들을 안내합니다.

## 2.1. Git 설치

`Git`은 분산 버전 관리 시스템으로, 소스 코드의 변경 사항을 추적하고 여러 개발자 간의 협업을 지원합니다. Git은 Open DID 프로젝트의 소스 코드를 관리하고 버전 관리를 위해 필수적입니다. 

설치가 성공하면 다음 명령어를 사용하여 Git의 버전을 확인할 수 있습니다.

```bash
git --version
```

> **참고 링크**
> - [Git 설치 가이드](https://docs.github.com/en/repositories/creating-and-managing-repositories/cloning-a-repository)

<br/>

## 2.2. PostgreSQL 설치
CAS 서버를 구동하려면 데이터베이스 설치가 필요하며, Open DID에서는 PostgreSQL을 사용합니다.

> **참고 링크**
- [PostgreSQL 설치 가이드 문서](https://www.postgresql.org/download/)
- [8. Docker postgreSQL 설치하기](#8-docker-postgresql-설치하기)

<br/>

## 2.3. Node.js 설치
React 기반의 ca Admin Console을 실행하려면 `Node.js`와 `npm`이 필요합니다.

npm(Node Package Manager)은 프론트엔드 개발에 필요한 의존성들을 설치하고 관리하는 데 사용됩니다.

설치가 완료되면 다음 명령어로 정상적으로 설치되었는지 확인할 수 있습니다:

```bash
node --version
npm --version
```

> **참고 링크**  
> - [Node.js 공식 다운로드 페이지](https://nodejs.org/)  
> - LTS(Long Term Support) 버전 설치를 권장합니다.  

> ✅ 설치 확인 팁  
> `node -v`와 `npm -v` 명령어를 입력했을 때 버전 정보가 출력되면 정상적으로 설치된 것입니다.

# 3. GitHub에서 소스 코드 복제하기

## 3.1. 소스코드 복제

`git clone` 명령은 GitHub에 호스팅된 원격 저장소에서 로컬 컴퓨터로 소스 코드를 복제하는 명령어입니다. 이 명령을 사용하면 프로젝트의 전체 소스 코드와 관련 파일들을 로컬에서 작업할 수 있게 됩니다. 복제한 후에는 저장소 내에서 필요한 작업을 진행할 수 있으며, 변경 사항은 다시 원격 저장소에 푸시할 수 있습니다.

터미널을 열고 다음 명령어를 실행하여 CAS 서버의 리포지토리를 로컬 컴퓨터에 복사합니다.
```bash
# Git 저장소에서 리포지토리 복제
git clone https://github.com/OmniOneID/did-ca-server.git

# 복제한 리포지토리로 이동
cd did-ca-server
```

> **참고 링크**
> - [Git Clone 가이드](https://docs.github.com/en/repositories/creating-and-managing-repositories/cloning-a-repository)

<br/>

## 3.2. 디렉토리 구조
복제된 프로젝트의 주요 디렉토리 구조는 다음과 같습니다:

```
did-ca-server
├── CHANGELOG.md
├── CLA.md
├── CODE_OF_CONDUCT.md
├── CONTRIBUTING.md
├── LICENSE
├── dependencies-license.md
├── MAINTAINERS.md
├── README.md
├── RELEASE-PROCESS.md
├── SECURITY.md
├── docs
│   └── admin
│       └── Open DID CA Admin Console Guide.md
│       └── Open DID CA Admin Console Guide_ko.md
│   └── api
│       └── CAS_API_ko.md
│   └── errorCode
│       └── CA_ErrorCode.md
│   └── installation
│       └── OpenDID_CAServer_InstallationAndOperation_Guide.md
│       └── OpenDID_CAServer_InstallationAndOperation_Guide_ko.md
│   └── db
│       └── OpenDID_TableDefinition_CAS.md
└── source
    └── did-ca-server
        ├── gradle
        ├── libs
            └── did-sdk-common-2.0.0.jar
            └── did-blockchain-sdk-server-2.0.0.jar
            └── did-core-sdk-server-2.0.0..jar
            └── did-crypto-sdk-server-2.0.0.jar
            └── did-datamodel-server-2.0.0.jar
            └── did-wallet-sdk-server-2.0.0.jar
            └── did-zkp-sdk-server-2.0.0.jar
        ├── sample
        └── src
        └── build.gradle
        └── README.md
    └── did-ca-admin
```

| Name                    | Description                              |
| ----------------------- | ---------------------------------------- |
| CHANGELOG.md            | 프로젝트의 버전별 변경 사항              |
| CODE_OF_CONDUCT.md      | 기여자들을 위한 행동 강령                |
| CONTRIBUTING.md         | 기여 지침 및 절차                        |
| LICENSE                 | 라이선스 정보 |
| dependencies-license.md | 프로젝트 의존 라이브러리의 라이선스 정보 |
| MAINTAINERS.md          | 프로젝트 관리자를 위한 지침              |
| RELEASE-PROCESS.md      | 새로운 버전을 릴리스하는 절차            |
| SECURITY.md             | 보안 정책 및 취약성 보고 방법            |
| docs                    | 문서                                     |
| ┖ admin                 | Admin 가이드 문서                          |
| ┖ api                   | API 가이드 문서                          |
| ┖ errorCode             | 오류 코드 및 문제 해결 가이드            |
| ┖ installation          | 설치 및 설정 가이드                      |
| ┖ db                    | 데이터베이스 ERD, 테이블 명세서          |
| source                  | 소스 코드                                |
| ┖ did-ca-server        | CAS 서버 소스 코드 및 빌드 파일          |
| ┖ gradle                | Gradle 빌드 설정 및 스크립트             |
| ┖ libs                  | 외부 라이브러리 및 의존성                |
| ┖ sample                | 샘플 파일                                |
| ┖ src                   | 주요 소스 코드 디렉토리                  |
| ┖ build.gradle          | Gradle 빌드 설정 파일                    |
| ┖ README.md             | 소스 코드 개요 및 안내                   |

<br/>

# 4. 서버 구동 방법
이 장에서는 서버를 구동하는 세 가지 방법을 안내합니다.

프로젝트 소스는 `source` 디렉토리 하위에 위치하며, 각 구동 방법에 따라 해당 디렉토리에서 소스를 불러와 설정해야 합니다.

1. **IDE를 사용하는 방법**: 통합 개발 환경(IDE)에서 프로젝트를 열고, 실행 구성을 설정한 후 서버를 직접 실행할 수 있습니다. 이 방법은 개발 중에 코드 변경 사항을 빠르게 테스트할 때 유용합니다.

2. **Build 후 콘솔 명령어를 사용하는 방법**: 프로젝트를 빌드한 후, 생성된 JAR 파일을 콘솔에서 명령어(`java -jar`)로 실행하여 서버를 구동할 수 있습니다. 이 방법은 서버를 배포하거나 운영 환경에서 실행할 때 주로 사용됩니다.

3. **Docker로 빌드하는 방법**: 서버를 Docker 이미지로 빌드하고, Docker 컨테이너로 실행할 수 있습니다. 이 방법은 환경 간 일관성을 유지하며, 배포 및 스케일링이 용이한 장점이 있습니다.
   
## 4.1. IDE로 구동하기 (Gradle 및 React 프로젝트 실행)

Open DID 프로젝트는 백엔드(Spring Boot 기반)와 프론트엔드(React 기반)로 구성되어 있으며, 각각 IntelliJ IDEA와 VS Code에서 개발 및 실행할 수 있습니다.

### 4.1.1. IntelliJ IDEA에서 백엔드(Spring Boot) 실행

IntelliJ IDEA는 Java 개발에 널리 사용되는 IDE로, Gradle 기반 프로젝트와 잘 호환됩니다. Open DID 서버는 Gradle을 사용하므로, IntelliJ에서 쉽게 실행할 수 있습니다.

#### 4.1.1.1. IntelliJ IDEA 설치

- [IntelliJ IDEA 다운로드](https://www.jetbrains.com/idea/download/)

#### 4.1.1.2. 프로젝트 열기

- `File -> New -> Project from Existing Sources` 선택  
- `source/did-ca-server` 디렉토리 선택  
- `build.gradle` 파일이 자동 인식되며, 필요한 의존성이 자동으로 다운로드됨

#### 4.1.1.3. Gradle 빌드

- `Gradle` 탭에서 `Tasks -> build -> build` 실행

#### 4.1.1.4. 서버 실행

- `Tasks -> application -> bootRun` 실행  
- 콘솔에 `"Started [ApplicationName] in [time] seconds"` 메시지 출력 시 정상 구동

> ⚠️ 기본 `sample` 프로파일로 실행됨. 데이터베이스 없이 테스트용으로 구동됨  
> 자세한 내용은 [6. 프로파일 설정 및 사용](#6-프로파일-설정-및-사용) 참고

#### 4.1.1.5. 데이터베이스 설치

- PostgreSQL 사용 (Docker 설치 권장)  
- 자세한 설치 방법은 [2.2. PostgreSQL 설치](#22-postgresql-설치) 참고
<br/>

#### 4.1.1.6. 서버 설정
- 서버는 배포 환경에 맞게 필요한 설정을 수정해야 하며, 이를 통해 서버가 안정적으로 작동할 수 있도록 해야 합니다. 예를 들어, 데이터베이스 연결 정보, 포트 번호, 이메일 연동 정보 등 각 환경에 맞는 구성 요소들을 조정해야 합니다.
- 서버의 설정 파일은 `src/main/resource/config` 경로에 위치해 있습니다.
- 자세한 설정 방법은 [5. 설정 가이드](#5-설정-가이드) 를 참고해 주세요.

<br/>

## 4.2. 콘솔 명령어로 구동하기

콘솔 명령어를 사용하여 Open DID 서버를 구동하는 방법을 안내합니다. Gradle을 이용해 프로젝트를 빌드하고, 생성된 JAR 파일을 사용하여 서버를 구동하는 과정을 설명합니다.
- Gradle 빌드시 프론트엔드(Admin Console)가 자동으로 함께 빌드되며, 정적 리소스로 포함됩니다.

### 4.2.1. Gradle 빌드 명령어

- gradlew를 사용하여 소스를 빌드합니다.
  ```shell
    # 복제한 리포지토리로의 소스폴더로 이동
    cd source/did-ca-server

    # Gradle Wrapper 실행 권한을 부여
    chmod 755 ./gradlew

    # 프로젝트를 클린 빌드 (이전 빌드 파일을 삭제하고 새로 빌드)
    ./gradlew clean build
  ```
  > 참고: 프론트엔드 빌드가 필요 없는 경우(예: 백엔드만 테스트하거나 프론트엔드 결과물을 이미 포함하고 있는 경우)는 다음과 같이 옵션을 추가하여 프론트엔드 빌드를 생략할 수 있습니다. 
  > - `./gradlew clean build -DskipFrontendBuild=true`


- 빌드된 폴더로 이동하여 JAR 파일이 생성된 것을 확인합니다.
    ```shell
      cd build/libs
      ls
    ```
- 이 명령어는 `did-ca-server-2.0.0.jar` 파일을 생성합니다.

<br/>

### 4.2.2. 서버 구동 방법
빌드된 JAR 파일을 사용하여 서버를 구동합니다:

```bash
java -jar did-ca-server-2.0.0.jar
```

- 서버가 정상적으로 구동되면, 브라우저에서 http://localhost:8094/swagger-ui/index.html 주소로 이동하여 Swagger UI를 통해 API 문서가 제대로 표시되는지 확인합니다.

> **주의**
> - CAS 서버는 초기에 sample 프로파일로 설정되어 있습니다.
> - sample 프로파일로 설정시, 필수 설정(예: 데이터베이스)을 무시하고 서버가 구동됩니다. 자세한 내용은 [6. 프로파일 설정 및 사용](#6-프로파일-설정-및-사용) 장을 참고해 주세요.

<br/>

### 4.2.3. 데이터베이스 설치
CAS 서버는 운영에 필요한 데이터를 데이터베이스에 저장하므로, 서버를 운영하려면 반드시 데이터베이스가 설치되어야 합니다. Open DID의 서버는 PostgreSQL 데이터베이스를 사용합니다. PostgreSQL 서버의 설치 방법은 여러가지가 있지만, Docker를 이용한 설치가 가장 간편하고 쉽습니다. PostgreSQL의 설치 방법은 [2.2. PostgreSQL 설치](#22-postgresql-설치) 장을 참고해 주세요.

<br/>

### 4.2.4. 서버 설정 방법
- 서버는 배포 환경에 맞게 필요한 설정을 수정해야 하며, 이를 통해 서버가 안정적으로 작동할 수 있도록 해야 합니다. 예를 들어, 데이터베이스 연결 정보, 포트 번호, 이메일 연동 정보 등 각 환경에 맞는 구성 요소들을 조정해야 합니다.
- 서버의 설정 파일은 `src/main/resource/config` 경로에 위치해 있습니다.
- 자세한 설정 방법은 [5. 설정 가이드](#5-설정-가이드) 를 참고하십시오.

<br/>

## 4.3. Docker로 구동하기
- Docker 이미지 빌드, 설정, 실행 등의 과정은 아래 [7. Docker로 빌드 후 구동하기](#7-docker로-빌드-후-구동하기) 를 참고하세요.

<br/>

# 5. 설정 가이드

이 장에서는 서버의 모든 설정 파일에 포함된 각 설정 값에 대해 안내합니다. 각 설정은 서버의 동작과 환경을 제어하는 중요한 요소로, 서버를 안정적으로 운영하기 위해 적절한 설정이 필요합니다. 항목별 설명과 예시를 참고하여 각 환경에 맞는 설정을 적용하세요.

🔒 아이콘이 있는 설정은 기본적으로 고정된 값이거나, 일반적으로 수정할 필요가 없는 값임을 참고해주세요.

## 5.1. application.yml

- 역할: application.yml 파일은 Spring Boot 애플리케이션의 기본 설정을 정의하는 파일입니다. 이 파일을 통해 애플리케이션의 이름, 데이터베이스 설정, 프로파일 설정 등 다양한 환경 변수를 지정할 수 있으며, 애플리케이션의 동작 방식에 중요한 영향을 미칩니다.

- 위치: `src/main/resources/`

### 5.1.1. Spring 기본 설정
Spring의 기본 설정은 애플리케이션의 이름과 활성화할 프로파일 등을 정의하며, 서버의 동작 환경을 설정하는 데 중요한 역할을 합니다.

* `spring.application.name`: 🔒
    - 애플리케이션의 이름을 지정합니다.
    - 용도: 주로 로그 메시지, 모니터링 도구, 또는 Spring Cloud 서비스에서 애플리케이션을 식별하는 데 활용됩니다
    - 예시: `cas`

* `spring.profiles.active`:  
  - 활성화할 프로파일을 정의합니다. 
  - 용도: 샘플 또는 개발 환경 중 하나를 선택하여 해당 환경에 맞는 설정을 로드합니다. 프로파일에 대한 자세한 내용은 [6. 프로파일 설정 및 사용](#6-프로파일-설정-및-사용) 장을 참고해 주세요.
  - 지원 프로파일: sample, dev
  - 예시: `sample`, `dev`

* `spring.profiles.group.dev`: 🔒
  - `dev` 프로파일 그룹에 포함된 개별 프로파일을 정의합니다.
  - 용도: 개발 환경에서 사용할 설정들을 묶어 관리합니다.
  - 프로파일 파일명 규칙: 각 프로파일에 해당하는 설정 파일은 그룹 내에 정의된 이름 그대로 사용됩니다. 예를 들어, auth 프로파일은 application-auth.yml, databases 프로파일은 application-databases.yml로 작성됩니다. group.dev 아래에 적힌 이름 그대로 파일명을 사용해야 합니다.

* `spring.profiles.group.sample`: 🔒
  - `sample` 프로파일 그룹에 포함된 개별 프로파일을 정의합니다.
  - 용도: 개발 환경에서 사용할 설정들을 묶어 관리합니다.
  - 프로파일 파일명 규칙: 각 프로파일에 해당하는 설정 파일은 그룹 내에 정의된 이름 그대로 사용됩니다. 예를 들어, auth 프로파일은 application-auth.yml, databases 프로파일은 application-databases.yml로 작성됩니다. group.dev 아래에 적힌 이름 그대로 파일명을 사용해야 합니다.

<br/>

### 5.1.2. Jackson 기본 설정

Jackson은 Spring Boot에서 기본적으로 사용되는 JSON 직렬화/역직렬화 라이브러리입니다. Jackson의 설정을 통해 JSON 데이터의 직렬화 방식이나 포맷을 조정할 수 있으며, 데이터 전송 시 성능과 효율성을 높일 수 있습니다.

* `spring.jackson.default-property-inclusion`: 🔒 
    - 속성 값이 null일 때 직렬화하지 않도록 설정합니다.
    - 예시: non_null

* `spring.jackson.default-property-inclusion`: 🔒 
    - 빈 객체를 직렬화할 때 오류를 발생시키지 않도록 설정합니다.
    - 예시: false

<br/>

### 5.1.3. 서버 설정 
서버 설정은 애플리케이션이 요청을 수신할 포트 번호 등을 정의합니다.

* `server.port`:  
    - 애플리케이션이 실행될 포트 번호입니다. CAS 서버의 기본 포트는 8094 입니다.
    - 값 : 8094

<br/>

### 5.1.4. TA(Trust Agent) 설정 
CAS 서버와 통신할 TAS 서버의 정보를 입력합니다.

* `tas.url`:  
    - TAS(Trust Agent Service) 서버의 URL입니다. 
    - 예시: `http://127.0.0.1:8090/tas`

<br/>

## 5.2. database.yml
- 역할: 데이터베이스의 연결 정보부터 Liquibase를 사용한 마이그레이션 설정, JPA 설정까지, 서버에서 데이터베이스를 어떻게 관리하고 운영할지를 정의합니다

- 위치: `src/main/resources/`
  
### 5.2.1. Spring Liquibase 설정 
iquibase는 데이터베이스 마이그레이션을 관리하는 도구로, 데이터베이스 스키마의 변경 사항을 추적하고 자동으로 적용할 수 있도록 도와줍니다. 이를 통해 개발 및 운영 환경에서 데이터베이스 일관성을 유지할 수 있습니다.

* `spring.liquibase.change-log`: 🔒 
    - 데이터베이스 변경 로그 파일의 위치를 지정합니다. Liquibase가 데이터베이스 스키마 변경을 추적하고 적용하는 데 사용하는 로그 파일의 위치입니다.
    - 예시: `classpath:/db/changelog/master.xml`

* `spring.liquibase.enabled`: 🔒 
    - Liquibase 활성화 여부를 설정합니다. true로 설정 시 애플리케이션 시작 시 Liquibase가 실행되어 데이터베이스 마이그레이션을 수행합니다. `sample` 프로파일은 데이터베이스 연동을 하지 않으므로 false로 설정해야 합니다.
    - 예시: `true` [dev], `false` [sample]

* `spring.liquibase.fall-on-error`: 🔒
    - Liquibase가 데이터베이스 마이그레이션을 수행하는 동안 오류가 발생했을 때의 동작을 제어합니다. `sample` 프로파일에서만 설정합니다.
    - 예시: `false` [sample]

<br/>

### 5.2.2. 데이터소스 설정
데이터소스 설정은 애플리케이션이 데이터베이스에 연결하기 위한 기본 정보를 정의합니다. 여기에는 데이터베이스 드라이버, URL, 사용자 이름 및 비밀번호 등의 정보가 포함됩니다.

* `spring.datasource.driver-class-name`: 🔒
    - 사용할 데이터베이스 드라이버 클래스를 지정합니다. 데이터베이스에 연결하기 위한 JDBC 드라이버를 지정합니다.
    - 예시: `org.postgresql.Driver`

* `spring.datasource.url`:  
    - 데이터베이스 연결 URL입니다. 애플리케이션이 연결할 데이터베이스의 위치와 이름을 지정합니다. 
    - 예시: `jdbc:postgresql://localhost:5432/cas_db`

* `spring.datasource.username`:  
    - 데이터베이스 접속 사용자 이름입니다.
    - 예시: `cas`

* `spring.datasource.password`:  
    - 데이터베이스 접속 비밀번호입니다.
    - 예시: `caspassword`

<br/>

### 5.2.3. JPA 설정
JPA 설정은 애플리케이션의 데이터베이스와 상호작용하는 방식을 제어하며, 성능과 가독성에 중요한 영향을 미칩니다.

* `spring.jpa.open-in-view`: 🔒 
    - OSIV(Open Session In View) 패턴 사용 여부를 설정합니다. true로 설정 시 HTTP 요청 전체에 대해 데이터베이스 연결을 유지합니다.
    - 예시: `true`

* `spring.jpa.show-sql`: 🔒 
    - SQL 쿼리 로깅 여부를 설정합니다. true로 설정 시 실행되는 SQL 쿼리를 로그에 출력합니다. 개발 시 디버깅에 유용합니다.
    - 예시: `true`

* `spring.jpa.hibernate.ddl-auto`: 🔒 
    - Hibernate의 DDL 자동 생성 모드를 설정합니다. 데이터베이스 스키마 자동 생성 전략을 지정합니다. 'none'으로 설정 시 자동 생성을 비활성화합니다.
    - 예시: `none`

* `spring.jpa.hibernate.naming.physical-strategy`: 🔒 
    - 데이터베이스 객체 명명 전략을 설정합니다. 엔티티 클래스의 이름을 데이터베이스 테이블 이름으로 변환하는 전략을 지정합니다.
    - 예시: `org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy`

* `spring.jpa.properties.hibernate.format_sql`: 🔒 
    - SQL 포맷팅 여부를 설정합니다. false로 설정 시 로그에 출력되는 SQL 쿼리의 포맷팅을 비활성화합니다.
    - 예시: `false`

<br/>

## 5.3. application-logging.yml
- 역할: 로그 그룹과 로그 레벨을 설정합니다. 이 설정 파일을 통해 특정 패키지나 모듈에 대해 로그 그룹을 정의하고, 각 그룹에 대한 로그 레벨을 개별적으로 지정할 수 있습니다.

- 위치: `src/main/resources/`
  
### 5.3.1. 로깅 설정

- 로그 그룹: logging.group 아래에 원하는 패키지를 그룹화하여 관리할 수 있습니다. 예를 들어, util 그룹에 org.omnione.did.base.util 패키지를 포함하고, 다른 패키지도 각각의 그룹으로 정의합니다.

- 로그 레벨: logging.level 설정을 통해 각 그룹에 대해 로그 레벨을 지정할 수 있습니다. debug, info, warn, error 등 다양한 로그 레벨을 설정하여 원하는 수준의 로그를 출력할 수 있습니다. 예를 들어, cas, aop 등의 그룹에 debug 레벨을 설정하여 해당 패키지에서 디버그 정보를 출력하도록 할 수 있습니다.

* `logging.level`: 
    - 로그 레벨을 설정합니다.
    - 레벨을 debug 설정함으로써, 지정된 패키지에 대해 DEBUG 레벨 이상(INFO, WARN, ERROR, FATAL)의 모든 로그 메시지를 볼 수 있습니다.

전체 예시:
```yaml
logging:
  group:
    cas:
      - org.omnione.did
  level:
    cas: debug
```

<br/>

## 5.4. application-spring-docs.yml

- 역할: 애플리케이션에서 SpringDoc 및 Swagger UI 설정을 관리합니다.

- 위치: `src/main/resources/`

* `springdoc.swagger-ui.path`: 🔒
  - Swagger UI에 접근할 수 있는 URL 경로를 정의합니다.
  - 예시: `/swagger-ui.html`

* `springdoc.swagger-ui.groups-order`: 🔒
  - Swagger UI에서 API 그룹을 표시하는 순서를 지정합니다.
  - 예시: `ASC`

* `springdoc.swagger-ui.operations-sorter`: 🔒
  - Swagger UI에서 HTTP 메서드 기준으로 API 엔드포인트를 정렬합니다.
  - 예시: `method`

* `springdoc.swagger-ui.disable-swagger-default-url`: 🔒
  - 기본 Swagger URL을 비활성화합니다.
  - 예시: `true`

* `springdoc.swagger-ui.display-request-duration`: 🔒
  - Swagger UI에 요청 시간을 표시할지 여부를 설정합니다.
  - 예시: `true`

* `springdoc.api-docs.path`: 🔒
  - API 문서가 제공되는 경로를 정의합니다.
  - 예시: `/api-docs`

* `springdoc.show-actuator`: 🔒
  - API 문서에서 Actuator 엔드포인트를 표시할지 여부를 설정합니다.
  - 예시: `true`

* `springdoc.default-consumes-media-type`: 🔒
  - API 문서에서 요청 본문의 기본 미디어 타입을 설정합니다.
  - 예시: `application/json`

* `springdoc.default-produces-media-type`: 🔒
  - API 문서에서 응답 본문의 기본 미디어 타입을 설정합니다.
  - 예시: `application/json`

<br/>

## 5.5. applicaiton-wallet.yml

- 역할: 서버에서 사용하는 월렛 파일 정보를 설정합니다.

- 위치: `src/main/resources/`

* `wallet.file-path`:  
    - 월렛 파일의 경로를 지정합니다. 파일 월렛을 저장하는 파일의 위치를 지정합니다. 이 파일은 개인키 등 중요한 정보를 포함할 수 있습니다. *반드시 절대경로로 입력해야합니다*
    - 예시: `/path/to/your/cas.wallet`

* `wallet.password`:  
    - 월렛 접근에 사용되는 비밀번호입니다. 월렛 파일의 접근시 사용되는 비밀번호입니다. 높은 보안이 요구되는 정보입니다.
    - 예시: `your_secure_wallet_password`

## 5.6. applicaiton-cas.yml
이 설정 파일은 CAS 서버의 기본 정보와 암호화 설정, 토큰 만료 시간 등을 정의합니다. 

* `cas.did`: 
  - CAS 서버의 DID를 설정합니다.
  - 예시: did:omn:cas

* `cas.certificate-vc`: 
  - CAS 서버의 가입증명서 VC 조회 API를 설정합니다. 
  - 포맷: {CAS 도메인}/cas/api/v1/certificate-vc
  - 예시: http://127.0.0.1:8094/cas/api/v1/certificate-vc

<br/>

## 5.7. blockchain.properties
- 역할: CA 서버에서 연동할 블록체인 서버 정보를 설정합니다. [Open DID Installation Guide]의 '5.3. Step 3: Blockchain 설치'에 따라 Hyperledger Besu 네트워크를 설치하면, 개인 키, 인증서, 서버 접속 정보 설정 파일이 자동으로 생성됩니다. blockchain.properties에서는 이들 파일이 위치한 경로와, Hyperledger Besu 설치 시 입력한 네트워크 이름을 설정합니다.

- 위치: `src/main/resources/properties`

### 5.7.1. 블록체인 연동 설정 

#### EVM Network Configuration

- `evm.network.url:`:
  - EVM Network 주소, 클라이언트와 동일한 로컬에 Besu를 구동하는 경우 해당 값은 고정 사용합니다. (Defalt Port : 8545)
  - 예시: http://localhost:8545

- `evm.chainId:`:
  - Chain ID 식별자입니다. 현재는 1337의 고정값을 사용중입니다.(Defalt Value : 1337)
  - 예시: 1337

- `evm.gas.limit:`:
  - Hyperledger Besu EVM 트랜잭션에서 최대로 허용되는 가스 한도, 현재는 Free Gas로서 고정으로 사용합니다. (Defalt Value : 100000000)
  - 예시: 100000000

- `evm.gas.price :`:
  - 유닛 단위 가스 가격, 현재는 Free Gas로서 0으로  고정으로 사용합니다.(Defalt Value : 0)
  - 예시: 0

- `evm.connection.timeout:`: 
  - 네트워크 커넥션 타임아웃 값(milliseconds), 현재는 권장 값인 10000으로 고정 사용합니다. (Defalt Value : 10000)
  - 예시: 10000

#### EVM Contract Configuration

- `evm.connection.address:`: 
  - Hardhat으로 Smart Contract 배포 시 리턴되는 OpenDID Contract의 Address 값, 상세 가이드는 [DID Besu Contract] 참조 바랍니다.
  - 예시: 0xa0E49611FB410c00f425E83A4240e1681c51DDf4

- `evm.connection.privateKey:`: 
  - API 접근 통제에 사용되는 k1 키, hardhat.config.js 내부 accounts에 정의된 키 문자열을 입력(앞에 0x 문자열은 제거)하면 Owner 권한으로 API 호출 가능(Default 설정), 상세 가이드는 [DID Besu Contract] 참조바랍니다.
  - 예시: 0x8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63
  
<br/>

# 6. 프로파일 설정 및 사용

## 6.1. 프로파일 개요 (`sample`, `dev`)
CAS 서버는 다양한 환경에서 실행될 수 있도록 `dev`와 `sample` 두 가지 프로파일을 지원합니다.

각 프로파일은 해당 환경에 맞는 설정을 적용하도록 설계되었습니다. 기본적으로 CAS 서버는 `sample` 프로파일로 설정되어 있으며, 이 프로파일은 데이터베이스나 블록체인과 같은 외부 서비스와의 연동 없이 서버를 독립적으로 구동할 수 있도록 설계되었습니다. `sample` 프로파일은 API 호출 테스트에 적합하여, 개발자가 애플리케이션의 기본 동작을 빠르게 확인할 수 있도록 지원합니다. 이 프로파일은 모든 API 호출에 대해 고정된 응답 데이터를 반환하므로, 초기 개발환경에서 유용합니다.

샘플 API 호출은 JUnit 테스트로 작성되어 있으므로, 테스트 작성 시 이를 참고할 수 있습니다.

반면, `dev` 프로파일은 실제 동작을 수행하도록 설계되었습니다. 이 프로파일을 사용하면 실데이터에 대한 테스트와 검증이 가능합니다. `dev` 프로파일을 활성화하면 실제 데이터베이스, 블록체인 등 외부 서비스와 연동되어, 실제 환경에서의 애플리케이션 동작을 테스트할 수 있습니다.

### 6.1.1. `sample` 프로파일
`sample` 프로파일은 외부 서비스(DB, 블록체인 등)와의 연동 없이 서버를 독립적으로 구동할 수 있도록 설계되었습니다. 이 프로파일은 API 호출 테스트에 적합하며, 개발자가 애플리케이션의 기본 동작을 빠르게 확인할 수 있습니다. 모든 API 호출에 대해 고정된 응답 데이터를 반환하므로, 초기 개발 단계나 기능 테스트에 유용합니다. 외부 시스템과의 연동이 전혀 필요하지 않기 때문에, 단독으로 서버를 실행하고 테스트할 수 있는 환경을 제공합니다.
> 참고: sample 프로파일을 사용 할 경우 Admin Console이 동작하지 않습니다.

### 6.1.2. `dev` 프로파일
`dev` 프로파일은 개발 환경에 적합한 설정을 포함하며, 개발 서버에서 사용됩니다. 이 프로파일을 사용하려면 개발 환경의 데이터베이스와 블록체인 노드에 대한 설정이 필요합니다.


## 6.2. 프로파일 설정 방법
각 구동 방법별로 프로파일을 변경하는 방법을 설명합니다.

### 6.2.1. IDE를 사용한 서버 구동 시
- **설정 파일 선택:** `src/main/resources` 경로에서 `application.yml` 파일을 선택합니다.
- **프로파일 지정:** IDE의 실행 설정(Run/Debug Configurations)에서 `--spring.profiles.active={profile}` 옵션을 추가해 원하는 프로파일을 활성화합니다.
- **설정 적용:** 활성화된 프로파일에 따라 해당 설정 파일이 적용됩니다.

### 6.2.2. 콘솔 명령어를 사용한 서버 구동 시
- **설정 파일 선택:** 빌드된 JAR 파일과 동일한 디렉토리 또는 설정 파일이 위치한 경로에 프로파일별 설정 파일을 준비합니다.
- **프로파일 지정:** 서버 구동 명령어에 `--spring.profiles.active={profile}` 옵션을 추가하여 원하는 프로파일을 활성화합니다.
  
  ```bash
  java -jar build/libs/did-ca-server-2.0.0.jar --spring.profiles.active={profile}
  ```

- **설정 적용:** 활성화된 프로파일에 따라 해당 설정 파일이 적용됩니다.

### 6.2.3. Docker를 사용한 서버 구동 시
- **설정 파일 선택:** Docker 이미지 생성 시, Dockerfile에서 설정 파일 경로를 지정하거나, 외부 설정 파일을 Docker 컨테이너에 마운트합니다.
- **프로파일 지정:** Docker Compose 파일 또는 Docker 실행 명령어에서 `SPRING_PROFILES_ACTIVE` 환경 변수를 설정하여 프로파일을 지정합니다.
  
  ```yaml
  environment:
    - SPRING_PROFILES_ACTIVE={profile}
  ```

- **설정 적용:** Docker 컨테이너 실행 시 지정된 프로파일에 따라 설정이 적용됩니다.

각 방법에 따라 프로파일별 설정을 유연하게 변경하여 사용할 수 있으며, 프로젝트 환경에 맞는 설정을 쉽게 적용할 수 있습니다.

# 7. Docker로 빌드 후 구동하기

## 7.1. Docker 이미지 빌드 방법 (`Dockerfile` 기반)

### 7.1.1. Docker 이미지 빌드
다음 명령어로 Docker 이미지를 빌드합니다:

```bash
cd {source_directory}
docker build -t did-ca-server -f did-ca-server/Dockerfile .
```

<br/>

## 7.2. Docker Compose를 이용한 구동

### 7.2.1. 디렉토리 및 설정 파일 준비

#### 1. docker-compose 디렉토리 및 config 디렉토리 생성
```bash
mkdir -p {docker_compose_directory}/config
```

#### 2. 설정 파일(yml)들을 config 디렉토리로 복사
```bash
cp {application_yml_directory}/* {docker_compose_directory}/config/
cp {blockchain_properties_path} {docker_compose_directory}/config/
```

#### 3. blockchain.properties 파일 수정
```yml
evm.network.url=http://host.docker.internal:8545
... 생략
```

> **host.docker.internal**은 Docker 컨테이너에서 호스트 머신을 가리키는 특별한 주소입니다.  
> 컨테이너 내부에서 localhost는 컨테이너 자신을 의미하므로, 호스트에서 실행 중인 서비스(PostgreSQL, 블록체인)에 접근하려면 host.docker.internal을 사용해야 합니다.

#### 4. application-database.yml 파일 수정
```yml
spring:
  ... 생략
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://host.docker.internal:5430/ca
    username: omn
    password: omn
  ... 생략
```

### 7.2.2. `docker-compose.yml` 파일 생성
`docker-compose.yml` 파일을 사용하여 여러 컨테이너를 쉽게 관리할 수 있습니다.

```yml
version: '3'
services:
  app:
    image: did-ca-server
    ports:
      - "8094:8094"
    volumes:
      - {config_directory}:/app/config
    environment:
      - SPRING_PROFILES_ACTIVE=dev
    extra_hosts:
      - "host.docker.internal:host-gateway"
```

> - 위의 예시에서 `config_directory` 디렉토리를 컨테이너 내 `/app/config`로 마운트하여 설정 파일을 공유합니다.
>   - `config_directory`에 위치한 설정 파일은 기본 설정 파일보다 우선적으로 적용됩니다.
>   - 자세한 설정 방법은 [5. 설정 가이드](#5-설정-가이드) 를 참고해 주세요.


### 7.2.3. 컨테이너 실행
```bash
cd {docker_compose_directory}
docker-compose up -d
```

<br/>

# 8. Docker PostgreSQL 설치하기

Docker를 사용해 PostgreSQL을 설치하는 방법을 설명합니다. 이 방법을 통해 PostgreSQL을 손쉽게 설치하고, 서버에 연동해 사용할 수 있습니다.

## 8.1. Docker Compose를 이용한 PostgreSQL 설치

다음은 Docker Compose를 이용해 PostgreSQL을 설치하는 방법입니다.

```yml
services:
  postgres:
    container_name: postgre-ca
    image: postgres:16.4
    restart: always
    volumes:
      - postgres_data_ca:/var/lib/postgresql/data
    ports:
      - 5434:5432
    environment:
      POSTGRES_USER: ${USER}
      POSTGRES_PASSWORD: ${PW}
      POSTGRES_DB: cas

volumes:
  postgres_data_ca:
```

이 Docker Compose 파일은 PostgreSQL 16.4. 버전을 설치하고, 다음과 같은 설정을 합니다:

- **container_name**: 컨테이너 이름을 `postgre-ca`로 지정합니다.
- **volumes**: `postgres_data_ca` 볼륨을 PostgreSQL의 데이터 디렉토리(`/var/lib/postgresql/data`)로 마운트합니다. 이를 통해 데이터가 영구적으로 보존됩니다.
- **ports**: 호스트의 5434 포트를 컨테이너의 5432 포트와 매핑합니다.
- **environment**: PostgreSQL의 사용자명, 비밀번호, 데이터베이스 이름을 설정합니다. 여기서 `${USER}`, `${PW}`는 환경 변수로 설정할 수 있습니다.

## 8.2. PostgreSQL 컨테이너 실행

위의 Docker Compose 파일을 사용해 PostgreSQL 컨테이너를 실행하려면, 아래 명령어를 터미널에서 실행합니다:

```bash
docker-compose up -d
```

이 명령어는 백그라운드에서 PostgreSQL 컨테이너를 실행합니다. 설정된 환경 변수에 따라 PostgreSQL 서버가 실행되며, 데이터베이스가 준비됩니다. 이 데이터베이스를 애플리케이션에서 사용할 수 있도록 연동 설정을 진행하면 됩니다.

<!-- References -->
[Open DID Installation Guide]: https://github.com/OmniOneID/did-release/blob/develop/release-V2.0.0.0/OpenDID_Installation_Guide-V2.0.0.0_ko.md
[DID Besu Contract]: https://github.com/OmniOneID/did-besu-contract
[Open DID Admin Console Guide]: ../admin/OpenDID_CA_Admin_Console_Guide_ko.md

