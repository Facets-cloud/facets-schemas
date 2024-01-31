## Introduction

A rabbitmq Instance in reader/writer datastore mode.

## Spec

| Property           | Type                                                              | Required | Description                                              |
|--------------------|-------------------------------------------------------------------|----------|----------------------------------------------------------|
| `rabbitmq_version` | string                                                            | **Yes**  | Version of Rabbitmq e.g. 3.10.7                          |
| `size`             | [object](../../traits/reader-writer-datastore-sizing.schema.json) | **Yes**  | Sizing attribute for postgres writer and reader instance |

## out

| Property     | Type               | Required | Description                                         |
|--------------|--------------------|----------|-----------------------------------------------------|
| `interfaces` | [object](#cluster) | **Yes**  | Broker details interfaces of type cluster as object |


Added an example of out
```json
{
  "out": {
    "interfaces": {
      "cluster": {
        "connection_string" : "string",
        "endpoint" : "string"
      }
    }
  }
}
```
### interfaces

RabbitMQ broker details

| Property            | Type   | Required | Description                    |
|---------------------|--------|----------|--------------------------------|
| `connection_string` | string | No       | Connection string to connect   |
| `host`              | string | No       | Host for service discovery     |
| `name`              | string | No       | Name of interface, same as key |
| `password`          | string | No       | Password to connect (if any)   |
| `port`              | string | No       | Port for service discovery     |
| `username`          | string | No       | Username to connect (if any)   |

### cluster

RabbitMQ cluster configuration

| Name              | Description                                                                                                                                | Type   | Required |
|-------------------|--------------------------------------------------------------------------------------------------------------------------------------------|--------|----------|
| endpoint          | A comma-separated string of endpoints in the format 'host1:port1,host2:port2'. The number of endpoints must be greater than or equal to 1. | string | Yes      |
| username          | The username to use for authentication when connecting to the datastore.                                                                   | string | No       |
| password          | The password to use for authentication when connecting to the datastore.                                                                   | string | No       |
| connection_string | The connection_string to use when connecting to the datastore.                                                                             | string | Yes      |

## Flavors

- k8s