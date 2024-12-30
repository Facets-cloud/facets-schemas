# Introduction

Kafka topic intent to create and manage Kafka topics within a Kafka cluster

## Properties

| Property   | Type                | Required | Description                           |
|------------|---------------------|----------|---------------------------------------|
| `flavor`   | string              | **Yes**  | Possible values are: `default`.       |
| `kind`     | string              | **Yes**  | Possible values are: `kafka_topic`.   |
| `spec`     | [object](#spec)     | **Yes**  |                                       |
| `version`  | string              | **Yes**  | Possible values are: `0.1`, `latest`. |
| `advanced` | [object](#advanced) | No       | Advanced section for the module       |

## Advanced

Advanced section for the module

### Properties

| Property      | Type                   | Required | Description                                |
|---------------|------------------------|----------|--------------------------------------------|
| `kafka_topic` | object | No       | Advanced parameters for kafka_topic module |


## Spec

### Properties

| Property | Type              | Required | Description                                                |
|----------|-------------------|----------|------------------------------------------------------------|
| `topics` | [object](#topics) | No       | Contains a map of configurations for multiple Kafka Topics |
| `tls` | [object](#tls) | No       | TLS configuration for the Kafka cluster connection. If not specified, defaults to the default from the Kafka cluster. |

### Topics

Contains a map of configurations for multiple Kafka Topics

| Property           | Type   | Required | Description |
|--------------------|--------|----------|-------------|
| topic_name         | string | Yes      | The topic name.|
| replication_factor | number | Yes       | The replication factor for each partition in the topic being created. If not supplied, defaults to the cluster default.|
| partitions         | number | Yes       | The number of partitions for the topic being created or altered (WARNING: If partitions are increased for a topic that has a key, the partition logic or ordering of the messages will be affected). If not supplied for create, defaults to the cluster default.|
| config             | object | No       | A topic [configuration](https://kafka.apache.org/documentation/#configuration) override for the topic |

### TLS

TLS configuration for the Kafka cluster connection. If not specified, defaults to the default from the Kafka cluster.

| Property                      | Type           | Required | Description |
|-------------------------------|----------------|----------|-------------|
| insecure_skip_verify          | boolean        | No      | Set this to true to disable certificate verification for the Kafka cluster connection. |
| client_certificate_secret_ref | [object](#client-certificate-secret-ref) | No      | Set this to reference a Kubernetes Secret containing the client certificate, key and CA certificate for the Kafka cluster connection. |


#### Client Certificate Secret Ref

Set this to reference a Kubernetes Secret containing the client certificate, key and CA certificate for the Kafka cluster connection.

| Property                      | Type           | Required | Description    |
|-------------------------------|----------------|----------|----------------|
| name                          | string         | Yes      | The name of the Secret containing the client certificate, key and CA certificate for the Kafka cluster connection. |
| namespace                     | string         | Yes      | The namespace of the Secret containing the client certificate key and CA certificate for the Kafka cluster connection. |
| keyField                      | string         | Yes      | The key in the Secret containing the client key for the Kafka cluster connection. |
| certField                     | string         | Yes      | The key in the Secret containing the client certificate for the Kafka cluster connection. |

