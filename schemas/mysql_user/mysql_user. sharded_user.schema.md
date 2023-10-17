# Introduction

Mysql_user of flavor sharded_user implementation

## Spec Configuration

### Properties

| Property      | Type                   | Required | Description                                                                                                                                                              |
| ------------- | ---------------------- | -------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `endpoint`    | string                 | **Yes**  | The PostgreSQL database endpoint. Syntax: String of Comma separated endpoints `<username>:<password>@<endpoint>:<port>`, Eg: `"mysql:testMySQLPassword@test-mysql.default.svc.cluster.local:3306.local:5432, alpha-mysql:vcgschgf@alpha-mysql.default.svc.cluster.local:3306"` |
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