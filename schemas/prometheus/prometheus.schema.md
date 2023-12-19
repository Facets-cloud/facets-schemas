# Introduction
Prometheus configuration intends to configure kube-prometheus-stack in the Facets environment.

## Properties

| Property                | Type                | Required | Description                                                                                                                          |
|-------------------------|---------------------|----------|--------------------------------------------------------------------------------------------------------------------------------------|
| `kind`                  | string              | **Yes**  | Describes the type of resource. Possible values are: `configuration`.                                                                |
| `for`                   | string              | **Yes**  | Possible values are: `prometheus`.                                                                                                   |
| `metadata`              | [object](#metadata) | **Yes**  | Metadata related to the resource.                                                                                                    |
| `spec`                  | [object](#spec)     | **Yes**  |                                                                                                                                      |
| `version`               | string              | **Yes**  | This field can be used to pin to a particular version. Possible values are: `0.1`, `latest`.                                         |
| `advanced`              | [object](#advanced) | No       | Advanced Prometheus Schema.                                                                                                          |
| `depends_on`            |                     | No       | Dependencies on other resources. e.g., application x may depend on MySQL.                                                            |
| `disabled`              | boolean             | No       | Flag to disable the resource.                                                                                                        |
| `lifecycle`             | string              | No       | Describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`). Possible values are: `ENVIRONMENT_BOOTSTRAP`. |
| `conditional_on_intent` | string              | No       | Defining the resource dashboard is dependent on for implementation. e.g., for a resource of kind redis, value would be "redis".      |

## Spec

### Properties

| Property              | Type                           | Required | Description                            |
|-----------------------|--------------------------------|----------|----------------------------------------|
| `alertmanager`        | [object](#Alertmanager)        | **Yes**  | Specifications for alertmanager        |
| `grafana`             | [object](#Grafana)             | **Yes**  | Specifications for grafana             |
| `prometheus-operator` | [object](#Prometheus Operator) | **Yes**  | Specifications for prometheus operator |
| `pushgateway`         | [object](#Pushgateway)         | **Yes**  | Specifications for pushgateway         |
| `prometheus`          | [object](#Prometheus)          | **Yes**  | Specifications for prometheus          |
| `diskSize`            | string                         | **No**   | Disk Size of prometheus                |

### Alertmanager

Specifications for Alertmanager

| Property | Type   | Required | Description                                                                              |
| -- | ------ | -------- | ---------------------------------------------------------------------------------------- |
| `size` | [object](#size) | **Yes**  | Sizing Requests.              |
| `receivers` | [object](#Size) | **Yes**  | Receivers for alertmanager. |

### Grafana

Specifications for Grafana

| Property | Type   | Required | Description                                                                              |
| -- | ------ | -------- | ---------------------------------------------------------------------------------------- |
| `size` | [object](#size) | **Yes**  | Sizing Requests.              |
| `additionalDataSources` | [object](#Additional DataSources) | **No**  | Additional datasource's for Grafana. |

### Prometheus Operator

Specifications for Prometheus Operator

| Property | Type   | Required | Description                                                                              |
| -- | ------ | -------- | ---------------------------------------------------------------------------------------- |
| `size` | [object](#size) | **Yes**  | Sizing Requests.              |

### Pushgateway

Specifications for Pushgateway

| Property | Type   | Required | Description                                                                              |
| -- | ------ | -------- | ---------------------------------------------------------------------------------------- |
| `size` | [object](#size) | **Yes**  | Sizing Requests.              |

### Prometheus

Specifications for Prometheus

| Property | Type   | Required | Description                                                                              |
| -- | ------ | -------- | ---------------------------------------------------------------------------------------- |
| `size` | [object](#size) | **Yes**  | Sizing Requests.              |

#### size

Sizing Requests

| Property         | Type    | Required | Description                                                                                                                           |
|------------------|---------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `cpu`            | string  | No       | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu       |
| `memory`         | string  | No       | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory |

#### receivers

Receivers can have multiple receivers configured for Alertmanager. Each receivers block is defined with the structure below. 

| Property        | Type    | Required | Description                                                |
|-----------------|---------|----------|------------------------------------------------------------|
| `url`           | string  | Yes      | URL of the receiver.                                       |
| `send_resolved` | boolean | No       | Flag to determine whether to notify about resolved alerts. |

#### Additional DataSources

This block can configure multiple datasources in Grafana. Each datasource block is defined with the structure below. Please note that Prometheus is already configured as the `default` datasource.

| Property   | Type    | Required | Description                                            |
|------------|---------|----------|--------------------------------------------------------|
| `name`     | string  | Yes      | Name of the datasource.                                |
| `access`   | string  | No       | Type of access for the datasource.                     |
| `editable` | boolean | Yes      | Flag indicating whether the datasource is editable.    |
| `jsonData` | object  | Yes      | Additional JSON data for the datasource configuration. |
| `orgId`    | integer | Yes      | Organization ID associated with the datasource.        |
| `type`     | string  | Yes      | Type of the datasource.                                |
| `url`      | string  | Yes      | URL of the datasource.                                 |
| `version`  | integer | Yes      | Version of the datasource.                             |

## Advanced

Advanced Prometheus Schema

### Properties

| Property                       | Type   | Required | Description                                                                                                              |
| ------------------------------ | ------ | -------- | ------------------------------------------------------------------------------------------------------------------------ |
| `kube-prometheus-stack.values` | object | No       | Helm values for [kube-prometheus-stack](https://artifacthub.io/packages/helm/prometheus-community/kube-prometheus-stack).  |
| `pushgateway.values`            | object | No       | The advanced options for [pushgateway](https://artifacthub.io/packages/helm/prometheus-community/prometheus-pushgateway). |
