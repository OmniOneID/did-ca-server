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

Open DID CAS Server Installation And Operation Guide
==

- Date: 2024-09-02
- Version: v1.0.0

Table of Contents
==

- [1. Introduction](#1-introduction)
  - [1.1. Overview](#11-overview)
  - [1.2. CAS Server Definition](#12-cas-server-definition)
  - [1.3. System Requirements](#13-system-requirements)
- [2. Prerequisites](#2-prerequisites)
  - [2.1. Git Installation](#21-git-installation)
  - [2.2. PostgreSQL Installation](#22-postgresql-installation)
- [3. Cloning Source Code from GitHub](#3-cloning-source-code-from-github)
  - [3.1. Source Code Cloning](#31-source-code-cloning)
  - [3.2. Directory Structure](#32-directory-structure)
- [4. Server Startup Methods](#4-server-startup-methods)
  - [4.1. Running with IntelliJ IDEA (Gradle Support)](#41-running-with-intellij-idea-gradle-support)
    - [4.1.1. IntelliJ IDEA Installation and Setup](#411-intellij-idea-installation-and-setup)
    - [4.1.2. Opening Project in IntelliJ](#412-opening-project-in-intellij)
    - [4.1.3. Gradle Build](#413-gradle-build)
    - [4.1.4. Server Startup](#414-server-startup)
    - [4.1.5. Database Installation](#415-database-installation)
    - [4.1.6. Server Configuration](#416-server-configuration)
  - [4.2. Running via Console Commands](#42-running-via-console-commands)
    - [4.2.1. Gradle Build Commands](#421-gradle-build-commands)
    - [4.2.2. Server Startup Method](#422-server-startup-method)
    - [4.2.3. Database Installation](#423-database-installation)
    - [4.2.4. Server Configuration Method](#424-server-configuration-method)
  - [4.3. Running with Docker](#43-running-with-docker)
- [5. Configuration Guide](#5-configuration-guide)
  - [5.1. application.yml](#51-applicationyml)
    - [5.1.1. Spring Basic Configuration](#511-spring-basic-configuration)
    - [5.1.2. Jackson Basic Configuration](#512-jackson-basic-configuration)
    - [5.1.3. Server Configuration](#513-server-configuration)
    - [5.1.5. TAS Configuration](#515-tas-configuration)
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
- [6. Profile Configuration and Usage](#6-profile-configuration-and-usage)
  - [6.1. Profile Overview (`sample`, `dev`)](#61-profile-overview-sample-dev)
    - [6.1.1. `sample` Profile](#611-sample-profile)
    - [6.1.2. `dev` Profile](#612-dev-profile)
  - [6.2. Profile Configuration Methods](#62-profile-configuration-methods)
    - [6.2.1. When Running Server Using IDE](#621-when-running-server-using-ide)
    - [6.2.2. When Running Server Using Console Commands](#622-when-running-server-using-console-commands)
    - [6.2.3. When Running Server Using Docker](#623-when-running-server-using-docker)
- [7. Building and Running with Docker](#7-building-and-running-with-docker)
  - [7.1. Docker Image Build Method (Based on `Dockerfile`)](#71-docker-image-build-method-based-on-dockerfile)
  - [7.2. Running Docker Image](#72-running-docker-image)
  - [7.3. Running with Docker Compose](#73-running-with-docker-compose)
    - [7.3.1. `docker-compose.yml` File Description](#731-docker-composeyml-file-description)
    - [7.3.2. Container Execution and Management](#732-container-execution-and-management)
    - [7.3.3. Server Configuration Method](#733-server-configuration-method)
- [8. Installing Docker PostgreSQL](#8-installing-docker-postgresql)
  - [8.1. PostgreSQL Installation Using Docker Compose](#81-postgresql-installation-using-docker-compose)
  - [8.2. Running PostgreSQL Container](#82-running-postgresql-container)
    

# 1. Introduction

## 1.1. Overview
This document provides a guide for installing and running the CAS (Certified App Service) server. It explains the installation process, configuration methods, and startup procedures of the CAS server step by step, guiding users to efficiently install and operate it.

For the complete installation guide of OpenDID, please refer to the [Open DID Installation Guide].

<br/>

## 1.2. CAS Server Definition

The CAS server is an authorized app service server, playing the role of certifying authorized apps for use within Open DID.

<br/>

## 1.3. System Requirements
- **Java 17** or higher
- **Gradle 7.0** or higher
- **Docker** and **Docker Compose** (when using Docker)
- Minimum **2GB RAM** and **10GB disk space**

<br/>

# 2. Prerequisites

This chapter guides you through the necessary preparations before installing the components of the Open DID project.

## 2.1. Git Installation

`Git` is a distributed version control system that tracks changes in source code and supports collaboration among multiple developers. Git is essential for managing and versioning the source code of the Open DID project.

After successful installation, you can verify the Git version using the following command:

```bash
git --version
```

> **Reference Links**
> - [Git Installation Guide](https://docs.github.com/en/repositories/creating-and-managing-repositories/cloning-a-repository)

<br/>

## 2.2. PostgreSQL Installation
To run the CAS server, a database installation is required, and Open DID uses PostgreSQL.

> **Reference Links**
- [PostgreSQL Installation Guide Document](https://www.postgresql.org/download/)
- [8. Installing Docker PostgreSQL](#8-installing-docker-postgresql)

<br/>


# 3. Cloning Source Code from GitHub

## 3.1. Source Code Cloning

The `git clone` command is used to clone source code from a remote repository hosted on GitHub to your local computer. This command allows you to work with the entire project's source code and related files locally. After cloning, you can proceed with necessary work within the repository, and changes can be pushed back to the remote repository.

Open a terminal and run the following commands to copy the CAS server repository to your local computer:
```bash
# Clone the repository from Git storage
git clone https://github.com/OmniOneID/did-ca-server.git

# Move to the cloned repository
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
            └── did-sdk-common-1.0.0.jar
            └── did-blockchain-sdk-server-1.0.0.jar
            └── did-core-sdk-server-1.0.0..jar
            └── did-crypto-sdk-server-1.0.0.jar
            └── did-datamodel-server-1.0.0.jar
            └── did-wallet-sdk-server-1.0.0.jar
        ├── sample
        └── src
        └── build.gradle
        └── README.md
```

| Name                    | Description                                         |
| ----------------------- | --------------------------------------------------- |
| CHANGELOG.md            | Version changes of the project                      |
| CODE_OF_CONDUCT.md      | Code of conduct for contributors                    |
| CONTRIBUTING.md         | Contribution guidelines and procedures              |
| LICENSE                 | License                                             |
| dependencies-license.md | License information for project dependencies        |
| MAINTAINERS.md          | Guidelines for project maintainers                  |
| RELEASE-PROCESS.md      | Procedure for releasing new versions                |
| SECURITY.md             | Security policy and vulnerability reporting method  |
| docs                    | Documentation                                       |
| ┖ api                   | API guide documents                                 |
| ┖ errorCode             | Error codes and troubleshooting guide               |
| ┖ installation          | Installation and setup guide                        |
| ┖ db                    | Database ERD, table specifications                  |
| source                  | Source code                                         |
| ┖ did-ca-server         | CAS server source code and build files              |
| ┖ gradle                | Gradle build settings and scripts                   |
| ┖ libs                  | External libraries and dependencies                 |
| ┖ sample                | Sample files                                        |
| ┖ src                   | Main source code directory                          |
| ┖ build.gradle          | Gradle build configuration file                     |
| ┖ README.md             | Source code overview and guide                      |

<br/>


# 4. Server Startup Methods
This chapter guides you through three methods to start the server.

The project source is located under the `source` directory, and you need to load and configure the source from this directory according to each startup method.

1. **Using an IDE**: You can open the project in an Integrated Development Environment (IDE), set up the run configuration, and directly run the server. This method is useful for quickly testing code changes during development.

2. **Using console commands after building**: You can build the project and then run the server by executing the generated JAR file with a console command (`java -jar`). This method is mainly used when deploying the server or running it in a production environment.

3. **Building with Docker**: You can build the server as a Docker image and run it as a Docker container. This method has the advantage of maintaining consistency across environments and facilitates deployment and scaling.
   
## 4.1. Running with IntelliJ IDEA (Gradle Support)

IntelliJ IDEA is a widely used Integrated Development Environment (IDE) for Java development, supporting build tools like Gradle, which makes project setup and dependency management very convenient. Open DID's server is built using Gradle, so you can easily set up the project and run the server in IntelliJ IDEA.

### 4.1.1. IntelliJ IDEA Installation and Setup
1. Install IntelliJ. (Refer to the link below for installation method)

> **Reference Links**
> - [IntelliJ IDEA Download](https://www.jetbrains.com/idea/download/)

### 4.1.2. Opening Project in IntelliJ
- Run IntelliJ and select `File -> New -> Project from Existing Sources`. When the file selection window appears, select the 'source/did-ca-server' folder from the repository cloned in [3.1. Source Code Cloning](#31-source-code-cloning).
- When you open the project, the build.gradle file is automatically recognized.
- Gradle automatically downloads the necessary dependency files, wait for this process to complete.

### 4.1.3. Gradle Build
- Run `Tasks -> build -> build` in the `Gradle` tab of IntelliJ IDEA. 
- When the build is successfully completed, the project is ready to run.

### 4.1.4. Server Startup
- Select and run Tasks -> application -> bootRun in the Gradle tab of IntelliJ IDEA.
- Gradle automatically builds and runs the server.
- Check the console log for the message "Started [ApplicationName] in [time] seconds" to confirm that the server has started normally.
- If the server starts normally, navigate to http://localhost:8094/swagger-ui/index.html in your browser to verify that the API documentation is properly displayed through Swagger UI.

> **Note**
> - The CAS server is initially set to the sample profile.
> - When set to the sample profile, the server runs ignoring essential settings (e.g., database). For more details, please refer to [6. Profile Configuration and Usage](#6-profile-configuration-and-usage) chapter.


### 4.1.5. Database Installation
The CAS server stores data necessary for operation in a database, so a database must be installed to operate the server. Open DID's server uses PostgreSQL database. There are several ways to install PostgreSQL server, but installation using Docker is the easiest and most convenient. Please refer to [2.2. PostgreSQL Installation](#22-postgresql-installation) chapter for PostgreSQL installation methods.

<br/>

### 4.1.6. Server Configuration
- The server needs to be configured according to the deployment environment, ensuring stable operation. For example, database connection information, port number, email integration information, and other components should be adjusted to fit each environment.
- The server's configuration files are located in the `src/main/resource/config` path.
- For detailed configuration methods, please refer to [5. Configuration Guide](#5-configuration-guide).

<br/>

## 4.2. Running via Console Commands

This section guides you on how to run the Open DID server using console commands. It explains the process of building the project using Gradle and running the server using the generated JAR file.

### 4.2.1. Gradle Build Commands

- Use gradlew to build the source:
  ```shell
    # Move to the source folder of the cloned repository
    cd source/did-ca-server

    # Grant execution permission to Gradle Wrapper
    chmod 755 ./gradlew

    # Clean build the project (delete previous build files and build anew)
    ./gradlew clean build
  ```
  
  > Note
  > - gradlew is short for Gradle Wrapper, a script used to run Gradle in the project. Even if Gradle is not installed locally, it automatically downloads and runs the version of Gradle specified in the project. This allows developers to build the project in the same environment regardless of whether Gradle is installed or not.

- Move to the built folder and check that the JAR file has been created.
    ```shell
      cd build/libs
      ls
    ```
- This command creates the `did-ca-server-1.0.0.jar` file.

<br/>

### 4.2.2. Server Startup Method
Use the built JAR file to start the server:

```bash
java -jar did-ca-server-1.0.0.jar
```

- If the server starts normally, navigate to http://localhost:8094/swagger-ui/index.html in your browser to verify that the API documentation is properly displayed through Swagger UI.

> **Note**
> - The CAS server is initially set to the sample profile.
> - When set to the sample profile, the server runs ignoring essential settings (e.g., database). For more details, please refer to [6. Profile Configuration and Usage](#6-profile-configuration-and-usage) chapter.

<br/>

### 4.2.3. Database Installation
The CAS server stores data necessary for operation in a database, so a database must be installed to operate the server. Open DID's server uses PostgreSQL database. There are several ways to install PostgreSQL server, but installation using Docker is the easiest and most convenient. Please refer to [2.2. PostgreSQL Installation](#22-postgresql-installation) chapter for PostgreSQL installation methods.

<br/>

### 4.2.4. Server Configuration Method
- The server needs to be configured according to the deployment environment, ensuring stable operation. For example, database connection information, port number, email integration information, and other components should be adjusted to fit each environment.
- The server's configuration files are located in the `src/main/resource/config` path.
- For detailed configuration methods, please refer to [5. Configuration Guide](#5-configuration-guide).

<br/>

## 4.3. Running with Docker
- For Docker image building, configuration, execution, etc., please refer to [7. Building and Running with Docker](#7-building-and-running-with-docker) below.

<br/>


# 5. Configuration Guide

This chapter provides guidance on each configuration value included in all server configuration files. Each setting is an important element that controls the server's behavior and environment, and appropriate settings are necessary for stable server operation. Please refer to the item-by-item explanations and examples to apply settings suitable for each environment.

Please note that settings with the 🔒 icon are either fixed values by default or generally do not need to be modified.

## 5.1. application.yml

- Role: The application.yml file defines the basic settings for the Spring Boot application. Through this file, you can specify various environment variables such as the application name, database settings, profile settings, etc., which have a significant impact on how the application operates.

- Location: `src/main/resources/`

### 5.1.1. Spring Basic Configuration
Spring's basic configuration defines the application name and profiles to activate, playing an important role in setting up the server's operating environment.

* `spring.application.name`: 🔒
    - Specifies the name of the application.
    - Usage: Mainly used to identify the application in log messages, monitoring tools, or Spring Cloud services.
    - Example: `cas`

* `spring.profiles.active`:  
  - Defines the profile to activate. 
  - Usage: Selects either sample or development environment to load the appropriate settings for that environment. For more details about profiles, please refer to [6. Profile Configuration and Usage](#6-profile-configuration-and-usage) chapter.
  - Supported profiles: sample, dev
  - Example: `sample`, `dev`

* `spring.profiles.group.dev`: 🔒
  - Defines individual profiles included in the `dev` profile group.
  - Usage: Groups settings to be used in the development environment.
  - Profile file naming convention: Configuration files for each profile use the name defined in the group as is. For example, the auth profile is written as application-auth.yml, and the databases profile as application-databases.yml. The filename should be used exactly as written under group.dev.

* `spring.profiles.group.sample`: 🔒
  - Defines individual profiles included in the `sample` profile group.
  - Usage: Groups settings to be used in the development environment.
  - Profile file naming convention: Configuration files for each profile use the name defined in the group as is. For example, the auth profile is written as application-auth.yml, and the databases profile as application-databases.yml. The filename should be used exactly as written under group.dev.

<br/>

### 5.1.2. Jackson Basic Configuration

Jackson is the JSON serialization/deserialization library used by default in Spring Boot. Through Jackson's settings, you can adjust the serialization method or format of JSON data, improving performance and efficiency in data transmission.

* `spring.jackson.default-property-inclusion`: 🔒 
    - Sets not to serialize when property values are null.
    - Example: non_null

* `spring.jackson.default-property-inclusion`: 🔒 
    - Sets not to cause errors when serializing empty objects.
    - Example: false

<br/>

### 5.1.3. Server Configuration 
Server configuration defines the port number on which the application will receive requests.

* `server.port`:  
    - The port number on which the application will run. The default port for the CAS server is 8094.
    - Value: 8094

<br/>

### 5.1.5. TAS Configuration 
Enter the information for the TAS server that will communicate with the CAS server.

* `tas.url`:  
    - The URL of the TAS (Trust Agent Service) server. 
    - Example: `http://127.0.0.1:8090/tas`

<br/>

## 5.2. database.yml
- Role: Defines how to manage and operate the database on the server, from database connection information to migration settings using Liquibase, and JPA settings.

- Location: `src/main/resources/`
  
### 5.2.1. Spring Liquibase Configuration 
Liquibase is a tool for managing database migrations, helping to track and automatically apply changes to database schemas. This allows maintaining database consistency in development and production environments.

* `spring.liquibase.change-log`: 🔒 
    - Specifies the location of the database change log file. This is the location of the log file that Liquibase uses to track and apply database schema changes.
    - Example: `classpath:/db/changelog/master.xml`

* `spring.liquibase.enabled`: 🔒 
    - Sets whether to activate Liquibase. When set to true, Liquibase runs when the application starts and performs database migration. The `sample` profile should be set to false as it does not connect to the database.
    - Example: `true` [dev], `false` [sample]

* `spring.liquibase.fall-on-error`: 🔒
    - Controls behavior when an error occurs during Liquibase database migration. Only set in the `sample` profile.
    - Example: `false` [sample]

<br/>

### 5.2.2. Datasource Configuration
Datasource configuration defines the basic information for the application to connect to the database. This includes information such as database driver, URL, username, and password.

* `spring.datasource.driver-class-name`: 🔒
    - Specifies the database driver class to use. Specifies the JDBC driver for connecting to the database.
    - Example: `org.postgresql.Driver`

* `spring.datasource.url`:  
    - The database connection URL. Specifies the location and name of the database the application will connect to. 
    - Example: `jdbc:postgresql://localhost:5432/cas_db`

* `spring.datasource.username`:  
    - The username for database access.
    - Example: `cas`

* `spring.datasource.password`:  
    - The password for database access.
    - Example: `caspassword`

<br/>

### 5.2.3. JPA Configuration
JPA configuration controls how the application interacts with the database and has a significant impact on performance and readability.

* `spring.jpa.open-in-view`: 🔒 
    - Sets whether to use the OSIV (Open Session In View) pattern. When set to true, it maintains the database connection for the entire HTTP request.
    - Example: `true`

* `spring.jpa.show-sql`: 🔒 
    - Sets whether to log SQL queries. When set to true, it outputs executed SQL queries to the log. Useful for debugging during development.
    - Example: `true`

* `spring.jpa.hibernate.ddl-auto`: 🔒 
    - Sets Hibernate's DDL auto-generation mode. Specifies the strategy for automatic database schema generation. Setting to 'none' disables auto-generation.
    - Example: `none`

* `spring.jpa.hibernate.naming.physical-strategy`: 🔒 
    - Sets the database object naming strategy. Specifies the strategy for converting entity class names to database table names.
    - Example: `org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy`

* `spring.jpa.properties.hibernate.format_sql`: 🔒 
    - Sets whether to format SQL. When set to false, it disables formatting of SQL queries output to the log.
    - Example: `false`

<br/>

## 5.3. application-logging.yml
- Role: Sets log groups and log levels. Through this configuration file, you can define log groups for specific packages or modules and individually specify log levels for each group.

- Location: `src/main/resources/`
  
### 5.3.1. Logging Configuration

- Log groups: You can group and manage desired packages under logging.group. For example, you can include the org.omnione.did.base.util package in the util group and define other packages as separate groups.

- Log levels: Through the logging.level setting, you can specify log levels for each group. You can set various log levels such as debug, info, warn, error to output logs at the desired level. For example, you can set the debug level for groups like cas, aop to output debug information from those packages.

* `logging.level`: 
    - Sets the log level.
    - By setting the level to debug, you can see all log messages at DEBUG level and above (INFO, WARN, ERROR, FATAL) for the specified packages.

Full example:
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
  - Specifies the order to display API groups in Swagger UI.
  - Example: `ASC`

* `springdoc.swagger-ui.operations-sorter`: 🔒
  - Sorts API endpoints in Swagger UI based on HTTP methods.
  - Example: `method`

* `springdoc.swagger-ui.disable-swagger-default-url`: 🔒
  - Disables the default Swagger URL.
  - Example: `true`

* `springdoc.swagger-ui.display-request-duration`: 🔒
  - Sets whether to display request time in Swagger UI.
  - Example: `true`

* `springdoc.api-docs.path`: 🔒
  - Defines the path where API documentation is provided.
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
    - Specifies the path of the wallet file. Specifies the location of the file storing the file wallet. This file may contain important information such as private keys. *Must be entered as an absolute path*
    - Example: `/path/to/your/cas.wallet`

* `wallet.password`:  
    - The password used to access the wallet. This is the password used when accessing the wallet file. It requires high security.
    - Example: `your_secure_wallet_password`

## 5.6. application-cas.yml
This configuration file defines the basic information of the CAS server, encryption settings, token expiration time, etc.

* `cas.did`: 
  - Sets the DID of the CAS server.
  - Example: did:omn:cas

* `cas.certificate-vc`: 
  - Sets the CAS server's membership certificate VC lookup API. 
  - Format: {CAS domain}/cas/api/v1/certificate-vc
  - Example: http://127.0.0.1:8094/cas/api/v1/certificate-vc

<br/>

## 5.7. blockchain.properties
- Role: Configures the blockchain server information that the CAS server will connect to. Following '5.1.1. Installing Hyperledger Fabric Test Network' in the [Open DID Installation Guide], private key, certificate, and server connection information configuration files are automatically generated when installing the Hyperledger Fabric test network. In blockchain.properties, you set the path where these files are located and the network name entered during the Hyperledger Fabric test network installation. Also, set the chaincode name of Open DID deployed in '5.1.2. Deploying Open DID Chaincode'.

- Location: `src/main/resources/properties`

### 5.7.1. Blockchain Integration Configuration 

* `fabric.configFilePath:`: 
  - Sets the path where the Hyperledger Fabric connection information file is located. This file is automatically generated when installing the Hyperledger Fabric test network, and the default filename is 'connection-org1.json'.
  - Example: {yourpath}/connection-org1.json

* `fabric.privateKeyFilePath:`: 
  - Sets the path of the private key file used by the Hyperledger Fabric client for transaction signing and authentication on the network. This file is automatically generated when installing the Hyperledger Fabric test network.
  - Example: {yourpath}/{private key filename}

* `fabric.certificateFilePath:`: 
  - Sets the path where the Hyperledger Fabric client certificate is located. This file is automatically generated when installing the Hyperledger Fabric test network, and the default filename is 'cert.pem'.
  - Example: /{yourpath}/cert.pem

* `fabric.mychannel:`: 
  - The name of the private network (channel) used in Hyperledger Fabric. You should set the channel name entered during the Hyperledger Fabric test network installation.
  - Example: mychannel

* `fabric.chaincodeName:`: 🔒
  - The chaincode name of Open DID used in Hyperledger Fabric. This value is fixed as 'opendid'.
  - Example: opendid

<br/>


# 6. Profile Configuration and Usage

## 6.1. Profile Overview (`sample`, `dev`)
The CAS server supports two profiles, `dev` and `sample`, to run in various environments.

Each profile is designed to apply settings appropriate to its environment. By default, the CAS server is set to the `sample` profile, which is designed to run the server independently without connecting to external services such as databases or blockchain. The `sample` profile is suitable for API call testing, allowing developers to quickly check the basic behavior of the application.

This profile returns fixed response data for all API calls, making it useful in initial development environments.

Sample API calls are written as JUnit tests, which can be referenced when writing tests.

On the other hand, the `dev` profile is designed to perform actual operations. Using this profile allows testing and validation with real data. When the `dev` profile is activated, it connects to actual external services such as databases and blockchain, allowing you to test the application's behavior in a real environment.

### 6.1.1. `sample` Profile
The `sample` profile is designed to run the server independently without connecting to external services (DB, blockchain, etc.). This profile is suitable for API call testing and allows developers to quickly check the basic behavior of the application. It returns fixed response data for all API calls, making it useful in initial development stages or for feature testing. Since it doesn't require any connection with external systems, it provides an environment where you can run and test the server independently.

### 6.1.2. `dev` Profile
The `dev` profile includes settings suitable for the development environment and is used on development servers. To use this profile, you need to configure the development environment's database and blockchain node settings.

## 6.2. Profile Configuration Methods
This section explains how to change profiles for each startup method.

### 6.2.1. When Running Server Using IDE
- **Select configuration file:** Select the `application.yml` file in the `src/main/resources` path.
- **Specify profile:** Add the `--spring.profiles.active={profile}` option in the IDE's run configuration (Run/Debug Configurations) to activate the desired profile.
- **Apply settings:** The corresponding configuration file is applied according to the activated profile.

### 6.2.2. When Running Server Using Console Commands
- **Select configuration file:** Prepare profile-specific configuration files in the same directory as the built JAR file or in the path where configuration files are located.
- **Specify profile:** Add the `--spring.profiles.active={profile}` option to the server startup command to activate the desired profile.
  
  ```bash
  java -jar build/libs/did-ca-server-1.0.0.jar --spring.profiles.active={profile}
  ```

- **Apply settings:** The corresponding configuration file is applied according to the activated profile.

### 6.2.3. When Running Server Using Docker
- **Select configuration file:** When creating a Docker image, specify the configuration file path in the Dockerfile or mount external configuration files to the Docker container.
- **Specify profile:** Set the `SPRING_PROFILES_ACTIVE` environment variable in the Docker Compose file or Docker run command to specify the profile.
  
  ```yaml
  environment:
    - SPRING_PROFILES_ACTIVE={profile}
  ```

- **Apply settings:** Settings are applied according to the profile specified when running the Docker container.

You can flexibly change and use profile-specific settings according to each method, and easily apply settings suitable for your project environment.

# 7. Building and Running with Docker

## 7.1. Docker Image Build Method (Based on `Dockerfile`)
Build the Docker image with the following command:

```bash
docker build -t did-ca-server .
```

## 7.2. Running Docker Image
Run the built image:

```bash
docker run -d -p 8094:8094 did-ca-server
```

## 7.3. Running with Docker Compose

### 7.3.1. `docker-compose.yml` File Description
You can easily manage multiple containers using the `docker-compose.yml` file.

```yaml
version: '3'
services:
  app:
    image: did-ca-server
    ports:
      - "8094:8094"
    volumes:
      - ${your-config-dir}:/app/config
    environment:
      - SPRING_PROFILES_ACTIVE=local
```

### 7.3.2. Container Execution and Management
Run containers using Docker Compose with the following command:

```bash
docker-compose up -d
```

### 7.3.3. Server Configuration Method
In the example above, the `${your-config-dir}` directory is mounted to `/app/config` inside the container to share configuration files.
- If additional configuration is needed, you can add separate property files to the mounted folder to change settings. 
  - For example, add an `application.yml` file to `${your-config-dir}` and write the settings you want to change in this file. 
  - The `application.yml` file located in `${your-config-dir}` takes precedence over the default configuration file.
- For detailed configuration methods, please refer to [5. Configuration Guide](#5-configuration-guide).

# 8. Installing Docker PostgreSQL

This section explains how to install PostgreSQL using Docker. Through this method, you can easily install PostgreSQL and use it in conjunction with the server.

## 8.1. PostgreSQL Installation Using Docker Compose

The following is how to install PostgreSQL using Docker Compose:

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

This Docker Compose file installs PostgreSQL version 16.4 and makes the following settings:

- **container_name**: Sets the container name to `postgre-ca`.
- **volumes**: Mounts the `postgres_data_ca` volume to PostgreSQL's data directory (`/var/lib/postgresql/data`). This ensures data is permanently preserved.
- **ports**: Maps the host's port 5434 to the container's port 5432.
- **environment**: Sets PostgreSQL's username, password, and database name. Here, `${USER}` and `${PW}` can be set as environment variables.

## 8.2. Running PostgreSQL Container

To run the PostgreSQL container using the above Docker Compose file, execute the following command in the terminal:

```bash
docker-compose up -d
```

This command runs the PostgreSQL container in the background. The PostgreSQL server runs according to the set environment variables, and the database is prepared. You can proceed with the integration settings to use this database in your application.

<!-- References -->
[Open DID Installation Guide]: https://github.com/OmniOneID/did-release/blob/main/release-V1.0.0.0/OepnDID_Installation_Guide-V1.0.0.0.md