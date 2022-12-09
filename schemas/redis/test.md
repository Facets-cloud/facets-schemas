## Properties

| Property     | Type                | Required | Description                                                                                                                                    |
|--------------|---------------------|----------|------------------------------------------------------------------------------------------------------------------------------------------------|
| `flavor`     | string              | **Yes**  | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc.                                     |
| `kind`       | string              | **Yes**  | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances                |
| `metadata`   | [object](#metadata) | **Yes**  | Metadata related to the resource                                                                                                               |
| `spec`       | [object](#spec)     | **Yes**  | Specification as per resource types schema                                                                                                     |
| `version`    | string              | **Yes**  | This field can be used to pin to a particular version                                                                                          |
| `advanced`   | [object](#advanced) | No       | Kubernetes Schema                                                                                                                              |
| `depends_on` |                     | No       | Dependencies on other resources. e.g. application x may depend on mysql                                                                        |
| `disabled`   | boolean             | No       | Flag to disable the resource                                                                                                                   |
| `lifecycle`  | string              | No       | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`) Possible values are: `ENVIRONMENT_BOOTSTRAP`. |
| `out`        | [object](#out)      | No       | Output given by the resource for others to refer.                                                                                              |
| `provided`   | boolean             | No       | Flag to tell if the resource should not be provisioned by facets                                                                               |

## advanced

Kubernetes Schema

### Properties

| Property      | Type                   | Required | Description                         |
|---------------|------------------------|----------|-------------------------------------|
| `elasticache` | [object](#elasticache) | No       | Advanced values for AWS Elasticache |
| `k8s`         | [object](#k8s)         | No       |                                     |
| `memorystore` | [object](#memorystore) | No       | Advanced values for GCP memorystore |

### elasticache

Advanced values for AWS Elasticache

#### Properties

| Property                            | Type                                         | Required | Description                                                                                                                                                           |
|-------------------------------------|----------------------------------------------|----------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `aws_elasticache_parameter_group`   | [object](#aws_elasticache_parameter_group)   | No       | Resource values as per the terraform resource documentation https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/elasticache_parameter_group   |
| `aws_elasticache_replication_group` | [object](#aws_elasticache_replication_group) | No       | Resource values as per the terraform resource documentation https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/elasticache_replication_group |
| `aws_security_group`                | [object](#aws_security_group)                | No       | Resource values as per the terraform resource documentation https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/security_group                |

#### aws_elasticache_parameter_group

Resource values as per the terraform resource documentation https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/elasticache_parameter_group

| Property | Type | Required | Description |
|----------|------|----------|-------------|

#### aws_elasticache_replication_group

Resource values as per the terraform resource documentation https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/elasticache_replication_group

| Property | Type | Required | Description |
|----------|------|----------|-------------|

#### aws_security_group

Resource values as per the terraform resource documentation https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/security_group

| Property | Type | Required | Description |
|----------|------|----------|-------------|

### k8s

#### Properties

| Property | Type             | Required | Description |
|----------|------------------|----------|-------------|
| `redis`  | [object](#redis) | No       |             |

#### redis

##### Properties

| Property | Type              | Required | Description                                                                                            |
|----------|-------------------|----------|--------------------------------------------------------------------------------------------------------|
| `values` | [object](#values) | No       | Helm values as per the Bitnami redis chart https://github.com/bitnami/charts/tree/master/bitnami/redis |

##### values

Helm values as per the Bitnami redis chart https://github.com/bitnami/charts/tree/master/bitnami/redis

| Property | Type | Required | Description |
|----------|------|----------|-------------|

### memorystore

Advanced values for GCP memorystore

#### Properties

| Property                | Type                             | Required | Description                                                                                                                                                                            |
|-------------------------|----------------------------------|----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `google_redis_instance` | [object](#google_redis_instance) | No       | Resource values as per the terraform resource documentation for redis instance creation  https://registry.terraform.io/providers/hashicorp/google/latest/docs/resources/redis_instance |

#### google_redis_instance

Resource values as per the terraform resource documentation for redis instance creation  https://registry.terraform.io/providers/hashicorp/google/latest/docs/resources/redis_instance

| Property | Type | Required | Description |
|----------|------|----------|-------------|

## metadata

Metadata related to the resource

### Properties

| Property | Type   | Required | Description                                        |
|----------|--------|----------|----------------------------------------------------|
| `name`   | string | No       | Name of the resource                               |
|          |        |          |     - if not specified, fallback is the `filename` |

## out

Output given by the resource for others to refer.

### Properties

| Property     | Type                  | Required | Description |
|--------------|-----------------------|----------|-------------|
| `instances`  | [object](#instances)  | No       |             |
| `interfaces` | [object](#interfaces) | No       |             |
| `spec`       | [object](#spec)       | No       |             |

### instances

| Property | Type | Required | Description |
|----------|------|----------|-------------|

### interfaces

#### Properties

| Property | Type              | Required | Description   |
|----------|-------------------|----------|---------------|
| `writer` | [object](#writer) | **Yes**  | Any Interface |
| `reader` | [object](#reader) | No       | Any Interface |

#### reader

Any Interface

##### Properties

| Property            | Type   | Required | Description                    |
|---------------------|--------|----------|--------------------------------|
| `connection_string` | string | No       | Connection string to connect   |
| `host`              | string | No       | Host for service discovery     |
| `name`              | string | No       | Name of interface, same as key |
| `password`          | string | No       | Password to connect (if any)   |
| `port`              | string | No       | Port for service discovery     |
| `username`          | string | No       | Username to connect (if any)   |

#### writer

Any Interface

##### Properties

| Property            | Type   | Required | Description                    |
|---------------------|--------|----------|--------------------------------|
| `connection_string` | string | No       | Connection string to connect   |
| `host`              | string | No       | Host for service discovery     |
| `name`              | string | No       | Name of interface, same as key |
| `password`          | string | No       | Password to connect (if any)   |
| `port`              | string | No       | Port for service discovery     |
| `username`          | string | No       | Username to connect (if any)   |

### spec

#### Properties

| Property              | Type            | Required | Description                        |
|-----------------------|-----------------|----------|------------------------------------|
| `authenticated`       | boolean         | **Yes**  | Make this redis Password protected |
| `persistence_enabled` | boolean         | **Yes**  | Enable Persistence for this redis  |
| `redis_version`       | string          | **Yes**  | Version of redis e.g. 6.3          |
| `size`                | [object](#size) | **Yes**  |                                    |

#### size

##### Properties

| Property | Type              | Required | Description        |
|----------|-------------------|----------|--------------------|
| `writer` | [object](#writer) | **Yes**  | The writer details |
| `reader` | [object](#reader) | No       | The reader details |

##### reader

The reader details

###### Properties

| Property        | Type    | Required | Description                                                                                                                           |
|-----------------|---------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `replica_count` | integer | **Yes**  | Number of reader instances needs to be deployed                                                                                       |
| `cpu`           | string  | No       | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu       |
| `instance`      | string  | No       | Instance name in certain cases                                                                                                        |
| `memory`        | string  | No       | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory |
| `volume`        | string  | No       | Volume request in kubernetes persistent volumes                                                                                       |

##### writer

The writer details

###### Properties

| Property        | Type    | Required | Description                                                                                                                           |
|-----------------|---------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `replica_count` | integer | **Yes**  | Number of writer instances needs to be deployed                                                                                       |
| `cpu`           | string  | No       | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu       |
| `instance`      | string  | No       | Instance name in certain cases                                                                                                        |
| `memory`        | string  | No       | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory |
| `volume`        | string  | No       | Volume request in kubernetes persistent volumes                                                                                       |

## spec

Specification as per resource types schema

### Properties

| Property              | Type            | Required | Description                        |
|-----------------------|-----------------|----------|------------------------------------|
| `authenticated`       | boolean         | **Yes**  | Make this redis Password protected |
| `persistence_enabled` | boolean         | **Yes**  | Enable Persistence for this redis  |
| `redis_version`       | string          | **Yes**  | Version of redis e.g. 6.3          |
| `size`                | [object](#size) | **Yes**  |                                    |

### size

#### Properties

| Property | Type              | Required | Description        |
|----------|-------------------|----------|--------------------|
| `writer` | [object](#writer) | **Yes**  | The writer details |
| `reader` | [object](#reader) | No       | The reader details |

#### reader

The reader details

##### Properties

| Property        | Type    | Required | Description                                                                                                                           |
|-----------------|---------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `replica_count` | integer | **Yes**  | Number of reader instances needs to be deployed                                                                                       |
| `cpu`           | string  | No       | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu       |
| `instance`      | string  | No       | Instance name in certain cases                                                                                                        |
| `memory`        | string  | No       | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory |
| `volume`        | string  | No       | Volume request in kubernetes persistent volumes                                                                                       |

#### writer

The writer details

##### Properties

| Property        | Type    | Required | Description                                                                                                                           |
|-----------------|---------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `replica_count` | integer | **Yes**  | Number of writer instances needs to be deployed                                                                                       |
| `cpu`           | string  | No       | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu       |
| `instance`      | string  | No       | Instance name in certain cases                                                                                                        |
| `memory`        | string  | No       | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory |
| `volume`        | string  | No       | Volume request in kubernetes persistent volumes                                                                                       |

