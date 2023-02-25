# Intent Schema Guidelines

## Terms

| Term | Description |
|------|-------------|
|**Resource**| An entity declared in facets|
|**Type of Resource** or **Intent**|A resource expressible in facets JSON representation having a defined schema|
|**Implementation of Resource** or **Flavor**|A specific way of implementing a resource, for example redis can be implemented as a stateful set in kubernetes and elasticcache in AWS.|
|**Blueprint**|Collection of resource to create a functional product|
|**Environment**|Manifestation of this blueprint in any cloud|
|**User**|Developer or ops person who is creating the blueprint|

## Basic Schema for a resource type/ intent
Anatomy of a Facets Resource JSON

### Introduction
In Facets, a resource is described using a JSON file that follows a specific schema. This schema defines the different properties of a resource and how it should be provisioned within an environment. In this document, we will outline the anatomy of a resource JSON file in Facets and explain the various properties it contains.

#### Kind:
The kind property specifies the type of resource that the JSON file represents. For example, it could be an ingress, an application, a MySQL database, etc. If this property is not specified, the default value is the folder name/instances.

#### Flavor:
The flavor property is used to select a specific implementation of the resource type. For example, for a resource type of ingress, a flavor of default, aws_alb, or gcp_alb could be specified. This property allows for flexibility in choosing the implementation that best fits the needs of the environment.

#### Version:
The version property is used to specify a particular version of the resource implementation. This is useful when there are multiple versions of an implementation available, and you want to pin the resource to a specific version. The default version is the latest version available.

#### Disabled:
The disabled property is a boolean flag that allows the user to disable the resource. This is useful when a resource is not needed or is temporarily unavailable.

#### Provided:
The provided property is a boolean flag that specifies whether the resource should be provisioned by Facets or not. For example, a MySQL database may be provisioned outside of Facets, but can still exist within the blueprint for other resources to refer to its URL, username, etc. In this case, the provided property would be set to true, and the out section would be populated by the user.

#### Depends On:
The depends_on property lists any dependencies that the resource has on other resources. For example, an application may depend on a MySQL database. The depends_on property would be set to ["mysql.y"].

#### Metadata:
The metadata property contains metadata related to the resource. This includes the name of the resource and any other relevant information. If the name property is not specified, the default value is the file name.

#### Spec:
The spec property contains the specification for the resource. This is where the specific details of the resource are defined, and it follows the schema for the specific resource type.

#### Out:
The out property contains the output given by the resource for others to refer. This includes any relevant information that other resources may need to use, such as URLs, usernames, and passwords. The out section follows the schema for the specific resource type.

#### Advanced:
The advanced property is an optional field that contains additional fields that are specific to a particular implementation of a resource. This allows for greater customization and configuration of the resource beyond the standard fields.

# Supported Services

| Kind  | Flavor         | Version | Schema                                                                                            | Sample                                                         | Readme                                                              |
|-------|----------------|---------|---------------------------------------------------------------------------------------------------|----------------------------------------------------------------|--------------------------------------------------------------------|
| mysql | cloudsql       |         | https://facets-cloud.github.io/facets-schemas/schemas/mysql/mysql.schema.json                 | [Sample](schemas/mysql/sample.json)                           | [Readme](schemas/mysql/mysql.schema.md)                            |
| mysql | rds            |         | https://facets-cloud.github.io/facets-schemas/schemas/mysql/mysql.schema.json                 | [Sample](schemas/mysql/sample.json)                           | [Readme](schemas/mysql/mysql.schema.md)                            |
| mysql | aurora         |         | https://facets-cloud.github.io/facets-schemas/schemas/mysql/mysql.schema.json                 | [Sample](schemas/mysql/sample.json)                           | [Readme](schemas/mysql/mysql.schema.md)                            |
| redis | elasticcache  |         | https://facets-cloud.github.io/facets-schemas/schemas/redis/redis.schema.json                  | [Sample](schemas/redis/sample.json)                            | [Readme](schemas/redis/redis.schema.md)                             |
| redis | memorystore   |         | https://facets-cloud.github.io/facets-schemas/schemas/redis/redis.schema.json                  | [Sample](schemas/redis/sample.json)                            | [Readme](schemas/redis/redis.schema.md)                             |
| redis | k8s           |         | https://facets-cloud.github.io/facets-schemas/schemas/redis/redis.schema.json              | [Sample](schemas/redis/sample.json)                        | [Readme](schemas/redis/redis.schema.md)                    |
| mongo | k8s           |         | https://facets-cloud.github.io/facets-schemas/schemas/mongo/mongo.schema.json              | [Sample](schemas/mongo/sample.json)                        | [Readme](schemas/mongo/mongo.schema.md)                    |
| elasticsearch | k8s   |         | https://facets-cloud.github.io/facets-schemas/schemas/elasticsearch/elasticsearch.schema.json   | [Sample](schemas/elasticsearch/sample.json)                  | [Readme](schemas/elasticsearch/elasticsearch.schema.md)           |
| loadbalancer | nlb_nginx  |  | https://facets-cloud.github.io/facets-schemas/schemas/loadbalancer/loadbalancer.schema.json | [Sample](schemas/loadbalancer/sample.json)                    | [Readme](schemas/loadbalancer/loadbalancer.schema.md)             |
| loadbalancer | gcp_alb     |  | https://facets-cloud.github.io/facets-schemas/schemas/loadbalancer/loadbalancer.schema.json | [Sample](schemas/loadbalancer/sample.json)                    | [Readme](schemas/loadbalancer/loadbalancer.schema.md)             |
| loadbalancer | aws_alb     |  | https://facets-cloud.github.io/facets-schemas/schemas/loadbalancer/loadbalancer.schema.json | [Sample](schemas/loadbalancer/sample.json)                    | [Readme](schemas/loadbalancer/loadbalancer.schema.md)             |
| service | default        |         | https://facets-cloud.github.io/facets-schemas/schemas/service/service.schema.json             | [Sample](schemas/service/sample.json)                          | [Readme](schemas/service/service.schema.md)                        |

