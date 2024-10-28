# Open DID CAS Database Table Definition

- Date: 2024-09-04
- Version: v1.0.2

## Contents
- [1. Overview](#1-overview)
  - [1.1. ERD](#11-erd)
- [2. Table Definition](#2-table-definition)
  - [2.1. User PII](#21-user-pii)
  - [2.2. Certificate VC](#22-certificate-vc)

## 1. Overview

This document defines the structure of the database tables used in the CA server. It describes the field attributes, relationships, and data flow for each table, serving as essential reference material for system development and maintenance.

### 1.1 ERD

Access the [ERD](https://www.erdcloud.com/d/rSvd7yt6oFpuEaq7C) site to view the diagram, which visually represents the relationships between the tables in the CA server database, including key attributes, primary keys, and foreign key relationships.

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