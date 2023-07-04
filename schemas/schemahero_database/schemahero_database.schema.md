## Introduction
Schema for the database object of schemahero


## Spec


| Property     | Type   | Required | Description                                                                                                                                                                           |
|--------------|--------|----------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `connection` | string | **Yes**  | Database connection type                                                                                                                                                              |
| `uri`        | string | **Yes**  | Connection string to connect to the database. Refer to https://schemahero.io/databases/postgresql/connecting/ and https://schemahero.io/databases/mysql/connecting/ for more details. |


## Advanced
| Name                       | Description                                                                  | Datatype | Required |
|----------------------------|------------------------------------------------------------------------------|----------|----------|
| default.immediateDeploy    | Boolean value to determine whether to immediately deploy the database schema | boolean  | no       |
| default.deploySeedData     | Boolean value to determine whether to deploy the seed data to the database.  | boolean  | no       |
| default.enableShellCommand | Boolean value indicating if shell command is enabled                         | boolean  | no       |

## out

| Property | Type   | Required | Description                            |
|----------|--------|----------|----------------------------------------|
| `name`   | string | **Yes**  | Name of the schemahero database object |

Added an example of out
```json
{
  "out": {
    "name": "string"
  }
}
```

## Flavors

- default