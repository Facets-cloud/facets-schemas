# Vault

A Hashicorp Vault Resource

## Properties

| Property     | Type                | Required | Description                                                                                                                                    |
| ------------ | ------------------- | -------- | ---------------------------------------------------------------------------------------------------------------------------------------------- |
| `flavor`     | string              | **Yes**  | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc.                                     |
| `kind`       | string              | **Yes**  | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances                |
| `metadata`   | [object](#metadata) | **Yes**  | Metadata related to the resource                                                                                                               |
| `spec`       | [object](#spec)     | **Yes**  | Specification as per resource types schema                                                                                                     |
| `version`    | string              | **Yes**  | This field can be used to pin to a particular version                                                                                          |
| `advanced`   | [object](#advanced) | No       | Additional fields if any for a particular implementation of a resource                                                                         |
| `depends_on` |                     | No       | Dependencies on other resources. e.g. application x may depend on mysql                                                                        |
| `disabled`   | boolean             | No       | Flag to disable the resource                                                                                                                   |
| `lifecycle`  | string              | No       | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`) Possible values are: `ENVIRONMENT_BOOTSTRAP`. |
| `provided`   | boolean             | No       | Flag to tell if the resource should not be provisioned by facets                                                                               |

## spec

Specification as per resource types schema

| Property              | Type            | Required | Description                         |
|-----------------------| --------------- | -------- |-------------------------------------|
| `vault_version`       | string          | **Yes**  | Version of vault server e.g. 1.17.2 |
| `size`                | [object](#size) | **Yes**  |                                     |

### size

The size details

| Property         | Type    | Required | Description                                                                                                                           |
|------------------| ------- | -------- |---------------------------------------------------------------------------------------------------------------------------------------|
| `instance_count` | integer | **Yes** | Number of vault instances needs to be deployed                                                                                        |
| `cpu`            | string | No | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu       |
| `memory`         | string | No | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory |
| `cpu_limit`      | string | No | CPU limit in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu         |
| `memory_limit`   | string | No | Memory limit in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory   |
| `volume`         | string | No | Volume request in kubernetes persistent volumes                                                                                       |

---

## advanced

Additional fields if any for a particular implementation of a resource

| Property | Type           | Required | Description                   |
| -------- | -------------- | -------- |-------------------------------|
| `k8s`    | [object](#k8s) | No       | Advanced k8s values for vault |

### k8s

Advanced k8s values for vault

| Property | Type             | Required | Description               |
|----------|------------------| -------- |---------------------------|
| `vault`  | [object](#vault) | No       | Advanced values for vault |

#### vault

Advanced values for vault

| Property | Type              | Required | Description                     |
| -------- | ----------------- | -------- |---------------------------------|
| `values` | [object](#values) | No       | Helm values as vault helm chart |



### Flavors

- `k8s`
