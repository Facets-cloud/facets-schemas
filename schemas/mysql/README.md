# Introduction

MySQL intent to deploy AWS Aurora,RDS compatible MySQL in AWS, Cloudsql compatible mysql in GCP.

## Properties

| Property     | Type            | Required | Description                                                                                                                                                      |
| ------------ | --------------- | -------- |------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `flavor`     | string          | **Yes**  | Implementation selector for the resource. e.g. for a resource type mysql, rds, aurora, cloudsql etc.                                                             |
| `kind`       | string          | **Yes**  | Describes the type of resource. e.g. mysql, application etc. If not specified, fallback is the `folder_name`/instances                                           |
| `metadata`   | object          | **Yes**  | Metadata related to the resource. e.g. name                                                                                                                      |
| `spec`       | [object](#spec) | **Yes**  | Specification as per resource types schema                                                                                                                       |
| `version`    | string          | **Yes**  | This field can be used to pin to a particular version                                                                                                            |
| `advanced`   | object          | No       | Additional fields if any for a particular implementation of a resource                                                                                           |
| `depends_on` | list            | No       | Dependencies on other resources. e.g. application x may depend on mysql                                                                                          |
| `disabled`   | boolean         | No       | Flag to disable the resource. By default it is false                                                                                                             |
| `lifecycle`  | string          | No       | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`) Possible values are: `ENVIRONMENT_BOOTSTRAP` and `ENVIRONMENT`. |
| `out`        | [object](#out)  | No       | Output given by the resource for others to refer.                                                                                                                |
| `provided`   | boolean         | No       | Flag to tell if the resource should not be provisioned by facets                                                                                                 |

## Spec

Specification as per resource types schema

| Property     | Type       | Required | Description                                                    |
| ------------ |------------| -------- |----------------------------------------------------------------|
| `mysql_version`     | string     | **Yes**  | Flavour compatible Mysql Version. e.g. 8.0.mysql_aurora.3.02.0 |
| `size`       | [object](../../traits/reader-writer-datastore-sizing.schema.json) | **Yes**  | Sizing attribute for Mysql writer and reader instance          |

## Out

Output given by the resource for others to refer.

| Property     | Type                                                        | Required | Description                                                          |
|--------------|-------------------------------------------------------------|----------|----------------------------------------------------------------------|
| `interfaces` | [object](../../traits/reader-writer-interfaces.schema.json) | **No**   | The output for your module, this can be generated or provided |

#### Property  interfaces

| Property | Type              | Required | Description        |
| -------- | ----------------- | -------- | ------------------ |
| `writer` | [object](#writer) | **Yes**  | The writer details |
| `reader` | [object](#reader) | No       | The reader details |


## reader

The reader details

### Properties

| Property            | Type   | Required | Description                    |
|---------------------|--------|----------|--------------------------------|
| `connection_string` | string | No       | Connection string to connect   |
| `host`              | string | No       | Host for service discovery     |
| `name`              | string | No       | Name of interface, same as key |
| `password`          | string | No       | Password to connect (if any)   |
| `port`              | string | No       | Port for service discovery     |
| `username`          | string | No       | Username to connect (if any)   |

## writer

The writer details

### Properties

| Property            | Type   | Required | Description                    |
|---------------------|--------|----------|--------------------------------|
| `connection_string` | string | No       | Connection string to connect   |
| `host`              | string | No       | Host for service discovery     |
| `name`              | string | No       | Name of interface, same as key |
| `password`          | string | No       | Password to connect (if any)   |
| `port`              | string | No       | Port for service discovery     |
| `username`          | string | No       | Username to connect (if any)   |



## Flavor

- [Aurora](mysql.aurora.schema.md)
- [RDS](mysql.rds.schema.md)
- [Cloudsql](mysql.cloudsql.schema.md)
- [Flexible Server](mysql.flexible_server.schema.md)

## Alerts

| Alert Name                  | Description                                                         | 
|-----------------------------|---------------------------------------------------------------------|
| `CCMysqlDown`               | MySQL server down                                                   | 
| `CCMysqlTooManyConnections` | MySQL connections has crossed 80% of its max connections configured | 
