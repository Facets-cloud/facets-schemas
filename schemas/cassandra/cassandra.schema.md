## Introduction

This JSON Schema defines the configuration for cassandra DB instance.

## Spec

| Property           | Type                                                              | Required | Description                                              |
|--------------------|-------------------------------------------------------------------|----------|----------------------------------------------------------|
| `cassandra_version` | string                                                            | **Yes**  | Version of Cassandra e.g. 3.11.6                          |
| `size`             | [object](../../traits/size-k8s.schema.json) | **Yes**  | Sizing attribute for cassandra instance |

### size

The size details

| Property        | Type    | Required | Description                                                 |
| --------------- | ------- | -------- | ----------------------------------------------------------- |
| `replica_count` | integer | **Yes**  | Number of cassandra instances needs to be deployed [DEPRECATED] |
| `instance_count` | integer | **Yes** | Number of cassandra instances needs to be deployed |
| `cpu` | string | No | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers |
| `memory` | string | No | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers |
| `volume` | string | No | Volume request in kubernetes persistent volumes |

## advanced

Additional fields if any for a particular implementation of a resource

| Property | Type           | Required | Description                   |
| -------- | -------------- | -------- | ----------------------------- |
| `k8s`    | [object](#k8s) | No       | Advanced k8s values for cassandra |

### k8s

Advanced k8s values for mongo

| Property | Type             | Required | Description               |
| -------- | ---------------- | -------- | ------------------------- |
| `cassandra`  | [object](#cassandra) | No       | Advanced values for cassandra |

#### cassandra

Advanced values for cassandra

| Property | Type              | Required | Description                                                                                               |
| -------- | ----------------- | -------- | --------------------------------------------------------------------------------------------------------- |
| `values` | [object](#values) | No       | Helm values as per the Bitnami [cassandra chart](https://github.com/bitnami/containers/tree/main/bitnami/cassandra) |

## out

| Property     | Type               | Required | Description                                         |
|--------------|--------------------|----------|-----------------------------------------------------|
| `interfaces` | [object](#cluster) | **Yes**  | Connection details of type Cassandra DB |


## NOTE
It is important to find a balance between the resource requests, limits and heap size in order for cassandra pod to start.

for Eg: 
```
limits:
    cpu: 2
    memory: 3Gi
  requests:
    cpu: 2
    memory: 2Gi

maxHeapSize: 2G
newHeapSize: 1G
```

#### Please refer to this [documentation](https://docs.datastax.com/en/archived/cassandra/2.0/cassandra/operations/ops_tune_jvm_c.html) to know how much heap size should be as per your requested resources