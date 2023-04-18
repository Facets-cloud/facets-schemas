## Introduction

A rabbitmq Instance in reader/writer datastore mode.

## Spec

| Property           | Type                                                              | Required | Description                                              |
|--------------------|-------------------------------------------------------------------|----------|----------------------------------------------------------|
| `rabbitmq_version` | string                                                            | **Yes**  | Version of Rabbitmq e.g. 3.10.7                          |
| `size`             | [object](../../traits/reader-writer-datastore-sizing.schema.json) | **Yes**  | Sizing attribute for postgres writer and reader instance |

## Outputs

| Property     | Type   | value               | Required | Description       |
|--------------|--------|---------------------|----------|-------------------|
| `interfaces` | object | [cluster](#cluster) | No       | Master SD details |

### cluster

| Name              | Description                                                                                                                                | Type   | Required |
|-------------------|--------------------------------------------------------------------------------------------------------------------------------------------|--------|----------|
| endpoint          | A comma-separated string of endpoints in the format 'host1:port1,host2:port2'. The number of endpoints must be greater than or equal to 1. | string | no       |
| username          | The username to use for authentication when connecting to the datastore.                                                                   | string | No       |
| password          | The password to use for authentication when connecting to the datastore.                                                                   | string | No       |
| connection_string | The connection_string to use when connecting to the datastore.                                                                             | string | Yes      |


## Flavors

- k8s


## Alerts

| Alert Name         | Impact                                      | Mitigation                                                                                                         |
|--------------------|---------------------------------------------|--------------------------------------------------------------------------------------------------------------------|
| RabbitMQServerDown    | RabbitMQ is inaccessible                    | Debug the instance health via metrics & logs                                                                       |
| RabbitMqClusterNotAllNodesRunning | RabbitMQ has very low node count accessible | Check the cluster's node count and trail node's inacitivty from dashboard                                          | 
| RabbitMqMemoryUsageHigh   | RabbitMQ memory usage is high               | Trail the memory usage of the rabbitmq server from the dashboard for all the instances as memory usage spiked <90% |