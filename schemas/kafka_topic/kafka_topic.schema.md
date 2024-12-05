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

## advanced

Advanced section for the module

### Properties

| Property      | Type                   | Required | Description                                |
|---------------|------------------------|----------|--------------------------------------------|
| `kafka_topic` | [object](#kafka_topic) | No       | Advanced parameters for kafka_topic module |


## spec

### Properties

| Property | Type              | Required | Description                                                |
|----------|-------------------|----------|------------------------------------------------------------|
| `topics` | [object](#topics) | No       | Contains a map of configurations for multiple Kafka Topics |

### topics

Contains a map of configurations for multiple Kafka Topics

| Property           | Type   | Required | Description |
|--------------------|--------|----------|-------------|
| topic_name         | string | Yes      | The topic name.|
| replication_factor | number | Yes       | The replication factor for each partition in the topic being created. If not supplied, defaults to the cluster default.|
| partitions         | number | Yes       | The number of partitions for the topic being created or altered (WARNING: If partitions are increased for a topic that has a key, the partition logic or ordering of the messages will be affected). If not supplied for create, defaults to the cluster default.|
| config             | object | No       | A topic configuration override for the topic|
