# Introduction
PostgreSQL User intent to create postgres role and grants

## Properties

| Property                | Type                | Required | Description                                                                                                                                    |
| ----------------------- | ------------------- | -------- | ---------------------------------------------------------------------------------------------------------------------------------------------- |
| `flavor`                | string              | **Yes**  | Implementation selector for the postgres_user resource. e.g. for a resource type default Possible values are: `default`.                       |
| `kind`                  | string              | **Yes**  | Describes the type of resource. Possible values are: `postgres_user`.                                                                          |
| `metadata`              | [object](#metadata) | **Yes**  | Metadata related to the resource                                                                                                               |
| `spec`                  | [object](#spec)     | **Yes**  |                                                                                                                                                |
| `version`               | string              | **Yes**  | This field can be used to pin to a particular version Possible values are: `0.1`, `latest`.                                                    |
| `advanced`              | [object](#advanced) | No       | Advanced PostgreSQL User Schema                                                                                                                |
| `depends_on`            |                     | No       | Dependencies on other resources. e.g. application x may depend on mysql                                                                        |
| `disabled`              | boolean             | No       | Flag to disable the resource                                                                                                                   |
| `lifecycle`             | string              | No       | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`) Possible values are: `ENVIRONMENT_BOOTSTRAP`. |
| `out`                   | [object](#out)      | No       |                                                                                                                                                |
| `provided`              | boolean             | No       | Flag to tell if the resource should not be provisioned by facets                                                                               |
| `conditional_on_intent` | string              | No       | Defining the resource dashboard is dependent on for implementation. e.g for resource of kind redis, value would be "redis"                     |

## Spec

### Properties

| Property      | Type                   | Required | Description                                                                                                                                                             |
| ------------- | ---------------------- | -------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `endpoint`    | string                 | **Yes**  | The PostgreSQL database endpoint. Syntax: `<username>:<password>@<endpoint>:<port>`, Eg: `postgres:testPostgresPassword@test-postgresql.default.svc.cluster.local:5432` |
| `permissions` | [object](#Permissions) | **Yes**  | Map of permissions to be applied to user                                                                                                                                |

### Permissions

Map of permissions to be applied to user

| Property     | Type   | Required | Description                                                                              |
| ------------ | ------ | -------- | ---------------------------------------------------------------------------------------- |
| `permission` | string | **Yes**  | The canned permission for grant. Allowed values are `ADMIN`, `RWO` and `RO`              |
| `database`   | string | **Yes**  | The database name. Note: You can use wildcard `*` to apply permissions to ALL databases. |
| `schema`     | string | **Yes**  | The schema name.                                                                         |
| `table`      | string | **Yes**  | The table name. Note: You can use wildcard `*` to apply permissions to ALL tables.       |

## Advanced

Advanced PostgreSQL User Schema

### Properties

| Property        | Type                     | Required | Description                              |
| --------------- | ------------------------ | -------- | ---------------------------------------- |
| `postgres_user` | [object](#postgres_user) | No       | The advanced options for PostgreSQL User |

### postgres_user

The advanced options for PostgreSQL User

#### Properties

| Property | Type            | Required | Description                                                                                                                               |
| -------- | --------------- | -------- | ----------------------------------------------------------------------------------------------------------------------------------------- |
| `role`   | [object](#role) | No       | The extra options for role. You can refer to this doc for more information - https://www.postgresql.org/docs/current/role-attributes.html |

#### role

The extra options for role. You can refer to this doc for more information - https://www.postgresql.org/docs/current/role-attributes.html

##### Properties

| Property          | Type                  | Required | Description                                                                                                                   |
| ----------------- | --------------------- | -------- | ----------------------------------------------------------------------------------------------------------------------------- |
| `connectionLimit` | number                | No       | The maximum number of concurrent connections to the database server. Default is 100                                           |
| `privileges`      | [object](#privileges) | No       | A database role can have a number of attributes that define its privileges and interact with the client authentication system |

##### privileges

A database role can have a number of attributes that define its privileges and interact with the client authentication system

###### Properties

| Property      | Type    | Required | Description                                                                                                                                                                                                              |
| ------------- | ------- | -------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `bypassRls`   | boolean | No       | A role must be explicitly given permission to bypass every row-level security (RLS) policy (except for superusers, since those bypass all permission checks)                                                             |
| `createDb`    | boolean | No       | A role must be explicitly given permission to create databases (except for superusers, since those bypass all permission checks)                                                                                         |
| `createRole`  | boolean | No       | A role must be explicitly given permission to create more roles (except for superusers, since those bypass all permission checks)                                                                                        |
| `inherit`     | boolean | No       | A role is given permission to inherit the privileges of roles it is a member of                                                                                                                                          |
| `login`       | boolean | No       | Only roles that have the LOGIN attribute can be used as the initial role name for a database connection                                                                                                                  |
| `replication` | boolean | No       | A role must explicitly be given permission to initiate streaming replication (except for superusers, since those bypass all permission checks). A role used for streaming replication must have LOGIN permission as well |
| `superUser`   | boolean | No       | A database superuser bypasses all permission checks, except the right to log in. This is a dangerous privilege and should not be used carelessly; it is best to do most of your work as a role that is not a superuser   |

## Out

Output given by the resource for others to refer.

### Properties

| Property   | Type   | Required | Description                  |
| ---------- | ------ | -------- | ---------------------------- |
| `password` | string | No       | The PostgreSQL user password |
| `username` | string | No       | The PostgreSQL username      |

## Flavor

- default
