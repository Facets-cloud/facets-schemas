## Introduction
A kafka level intent which can be used to churn out kafka clusters

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

| Property              | Type            | Required | Description                           |
|-----------------------|-----------------|----------|---------------------------------------|
| `authenticated`       | boolean         | **Yes**  | Make this kafka is Password protected |
| `kafka_version`       | string          | **Yes**  | Version of kafka e.g. 3.2.3           |
| `persistence_enabled` | boolean         | **Yes**  | Enable Persistence for this redis     |
| `size`                | [object](#size) | **Yes**  |                                       |

### size

| Property    | Type                 | Required | Description           |
|-------------|----------------------|----------|-----------------------|
| `kafka`     | [object](#kafka)     | **Yes**  | The kafka details     |
| `zookeeper` | [object](#zookeeper) | **Yes**  | The zookeeper details |

#### kafka

The kafka details

| Property        | Type    | Required | Description                                                                                                                           |
|-----------------|---------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `replica_count` | integer | **Yes**  | Number of kafka instances needs to be deployed                                                                                        |
| `cpu`           | string  | No       | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu       |
| `instance`      | string  | No       | Instance name in certain cases                                                                                                        |
| `memory`        | string  | No       | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory |
| `volume`        | string  | No       | Volume request in kubernetes persistent volumes                                                                                       |

#### zookeeper

The zookeeper details

| Property        | Type    | Required | Description                                                                                                                           |
|-----------------|---------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `replica_count` | integer | **Yes**  | Number of zookeeper instances needs to be deployed                                                                                    |
| `cpu`           | string  | No       | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu       |
| `instance`      | string  | No       | Instance name in certain cases                                                                                                        |
| `memory`        | string  | No       | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory |
| `volume`        | string  | No       | Volume request in kubernetes persistent volumes                                                                                       |


## advanced

Additional fields if any for a particular implementation of a resource

| Property | Type           | Required | Description                   |
|----------|----------------|----------|-------------------------------|
| `k8s`    | [object](#k8s) | No       | Advanced k8s values for kafka |

### k8s

Advanced k8s values for kafka

| Property | Type             | Required | Description               |
|----------|------------------|----------|---------------------------|
| `kafka`  | [object](#kafka) | No       | Advanced values for kafka |

#### kafka

Advanced values for kafka

| Property | Type              | Required | Description                                                                                            |
|----------|-------------------|----------|--------------------------------------------------------------------------------------------------------|
| `values` | [object](#values) | No       | Helm values as per the Bitnami [kafka chart](https://github.com/bitnami/charts/tree/master/bitnami/kafka)  |


## out

Output given by the resource for others to refer.

| Property         | Type                      | Required | Description                                             |
|------------------|---------------------------|----------|---------------------------------------------------------|
| `extra_settings` | [object](#extra_settings) | **Yes**  | The extra setting required for kafka brokers connection |
| `interfaces`     | [object](#interfaces)     | **Yes**  | Kafka broker details                                    |

### extra_settings

The extra setting required for kafka brokers connection


| Property | Type             | Required | Description                         |
|----------|------------------|----------|-------------------------------------|
| `kafka`  | [object](#kafka) | No       | Kafka extra settings for connection |

#### kafka

Kafka extra settings for connection

| Property            | Type   | Required | Description                                                                                                       |
|---------------------|--------|----------|-------------------------------------------------------------------------------------------------------------------|
| `sasl_mechanism`    | string | No       | The sasl mechanism required to connect to the kafka broker Possible values are: `SCRAM-SHA-256`, `PLAIN`.         |
| `security_protocol` | string | No       | The security protocol required to connect to the kafka broker Possible values are: `SASL_PLAINTEXT`, `PLAINTEXT`. |

### cluster

Kafka cluster configuration

| Name              | Description                                                                                                                                | Type   | Required |
|-------------------|--------------------------------------------------------------------------------------------------------------------------------------------|--------|----------|
| endpoint          | A comma-separated string of endpoints in the format 'host1:port1,host2:port2'. The number of endpoints must be greater than or equal to 1. | string | Yes      |
| username          | The username to use for authentication when connecting to the datastore.                                                                   | string | No       |
| password          | The password to use for authentication when connecting to the datastore.                                                                   | string | No       |
| connection_string | The connection_string to use when connecting to the datastore.                                                                             | string | Yes      |



### Flavors

* k8s
* [msk](kafka.msk.schema.md)

### Alerts


| Alert Name                      | Description                                                     | mitigation                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 | 
|---------------------------------|-----------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `UnderReplicatedPartitionCount` | Kafka is under replicated partitons                             | Debug the reason why few partitions are under-replicated                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | 
| `ActiveController`              | No broker in the cluster is reporting as the active controller. | During steady state there should be only one active controller per cluster.                                                                                                                                                                                                                                                                                                                                                                                                                                                | 
| `UncleanLeaderElection`         | Unclean partition leader elections in the cluster reported      | When unclean leader election is held among out-of-sync replicas, there is a possibility of data loss if any messages were not synced prior to the loss of the former leader. So if the number of unclean elections is greater than 0, investigate broker logs to determine why leaders were re-elected, and look for WARN or ERROR messages. Consider setting the broker configuration parameter unclean.leader.election.enable to false so that a replica outside of the set of in-sync replicas is never elected leader. |
| `BrokerCount`                   | No Brokers online                                               | No Broker is available. Debug the instance health via metrics & logs                                                                                                                                                                                                                                                                                                                                                                                                                                                       |