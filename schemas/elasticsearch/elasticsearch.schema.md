# Elasticsearch

A elasticsearch Instance in clustering mode.

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
| `out`        | [object](#out)      | No       | Output given by the resource for others to refer.                                                                                              |
| `provided`   | boolean             | No       | Flag to tell if the resource should not be provisioned by facets                                                                               |

## spec

Specification as per resource types schema

| Property                | Type            | Required | Description                         |
|-------------------------|-----------------|----------|-------------------------------------|
| `elasticsearch_version` | string          | **Yes**  | Version of elasticsearch e.g. 8.5.1 |
| `size`                  | [object](#size) | **Yes**  |                                     |

### size

The size details

| Property         | Type    | Required | Description                                                                                                                           |
|------------------|---------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `replica_count`  | integer | **Yes**  | Number of elasticsearch instances needs to be deployed [DEPRECATED]                                                                   |
| `instance_count` | integer | **Yes**  | Number of elasticsearch instances needs to be deployed                                                                                |
| `cpu`            | string  | No       | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu       |
| `instance`       | string  | No       | Instance name in certain cases                                                                                                        |
| `memory`         | string  | No       | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory |
| `volume`         | string  | No       | Volume request in kubernetes persistent volumes                                                                                       |

---

## advanced

Additional fields if any for a particular implementation of a resource

| Property | Type           | Required | Description                           |
|----------|----------------|----------|---------------------------------------|
| `k8s`    | [object](#k8s) | No       | Advanced k8s values for elasticsearch |
| `aws`    | [object](#aws) | No       | Advanced aws values for elasticsearch |

### k8s

Advanced k8s values for elasticsearch

| Property        | Type                     | Required | Description                       |
|-----------------|--------------------------|----------|-----------------------------------|
| `elasticsearch` | [object](#elasticsearch) | No       | Advanced values for elasticsearch |


### aws

Advanced aws values for elasticsearch

| Property                   | Type                                                                                                       | Required | Description                                                     |
|----------------------------|------------------------------------------------------------------------------------------------------------|----------|-----------------------------------------------------------------|
| `aws_elasticsearch_domain` | [object](https://registry.terraform.io/providers/hashicorp/aws/3.74.0/docs/resources/elasticsearch_domain) | No       | Advanced values for terraform resource aws_elasticsearch_domain |

#### elasticsearch

Advanced values for elasticsearch

| Property | Type              | Required | Description                                                                                               |
| -------- | ----------------- | -------- | --------------------------------------------------------------------------------------------------------- |
| `values` | [object](#values) | No       | Helm values as per the Bitnami [elasticsearch chart](https://artifacthub.io/packages/helm/elastic/elasticsearch) |

## out

Output given by the resource for others to refer.

| Property     | Type               | Required | Description                                                       |
|--------------|--------------------|----------|-------------------------------------------------------------------|
| `interfaces` | [object](#cluster) | **Yes**  | elasticsearch broker details interfaces of type cluster as object |
| `interfaces` | [object](#http)    | **No**   | elasticsearch broker details interfaces of type http as object    |

Added an example of out
```json
{
  "out": {
    "interfaces": {
      "cluster": {
        "connection_string" : "string",
        "endpoint" : "string"
      }
    }
  }
}
```


### cluster

elasticsearch broker details

| Name              | Description                                                                                                                                | Type   | Required |
|-------------------|--------------------------------------------------------------------------------------------------------------------------------------------|--------|----------|
| endpoint          | A comma-separated string of endpoints in the format 'host1:port1,host2:port2'. The number of endpoints must be greater than or equal to 1. | string | Yes      |
| username          | The username to use for authentication when connecting to the datastore.                                                                   | string | No       |
| password          | The password to use for authentication when connecting to the datastore.                                                                   | string | No       |
| connection_string | The connection_string to use when connecting to the datastore.                                                                             | string | Yes      |


### http

elasticsearch broker details

| Name              | Description                                                          | Type   | Required |
|-------------------|----------------------------------------------------------------------|--------|----------|
| host              | The host for the Elasticsearch cluster endpoint.                     | string | Yes      |
| port              | The port to use for authentication when connecting to the datastore. | string | No       |
| connection_string | The connection_string to use when connecting to the datastore.       | string | Yes      |
### Flavors

- `k8s`
