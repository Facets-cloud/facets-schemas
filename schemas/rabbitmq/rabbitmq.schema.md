## Introduction

A rabbitmq Instance in reader/writer datastore mode.

## Spec

| Property           | Type                                                              | Required | Description                                              |
|--------------------|-------------------------------------------------------------------|----------|----------------------------------------------------------|
| `rabbitmq_version` | string                                                            | **Yes**  | Version of Rabbitmq e.g. 3.10.7                          |
| `size`             | [object](../../traits/reader-writer-datastore-sizing.schema.json) | **Yes**  | Sizing attribute for postgres writer and reader instance |

## Outputs

| Property            | Type                                                   | Required | Description                                                                |
|---------------------|--------------------------------------------------------|----------|----------------------------------------------------------------------------|
| `interfaces`        | [object](../../traits/datastore-interface.schema.json) | No       | Master SD details                                                          |


## Flavors

- k8s


## Alerts

| Alert Name         | Impact                                      | Mitigation                                                                                                         |
|--------------------|---------------------------------------------|--------------------------------------------------------------------------------------------------------------------|
| RabbitMQServerDown    | RabbitMQ is inaccessible                    | Debug the instance health via metrics & logs                                                                       |
| RabbitMqClusterNotAllNodesRunning | RabbitMQ has very low node count accessible | Check the cluster's node count and trail node's inacitivty from dashboard                                          | 
| RabbitMqMemoryUsageHigh   | RabbitMQ memory usage is high               | Trail the memory usage of the rabbitmq server from the dashboard for all the instances as memory usage spiked <90% |