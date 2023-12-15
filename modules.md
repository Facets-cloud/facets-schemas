---
title: "Defining Blueprint"
slug: "modules"
excerpt: "Complete Architecture in one place"
hidden: true
createdAt: "2022-10-07T05:05:27.568Z"
updatedAt: "2022-10-12T06:58:11.807Z"
---
# Introduction

A Blueprint in Facets Control Plane is a logical definition of the services and resources you require. Optionally, it includes the schema, seed data, dashboards, alerts, and everything else that you would want to keep as a single source of truth.

To achieve the above, the complete architecture is represented in terms of resource JSONs. Each resource has a type associated with the basis of the functionality it achieves. e.g. MySQL, Application, etc.

# Terms

| Term                    | Description                                                                  |
| :---------------------- | :--------------------------------------------------------------------------- |
| Blueprint               | Collection of resources to create a functional product                       |
| Resource                | The building block of a Blueprint                                            |
| Resource/Type or Intent | A resource expressible in facets JSON representation having a defined schema |
| Environment             | Manifestation of a blueprint in any cloud                                    |

## Resource Type/ Intent

Any defined resource type is called an `Intent`.

Some examples of `intents` are Redis, MySQL, Peering, Load Balancers, etc. Typically, any technology that needs to define in your product stack can be an `intent`.

An `intent` is defined in a schema called the `Intent Schema`, which is a publicly hosted JSON Schema. 

You can define a resource in your blueprint by adding an `<intent>.json` file with the respective schema of the intent. The directory structure for the same looks like `<intent name>/instances/<json>`.

```bash
vim mysql/instances/my-mysql.json
```



# Schema for a Resource Type/ Intent

The following fields consist of a basic schema for any Resource Type/Intent.

## kind

`Kind` is used to specify the type of resource/`intent`. If not specified, the fallback is determined by where the JSON is placed `folder_name`/instances 

- Application, MySQL, or Redis are a `kind` of `intent`.

## flavor

`Flavor` is used to select a particular implementation for a Resource Type/`Intent`.

- `k8s`, `elasticache`, and `memorystore` are all flavors of Redis Intent

## version

In case of multiple versions of a resource implementation, this field can be used to pin to a particular version. The default version is `latest`

## lifecycle

There are multiple phases in the lifecycle of an environment, `lifecycle` describes the phase in which a particular resource has to be invoked.

- `ENVIRONMENT_BOOTSTRAP`, and `ENVIRONMENT_NORMAL` are different values for `lifecycle`.

## disabled

As the name suggests, this is a flag to disable a resource.

## spec

This section is used for specifying properties specific to a Resource Type/ `Intent`. These properties are independent of the implementation and are relevant to the `Intent`

- e.g. Redis version for Redis `Intent`
- e.g. No. of slaves for a Redis `Intent` 

## out

The `out` section is there so that the user may know what outputs are there in a resource type/Intent to refer from other resources. This is very essential in wiring. For example, an application may want to know the MySQL username.

Any field in this block can be referenced in other JSON by following the convention

```JSON
{
  "format": "${resource_type.resource_name.out.fieldname}",
  "example": "${mysql.omsdb.out.interfaces.master.host}"
}
```



## advanced

Every implementation/ `flavor` is free to inherit the resource type schema and add additional fields in the `advanced` section. 

## provided

Flag to tell if facets should not provision the resource. The Out values have to be provided by the user in this case.

- A MySQL can be provisioned outside the facets but can exist in the blueprint for others to refer to the URL, username, etc.

***



# Traits

There are many traits that may be common across resource types/`Intents` like there can be multiple resources that expose their endpoints or which take sizing, all such common features are extracted in traits to maintain consistency across `Intent Schemas`

## Base

| Key          | Optional | Type    | Description                                                                                                                     |
| :----------- | :------- | :------ | :------------------------------------------------------------------------------------------------------------------------------ |
| `kind`       | No       | string  | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances |
| `flavor`     | No       | string  | Implementation selector for the resource. e.g. for a resource type ingress, it can be default, aws_alb, gcp_alb etc.            |
| `version`    | No       | string  | Used to pin to a particular version. Default value `latest`.                                                                    |
| `disabled`   | Yes      | boolean | Flag to disable the resource.                                                                                                   |
| `provided`   | Yes      | boolean | Flag to tell if the resource should not be provisioned by Facets.                                                               |
| `depends_on` | Yes      | array   | Dependencies on other resources. e.g. application x may depend on mysql.                                                        |
| `metadata`   | No       | object  | Metadata related to the resource.                                                                                               |
| `spec`       | No       | object  | Specification as per resource types schema                                                                                      |
| `out`        | Yes      | object  | Output given by the resource for others to refer                                                                                |
| `advanced`   | Yes      | object  | Additional fields if any for a particular implementation of a resource                                                          |

> 📘 NOTE: `spec`, `out `and `advanced` will vary per intent.

## Interface

| Key        | Optional | Type   | Description                     |
| :--------- | :------- | :----- | :------------------------------ |
| `username` | Yes      | string | Username to connect (if any).   |
| `password` | Yes      | string | Password to connect (if any).   |
| `host`     | Yes      | string | Host for service discovery.     |
| `port`     | Yes      | string | Port for service discovery.     |
| `name`     | Yes      | string | Name of interface, same as key. |

## Reader-Writer Interface

| Key      | Optional | Type                                                            | Description         |
| :------- | :------- | :-------------------------------------------------------------- | :------------------ |
| `reader` | Yes      | [Interface](https://readme.facets.cloud/docs/modules#interface) | The reader details. |
| `writer` | No       | [Interface](https://readme.facets.cloud/docs/modules#interface) | The writer details. |

## Reader-Writer

| Key                    | Optional | Type                                                  | Description                                   |
| :--------------------- | :------- | :---------------------------------------------------- | :-------------------------------------------- |
| `reader`               | Yes      | [Size](https://readme.facets.cloud/docs/modules#size) | Size of reader                                |
| `writer`               | No       | [Size](https://readme.facets.cloud/docs/modules#size) | Size of writer                                |
| `reader.replica_count` | No       | integer                                               | Number of instances to be deployed for reader |
| `writer.replica_count` | No       | integer                                               | Number of instances to be deployed for writer |

## Size

| Key        | Optional | Type   | Description                     |
| :--------- | :------- | :----- | :------------------------------ |
| `cpu`      | Yes      | string | Number of cores in CPU          |
| `memory`   | Yes      | string | Memory size in GB               |
| `volume`   | Yes      | string | Disk space size in GB           |
| `instance` | Yes      | string | Instance name in certain cases. |