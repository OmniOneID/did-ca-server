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

Table of Contents
==

- [1. Introduction](#1-introduction)
  - [1.1. Overview](#11-overview)
  - [1.2. Admin Console Definition](#12-admin-console-definition)
- [2. Basic Manual](#2-basic-manual)
  - [2.1. Login](#21-login)
  - [2.2. Main Screen Configuration](#22-main-screen-configuration)
  - [2.3. Menu Structure](#23-menu-structure)
  - [2.4. Password Change Management](#24-password-change-management)
- [3. Detailed Function Manual](#3-detailed-function-manual)
  - [3.1. CA Management](#31-ca-management)
    - [3.1.1 DID Document Lookup](#311-did-document-lookup)
  - [3.2. User Management](#32-user-management)
    - [3.2.1 User List Lookup](#321-user-list-lookup)
    - [3.2.2 User Detail Information Lookup](#322-user-detail-information-lookup)
  - [3.3. Admin Management](#33-admin-management)
    - [3.3.1 Admin List Lookup](#331-admin-list-lookup)
    - [3.3.2 Admin Registration](#332-admin-registration)

# 1. Introduction

## 1.1. Overview

This document provides a guide on how to use the Open DID CA Admin Console. It explains the functions and usage methods step by step to help administrators efficiently manage and operate the CA(Certificate Application) system.

For a complete installation guide for OpenDID, please refer to the [Open DID Installation Guide].

## 1.2. Admin Console Definition

The CA Admin Console is a web-based management tool for managing the core functions of the CA(Certificate Application) server in the Open DID system, including certificate issuance, user management, and administrator authority control. Through this console, administrators can monitor the overall status of the system and perform necessary settings and operations.

The CA Admin Console provides the following main functions:

- CA basic information management
- User information lookup
- Administrator account management

<br/>

# 2. Basic Manual

## 2.1. Login

Follow these steps to access the Admin Console:

1. Open a web browser and navigate to the CA Admin Console URL.

   ```
   http://<ca_domain>:<port>
   ```

2. Enter the administrator account email and password on the login screen.
   - Default administrator account: <admin@opendid.omnione.net>
   - Initial password: password (password change required after first login)

3. Click the 'Login' button.

> **Note**: For security reasons, a password change is required after the first login.

## 2.2. Main Screen Configuration

The main screen displayed after login consists of the following elements:

<img src="./images/main_screen.jpg" width="800"/>

| Number | Area | Description |
|--------|------|-------------|
| 1 | Settings Button | Click the 'SETTING' button to navigate to the screen where you can change the password for the currently logged-in account. |
| 2 | Content Header | Displays the title of the currently selected menu. The page name is shown according to each menu. |
| 3 | Sidebar Menu | Lists menus for accessing main functions including CA Management. |
| 4 | User Information Area | Displays the email of the currently logged-in administrator (<admin@opendid.omnione.net>) and the logout button. |

<br/>

## 2.3. Menu Structure

The Admin Console menu is structured as follows:

<img src="./images/menu_structure.jpg" width="800"/>

| Number | Function Name | Function Description |
|--------|---------------|----------------------|
| 1 | **CA Management** | A menu for managing the service's CA(Certificate Application). CA-related settings and management tasks can be performed here. |
| 2 | **User Management** | A menu for viewing user information registered in the system. You can check users' personally identifiable information (PII). |
| 3 | **Admin Management** | A menu for managing administrator-related settings. You can adjust administrator accounts and permissions. |

Detailed functions for each menu are explained in the [3. Detailed Function Manual](#3-detailed-function-manual) section.

## 2.4. Password Change Management

User password changes can be performed through the following steps:

1. Click the 'SETTING' button in the header area.
2. Select 'Change Password' from the settings menu.
3. On the password change screen:
   - Enter current password
   - Enter new password
   - Enter new password confirmation
4. Click the 'Save' button to apply the changes.

> **Note**: Passwords must be 8 or more characters and 64 or fewer characters.

# 3. Detailed Function Manual

This chapter provides detailed usage instructions for the main functions of the Open DID CA Admin Console.

## 3.1. CA Management

The CA Management menu allows you to view information about the CA(Certificate Application) server registered with a certificate. This screen displays the basic information of the CA, and modification or deletion is not possible.

<img src="./images/ca_management.png" width="800"/>

| Number | Item | Description |
|--------|------|-------------|
| 1 | DID | The unique identifier of the CA. It is displayed in a format such as 'did:omn:ca'. |
| 2 | Name | The name of the CA. This name is displayed when issuing VCs. |
| 3 | Status | Indicates the activation status of the CA. Either ACTIVATE or DEACTIVATE status is displayed. |
| 4 | URL | The base URL address of the CA service. |
| 5 | Certificate URL | The URL address where you can check the CA's registration certificate. |
| 6 | Registered At | Displays the date and time when the CA was registered. |
| 7 | VIEW DID DOCUMENT | A button to check the DID document. When clicked, the DID document information registered on the blockchain is displayed in a popup. |
| 8 | DID Document Content | The content of the DID Document displayed when the VIEW DID DOCUMENT button is clicked. It includes the CA's DID information, controller, creation date and time, verification methods, etc. in JSON format. |

### 3.1.1. DID Document Lookup

Follow these steps to view a DID Document:

1. Click the 'VIEW DID DOCUMENT' button on the CA Management screen.
2. A popup window will open, displaying the full content of the DID Document registered on the blockchain in JSON format.
3. Click outside the popup window to close it.

<br/>

## 3.2. User Management

The User Management menu provides the function to view user information registered in the system. Through this function, administrators can check users' personally identifiable information (PII).

### 3.2.1. User List Lookup

<img src="./images/user_management.jpg" width="800"/>

The User Management screen displays the following key information:

| Number | Item | Description |
|--------|------|-------------|
| 1 | ID | The unique identifier of the user. Click to view detailed information. |
| 2 | PII | The personally identifiable information of the user. |
| 3 | Registered At | Displays the date and time when the user was registered. |
| 4 | Updated At | Displays the date and time when the user information was last updated. |
| 5 | Pagination | Manages the pages of the user list. You can check the number of rows per page and current page information. |

> **Note**: The user list screen only allows reading, and there are no functions to add, modify, or delete user information.

### 3.2.2. User Detail Information Lookup

Click on a user ID in the user list to view detailed information about that user.

<img src="./images/user_detail.png" width="800"/>

The user detail information screen displays the following information:

| Section | Information Included |
|---------|----------------------|
| **Basic Information** | - ID<br>- PII<br>- Registered At<br>- Updated At |

> **Important**: The personally identifiable information (PII) is currently arbitrary values entered through the Demo Server. The CA server provides this lookup function for settings that require having user information.

## 3.3. Admin Management

The `Admin Management` menu is a function for managing administrator accounts that can access the CA Admin Console.

When the CA server is installed, an `admin@opendid.omnione.net` account is automatically created with ROOT privileges.
This account is the only ROOT account in the system and cannot be deleted.

Administrator accounts are divided into two permission types: **ROOT** and **Normal Admin**.
ROOT accounts can perform all functions in the `Admin Management` menu, while Normal Admin accounts can only perform general lookup functions.

---
> **Note:** Currently, the only difference between ROOT accounts and Normal Admin accounts is
> the difference in buttons displayed in the `Admin Management` menu (only ROOT can use REGISTER / DELETE / CHANGE PASSWORD functions).
> There are no access restrictions or functional limitations for other menus in the system yet.
---

### 3.3.1. Admin List Lookup

When you enter the `Admin Management` menu, a list of registered administrator accounts is displayed in table format.

<img src="./images/admin-management.png" width="800"/>

| Number | Item | Description |
|--------|------|-------------|
| 1 | **REGISTER Button** | Navigates to the registration page where you can register a new administrator account. |
| 2 | **DELETE Button** | Deletes the selected administrator account. (Only available to ROOT administrators) |
| 3 | **CHANGE PASSWORD Button** | Allows you to change the password of the selected administrator account. |
| 4 | **ID** | The email ID of the registered administrator account. |
| 5 | **Role** | The role of the administrator account. (e.g., ROOT, Normal Admin, etc.) |
| 6 | **Registered At** | The date and time when the account was first registered. |
| 7 | **Updated At** | The date and time when the account was last modified. |

<br/>

### 3.3.2. Admin Registration

When you click the **REGISTER** button on the `Admin Management` screen, you will be directed to the registration screen shown below.

<img src="./images/admin-registration.png" width="600"/>

| Number | Item | Description |
|--------|------|-------------|
| 1 | **ID** | The ID of the administrator account to be registered. It must be in email format. |
| 2 | **Check Availability Button** | Checks if the entered ID is not already in use. |
| 3 | **Role** | Selects the permission for the administrator account to be registered. (e.g., Normal Admin) |
| 4 | **Password** | Enters the password to be used for login. |
| 5 | **Re-enter Password** | Enters the password once more to confirm it matches. |
| 6 | **REGISTER Button** | Registers the administrator account based on the information entered. |
| 7 | **RESET Button** | Resets all input values. |
| 8 | **CANCEL Button** | Cancels the registration and returns to the previous screen. |

[Open DID Installation Guide]: https://github.com/OmniOneID/did-release/blob/main/release-V1.0.0.0/OepnDID_Installation_Guide-V1.0.0.0.md