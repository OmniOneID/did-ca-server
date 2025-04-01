# Open DID CAS Database Table Definition

- Date: 2025-03-31
- Version: v1.0.1 (dev)

## Contents
- [1. Overview](#1-overview)
  - [1.1. ERD](#11-erd)
- [2. Table Definition](#2-table-definition)
  - [2.1. User PII](#21-user-pii)
  - [2.2. Certificate VC](#22-certificate-vc)
  - [2.3. CAS](#23-cas)
  - [2.4. Admin](#24-admin)

## 1. Overview

This document defines the structure of the database tables used in the CA server. It describes the field attributes, relationships, and data flow for each table, serving as essential reference material for system development and maintenance.

### 1.1 ERD

Access the [ERD](https://www.erdcloud.com/d/5tR4y8YGnkSzeheBN) site to view the diagram, which visually represents the relationships between the tables in the CA server database, including key attributes, primary keys, and foreign key relationships.

## 2. Table Definition

### 2.1. User PII

This table stores user PII (Personally Identifiable Information) data.

| Key  | Column Name        | Data Type  | Length | Nullable | Default  | Description                       |
|------|--------------------|------------|--------|----------|----------|-----------------------------------|
| PK   | id                 | BIGINT     |        | NO       | N/A      | id                                |
|      | user_id            | VARCHAR    | 50     | NO       | N/A      | user id                           |
|      | pii                | VARCHAR    | 64     | NO       | N/A      | pii                               |
|      | created_at         | TIMESTAMP  |        | NO       | now()    | created date                      |
|      | updated_at         | TIMESTAMP  |        | YES      | N/A      | updated date                      |

### 2.2. Certificate VC

This table stores Certificate VC (Verifiable Credential) information.

| Key  | Column Name        | Data Type  | Length | Nullable | Default  | Description                       |
|------|--------------------|------------|--------|----------|----------|-----------------------------------|
| PK   | id                 | BIGINT     |        | NO       | N/A      | id                                |
|      | vc                 | TEXT       |        | NO       | N/A      | vc                                |
|      | created_at         | TIMESTAMP  |        | NO       | now()    | created date                      |
|      | updated_at         | TIMESTAMP  |        | YES      | N/A      | updated date                      |

### 2.3. CAS

This table stores information about CA server.

| Key | Column Name     | Data Type | Length | Nullable | Default | Description             |
| --- | --------------- | --------- | ------ | -------- | ------- | ----------------------- |
| PK  | id              | BIGINT    |        | NO       | N/A     | id                      |
|     | did             | VARCHAR   | 200    | NO       | N/A     | did                     |
|     | name            | VARCHAR   | 200    | NO       | N/A     | name                    |
|     | status          | VARCHAR   | 50     | NO       | N/A     | cas status              |
|     | server_url      | VARCHAR   | 2000   | YES      | N/A     | URL of the CAS server   |
|     | certificate_url | VARCHAR   | 2000   | YES      | N/A     | URL for CAS certificate |
|     | created_at      | TIMESTAMP |        | NO       | now()   | created date            |
|     | updated_at      | TIMESTAMP |        | YES      | N/A     | updated date            |

### 2.4. Admin

This table stores information about administrator accounts.

| Key | Column Name            | Data Type | Length | Nullable | Default | Description                        |
| --- | ---------------------- | --------- | ------ | -------- | ------- | ---------------------------------- |
| PK  | id                     | BIGINT    |        | NO       | N/A     | id                                 |
|     | login_id               | VARCHAR   | 50     | NO       | N/A     | login ID                           |
|     | login_password         | VARCHAR   | 64     | NO       | N/A     | hashed login password              |
|     | name                   | VARCHAR   | 200    | YES      | N/A     | administrator name                 |
|     | email                  | VARCHAR   | 100    | YES      | N/A     | email address                      |
|     | email_verified         | BOOLEAN   |        | YES      | false   | whether email is verified          |
|     | require_password_reset | BOOLEAN   |        | NO       | true    | whether password reset is required |
|     | role                   | VARCHAR   | 50     | NO       | N/A     | administrator role                 |
|     | created_by             | VARCHAR   | 50     | NO       | N/A     | creator's login ID                 |
|     | created_at             | TIMESTAMP |        | NO       | now()   | created date                       |
|     | updated_at             | TIMESTAMP |        | YES      | N/A     | updated date                       |
