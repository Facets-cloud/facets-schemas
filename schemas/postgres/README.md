## Introduction

A Postgres Instance in master/slave or reader/writer mode.

## Spec

| Property              | Type            | Required | Description                                              |
|-----------------------|-----------------|----------|----------------------------------------------------------|
| `postgres_version`    | string          | **Yes**  | Version of Postgres e.g.POSTGRES_12                      |
| `size`                | [object](../../traits/size.md) | **Yes**  | Sizing attribute for postgres writer and reader instance |

## Outputs

| Property     | Type                                                     | Required | Description                                                                |
|--------------|----------------------------------------------------------|----------|----------------------------------------------------------------------------|
| `instances`  | Map<string, [interface](../../traits/interface.md)>      | No       | List of all instances                                                      |
| `interfaces` | [object](../../traits/reader-writer-interfaces.schema.md) | No       | Master SD details                                                          |
| `spec`       | [object](#spec)                                          | No       | Details of the sizing                                                      |
| `defaultDatabase`| String      | Yes | The default database was created after creating the database               |
| `postgresVersion` | String | Yes | A version of the Postgres database                                         | 
|`excludedDatabases` | String | Yes | List of the databases to be excluded for collection of Prometheus metrics. |




## Flavors

- aurora
- cloudsql

## Alerts

| Alert Name                 | Impact                                                                                    | Mitigation                                                                                                 |
|----------------------------|-------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|
| PostgresqlDown                           | PostgresSQL server is inaccessible                                                        | Debug the instance health via metrics & logs                                                               |
| PostgresqlTooManyConnections | Performance degrades precipitously (both in terms of transactions-per-second and latency) | Debug the instance checking the metrics like transactions-per-second and latency VS Concurrent connections |
| PostgresqlTooManyDeadTuples | Increase in idle table snapshots utilization | Debug the disk utilization as due to WAL generation and high transaction database snapshots residues which leads to disk utilization| 
