Schema for Prometheus

## Properties

| Property   | Type                | Required | Description                                                                                                                                                    |
|------------|---------------------|----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `spec`     | [object](#spec)     | **Yes**  |                                                                                                                                                                |
| `advanced` | [object](#advanced) | No       | Advanced block of the Prometheus configuration                                                                                                                 |
| `flavor`   | string              | No       | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc. Possible values are: `default`, `victoria_metrics`. |

## advanced

Advanced block of the Prometheus configuration

### Properties

| Property                     | Type                                  | Required | Description                                                                                                                                           |
|------------------------------|---------------------------------------|----------|-------------------------------------------------------------------------------------------------------------------------------------------------------|
| `enable_host_anti_affinty`   | boolean                               | No       | Enable if you want to spread victoria metrics pods across different nodes, should only be enabled when flavor is victoria_metrics and mode is cluster |
| `enable_migration`           | boolean                               | No       | Enable if you want to migrate existing prometheus data to victoria_metrics. this should be only enabled when flavor is victoria_metrics               |
| `kube-prometheus-stack`      | [object](#kube-prometheus-stack)      | No       | Advanced values of kube-prometheus-stack                                                                                                              |
| `pushgateway`                | [object](#pushgateway)                | No       | Advanced values of pushgateway                                                                                                                        |
| `victoria-metrics-k8s-stack` | [object](#victoria-metrics-k8s-stack) | No       | Advanced values of victoria-metrics-k8s-stack                                                                                                         |

### kube-prometheus-stack

Advanced values of kube-prometheus-stack

#### Properties

| Property | Type              | Required | Description                                                                                                       |
|----------|-------------------|----------|-------------------------------------------------------------------------------------------------------------------|
| `values` | [object](#values) | No       | Helm values as per the helm chart https://artifacthub.io/packages/helm/prometheus-community/kube-prometheus-stack |

#### values

Helm values as per the helm chart https://artifacthub.io/packages/helm/prometheus-community/kube-prometheus-stack

| Property | Type | Required | Description |
|----------|------|----------|-------------|

### pushgateway

Advanced values of pushgateway

#### Properties

| Property | Type              | Required | Description                                                                                                        |
|----------|-------------------|----------|--------------------------------------------------------------------------------------------------------------------|
| `values` | [object](#values) | No       | Helm values as per the helm chart https://artifacthub.io/packages/helm/prometheus-community/prometheus-pushgateway |

#### values

Helm values as per the helm chart https://artifacthub.io/packages/helm/prometheus-community/prometheus-pushgateway

| Property | Type | Required | Description |
|----------|------|----------|-------------|

### victoria-metrics-k8s-stack

Advanced values of victoria-metrics-k8s-stack

#### Properties

| Property | Type              | Required | Description                                                                                                                                |
|----------|-------------------|----------|--------------------------------------------------------------------------------------------------------------------------------------------|
| `values` | [object](#values) | No       | Helm values as per the helm chart https://github.com/VictoriaMetrics/helm-charts/blob/master/charts/victoria-metrics-k8s-stack/values.yaml |

#### values

Helm values as per the helm chart https://github.com/VictoriaMetrics/helm-charts/blob/master/charts/victoria-metrics-k8s-stack/values.yaml

| Property | Type | Required | Description |
|----------|------|----------|-------------|

## spec

### Properties

| Property              | Type                           | Required | Description                                                                                                                                             |
|-----------------------|--------------------------------|----------|---------------------------------------------------------------------------------------------------------------------------------------------------------|
| `alertmanager`        | [object](#alertmanager)        | **Yes**  | Alertmanager specifications                                                                                                                             |
| `grafana`             | [object](#grafana)             | **Yes**  | Grafana specifications                                                                                                                                  |
| `prometheus`          | [object](#prometheus)          | **Yes**  | Prometheus specifications                                                                                                                               |
| `pushgateway`         | [object](#pushgateway)         | **Yes**  | Pushgateway specifications                                                                                                                              |
| `diskSize`            | string                         | No       | Size of the prometheus PV                                                                                                                               |
| `mode`                | string                         | No       | if the flavor is victoria_metrics, you can choose your mode of installation. either standalone or cluster Possible values are: `cluster`, `standalone`. |
| `prometheus-operator` | [object](#prometheus-operator) | No       | Prometheus Operator specifications                                                                                                                      |
| `vmagent`             | [object](#vmagent)             | No       | vmalert specifications                                                                                                                                  |
| `vmalert`             | [object](#vmalert)             | No       | vmalert specifications                                                                                                                                  |
| `vminsert`            | [object](#vminsert)            | No       | vminsert specifications                                                                                                                                 |
| `vmselect`            | [object](#vmselect)            | No       | vmselect specifications                                                                                                                                 |
| `vmsingle`            | [object](#vmsingle)            | No       | vmsingle specifications                                                                                                                                 |
| `vmstorage`           | [object](#vmstorage)           | No       | vmstorage specifications                                                                                                                                |

### alertmanager

Alertmanager specifications

#### Properties

| Property    | Type                 | Required | Description                |
|-------------|----------------------|----------|----------------------------|
| `receivers` | [object](#receivers) | No       | Receivers for alertmanager |
| `size`      | [object](#size)      | No       | Size of alertmanager pod   |

#### receivers

Receivers for alertmanager

| Property | Type | Required | Description |
|----------|------|----------|-------------|

#### size

Size of alertmanager pod

##### Properties

| Property | Type   | Required | Description                                                                                                                           |
|----------|--------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `cpu`    | string | **Yes**  | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu       |
| `memory` | string | **Yes**  | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory |
| `volume` | string | No       | Volume request in kubernetes persistent volumes                                                                                       |

### grafana

Grafana specifications

#### Properties

| Property                | Type                             | Required | Description              |
|-------------------------|----------------------------------|----------|--------------------------|
| `additionalDataSources` | [object](#additionaldatasources) | No       | Additional data sources  |
| `size`                  | [object](#size)                  | No       | Size of alertmanager pod |

#### additionalDataSources

Additional data sources

| Property | Type | Required | Description |
|----------|------|----------|-------------|

#### size

Size of alertmanager pod

##### Properties

| Property | Type   | Required | Description                                                                                                                           |
|----------|--------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `cpu`    | string | **Yes**  | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu       |
| `memory` | string | **Yes**  | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory |
| `volume` | string | No       | Volume request in kubernetes persistent volumes                                                                                       |

### prometheus

Prometheus specifications

#### Properties

| Property  | Type            | Required | Description                                |
|-----------|-----------------|----------|--------------------------------------------|
| `enabled` | boolean         | No       | you want prometheus to be enabled/disabled |
| `size`    | [object](#size) | No       | Size of alertmanager pod                   |

#### size

Size of alertmanager pod

##### Properties

| Property | Type   | Required | Description                                                                                                                           |
|----------|--------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `cpu`    | string | **Yes**  | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu       |
| `memory` | string | **Yes**  | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory |
| `volume` | string | No       | Volume request in kubernetes persistent volumes                                                                                       |

### prometheus-operator

Prometheus Operator specifications

#### Properties

| Property | Type            | Required | Description              |
|----------|-----------------|----------|--------------------------|
| `size`   | [object](#size) | No       | Size of alertmanager pod |

#### size

Size of alertmanager pod

##### Properties

| Property | Type   | Required | Description                                                                                                                           |
|----------|--------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `cpu`    | string | **Yes**  | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu       |
| `memory` | string | **Yes**  | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory |
| `volume` | string | No       | Volume request in kubernetes persistent volumes                                                                                       |

### pushgateway

Pushgateway specifications

#### Properties

| Property | Type            | Required | Description              |
|----------|-----------------|----------|--------------------------|
| `size`   | [object](#size) | No       | Size of alertmanager pod |

#### size

Size of alertmanager pod

##### Properties

| Property | Type   | Required | Description                                                                                                                           |
|----------|--------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `cpu`    | string | **Yes**  | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu       |
| `memory` | string | **Yes**  | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory |
| `volume` | string | No       | Volume request in kubernetes persistent volumes                                                                                       |

### vmagent

vmalert specifications

#### Properties

| Property | Type            | Required | Description         |
|----------|-----------------|----------|---------------------|
| `size`   | [object](#size) | No       | Size of vmagent pod |

#### size

Size of vmagent pod

##### Properties

| Property | Type   | Required | Description                                                                                                                           |
|----------|--------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `cpu`    | string | **Yes**  | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu       |
| `memory` | string | **Yes**  | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory |
| `volume` | string | No       | Volume request in kubernetes persistent volumes                                                                                       |

### vmalert

vmalert specifications

#### Properties

| Property | Type            | Required | Description         |
|----------|-----------------|----------|---------------------|
| `size`   | [object](#size) | No       | Size of vmalert pod |

#### size

Size of vmalert pod

##### Properties

| Property | Type   | Required | Description                                                                                                                           |
|----------|--------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `cpu`    | string | **Yes**  | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu       |
| `memory` | string | **Yes**  | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory |
| `volume` | string | No       | Volume request in kubernetes persistent volumes                                                                                       |

### vminsert

vminsert specifications

#### Properties

| Property | Type            | Required | Description          |
|----------|-----------------|----------|----------------------|
| `size`   | [object](#size) | No       | Size of vminsert pod |

#### size

Size of vminsert pod

##### Properties

| Property | Type   | Required | Description                                                                                                                           |
|----------|--------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `cpu`    | string | **Yes**  | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu       |
| `memory` | string | **Yes**  | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory |
| `volume` | string | No       | Volume request in kubernetes persistent volumes                                                                                       |

### vmselect

vmselect specifications

#### Properties

| Property | Type            | Required | Description          |
|----------|-----------------|----------|----------------------|
| `size`   | [object](#size) | No       | Size of vmselect pod |

#### size

Size of vmselect pod

##### Properties

| Property | Type   | Required | Description                                                                                                                           |
|----------|--------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `cpu`    | string | **Yes**  | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu       |
| `memory` | string | **Yes**  | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory |
| `volume` | string | No       | Volume request in kubernetes persistent volumes                                                                                       |

### vmsingle

vmsingle specifications

#### Properties

| Property | Type            | Required | Description          |
|----------|-----------------|----------|----------------------|
| `size`   | [object](#size) | No       | Size of vmsingle pod |

#### size

Size of vmsingle pod

##### Properties

| Property | Type   | Required | Description                                                                                                                           |
|----------|--------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `cpu`    | string | **Yes**  | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu       |
| `memory` | string | **Yes**  | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory |
| `volume` | string | No       | Volume request in kubernetes persistent volumes                                                                                       |

### vmstorage

vmstorage specifications

#### Properties

| Property | Type            | Required | Description           |
|----------|-----------------|----------|-----------------------|
| `size`   | [object](#size) | No       | Size of vmstorage pod |

#### size

Size of vmstorage pod

##### Properties

| Property | Type   | Required | Description                                                                                                                           |
|----------|--------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `cpu`    | string | **Yes**  | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu       |
| `memory` | string | **Yes**  | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory |
| `volume` | string | No       | Volume request in kubernetes persistent volumes                                                                                       |

