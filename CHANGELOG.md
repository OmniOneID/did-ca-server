# Changelog

## [Unreleased] (v3.0.0)

### 🚀 New Features
- User registration without a password or OTP: the app creates the user ID and calls `signup`
- Stores the certificate VC URL published to the List Provider (TA enrollment response)
- LSS API key authentication (`lss` profile)
- Admin Console
    - Server Configuration (LSS API key)
    - Administrator password policy, admin ID change, and session management
    - Certificate VC view on the server management page
    - Search and refresh on list pages
    - API call logs and audit logs

### 🔧 Changes
- Removed password, OTP, and OP token based user authentication
- Replaced local utilities with did-common-sdk-server 3.0.0
- Upgraded Node.js to 22.23.4 for the admin console build
- Updated the Dockerfile Java image to eclipse-temurin 21 (jammy)
- Server version 3.0.0 (`did-ca-server-3.0.0.jar`)
- Expanded the admin guide with item tables and example screens aligned with the admin console

### 🐛 Bug Fixes
- Fixed a typo in the SCRVCFA01006 error message

## [Released] (v2.0.0)

## v2.0.0 (2025-05-30)
### 🚀 New Features
- Support for Besu blockchain

## [Unreleased] (v1.0.1)

### 🚀 New Features
- Admin Console
    - CA Management
    - User Management
    - Admin Management
- Removed hardcoded logic and sample data from CA Server through Admin Console
  - Removed hardcoded YAML configuration values
  - Removed sample data previously embedded in Java source code
  
## v1.0.0 (2024-10-18)

### 🚀 New Features
- CA Server
    - Generate Wallet Token Data
    - Generate Attested App Info
