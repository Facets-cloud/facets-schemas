## Introduction

A Redis Instance in master/slave or reader/writer mode.

## Spec

| Property              | Type                                    | Required | Description                        |
|-----------------------|-----------------------------------------|----------|------------------------------------|
| `authenticated`       | boolean                                 | **Yes**  | Make this redis Password protected |
| `persistence_enabled` | boolean                                 | **Yes**  | Enable Persistence for this redis  |
| `redis_version`       | string                                  | **Yes**  | Version of redis e.g. 6.3          |
| `size`                | [object](../../traits/reader-writer.md) | **Yes**  |                                    |

## Outputs

| Property     | Type                                                      | Required | Description |
|--------------|-----------------------------------------------------------|----------|-------------|
| `interfaces` | [object](../../traits/reader-writer-interfaces.schema.md) | No       |             |


## Flavors

- k8s
- elasticache
- memorystore
- azure_cache

## Alerts

| Alert Name                    | Impact                         | Mitigation                                           |
|-------------------------------|--------------------------------|------------------------------------------------------|
| RedisDown                     | Redis is inaccessible          | Debug the instance health via metrics & logs         |
 | RedisOutOfConfiguredMaxmemory | Redis is running out of memory | Debug using redis dashboards in Facets control plane |
