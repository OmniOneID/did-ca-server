CA Server
==

CA 서버 Repository에 오신 것을 환영합니다. <br>
이 Repository는 CA 서버의 소스 코드, 문서, 관련 리소스를 포함하고 있습니다.

## 폴더 구조
프로젝트 디렉터리 내 주요 폴더와 문서에 대한 개요:

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
├── README_ko.md
├── RELEASE-PROCESS.md
├── SECURITY.md
├── docs
│   └── admin
│       ├── OpenDID_CAAdmin_Operation_Guide.md
│       └── OpenDID_CAAdmin_Operation_Guide_ko.md
│   └── api
│       ├── CAS_API.md
│       └── CAS_API_ko.md
│   └── errorCode
│       ├── CA_ErrorCode.md
│       └── CA_ErrorCode_ko.md
│   └── installation
│       └── OpenDID_CAServer_InstallationAndOperation_Guide.md
│       └── OpenDID_CAServer_InstallationAndOperation_Guide_ko.md
│   └── db
│       ├── OpenDID_TableDefinition_CAS.md
│       └── OpenDID_TableDefinition_CAS_ko.md
└── source
    └── did-ca-admin
        ├── frontend
    └── did-ca-server
```

<br/>

각 폴더와 파일에 대한 설명은 다음과 같습니다:

| 이름                         | 설명                                     |
| ---------------------------- | ---------------------------------------- |
| CHANGELOG.md                 | 프로젝트의 버전별 변경 사항              |
| CODE_OF_CONDUCT.md           | 기여자 행동 강령                         |
| CONTRIBUTING.md              | 기여 지침과 절차                         |
| LICENSE                      | 라이선스                                 |
| dependencies-license.md      | 프로젝트 의존 라이브러리의 라이선스 정보 |
| MAINTAINERS.md               | 프로젝트 유지 관리자 지침                |
| RELEASE-PROCESS.md           | 새 버전 릴리스 절차                      |
| SECURITY.md                  | 보안 정책 및 취약성 보고 방법            |
| docs                         | 문서화                                   |
| ┖ api                        | API 가이드 문서                          |
| ┖ errorCode                  | 오류 코드 및 문제 해결 가이드            |
| ┖ installation               | 설치 및 설정 지침                        |
| ┖ db                         | 데이터베이스 ERD, 테이블 명세서          |
| source                       | 서버 및 관리자 콘솔 소스 코드 프로젝트   |
| ┖ did-ca-server              | CA 서버 소스 코드                        |
| ┖ did-ca-admin               | CA Admin 소스 코드                       |
| &nbsp;&nbsp;&nbsp;┖ frontend | CA Admin 프론트엔드 소스 코드            |

<br/>

## 설치 및 운영 가이드

CA 서버의 설치에 대한 자세한 지침은 아래 가이드를 참조하십시오:
- [OpenDID CA 서버 설치 가이드](docs/installation/OpenDID_CAServer_Installation_Guide.md)  

CA Admin Console의 운영에 대한 자세한 지침은 아래 가이드를 참조하십시오:
- [OpenDID CA Admin Console 운영 가이드](docs/admin/OpenDID_CAAdmin_Operation_Guide_ko.md)  

## API 참고 문서

- **CAS API**: CA 서버의 API 엔드포인트 및 사용법에 대한 자세한 참고 자료입니다.
  - [CAS API 참고 자료](docs/api/CAS_API_ko.md)

## Change Log

Change Log에는 버전별 변경 사항과 업데이트가 자세히 기록되어 있습니다. 다음에서 확인할 수 있습니다:
- [Change Log](./CHANGELOG.md)  

## OpenDID 시연 영상

OpenDID 시스템의 시연 영상을 보려면 [데모 Repository](https://github.com/OmniOneID/did-demo-server)를 방문하십시오. <br>

이 영상에서는 사용자 등록, VC 발급, VP 제출 프로세스 등 주요 기능을 시연합니다.

## 기여

기여 절차와 행동 강령에 대한 자세한 내용은 [CONTRIBUTING.md](CONTRIBUTING.md)와 [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md)를 참조해 주십시오.

## 라이선스
[Apache 2.0](LICENSE)
