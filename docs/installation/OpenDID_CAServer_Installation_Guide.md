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

## Table of Contents

- [Open DID CA Server Installation Guide](#open-did-ca-server-installation-guide)
  - [Table of Contents](#table-of-contents)
- [1. Introduction](#1-introduction)
  - [1.1. Overview](#11-overview)
  - [1.2. CA Server Definition](#12-ca-server-definition)
  - [1.3. System Requirements](#13-system-requirements)
- [2. Prerequisites](#2-prerequisites)
  - [2.1. Git Installation](#21-git-installation)
  - [2.2. PostgreSQL Installation](#22-postgresql-installation)
  - [2.3. Node.js Installation](#23-nodejs-installation)
- [3. Cloning Source Code from GitHub](#3-cloning-source-code-from-github)
  - [3.1. Source Code Cloning](#31-source-code-cloning)
  - [3.2. Directory Structure](#32-directory-structure)
- [4. Server Operation Methods](#4-server-operation-methods)
  - [4.1. Running with IDE (Gradle and React Project Execution)](#41-running-with-ide-gradle-and-react-project-execution)
    - [4.1.1. Running Backend (Spring Boot) in IntelliJ IDEA](#411-running-backend-spring-boot-in-intellij-idea)
      - [4.1.1.1. IntelliJ IDEA Installation](#4111-intellij-idea-installation)
      - [4.1.1.2. Opening Project](#4112-opening-project)
      - [4.1.1.3. Gradle Build](#4113-gradle-build)
      - [4.1.1.4. Server Execution](#4114-server-execution)
      - [4.1.1.5. Database Installation](#4115-database-installation)
      - [4.1.1.6. Server Configuration](#4116-server-configuration)
  - [4.2. Running with Console Commands](#42-running-with-console-commands)
    - [4.2.1. Gradle Build Commands](#421-gradle-build-commands)
    - [4.2.2. Server Operation Method](#422-server-operation-method)
    - [4.2.3. Database Installation](#423-database-installation)
    - [4.2.4. Server Configuration Method](#424-server-configuration-method)
  - [4.3. Running with Docker](#43-running-with-docker)
- [5. Configuration Guide](#5-configuration-guide)
  - [5.1. application.yml](#51-applicationyml)
    - [5.1.1. Spring Basic Configuration](#511-spring-basic-configuration)
    - [5.1.2. Jackson Basic Configuration](#512-jackson-basic-configuration)
    - [5.1.3. Server Configuration](#513-server-configuration)
    - [5.1.4. TA(Trust Agent) Configuration](#514-tatrust-agent-configuration)
  - [5.2. database.yml](#52-databaseyml)
    - [5.2.1. Spring Liquibase Configuration](#521-spring-liquibase-configuration)
    - [5.2.2. Datasource Configuration](#522-datasource-configuration)
    - [5.2.3. JPA Configuration](#523-jpa-configuration)
  - [5.3. application-logging.yml](#53-application-loggingyml)
    - [5.3.1. Logging Configuration](#531-logging-configuration)
  - [5.4. application-spring-docs.yml](#54-application-spring-docsyml)
  - [5.5. application-wallet.yml](#55-application-walletyml)
  - [5.6. application-cas.yml](#56-application-casyml)
  - [5.7. blockchain.properties](#57-blockchainproperties)
    - [5.7.1. Blockchain Integration Configuration](#571-blockchain-integration-configuration)
      - [EVM Network Configuration](#evm-network-configuration)
      - [EVM Contract Configuration](#evm-contract-configuration)
- [6. Profile Configuration and Usage](#6-profile-configuration-and-usage)
  - [6.1. Profile Overview (`sample`, `dev`)](#61-profile-overview-sample-dev)
    - [6.1.1. `sample` Profile](#611-sample-profile)
    - [6.1.2. `dev` Profile](#612-dev-profile)
  - [6.2. Profile Configuration Method](#62-profile-configuration-method)
    - [6.2.1. When Running Server Using IDE](#621-when-running-server-using-ide)
    - [6.2.2. When Running Server Using Console Commands](#622-when-running-server-using-console-commands)
    - [6.2.3. When Running Server Using Docker](#623-when-running-server-using-docker)
- [7. Running After Building with Docker](#7-running-after-building-with-docker)
  - [7.1. Docker Image Build Method (Based on `Dockerfile`)](#71-docker-image-build-method-based-on-dockerfile)
    - [7.1.1. Build Docker image](#711-build-docker-image)
  - [7.2. Running with Docker Compose](#72-running-with-docker-compose)
    - [7.2.1. Preparing Directories and Configuration Files](#721-preparing-directories-and-configuration-files)
      - [1. Create docker-compose directory and config directory](#1-create-docker-compose-directory-and-config-directory)
      - [2. Copy configuration files (yml) to config directory](#2-copy-configuration-files-yml-to-config-directory)
      - [3. Modify blockchain.properties file](#3-modify-blockchainproperties-file)
      - [4. Modify application-database.yml file](#4-modify-application-databaseyml-file)
    - [7.2.2. Create `docker-compose.yml` file](#722-create-docker-composeyml-file)
    - [7.2.3. Run Container](#723-run-container)
- [8. Installing Docker PostgreSQL](#8-installing-docker-postgresql)
  - [8.1. PostgreSQL Installation using Docker Compose](#81-postgresql-installation-using-docker-compose)
  - [8.2. PostgreSQL Container Execution](#82-postgresql-container-execution)

# 1. Introduction

## 1.1. Overview
This document provides a guide for the installation, configuration, and operation of the Open DID CA (Certificate Application) server. The CA server consists of a Spring Boot-based backend and a React-based Admin console frontend, enabling integrated deployment through Gradle builds. It provides step-by-step instructions for the installation process, environment configuration, Docker execution methods, and profile settings, allowing users to efficiently install and run the server.

- For the overall installation guide of OpenDID, please refer to [Open DID Installation Guide].
- For the Admin console guide, please refer to [Open DID Admin Console Guide].

<br/>

## 1.2. CA Server Definition

The CA server is an authorized app server that guarantees authorized apps can be used within Open DID.

Throughout the server documentation, Certified App Server (abbreviated as 'CAS' or 'CA-Server') is used interchangeably depending on context and usage. These all refer to the same system.

<br/>

## 1.3. System Requirements
- **Java 21** or higher
- **Gradle 7.0** or higher
- **Docker** and **Docker Compose** (when using Docker)
- Minimum **2GB RAM** and **10GB disk space**

<br/>

# 2. Prerequisites

This chapter guides you through the prerequisite items needed before installing the components of the Open DID project.

## 2.1. Git Installation

`Git` is a distributed version control system that tracks changes in source code and supports collaboration among multiple developers. Git is essential for managing the source code of the Open DID project and version control.

If the installation is successful, you can check Git's version using the following command:

```bash
git --version
```

> **Reference Links**
> - [Git Installation Guide](https://docs.github.com/en/repositories/creating-and-managing-repositories/cloning-a-repository)

<br/>

## 2.2. PostgreSQL Installation
To run the CAS server, database installation is required, and Open DID uses PostgreSQL.

> **Reference Links**
- [PostgreSQL Installation Guide Document](https://www.postgresql.org/download/)
- [8. Installing Docker PostgreSQL](#8-installing-docker-postgresql)

<br/>

## 2.3. Node.js Installation
To run the React-based ca Admin Console, `Node.js` and `npm` are required.

npm (Node Package Manager) is used to install and manage dependencies needed for frontend development.

After installation is complete, you can verify proper installation with the following commands:

```bash
node --version
npm --version
```

> **Reference Links**  
> - [Node.js Official Download Page](https://nodejs.org/)  
> - LTS (Long Term Support) version installation is recommended.  

> ✅ Installation Verification Tip  
> If version information is displayed when you enter `node -v` and `npm -v` commands, the installation was successful.

# 3. Cloning Source Code from GitHub

## 3.1. Source Code Cloning

The `git clone` command clones source code from a remote repository hosted on GitHub to your local computer. Using this command allows you to work with the entire project source code and related files locally. After cloning, you can proceed with necessary work within the repository, and changes can be pushed back to the remote repository.

Open a terminal and execute the following commands to copy the CAS server repository to your local computer:
```bash
# Clone repository from Git repository
git clone https://github.com/OmniOneID/did-ca-server.git

# Navigate to the cloned repository
cd did-ca-server
```

> **Reference Links**
> - [Git Clone Guide](https://docs.github.com/en/repositories/creating-and-managing-repositories/cloning-a-repository)

<br/>

## 3.2. Directory Structure
The main directory structure of the cloned project is as follows:

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
| CHANGELOG.md            | Version-specific changes of the project |
| CODE_OF_CONDUCT.md      | Code of conduct for contributors         |
| CONTRIBUTING.md         | Contribution guidelines and procedures   |
| LICENSE                 | License information                      |
| dependencies-license.md | License information for project dependency libraries |
| MAINTAINERS.md          | Guidelines for project maintainers      |
| RELEASE-PROCESS.md      | Procedures for releasing new versions    |
| SECURITY.md             | Security policy and vulnerability reporting methods |
| docs                    | Documentation                            |
| ┖ admin                 | Admin guide documentation                |
| ┖ api                   | API guide documentation                  |
| ┖ errorCode             | Error codes and troubleshooting guide    |
| ┖ installation          | Installation and configuration guide     |
| ┖ db                    | Database ERD, table specifications       |
| source                  | Source code                              |
| ┖ did-ca-server        | CAS server source code and build files  |
| ┖ gradle                | Gradle build settings and scripts       |
| ┖ libs                  | External libraries and dependencies      |
| ┖ sample                | Sample files                             |
| ┖ src                   | Main source code directory              |
| ┖ build.gradle          | Gradle build configuration file         |
| ┖ README.md             | Source code overview and guide          |

<br/>

# 4. Server Operation Methods
This chapter guides you through three methods of running the server.

The project source is located under the `source` directory, and depending on each operation method, you need to load and configure the source from that directory appropriately.

1. **Using IDE**: You can open the project in an Integrated Development Environment (IDE), set up execution configurations, and run the server directly. This method is useful for quickly testing code changes during development.

2. **Using Build and Console Commands**: After building the project, you can run the server by executing the generated JAR file with console commands (`java -jar`). This method is mainly used when deploying servers or running in production environments.

3. **Building with Docker**: You can build the server as a Docker image and run it as a Docker container. This method maintains consistency across environments and has advantages for easy deployment and scaling.
   
## 4.1. Running with IDE (Gradle and React Project Execution)

The Open DID project consists of a backend (Spring Boot-based) and frontend (React-based), which can be developed and executed in IntelliJ IDEA and VS Code, respectively.

### 4.1.1. Running Backend (Spring Boot) in IntelliJ IDEA

IntelliJ IDEA is an IDE widely used for Java development and is well compatible with Gradle-based projects. Since the Open DID server uses Gradle, it can be easily executed in IntelliJ.

#### 4.1.1.1. IntelliJ IDEA Installation

- [IntelliJ IDEA Download](https://www.jetbrains.com/idea/download/)

#### 4.1.1.2. Opening Project

- Select `File -> New -> Project from Existing Sources`  
- Select the `source/did-ca-server` directory  
- The `build.gradle` file is automatically recognized, and necessary dependencies are automatically downloaded

#### 4.1.1.3. Gradle Build

- In the `Gradle` tab, execute `Tasks -> build -> build`

#### 4.1.1.4. Server Execution

- Execute `Tasks -> application -> bootRun`  
- When the message `"Started [ApplicationName] in [time] seconds"` appears in the console, it indicates normal operation

> ⚠️ Runs with the default `sample` profile. Operates for testing purposes without a database  
> For details, refer to [6. Profile Configuration and Usage](#6-profile-configuration-and-usage)

#### 4.1.1.5. Database Installation

- Uses PostgreSQL (Docker installation recommended)  
- For detailed installation methods, refer to [2.2. PostgreSQL Installation](#22-postgresql-installation)
<br/>

#### 4.1.1.6. Server Configuration
- The server needs to modify necessary configurations according to the deployment environment to ensure stable operation. For example, database connection information, port numbers, email integration information, and other components should be adjusted according to each environment.
- Server configuration files are located in the `src/main/resource/config` path.
- For detailed configuration methods, please refer to [5. Configuration Guide](#5-configuration-guide).

<br/>

## 4.2. Running with Console Commands

This section guides you through running the Open DID server using console commands. It explains the process of building the project using Gradle and running the server using the generated JAR file.
- When building with Gradle, the frontend (Admin Console) is automatically built together and included as static resources.

### 4.2.1. Gradle Build Commands

- Build the source using gradlew:
  ```shell
    # Navigate to the source folder of the cloned repository
    cd source/did-ca-server

    # Grant execution permission to Gradle Wrapper
    chmod 755 ./gradlew

    # Clean build the project (delete previous build files and build anew)
    ./gradlew clean build
  ```
  > Note: If frontend build is not needed (e.g., when testing only the backend or when frontend artifacts are already included), you can skip the frontend build by adding the following option: 
  > - `./gradlew clean build -DskipFrontendBuild=true`


- Navigate to the built folder and verify that the JAR file has been created:
    ```shell
      cd build/libs
      ls
    ```
- This command generates the `did-ca-server-2.0.0.jar` file.

<br/>

### 4.2.2. Server Operation Method
Run the server using the built JAR file:

```bash
java -jar did-ca-server-2.0.0.jar
```

- When the server runs normally, navigate to http://localhost:8094/swagger-ui/index.html in your browser to verify that the API documentation is properly displayed through Swagger UI.

> **Caution**
> - The CAS server is initially set to the sample profile.
> - When set to the sample profile, the server runs ignoring essential configurations (e.g., database). For details, refer to the [6. Profile Configuration and Usage](#6-profile-configuration-and-usage) chapter.

<br/>

### 4.2.3. Database Installation
Since the CAS server stores data necessary for operation in a database, a database must be installed to operate the server. Open DID servers use the PostgreSQL database. There are several ways to install PostgreSQL, but installation using Docker is the most convenient and easy. For PostgreSQL installation methods, please refer to the [2.2. PostgreSQL Installation](#22-postgresql-installation) chapter.

<br/>

### 4.2.4. Server Configuration Method
- The server needs to modify necessary configurations according to the deployment environment to ensure stable operation. For example, database connection information, port numbers, email integration information, and other components should be adjusted according to each environment.
- Server configuration files are located in the `src/main/resource/config` path.
- For detailed configuration methods, please refer to [5. Configuration Guide](#5-configuration-guide).

<br/>

## 4.3. Running with Docker
- For Docker image building, configuration, execution, and other processes, please refer to [7. Building and Running with Docker](#7-building-and-running-with-docker) below.

<br/>

# 5. Configuration Guide

This chapter guides you through each configuration value included in all server configuration files. Each configuration is an important element that controls the server's operation and environment, and proper configuration is necessary for stable server operation. Please refer to the item-specific descriptions and examples to apply configurations appropriate for each environment.

Settings with the 🔒 icon are typically fixed values or values that generally do not need to be modified.

## 5.1. application.yml

- Role: The application.yml file is a file that defines the basic settings of a Spring Boot application. Through this file, you can specify various environment variables such as application name, database settings, profile settings, etc., which significantly affect the application's operation.

- Location: `src/main/resources/`

### 5.1.1. Spring Basic Configuration
Spring's basic configuration defines the application name and profiles to activate, playing an important role in setting the server's operating environment.

* `spring.application.name`: 🔒
    - Specifies the application name.
    - Purpose: Mainly used to identify applications in log messages, monitoring tools, or Spring Cloud services
    - Example: `cas`

* `spring.profiles.active`:  
  - Defines the profile to activate. 
  - Purpose: Selects either sample or development environment to load configurations appropriate for that environment. For detailed information about profiles, refer to the [6. Profile Configuration and Usage](#6-profile-configuration-and-usage) chapter.
  - Supported profiles: sample, dev
  - Example: `sample`, `dev`

* `spring.profiles.group.dev`: 🔒
  - Defines individual profiles included in the `dev` profile group.
  - Purpose: Groups and manages settings to be used in the development environment.
  - Profile file naming convention: Configuration files corresponding to each profile use the names defined in the group as-is. For example, the auth profile is written as application-auth.yml, and the databases profile as application-databases.yml. The file names must use the names written under group.dev as-is.

* `spring.profiles.group.sample`: 🔒
  - Defines individual profiles included in the `sample` profile group.
  - Purpose: Groups and manages settings to be used in the development environment.
  - Profile file naming convention: Configuration files corresponding to each profile use the names defined in the group as-is. For example, the auth profile is written as application-auth.yml, and the databases profile as application-databases.yml. The file names must use the names written under group.dev as-is.

<br/>

### 5.1.2. Jackson Basic Configuration

Jackson is the JSON serialization/deserialization library used by default in Spring Boot. Through Jackson configuration, you can adjust the serialization method or format of JSON data, improving performance and efficiency during data transmission.

* `spring.jackson.default-property-inclusion`: 🔒 
    - Sets not to serialize when property values are null.
    - Example: non_null

* `spring.jackson.default-property-inclusion`: 🔒 
    - Sets not to generate errors when serializing empty objects.
    - Example: false

<br/>

### 5.1.3. Server Configuration 
Server configuration defines the port number where the application will receive requests.

* `server.port`:  
    - The port number on which the application will run. The default port for the CAS server is 8094.
    - Value: 8094

<br/>

### 5.1.4. TA(Trust Agent) Configuration 
Enter information for the TAS server that will communicate with the CAS server.

* `tas.url`:  
    - URL of the TAS (Trust Agent Service) server. 
    - Example: `http://127.0.0.1:8090/tas`

<br/>

## 5.2. database.yml
- Role: Defines everything from database connection information to Liquibase migration settings and JPA settings, determining how the server will manage and operate databases

- Location: `src/main/resources/`
  
### 5.2.1. Spring Liquibase Configuration 
Liquibase is a tool for managing database migration, helping to track and automatically apply database schema changes. This maintains database consistency in development and production environments.

* `spring.liquibase.change-log`: 🔒 
    - Specifies the location of the database change log file. This is the location of the log file used by Liquibase to track and apply database schema changes.
    - Example: `classpath:/db/changelog/master.xml`

* `spring.liquibase.enabled`: 🔒 
    - Sets whether to enable Liquibase. When set to true, Liquibase runs at application startup to perform database migration. The `sample` profile should be set to false as it does not integrate with databases.
    - Example: `true` [dev], `false` [sample]

* `spring.liquibase.fall-on-error`: 🔒
    - Controls the behavior when errors occur while Liquibase performs database migration. Set only in the `sample` profile.
    - Example: `false` [sample]

<br/>

### 5.2.2. Datasource Configuration
Datasource configuration defines basic information for the application to connect to the database. This includes information such as database driver, URL, username, and password.

* `spring.datasource.driver-class-name`: 🔒
    - Specifies the database driver class to use. Specifies the JDBC driver for connecting to the database.
    - Example: `org.postgresql.Driver`

* `spring.datasource.url`:  
    - Database connection URL. Specifies the location and name of the database the application will connect to. 
    - Example: `jdbc:postgresql://localhost:5432/cas_db`

* `spring.datasource.username`:  
    - Database access username.
    - Example: `cas`

* `spring.datasource.password`:  
    - Database access password.
    - Example: `caspassword`

<br/>

### 5.2.3. JPA Configuration
JPA configuration controls how the application interacts with the database and significantly affects performance and readability.

* `spring.jpa.open-in-view`: 🔒 
    - Sets whether to use the OSIV (Open Session In View) pattern. When set to true, maintains database connection for the entire HTTP request.
    - Example: `true`

* `spring.jpa.show-sql`: 🔒 
    - Sets whether to log SQL queries. When set to true, outputs executed SQL queries to logs. Useful for debugging during development.
    - Example: `true`

* `spring.jpa.hibernate.ddl-auto`: 🔒 
    - Sets Hibernate's DDL automatic generation mode. Specifies the database schema automatic generation strategy. When set to 'none', disables automatic generation.
    - Example: `none`

* `spring.jpa.hibernate.naming.physical-strategy`: 🔒 
    - Sets the database object naming strategy. Specifies the strategy for converting entity class names to database table names.
    - Example: `org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy`

* `spring.jpa.properties.hibernate.format_sql`: 🔒 
    - Sets whether to format SQL. When set to false, disables formatting of SQL queries output to logs.
    - Example: `false`

<br/>

## 5.3. application-logging.yml
- Role: Sets log groups and log levels. Through this configuration file, you can define log groups for specific packages or modules and specify log levels individually for each group.

- Location: `src/main/resources/`
  
### 5.3.1. Logging Configuration

- Log Groups: Under logging.group, you can group and manage desired packages. For example, include the org.omnione.did.base.util package in the util group, and define other packages as separate groups.

- Log Levels: Through logging.level settings, you can specify log levels for each group. You can set various log levels such as debug, info, warn, error to output logs at the desired level. For example, you can set debug level for groups like cas, aop to output debug information from those packages.

* `logging.level`: 
    - Sets log levels.
    - By setting the level to debug, you can see all log messages at DEBUG level and above (INFO, WARN, ERROR, FATAL) for specified packages.

Complete example:
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

- Role: Manages SpringDoc and Swagger UI settings in the application.

- Location: `src/main/resources/`

* `springdoc.swagger-ui.path`: 🔒
  - Defines the URL path to access Swagger UI.
  - Example: `/swagger-ui.html`

* `springdoc.swagger-ui.groups-order`: 🔒
  - Specifies the order for displaying API groups in Swagger UI.
  - Example: `ASC`

* `springdoc.swagger-ui.operations-sorter`: 🔒
  - Sorts API endpoints in Swagger UI by HTTP method.
  - Example: `method`

* `springdoc.swagger-ui.disable-swagger-default-url`: 🔒
  - Disables the default Swagger URL.
  - Example: `true`

* `springdoc.swagger-ui.display-request-duration`: 🔒
  - Sets whether to display request duration in Swagger UI.
  - Example: `true`

* `springdoc.api-docs.path`: 🔒
  - Defines the path where API documentation is served.
  - Example: `/api-docs`

* `springdoc.show-actuator`: 🔒
  - Sets whether to display Actuator endpoints in API documentation.
  - Example: `true`

* `springdoc.default-consumes-media-type`: 🔒
  - Sets the default media type for request bodies in API documentation.
  - Example: `application/json`

* `springdoc.default-produces-media-type`: 🔒
  - Sets the default media type for response bodies in API documentation.
  - Example: `application/json`

<br/>

## 5.5. application-wallet.yml

- Role: Configures wallet file information used by the server.

- Location: `src/main/resources/`

* `wallet.file-path`:  
    - Specifies the path to the wallet file. Specifies the location of the file where the file wallet is stored. This file may contain important information such as private keys. *Must be entered as an absolute path*
    - Example: `/path/to/your/cas.wallet`

* `wallet.password`:  
    - Password used for wallet access. Password used when accessing the wallet file. This is highly security-sensitive information.
    - Example: `your_secure_wallet_password`

## 5.6. application-cas.yml
This configuration file defines basic information for the CAS server, encryption settings, token expiration times, etc.

* `cas.did`: 
  - Sets the DID of the CAS server.
  - Example: did:omn:cas

* `cas.certificate-vc`: 
  - Sets the CAS server's certificate VC lookup API.
  - Format: {CAS domain}/cas/api/v1/certificate-vc
  - Example: http://127.0.0.1:8094/cas/api/v1/certificate-vc

<br/>

## 5.7. blockchain.properties
- Role: Configures blockchain server information to be integrated with the CA server. When you install the Hyperledger Besu network according to '5.3. Step 3: Blockchain Installation' in the [Open DID Installation Guide], private keys, certificates, and server connection information configuration files are automatically generated. In blockchain.properties, you configure the paths where these files are located and the network name entered during Hyperledger Besu installation.

- Location: `src/main/resources/properties`

### 5.7.1. Blockchain Integration Configuration 

#### EVM Network Configuration

- `evm.network.url:`:
  - EVM Network address. When running Besu locally on the same machine as the client, use this fixed value. (Default Port: 8545)
  - Example: http://localhost:8545

- `evm.chainId:`:
  - Chain ID identifier. Currently using a fixed value of 1337. (Default Value: 1337)
  - Example: 1337

- `evm.gas.limit:`:
  - Maximum gas limit allowed for Hyperledger Besu EVM transactions. Currently used as fixed value for Free Gas. (Default Value: 100000000)
  - Example: 100000000

- `evm.gas.price :`:
  - Gas price per unit. Currently used as fixed value 0 for Free Gas. (Default Value: 0)
  - Example: 0

- `evm.connection.timeout:`: 
  - Network connection timeout value (milliseconds). Currently using the recommended fixed value of 10000. (Default Value: 10000)
  - Example: 10000

#### EVM Contract Configuration

- `evm.connection.address:`: 
  - Address value of the OpenDID Contract returned when deploying Smart Contract with Hardhat. For detailed guide, refer to [DID Besu Contract].
  - Example: 0xa0E49611FB410c00f425E83A4240e1681c51DDf4

- `evm.connection.privateKey:`: 
  - k1 key used for API access control. Enter the key string defined in accounts inside hardhat.config.js (remove the 0x prefix) to enable API calls with Owner permissions (Default setting). For detailed guide, refer to [DID Besu Contract].
  - Example: 0x8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63

<br/>

# 6. Profile Configuration and Usage

## 6.1. Profile Overview (`sample`, `dev`)
The CAS server supports two profiles, `dev` and `sample`, to run in various environments.

Each profile is designed to apply configurations appropriate for the corresponding environment. By default, the CAS server is set to the `sample` profile, which is designed to run the server independently without integration with external services such as databases or blockchains. The `sample` profile is suitable for API call testing, allowing developers to quickly verify the basic operation of the application. This profile returns fixed response data for all API calls, making it useful in initial development environments.

Sample API calls are written as JUnit tests, so you can refer to them when writing tests.

On the other hand, the `dev` profile is designed to perform actual operations. This profile enables testing and verification with real data. When the `dev` profile is activated, it integrates with actual external services such as databases and blockchains, allowing you to test application behavior in real environments.

### 6.1.1. `sample` Profile
The `sample` profile is designed to run the server independently without integration with external services (DB, blockchain, etc.). This profile is suitable for API call testing and allows developers to quickly verify the basic operation of the application. It returns fixed response data for all API calls, making it useful for initial development stages or functional testing. Since no integration with external systems is required, it provides an environment where you can run and test the server standalone.
> Note: When using the sample profile, the Admin Console will not function.

### 6.1.2. `dev` Profile
The `dev` profile includes configurations suitable for development environments and is used on development servers. To use this profile, configuration for development environment databases and blockchain nodes is required.

## 6.2. Profile Configuration Method
This section explains how to change profiles for each operation method.

### 6.2.1. When Running Server Using IDE
- **Configuration File Selection:** Select the `application.yml` file in the `src/main/resources` path.
- **Profile Specification:** Add the `--spring.profiles.active={profile}` option in the IDE's run configuration (Run/Debug Configurations) to activate the desired profile.
- **Configuration Application:** The corresponding configuration file is applied according to the activated profile.

### 6.2.2. When Running Server Using Console Commands
- **Configuration File Selection:** Prepare profile-specific configuration files in the same directory as the built JAR file or in the path where configuration files are located.
- **Profile Specification:** Add the `--spring.profiles.active={profile}` option to the server startup command to activate the desired profile.
  
  ```bash
  java -jar build/libs/did-ca-server-2.0.0.jar --spring.profiles.active={profile}
  ```

- **Configuration Application:** The corresponding configuration file is applied according to the activated profile.

### 6.2.3. When Running Server Using Docker
- **Configuration File Selection:** When creating a Docker image, specify the configuration file path in the Dockerfile, or mount external configuration files to the Docker container.
- **Profile Specification:** Set the `SPRING_PROFILES_ACTIVE` environment variable in the Docker Compose file or Docker run command to specify the profile.
  
  ```yaml
  environment:
    - SPRING_PROFILES_ACTIVE={profile}
  ```

- **Configuration Application:** Configuration is applied according to the specified profile when running the Docker container.

You can flexibly change profile-specific configurations according to each method and easily apply configurations suitable for your project environment.

# 7. Running After Building with Docker

## 7.1. Docker Image Build Method (Based on `Dockerfile`)

### 7.1.1. Build Docker image
Build the Docker image with the following command:

```bash
cd {source_directory}
docker build -t did-ca-server -f did-ca-server/Dockerfile .
```

<br/>

## 7.2. Running with Docker Compose

### 7.2.1. Preparing Directories and Configuration Files

#### 1. Create docker-compose directory and config directory
```bash
mkdir -p {docker_compose_directory}/config
```

#### 2. Copy configuration files (yml) to config directory
```bash
cp {application_yml_directory}/* {docker_compose_directory}/config/
cp {blockchain_properties_path} {docker_compose_directory}/config/
```

#### 3. Modify blockchain.properties file
```yml
evm.network.url=http://host.docker.internal:8545
... (omitted)
```

> **host.docker.internal** is a special address that points to the host machine from within a Docker container.  
> Since localhost inside a container refers to the container itself, you must use host.docker.internal to access services (PostgreSQL, blockchain) running on the host.

#### 4. Modify application-database.yml file
```yml
spring:
 ... (omitted)
 datasource:
   driver-class-name: org.postgresql.Driver
   url: jdbc:postgresql://host.docker.internal:5430/ca
   username: omn
   password: omn
 ... (omitted)
```

### 7.2.2. Create `docker-compose.yml` file
You can easily manage multiple containers using the `docker-compose.yml` file.

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

> - In the example above, the `config_directory` is mounted to `/app/config` inside the container to share configuration files.
>   - Configuration files located in `config_directory` take priority over default configuration files.
>   - For detailed configuration instructions, please refer to [5. Configuration Guide](#5-configuration-guide).


### 7.2.3. Run Container
```bash
cd {docker_compose_directory}
docker-compose up -d
```

<br/>

# 8. Installing Docker PostgreSQL

This section explains how to install PostgreSQL using Docker. Through this method, you can easily install PostgreSQL and use it integrated with the server.

## 8.1. PostgreSQL Installation using Docker Compose

The following is how to install PostgreSQL using Docker Compose.

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

This Docker Compose file installs PostgreSQL version 16.4 and makes the following configurations:

- **container_name**: Sets the container name as `postgre-ca`.
- **volumes**: Mounts the `postgres_data_ca` volume to PostgreSQL's data directory (`/var/lib/postgresql/data`). This ensures data is permanently preserved.
- **ports**: Maps host port 5434 to container port 5432.
- **environment**: Sets PostgreSQL's username, password, and database name. Here, `${USER}` and `${PW}` can be set as environment variables.

## 8.2. PostgreSQL Container Execution

To run the PostgreSQL container using the above Docker Compose file, execute the following command in the terminal:

```bash
docker-compose up -d
```

This command runs the PostgreSQL container in the background. The PostgreSQL server runs according to the set environment variables, and the database is prepared. You can proceed with integration settings to use this database in your application.

<!-- References -->
[Open DID Installation Guide]: https://github.com/OmniOneID/did-release/blob/main/release-V2.0.0.0/OpenDID_Installation_Guide-V2.0.0.0.md
[DID Besu Contract]: https://github.com/OmniOneID/did-besu-contract
[Open DID Admin Console Guide]: ../../docs/admin/OpenDID_CAAdmin_Operation_Guide.md