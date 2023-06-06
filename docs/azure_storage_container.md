# Azure Storage Container

## Schema Properties

|                       | Type                          | Description                                                                                                                                                                    | Required   |
|:----------------------|:------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| conditional_on_intent | string                        | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| depends_on            | array                         | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| disabled              | boolean                       | Flag to disable the resource                                                                                                                                                   | No         |
| flavor                | enum: default                 |                                                                                                                                                                                | Yes        |
| kind                  | enum: azure_storage_container |                                                                                                                                                                                | Yes        |
| lifecycle             | enum: ENVIRONMENT_BOOTSTRAP   | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`)                                                                               | No         |
| provided              | boolean                       | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| version               | enum: 0.1, latest             |                                                                                                                                                                                | Yes        |
| metadata.name         | string                        | Name of the resource                                                                                                                                                           | No         |
|                       |                               |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                  | object                        |                                                                                                                                                                                | Yes        |

## Outputs

|                                     | Type   | Description                                                                            | Required   | Referencing                                                                  |
|:------------------------------------|:-------|:---------------------------------------------------------------------------------------|:-----------|:-----------------------------------------------------------------------------|
| out.attributes.storage_account_name | string | The storage account name where the containers are created                              | No         | ${azure_storage_container.RESOURCE_NAME.out.attributes.storage_account_name} |
| out.attributes.storage_account_id   | string | The storage account id where the containers are created                                | No         | ${azure_storage_container.RESOURCE_NAME.out.attributes.storage_account_id}   |
| out.attributes.primary_access_key   | string | The primary access key for the storage account                                         | No         | ${azure_storage_container.RESOURCE_NAME.out.attributes.primary_access_key}   |
| out.attributes.container_name       | string | The name of the container that is created and will be referenced by other applications | No         | ${azure_storage_container.RESOURCE_NAME.out.attributes.container_name}       |

