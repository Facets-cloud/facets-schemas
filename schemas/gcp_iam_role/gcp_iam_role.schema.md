# GCP IAM Role JSON Schema

This is a JSON schema for defining AWS IAM roles. It specifies the properties and required fields for creating an IAM role.

## Schema Overview

| Property    | Type   | Required | Description                                                      |
|-------------|--------|----------|------------------------------------------------------------------|
| `kind`      | string | Yes      | The kind of GCP IAM role (e.g., "aws_iam_role").                  |
| `flavor`    | string | Yes      | The flavor of the GCP IAM role (e.g., "default").                |
| `version`   | string | Yes      | The version of the schema (e.g., "0.1", "latest").               |
| `spec`      | object | Yes      | The specification for the GCP IAM role.                           |
| `advanced`  | object | No       | The advanced section for the GCP IAM role.                        |

## `spec` Object
| Property                | Type   | Required | Description                                                                                          |
|-------------------------|--------|----------|------------------------------------------------------------------------------------------------------|
| `role_id`               | string | Yes      | The role ID for the custom role with regex; example: testRole.                                       |
| `permissions`           | list   | No       | The list of permissions for the custom role.                                                         |
| `excluded_permissions`  | list   | No       | The list of excluded permissions for the custom role.                                                |
| `members`               | list   | No       | The list of members (users, groups, or service accounts) assigned to the role.                       |
| `base_roles`            | list   | No       | The list of base roles included in the custom role.  example: roles/artifactregistry.createOnPushWriter                                       |
| `stage`                 | string | No       | The stage of the custom role (default is "GA" - General Availability).                               |
| `title`                 | string | No       | The title for the custom role (defaults to "${module.gcp_custome_role_name.name}-${local.role_id}"). |




## `out` Object

| Property           | Type   | Required | Description                      |
|--------------------|--------|----------|----------------------------------|
| `role_id`| string | No       | The custom_role ID for the role. |
