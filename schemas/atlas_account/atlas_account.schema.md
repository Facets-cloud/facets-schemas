## Introduction

An Atlas Account intent which can be used to configure Atlas provider to create atlas mongodb

## Properties

| Property   | Type                | Required | Description                           |
|------------|---------------------|----------|---------------------------------------|
| `flavor`   | string              | **Yes**  | Possible values are: `default`.       |
| `kind`     | string              | **Yes**  | Possible values are: `atlas_account`.    |
| `spec`     | [object](#spec)     | **Yes**  |                                       |
| `version`  | string              | **Yes**  | Possible values are: `0.1`. |
| `advanced` | [object](#advanced) | No       |                                       |

## spec

### Properties

| Property    | Type                 | Required | Description           |
| ----------- | -------------------- | -------- | --------------------- |
| `project_id` | string | **Yes**  | Project ID for Atlas account. |
| `public_key` | string | **Yes**  | Public Key for mongoatlas provider configuration. |
| `private_key` | string | **Yes**  | Private Key for mongoatlas provider configuration. |

### Flavors

* `default`
