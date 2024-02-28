## Introduction
This document describes the structure of the Azure functions configuration schema.

### spec 

The `spec` object defines the configuration for Azure functions.

| Property      | Type              | Description                                       |
|---------------|-------------------|---------------------------------------------------|
| `release`     | [object](#topics) | Topics configuration.                             |
| `runtime`     | [object](#queues) | Queues configuration.                             |
| `app_setting` | [object](#size)   | Environment variables to be passed to application |
| `os`          | string            | Operating system type between Linux or windows    |
| `sku`         | string            | SKU to be used for instance                       |
| `deploy_as`   | string            | Deploy as `code` or `container`                   |
| `in_vnet`     | string            | Fucntions to be added in cluster's vnet           |


#### release
       
The `release` object defines the configuration for topics in Azure functions.

| Property                        | Type    | Description                                                                                                  | Default  | Required |
|---------------------------------|---------|--------------------------------------------------------------------------------------------------------------|----------|----------|
| `zip_path`                      | string  | Zip path of the package.                                                                                     | `null`   | No       |

#### runtime

The `runtime` object defines the configuration for runtime in Azure functions.


| Property                               | Type    | Description                                                                                                                    | Default | Required |
|----------------------------------------|---------|--------------------------------------------------------------------------------------------------------------------------------|---------|---------|
| `stack`                                | string  | The name of the queue.                                                                                                         | NA      | Yes     |
| `version`                              | string  | The status of the queue.                                                                                                       | NA      | Yes     |



## Outputs

| Property                           | Type   | Description                                              |
|------------------------------------|--------|----------------------------------------------------------|
| function_app_kind                  | string | The kind of the function app.                            |
| function_app_name                  | string | The name of the function app.                            |
| function_app_slot_default_hostname | string | The default hostname of the function app slot.           |
| storage_account_id                 | string | The ID of the storage account used for the function app. |
| site_name                          | string | The name of the function app site credential.            |
| site_password                      | string | The password for the function app site credential.       |
