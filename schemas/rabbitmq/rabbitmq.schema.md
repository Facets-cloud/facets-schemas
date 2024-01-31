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