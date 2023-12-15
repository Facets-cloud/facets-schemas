---
title: "User Management"
slug: "user-management-2"
hidden: false
createdAt: "2023-02-14T20:15:22.492Z"
updatedAt: "2023-07-06T10:44:51.661Z"
---
## Introduction

Facets provides a comprehensive solution for user management that allows you to formalize who has access to what resources, and what they can do with those resources. Facets uses Role-based Access Control (RBAC) to manage user access to resources.

### System-Defined Roles

Facets provides some predefined system defined roles with a set of permissions. These roles can be used as is, or as a starting point to create custom roles with unique permissions.

### Custom Roles

Facets provides the ability to create custom roles to meet your specific organization's access control needs. custom roles allow administrators to define unique sets of permissions, allowing you to grant or restrict access to specific resources and actions.

### User Management

Admins can create/edit users and add them to User Groups.

### User Groups

Admins can create User Groups that encompass roles and assign environment-specific permissions to users in that User Group. A User Group can contain one or more roles, and each role can have different permissions. When a user is added to a User Group, they automatically inherit the permissions assigned to the roles in that User Group.

### Resource Groups

Resource groups in Facets offer a structured approach to organizing and managing resources. They simplify resource management by bundling related resources together. With Resource Groups, you can group services, databases, caches, and other resources in one resource group, thereby aligning your team and pod structures.  
With role-based access control, different departments can have dedicated user groups with access to specific resource groups, enabling teams to access and manage resources specific to their responsibilities. Other key benefits of this feature include high accessibility control, simplified management, improved collaboration, and flexibility to reorganize resources.

### OAuth Integrations

Facets now offers seamless integration with popular identity providers such as Google OAuth, OneLogin, and Okta to enable OAuth integrations. This integration allows users to securely authenticate and authorize access to the Facets Control Plane. Users can easily connect their Facets account with their preferred identity provider, simplifying the authentication process and providing an added layer of security.

## Related Guides

- [Integrating with Google Oauth](doc:google-oauth-integration-for-user-management)
- [Integrating with OneLogin](doc:integrating-with-onelogin)
- [Integrating with Okta](doc:integrating-with-okta)
- [Creating Custom Roles](doc:custom-roles)
- [Creating a User](doc:creating-a-user)
- [Creating a User Group](doc:creating-a-user-group)