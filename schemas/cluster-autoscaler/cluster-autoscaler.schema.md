# Introduction
Configuration that intends to configure AWS cluster autoscaler in the Facets environment.

## Properties

| Property                | Type                | Required | Description                                                                                                                          |
|-------------------------|---------------------|----------|--------------------------------------------------------------------------------------------------------------------------------------|
| `kind`                  | string              | **Yes**  | Describes the type of resource. Possible values are: `configuration`.                                                                |
| `for`                   | string              | **Yes**  | Possible values are: `cluster-autoscaler`.                                                                                           |
| `metadata`              | [object](#metadata) | **Yes**  | Metadata related to the resource.                                                                                                    |
| `version`               | string              | **Yes**  | This field can be used to pin to a particular version. Possible values are: `0.1`, `latest`.                                         |
| `advanced`              | [object](#advanced) | No       | Advanced Cluster Autoscaler Schema.                                                                                                  |
| `depends_on`            |                     | No       | Dependencies on other resources. e.g., application x may depend on MySQL.                                                            |
| `disabled`              | boolean             | No       | Flag to disable the resource.                                                                                                        |
| `lifecycle`             | string              | No       | Describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`). Possible values are: `ENVIRONMENT_BOOTSTRAP`. |
| `conditional_on_intent` | string              | No       | Defining the resource dashboard is dependent on for implementation. e.g., for a resource of kind redis, value would be "redis".      |

## Spec

### Properties

| Property | Type            | Required | Description     |
|----------|-----------------|----------|-----------------|
| `spec`   | [object](#size) | **Yes**  | Sizing Requests |

#### size

Sizing Requests

| Property         | Type    | Required | Description                                                                                                                           |
|------------------|---------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `cpu`            | string  | No       | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu       |
| `memory`         | string  | No       | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory |

## Advanced

Advanced Cluster Autoscaler Schema

### Properties

| Property                    | Type   | Required | Description                                                                                                       |
|-----------------------------|--------|----------|-------------------------------------------------------------------------------------------------------------------|
| `cluster-autoscaler.values` | object | No       | Helm values for [cluster-autoscaler](https://artifacthub.io/packages/helm/cluster-autoscaler/cluster-autoscaler). |
