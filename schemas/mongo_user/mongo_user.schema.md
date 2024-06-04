# Introduction
Mongo User intent to create mongodb roles and user

## Properties

| Property                | Type                | Required | Description                                                                                                                                    |
|-------------------------|---------------------|----------|------------------------------------------------------------------------------------------------------------------------------------------------|
| `flavor`                | string              | **Yes**  | Implementation selector for the mongo_user resource. e.g. for a resource type default Possible values are: `default`.                          |
| `kind`                  | string              | **Yes**  | Describes the type of resource. Possible values are: `mongo_user`.                                                                             |
| `metadata`              | [object](#metadata) | **Yes**  | Metadata related to the resource                                                                                                               |
| `spec`                  | [object](#spec)     | **Yes**  |                                                                                                                                                |
| `version`               | string              | **Yes**  | This field can be used to pin to a particular version Possible values are: `0.1`, `latest`.                                                    |
| `advanced`              | [object](#advanced) | No       | Advanced MongoDB User Schema                                                                                                                   |
| `depends_on`            |                     | No       | Dependencies on other resources. e.g. application x may depend on mysql                                                                        |
| `disabled`              | boolean             | No       | Flag to disable the resource                                                                                                                   |
| `lifecycle`             | string              | No       | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`) Possible values are: `ENVIRONMENT_BOOTSTRAP`. |
| `provided`              | boolean             | No       | Flag to tell if the resource should not be provisioned by facets                                                                               |
| `conditional_on_intent` | string              | No       | Defining the resource dashboard is dependent on for implementation. e.g for resource of kind redis, value would be "redis"                     |

## Spec

### Properties

| Property      | Type                   | Required | Description                                                                                                                                                                    |
|---------------|------------------------|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `endpoint`    | string                 | **Yes**  | The MongoDB database endpoint. Syntax: `mongodb://<username>:<password>@<endpoint>:<port>`, Eg: `mongodb://mongo:testmongoPassword@test-mongo.default.svc.cluster.local:27017` |
| `permissions` | [object](#Permissions) | **Yes**  | Map of permissions to be applied to user                                                                                                                                       |
| `database`    | string                 | **Yes**  | The Database where user will be created.                                                                                                                                       |

### Permissions

Map of permissions to be applied to user

| Property     | Type   | Required | Description                                                                                                                       |
|--------------|--------|----------|-----------------------------------------------------------------------------------------------------------------------------------|
| `permission` | string | **Yes**  | The actions . Allowed values are https://www.mongodb.com/docs/manual/reference/privilege-actions/#std-label-security-user-actions |
| `database`   | string | **Yes**  | The database name.                                                                                                                |
| `collection` | string | **Yes**  | The collection name. Applies for all collection if empty                                                                          |
| `cluster`    | string | **No**   | Value is true if permissions needs to be applied for all collections.                                                             |

## Advanced

Advanced MongoDB User Schema

### Properties

| Property     | Type                  | Required | Description                           |
|--------------|-----------------------|----------|---------------------------------------|
| `mongo_user` | [object](#mongo_user) | No       | The advanced options for MongoDB User |

### mongo_user

The advanced options for MongoDB User

#### Properties

| Property | Type            | Required | Description                                                                                                                                     |
|----------|-----------------|----------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| `role`   | [object](#role) | No       | The extra options for role. You can refer to this doc for more information - https://www.mongodb.com/docs/manual/reference/method/db.createRole |
| `user`   | [object](#user) | No       | The extra options for user. You can refer to this doc for more information - https://www.mongodb.com/docs/manual/reference/method/db.createUser |

#### role

The extra options for role.

##### Properties

| Property      | Type   | Required | Description                                                                                                                         |
|---------------|--------|----------|-------------------------------------------------------------------------------------------------------------------------------------|
| `rolesToRole` | string | No       | A string of comma separated roles this role inherits privileges. The Option available only for users in admin database              |
| `dbRoles`     | object | No       | A map of databases and roles this role inherits privileges, roles should belong from the same database for non-admin database users |

#### user

The extra options for user. For more information - https://www.mongodb.com/docs/manual/reference/method/db.createUser

##### Properties

| Property                     | Type   | Required | Description                                                                                                                         |
|------------------------------|--------|----------|-------------------------------------------------------------------------------------------------------------------------------------|
| `authenticationRestrictions` | object | No       | To manage authentication restriction                                                                                                |
| `username`                   | string | No       | Name of the user                                                                                                                    |
| `password`                   | string | No       | Password of the user (plain text)                                                                                                   |
| `customData`                 | object | No       | Custom data that defines the user                                                                                                   |
| `mechanisms`                 | string | No       | A comma separated SCRAM mechanisms for user credentials                                                                             |
| `rolesToRole`                | string | No       | A string of comma separated roles this user will be attached to. The Option available only for users in admin database              |
| `dbRoles`                    | object | No       | A map of databases and roles this user will be attached to, roles should belong from the same database for non-admin database users |

## Flavor

- default
