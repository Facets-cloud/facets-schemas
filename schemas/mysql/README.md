## Introduction
A mysql level intent to deploy database in kubernetes cluster.
## Properties

| Property     | Type                | Required | Description                                                                                                                                      |
|--------------|---------------------|----------|--------------------------------------------------------------------------------------------------------------------------------------------------|
| `flavor`     | string              | **Yes**  | Implementation selector for the resource. e.g. for a resource type aurora, default, rds, cloudsql etc.                                           |
| `kind`       | string              | **Yes**  | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances                  |
| `metadata`   | [object](#metadata) | **Yes**  | Metadata related to the resource                                                                                                                 |
| `spec`       | [object](#spec)     | **Yes**  | Specification as per resource types schema                                                                                                       |
| `version`    | string              | **Yes**  | This field can be used to pin to a particular version                                                                                            |
| `advanced`   | [object](#advanced) | No       | Additional fields if any for a particular implementation of a resource                                                                           |
| `depends_on` |                     | No       | Dependencies on other resources. e.g. application x may depend on mysql                                                                          |
| `disabled`   | boolean             | No       | Flag to disable the resource                                                                                                                     |
| `lifecycle`  | string              | No       | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`) Possible values are: `ENVIRONMENT_BOOTSTRAP`, `ENVIRONMENT`. |
| `out`        | [object](#out)      | No       | Output given by the resource for others to refer.                                                                                                |
| `provided`   | boolean             | No       | Flag to tell if the resource should not be provisioned by facets                                                                                 |

## spec

Specification as per resource types schema

| Property        | Type   | Required | Description                     |
|-----------------|--------|----------|---------------------------------|
| `mysql_version` | string | **Yes**  | Mysql version to be used        |
| `size`          | map    | **Yes**  |  for `reader`,`writer` |

#### size

| Component | Type | Required | Description                     |
|-----------|------|----------|---------------------------------|
| `reader`  | map  | **Yes**  | Mysql version to be used        |
| `writer`  | map  | **Yes**  | cpu/memory/volume/replicas for `reader`,`writer` |


## advanced

Additional fields if any for a particular implementation of a resource

| Property          | Type | Required | Description |
|-------------------|------|----------|-------------|
| `aurora`          | [object] | No       | Advanced section of the aurora
| `rds`             | [object]  | No       | Advanced section of the rds
| `cloudsql`        | [object]  | No       | Advanced section of the cloudsql
| `flexible_server` | [object] | No       | Advanced section of the flexible_server


## out

Output given by the resource for others to refer.

### Properties

| Property     | Type                  | Required | Description                                                           |
|--------------|-----------------------|----------|-----------------------------------------------------------------------|
| `interfaces` | [object](#interfaces) | No       | The output for your ingress module, this can be generated or provided |

### Flavors

* `aws_alb`
* `gcp_alb`
* `nginx_ingress_controller`
* `azure_agic`
