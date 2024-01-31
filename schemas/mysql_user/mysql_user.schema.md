# Introduction
MySQL User intent to create mysql user and grants

## Properties

| Property                | Type                | Required | Description                                                                                                                                    |
| ----------------------- | ------------------- | -------- | ---------------------------------------------------------------------------------------------------------------------------------------------- |
| `flavor`                | string              | **Yes**  | Implementation selector for the mysql_user resource. e.g. for a resource type default Possible values are: `default`.                          |
| `kind`                  | string              | **Yes**  | Describes the type of resource. Possible values are: `mysql_user`.                                                                             |
| `metadata`              | [object](#metadata) | **Yes**  | Metadata related to the resource                                                                                                               |
| `spec`                  | [object](#spec)     | **Yes**  | Specification as per resource types schema                                                                                                     |
| `version`               | string              | **Yes**  | This field can be used to pin to a particular version Possible values are: `0.1`, `latest`.                                                    |
| `advanced`              | [object](#advanced) | No       | Advanced MySQL User Schema                                                                                                                     |
| `depends_on`            |                     | No       | Dependencies on other resources. e.g. application x may depend on mysql                                                                        |
| `disabled`              | boolean             | No       | Flag to disable the resource                                                                                                                   |
| `lifecycle`             | string              | No       | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`) Possible values are: `ENVIRONMENT_BOOTSTRAP`. |
| `out`                   | [object](#out)      | No       |                                                                                                                                                |
| `provided`              | boolean             | No       | Flag to tell if the resource should not be provisioned by facets                                                                               |
| `conditional_on_intent` | string              | No       | Defining the resource dashboard is dependent on for implementation. e.g for resource of kind redis, value would be "redis"                     |

## Spec

### Properties

| Property      | Type                   | Required | Description                                                                                                                                                              |
| ------------- | ---------------------- | -------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `endpoint`    | string                 | **Yes**  | The PostgreSQL database endpoint. Syntax: `<username>:<password>@<endpoint>:<port>`, Eg: `"mysql:testMySQLPassword@test-mysql.default.svc.cluster.local:3306.local:5432` |
| `permissions` | [object](#Permissions) | **Yes**  | Map of permissions to be applied to user                                                                                                                                 |

### Permissions

Map of permissions to be applied to user

| Property     | Type   | Required | Description                                                                         |
| ------------ | ------ | -------- | ----------------------------------------------------------------------------------- |
| `permission` | string | **Yes**  | The canned permission for grant. Allowed values are `ADMIN`, `RWO` and `RO`         |
| `database`   | string | **Yes**  | The database. Note: You can use wildcard `*` to apply permissions to ALL databases. |
| `table`      | string | **Yes**  | The table name. Note: You can use wildcard `*` to apply permissions to ALL table.   |

## Advanced

Advanced MySQL User Schema

### Properties

| Property     | Type                  | Required | Description                              |
| ------------ | --------------------- | -------- | ---------------------------------------- |
| `mysql_user` | [object](#mysql_user) | No       | The advanced options for MySQL User |

### mysql_user

The advanced options for PostgreSQL User

#### Properties

| Property           | Type   | Required | Description                                                                                                                                                                                                                         |
| ------------------ | ------ | -------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `resource_options` | object | No       | The resource options for MySQL User. You can refer to this doc for more information - https://doc.crds.dev/github.com/crossplane-contrib/provider-sql/mysql.sql.crossplane.io/User/v1alpha1@v0.7.0#spec-forProvider-resourceOptions |
| `user_name` | string | No       | User defined name for mysql shard user |
| `user_password` | string | No       | User Defined password for mysql shard user |

## Out

Output given by the resource for others to refer.

### Properties

| Property   | Type   | Required | Description                  |
| ---------- | ------ | -------- | ---------------------------- |
| `password` | string | No       | The MySQL user password |
| `username` | string | No       | The MySQL username      |

## Flavor

- default
- sharded_user
