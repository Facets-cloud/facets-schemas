## Introduction

A Postgres Instance in reader/writer datastore mode.

## Spec

| Property           | Type                                                              | Required | Description                                              |
|--------------------|-------------------------------------------------------------------|----------|----------------------------------------------------------|
| `postgres_version` | string                                                            | **Yes**  | Version of Postgres e.g. 12.11                           |
| `size`             | [object](../../traits/reader-writer-datastore-sizing.schema.json) | **Yes**  | Sizing attribute for postgres writer and reader instance |
| `max_connections`  | object({"reader": number, "writer": number })                     | **No**  | The maximum permitted number of simultaneous client connections. |
| `db_schemas`  | object({"schema1": {"db": "<db_name>", "schema": "<schema_name>" }})                     | **No**  | Map of schema objects containing schema name and db name under which schema needs to be created.  |

## Outputs

| Property            | Type                                                   | Required | Description                                                                |
|---------------------|--------------------------------------------------------|----------|----------------------------------------------------------------------------|
| `interfaces`        | [object](../../traits/datastore-interface.schema.json) | No       | Master SD details                                                          |





## Flavors

- [Aurora](postgres.aurora.schema.md)
- [Cloudsql](postgres.cloudsql.schema.md)
- [AlloyDB](postgres.alloydb.schema.md)
- [K8s](postgres.k8s.schema.md)

## Alerts

| Alert Name                     | Impact                                                                                    | Mitigation                                                                                                                                                                                                         |
|--------------------------------|-------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `PostgresqlDown`               | PostgresSQL server is inaccessible                                                        | Debug the instance health via metrics & logs                                                                                                                                                                       |
| `PostgresqlTooManyConnections` | Performance degrades precipitously (both in terms of transactions-per-second and latency) | Debug the instance checking the metrics like transactions-per-second and latency VS Concurrent connections. Refer Max connection and Database stats in Postgres dashboard for analysis.                            |
| `PostgresqlTooManyDeadTuples`  | Increase in idle table's snapshot                                                         | Debug the disk utilization as due to WAL generation and high transaction, database snapshot residues leads to high disk utilization. Refer "WAL" and "transation" titled pane in the dashboard for more reference. | 
