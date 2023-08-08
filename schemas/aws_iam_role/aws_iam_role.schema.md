# AWS IAM Role JSON Schema

This is a JSON schema for defining AWS IAM roles. It specifies the properties and required fields for creating an IAM role.

## Schema Overview

| Property    | Type   | Required | Description                                                      |
|-------------|--------|----------|------------------------------------------------------------------|
| `kind`      | string | Yes      | The kind of AWS IAM role (e.g., "aws_iam_role").                  |
| `flavor`    | string | Yes      | The flavor of the AWS IAM role (e.g., "default").                |
| `version`   | string | Yes      | The version of the schema (e.g., "0.1", "latest").               |
| `spec`      | object | Yes      | The specification for the AWS IAM role.                           |
| `advanced`  | object | No       | The advanced section for the AWS IAM role.                        |

## `spec` Object

| Property           | Type   | Required | Description                                                            |
|--------------------|--------|----------|------------------------------------------------------------------------|
| `irsa`             | object | No       | The IRSA block required to attach EKS OIDC to service accounts.       |
| `policies`         | object | Yes      | The map of all policy ARNs that you want to attach to the role.       |

### `irsa` Object

| Property              | Type   | Required | Description                                                                    |
|-----------------------|--------|----------|--------------------------------------------------------------------------------|
| `service_accounts`    | object | Yes      | The map of all service accounts that you want to attach IRSA.                   |
| `oidc_providers`      | object | No      | The OIDC providers block required to attach EKS to service accounts.           |

#### `service_accounts` Object

| Property            | Type   | Required | Description                                                                 |
|---------------------|--------|----------|-----------------------------------------------------------------------------|
| `[name]`            | object | Yes      | An arbitrary name given to the service account which is not used anywhere.  |

#### `oidc_providers` Object

| Property            | Type   | Required | Description                                                            |
|---------------------|--------|----------|------------------------------------------------------------------------|
| `[arn]`             | object | Yes      | An arbitrary name given to the OIDC provider which is not used anywhere. |

### `policies` Object

| Property      | Type   | Required | Description                                                    |
|---------------|--------|----------|----------------------------------------------------------------|
| `[arn]`       | object | Yes      | An arbitrary name given to the policies which is not used anywhere. |

## `out` Object

| Property           | Type   | Required | Description                                                 |
|--------------------|--------|----------|-------------------------------------------------------------|
| `irsa_iam_role_arn`| string | No       | The ARN of the AWS IAM role which has the IRSA.            |
