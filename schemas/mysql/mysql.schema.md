# Introduction

MySQL intent to deploy AWS Aurora compatible MySQL in AWS

## Properties

| Property     | Type            | Required | Description                                                                                                                                    |
| ------------ | --------------- | -------- | ---------------------------------------------------------------------------------------------------------------------------------------------- |
| `flavor`     | string          | **Yes**  | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc.                                     |
| `kind`       | string          | **Yes**  | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances                |
| `metadata`   | object          | **Yes**  | Metadata related to the resource                                                                                                               |
| `spec`       | [object](#spec) | **Yes**  | Specification as per resource types schema                                                                                                     |
| `version`    | string          | **Yes**  | This field can be used to pin to a particular version                                                                                          |
| `advanced`   | object          | No       | Additional fields if any for a particular implementation of a resource                                                                         |
| `depends_on` | list            | No       | Dependencies on other resources. e.g. application x may depend on mysql                                                                        |
| `disabled`   | boolean         | No       | Flag to disable the resource                                                                                                                   |
| `lifecycle`  | string          | No       | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`) Possible values are: `ENVIRONMENT_BOOTSTRAP`. |
| `out`        | [object](#out)  | No       | Output given by the resource for others to refer.                                                                                              |
| `provided`   | boolean         | No       | Flag to tell if the resource should not be provisioned by facets                                                                               |

## out

Output given by the resource for others to refer.

### Properties

| Property     | Type                  | Required | Description |
| ------------ | --------------------- | -------- | ----------- |
| `instances`  | object                | No       |             |
| `interfaces` | [object](#interfaces) | No       |             |
| `spec`       | [object](#spec)       | No       |             |

### interfaces

#### Properties

| Property | Type              | Required | Description        |
| -------- | ----------------- | -------- | ------------------ |
| `writer` | [object](#writer) | **Yes**  | The writer details |
| `reader` | [object](#reader) | No       | The reader details |

#### reader

The reader details

##### Properties

| Property            | Type   | Required | Description                    |
| ------------------- | ------ | -------- | ------------------------------ |
| `connection_string` | string | No       | Connection string to connect   |
| `host`              | string | No       | Host for service discovery     |
| `name`              | string | No       | Name of interface, same as key |
| `password`          | string | No       | Password to connect (if any)   |
| `port`              | string | No       | Port for service discovery     |
| `username`          | string | No       | Username to connect (if any)   |

#### writer

The writer details

##### Properties

| Property            | Type   | Required | Description                    |
| ------------------- | ------ | -------- | ------------------------------ |
| `connection_string` | string | No       | Connection string to connect   |
| `host`              | string | No       | Host for service discovery     |
| `name`              | string | No       | Name of interface, same as key |
| `password`          | string | No       | Password to connect (if any)   |
| `port`              | string | No       | Port for service discovery     |
| `username`          | string | No       | Username to connect (if any)   |

### spec

#### Properties

| Property        | Type            | Required | Description                                                   |
| --------------- | --------------- | -------- | ------------------------------------------------------------- |
| `mysql_version` | string          | **Yes**  | Aurora compatible MySQL version (eg: 5.7.mysql_aurora.2.10.1) |
| `size`          | [object](#size) | **Yes**  |                                                               |

#### size

##### Properties

| Property | Type              | Required | Description        |
| -------- | ----------------- | -------- | ------------------ |
| `reader` | [object](#reader) | No       | The reader details |
| `writer` | [object](#writer) | No       | The writer details |

##### reader

The reader details

###### Properties

| Property        | Type    | Required | Description                                                                                                                             |
| --------------- | ------- | -------- | --------------------------------------------------------------------------------------------------------------------------------------- |
| `replica_count` | integer | **Yes**  | Number of reader instances needs to be deployed                                                                                         |
| `cpu`           | string  | No       | CPU request in format mentioned @ <https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu>       |
| `instance`      | string  | No       | Instance name in certain cases                                                                                                          |
| `memory`        | string  | No       | Memory request in format mentioned @ <https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory> |
| `volume`        | string  | No       | Volume request in kubernetes persistent volumes                                                                                         |

##### writer

The writer details

###### Properties

| Property        | Type    | Required | Description                                                                                                                             |
| --------------- | ------- | -------- | --------------------------------------------------------------------------------------------------------------------------------------- |
| `replica_count` | integer | **Yes**  | Number of writer instances needs to be deployed                                                                                         |
| `cpu`           | string  | No       | CPU request in format mentioned @ <https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu>       |
| `instance`      | string  | No       | Instance name in certain cases                                                                                                          |
| `memory`        | string  | No       | Memory request in format mentioned @ <https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory> |
| `volume`        | string  | No       | Volume request in kubernetes persistent volumes                                                                                         |

## spec

Specification as per resource types schema

### Properties

| Property        | Type            | Required | Description                                                   |
| --------------- | --------------- | -------- | ------------------------------------------------------------- |
| `mysql_version` | string          | **Yes**  | Aurora compatible MySQL version (eg: 5.7.mysql_aurora.2.10.1) |
| `size`          | [object](#size) | **Yes**  |                                                               |

### size

#### Properties

| Property | Type              | Required | Description        |
| -------- | ----------------- | -------- | ------------------ |
| `reader` | [object](#reader) | No       | The reader details |
| `writer` | [object](#writer) | No       | The writer details |

#### reader

The reader details

##### Properties

| Property        | Type    | Required | Description                                                                                                                             |
| --------------- | ------- | -------- | --------------------------------------------------------------------------------------------------------------------------------------- |
| `replica_count` | integer | **Yes**  | Number of reader instances needs to be deployed                                                                                         |
| `cpu`           | string  | No       | CPU request in format mentioned @ <https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu>       |
| `instance`      | string  | No       | Instance name in certain cases                                                                                                          |
| `memory`        | string  | No       | Memory request in format mentioned @ <https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory> |
| `volume`        | string  | No       | Volume request in kubernetes persistent volumes                                                                                         |

#### writer

The writer details

##### Properties

| Property        | Type    | Required | Description                                                                                                                             |
| --------------- | ------- | -------- | --------------------------------------------------------------------------------------------------------------------------------------- |
| `replica_count` | integer | **Yes**  | Number of writer instances needs to be deployed                                                                                         |
| `cpu`           | string  | No       | CPU request in format mentioned @ <https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu>       |
| `instance`      | string  | No       | Instance name in certain cases                                                                                                          |
| `memory`        | string  | No       | Memory request in format mentioned @ <https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory> |
| `volume`        | string  | No       | Volume request in kubernetes persistent volumes                                                                                         |
