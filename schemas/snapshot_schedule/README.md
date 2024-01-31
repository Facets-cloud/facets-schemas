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
| `schedule`   | string | **Yes**  | The cronspec (https://en.wikipedia.org/wiki/Cron#Overview) that defines the schedule. It is interpreted with respect to the UTC timezone. The following pre-defined shortcuts are also supported: @hourly, @daily, @weekly, @monthly, and @yearly |
| `resource_name`   | string | **Yes**  | The name of the resource that you want to create a volume snapshot backup, if this is left blank, it will create volume backups for all resource name in the resource type |
| `resource_type`   | string | **Yes**  | The name of the resource type which you need to create volume snapshot backup |
| `retention_policy`   | [object](#data) | **Yes**  | The retention policy for your backup snapshot |

## retention policy

The retention policy for your backup snapshot

### Properties

| Property | Type            | Required | Description                                |
|----------|-----------------|----------|--------------------------------------------|
| `expiry`   | string | **Yes**  | The length of time a given snapshot should be retained, specified in hours. (168h = 1 week) |
| `count`   | string | **Yes**  | The maximum number of snapshots per PVC to keep |



## advanced

Additional fields if any for a particular implementation of a resource

### Properties

| Property | Type           | Required | Description                                           |
|----------|----------------|----------|-------------------------------------------------------|
| `snapshot_schedule`    | [object](#snapshot_schedule) | No       | The advanced section of snapshot schedule module |


## out

The output for your azure storage container module, this can be generated or provided

| Property | Type | Required | Description |
|----------|------|----------|-------------|

### Flavors

* `default`