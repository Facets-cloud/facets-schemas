# Introduction

Log collector module to deploy Loki with dependent components (minio, promtail) in Kubernetes cluster

## Properties

| Property     | Type            | Required | Description                                                                                                                                    |
| ------------ | --------------- | -------- | ---------------------------------------------------------------------------------------------------------------------------------------------- |
| `flavor`     | string          | **Yes**  | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc.                                     |
| `kind`       | string          | **Yes**  | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances                |
| `metadata`   | object          | **Yes**  | Metadata related to the resource                                                                                                               |
| `spec`       | [object](#spec) | **Yes**  | Specification as per resource types schema                                                                                                     |
| `version`    | string          | **Yes**  | This field can be used to pin to a particular version                                                                                          |
| `advanced`   | object          | No       | Additional fields if any for a particular implementation of a resource                                                                         |
| `depends_on` |                 | No       | Dependencies on other resources. e.g. application x may depend on mysql                                                                        |
| `disabled`   | boolean         | No       | Flag to disable the resource                                                                                                                   |
| `lifecycle`  | string          | No       | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`) Possible values are: `ENVIRONMENT_BOOTSTRAP`. |
| `out`        | [object](#out)  | No       | Output given by the resource for others to refer.                                                                                              |
| `provided`   | boolean         | No       | Flag to tell if the resource should not be provisioned by facets                                                                               |

## spec

Specification as per resource types schema

### Properties

| Property           | Type   | Required | Description                                                                                                                                                                                                                              |
| ------------------ | ------ | -------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `retentation_days` | number | **Yes**  | The number of days to retain an object after being created. Specify the number of days as an integer, e.g. 30 for 30 days                                                                                                                |
| `storage_size`     | string | **Yes**  | Storage capacity to store logs. You can express storage size as a plain integer or as a fixed-point number using one of these quantity suffixes: E, P, T, G, M, k. You can also use the power-of-two equivalents: Ei, Pi, Ti, Gi, Mi, Ki |


## out

Output given by the resource for others to refer.

#### Properties

| Property           | Type   | Required | Description                                                                                                                                                                                                                              |
| ------------------ | ------ | -------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `retentation_days` | number | **Yes**  | The number of days to retain an object after being created. Specify the number of days as an integer, e.g. 30 for 30 days                                                                                                                |
| `storage_size`     | string | **Yes**  | Storage capacity to store logs. You can express storage size as a plain integer or as a fixed-point number using one of these quantity suffixes: E, P, T, G, M, k. You can also use the power-of-two equivalents: Ei, Pi, Ti, Gi, Mi, Ki |

### flavors
- [loki](log_collector.loki.schema.md)
- [loki_s3](log_collector.loki_s3.schema.md)
- [loki_blob](log_collector.loki_blob.schema.md)
- [loki_gcs](log_collector.loki_gcs.schema.md)