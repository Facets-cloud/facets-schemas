## Introduction
A configmap level intent which can be used to create config map in kubernetes clusters that can be mounted to deployments

## Properties

| Property     | Type                | Required | Description                                                                                                                                    |
|--------------|---------------------|----------|------------------------------------------------------------------------------------------------------------------------------------------------|
| `flavor`     | string              | **Yes**  | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc.                                     |
| `kind`       | string              | **Yes**  | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances                |
| `metadata`   | [object](#metadata) | **Yes**  | Metadata related to the resource                                                                                                               |
| `spec`       | [object](#spec)     | **Yes**  | Specification as per resource types schema                                                                                                     |
| `version`    | string              | **Yes**  | This field can be used to pin to a particular version                                                                                          |
| `advanced`   | [object](#advanced) | No       | Additional fields if any for a particular implementation of a resource                                                                         |
| `depends_on` |                     | No       | Dependencies on other resources. e.g. application x may depend on mysql                                                                        |
| `disabled`   | boolean             | No       | Flag to disable the resource                                                                                                                   |
| `lifecycle`  | string              | No       | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`) Possible values are: `ENVIRONMENT_BOOTSTRAP`. |
| `out`        | [object](#out)      | No       | The output for your configmap module, this can be generated or provided                                                                        |
| `provided`   | boolean             | No       | Flag to tell if the resource should not be provisioned by facets                                                                               |

## spec

Specification as per resource types schema

### Properties

| Property | Type            | Required | Description                                |
|----------|-----------------|----------|--------------------------------------------|
| `data`   | [object](#data) | **Yes**  | the map of all filename and their contents |



## advanced

Additional fields if any for a particular implementation of a resource

### Properties

| Property | Type           | Required | Description                                           |
|----------|----------------|----------|-------------------------------------------------------|
| `azure_storage_container`    | [object](#azure_storage_container) | No       | The advanced section of all the azure storage container module |

### azure_storage_container

The advanced section of all the azure_storage_container module

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `az_storage_account_name`    | string | No       | The name of the storage account which you want this container to be added |
| `az_storage_account_id`    | string | No       | The id of the storage account which you want this container to be added |
| `access_type`    | string | No       | The Access Level configured for this Container. Possible values are blob, container or private. Defaults to private |
| `lifecycles`    | [object](#lifecycles) | No       | The lifecycle map that you can give to the container blobs |

# lifecycles

The lifecycle map that you can give to the container blobs

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `^[a-zA-Z0-9_.-]*$`    | [object](#lifecycles) | No       | This can be a random name to specify a lifecycle rule |

#### ^[a-zA-Z0-9_.-]*$

The lifecycle map that you can give to the container blobs

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `prefix_match`    | string | No       | An array of strings for prefixes to be matched |
| `tier_to_cool_after_days`    | integer | No       | The age in days after last modification to tier blobs to cool storage. Supports blob currently at Hot tier. Must be between 0 and 99999. Defaults to -1 |
| `tier_to_archive_after_days`    | integer | No       | The age in days after last modification to tier blobs to archive storage. Supports blob currently at Hot or Cool tier. Must be between 0 and 99999. Defaults to -1 |
| `delete_after_days`    | integer | No       | The age in days after last modification to delete the blob. Must be between 0 and 99999. Defaults to -1 |
| `snapshot_delete_after_days`    | integer | No       | The age in days after creation to delete the blob snapshot. Must be between 0 and 99999. Defaults to -1 |


## out

The output for your azure storage container module, this can be generated or provided

| Property | Type | Required | Description |
|----------|------|----------|-------------|

### Flavors

* `default`