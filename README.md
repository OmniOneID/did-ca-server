CA Server
==

Welcome to the CA Server Repository. <br>
This repository contains the source code, documentation, and related resources for the CA Server.

## Folder Structure
Overview of the major folders and documents in the project directory:

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

Below is a description of each folder and file in the directory:

| Name                         | Description                                     |
| ---------------------------- | ----------------------------------------------- |
| CHANGELOG.md                 | Version-specific changes in the project         |
| CODE_OF_CONDUCT.md           | Code of conduct for contributors                |
| CONTRIBUTING.md              | Contribution guidelines and procedures          |
| LICENSE                      | Licenses                                        |
| dependencies-license.md      | Licenses for the project’s dependency libraries |
| MAINTAINERS.md               | Guidelines for project maintainers              |
| RELEASE-PROCESS.md           | Procedures for releasing new versions           |
| SECURITY.md                  | Security policies and vulnerability reporting   |
| docs                         | Documentation                                   |
| ┖ api                        | API guide documentation                         |
| ┖ errorCode                  | Error codes and troubleshooting guides          |
| ┖ installation               | Installation and setup instructions             |
| ┖ db                         | Database ERD,  Table Specifications             |
| source                       | Source code for the server and admin console    |
| ┖ did-ca-server              | CA Server source code                           |
| ┖ did-ca-admin               | CA Admin source code                            |
| &nbsp;&nbsp;&nbsp;┖ frontend | CA Admin frontend source code                   |

<br/>

## Installation And Operation Guide

For detailed instructions on installing the CA Server, please refer to the guide below:
- [OpenDID CA Server Installation and Operation Guide](docs/installation/OpenDID_CAServer_Installation_Guide.md)  

For detailed instructions on operating the CA Admin Console, please refer to the guide below:  
- [OpenDID CA Admin Console Operation Guide](docs/admin/OpenDID_CAAdmin_Operation_Guide_ko.md)

## API Reference

- **CAS API**: Detailed reference for the CA Server's API endpoints and usage.
  - [CAS API Reference](docs/api/CAS_API_ko.md)

## Change Log

The Change Log provides a detailed record of version-specific changes and updates. You can find it here:
- [Change Log](./CHANGELOG.md)

## OpenDID Demonstration Videos <br>
To watch our demonstration videos of the OpenDID system in action, please visit our [Demo Repository](https://github.com/OmniOneID/did-demo-server). <br>

These videos showcase key features including user registration, VC issuance, and VP submission processes.

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) and [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) for details on our code of conduct, and the process for submitting pull requests to us.

## License
[Apache 2.0](LICENSE)
