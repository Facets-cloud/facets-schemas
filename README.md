# Intent Schema Guidelines

## Terms

| Term                                         | Description                                                                                                                              |
|----------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------|
| **Resource**                                 | An entity declared in facets                                                                                                             |
| **Type of Resource** or **Intent**           | A resource expressible in facets JSON representation having a defined schema                                                             |
| **Implementation of Resource** or **Flavor** | A specific way of implementing a resource, for example redis can be implemented as a stateful set in kubernetes and elasticcache in AWS. |
| **Blueprint**                                | Collection of resource to create a functional product                                                                                    |
| **Environment**                              | Manifestation of this blueprint in any cloud                                                                                             |
| **User**                                     | Developer or ops person who is creating the blueprint                                                                                    |

## Anatomy of a Facets Resource JSON

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

| Kind                 | Flavor           | Version | Schema                                                                                                | Sample                                                            | Readme                                                          |
|----------------------|------------------|---------|-------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------|-----------------------------------------------------------------|
| mysql                | cloudsql         | 0.1     | https://facets-cloud.github.io/facets-schemas/schemas/mysql/mysql.schema.json                         | [Sample](schemas/mysql/mysql.cloudsql.sample.json)                | [Readme](schemas/mysql/README.md)                               |
| mysql                | rds              | 0.1     | https://facets-cloud.github.io/facets-schemas/schemas/mysql/mysql.schema.json                         | [Sample](schemas/mysql/mysql.rds.sample.json)                     | [Readme](schemas/mysql/README.md)                               |
| mysql                | aurora           | 0.1     | https://facets-cloud.github.io/facets-schemas/schemas/mysql/mysql.schema.json                         | [Sample](schemas/mysql/mysql.aurora.sample.json)                  | [Readme](schemas/mysql/README.md)                               |
| mysql                | flexible_server  | 0.1     | https://facets-cloud.github.io/facets-schemas/schemas/mysql/mysql.schema.json                         | [Sample](schemas/mysql/mysql.flexible_server.sample.json)         | [Readme](schemas/mysql/README.md)                               |
| redis                | elasticache      | 0.1     | https://facets-cloud.github.io/facets-schemas/schemas/redis/redis.schema.json                         | [Sample](schemas/redis/sample.json)                               | [Readme](schemas/redis/README.md)                               |
| redis                | memorystore      | 0.2     | https://facets-cloud.github.io/facets-schemas/schemas/redis/redis.schema.json                         | [Sample](schemas/redis/sample.json)                               | [Readme](schemas/redis/README.md)                               |
| redis                | k8s              | 0.1     | https://facets-cloud.github.io/facets-schemas/schemas/redis/redis.schema.json                         | [Sample](schemas/redis/sample.json)                               | [Readme](schemas/redis/README.md)                               |
| ingress              | nlb_nginx        | 0.1     | https://facets-cloud.github.io/facets-schemas/schemas/loadbalancer/ingress.schema.json                | [Sample](schemas/loadbalancer/ingress.nlb_nginx.sample.json)      | [Readme](schemas/loadbalancer/ingress.schema.md)                |
| ingress              | gcp_alb          | 0.2     | https://facets-cloud.github.io/facets-schemas/schemas/loadbalancer/ingress.schema.json                | [Sample](schemas/loadbalancer/ingress.gcp_alb.sample.json)        | [Readme](schemas/loadbalancer/ingress.schema.md)                |
| ingress              | aws_alb          | 0.2     | https://facets-cloud.github.io/facets-schemas/schemas/loadbalancer/ingress.schema.json                | [Sample](schemas/loadbalancer/ingress.aws_alb.sample.json)        | [Readme](schemas/loadbalancer/ingress.schema.md)                |
| service              | default          | 0.1     | https://facets-cloud.github.io/facets-schemas/schemas/service/service.schema.json                     | [Sample](schemas/service/main.json)                               | [Readme](schemas/service/service.schema.md)                     |
| postgres             | aurora           | 0.1     | https://facets-cloud.github.io/facets-schemas/schemas/postgres/postgres.schema.json                   | [Sample](schemas/postgres/postgres.aurora.sample.json)            | [Readme](schemas/postgres/README.md)                            |
| postgres             | cloudsql         | 0.1     | https://facets-cloud.github.io/facets-schemas/schemas/postgres/postgres.schema.json                   | [Sample](schemas/postgres/postgres.cloudsql.sample.json)          | [Readme](schemas/postgres/README.md)                            |
| kubernetes_node_pool | aks              | 0.1     | https://facets-cloud.github.io/facets-schemas/schemas/nodepool/nodepool.schema.json                   | [Sample](schemas/nodepool/nodepool.aks.sample.json)               | [Readme](schemas/nodepool/nodepool.schema.md)                   |
| kubernetes_node_pool | eks_managed      | 0.1     | https://facets-cloud.github.io/facets-schemas/schemas/nodepool/nodepool.schema.json                   | [Sample](schemas/nodepool/nodepool.eks-managed.sample.json)       | [Readme](schemas/nodepool/nodepool.schema.md)                   |
| kubernetes_node_pool | eks_self_managed | 0.1     | https://facets-cloud.github.io/facets-schemas/schemas/nodepool/nodepool.schema.json                   | [Sample](schemas/nodepool/nodepool.self-managed.sample.json)      | [Readme](schemas/nodepool/nodepool.schema.md)                   |
| k8s_resource         | default          | 0.1     | https://facets-cloud.github.io/facets-schemas/schemas/k8s_resource/k8s_resource.schema.json           | [Sample](schemas/k8s_resource/sample.json)                        | [Readme](schemas/k8s_resource/README.md)                        |
| s3                   | default          | 0.2     | https://facets-cloud.github.io/facets-schemas/schemas/s3/s3.schema.json                               | [Sample](schemas/s3/s3.sample.json)                               | [Readme](schemas/s3/s3.schema.md)                               |
| alert_group          | default          | 0.1     | https://facets-cloud.github.io/facets-schemas/schemas/alert_group/alert-group.schema.json             | [Sample](schemas/alert_group/sample.json)                         | [Readme](schemas/alert_group/README.md)                         |
| log_collector        | loki             | 0.1     | https://facets-cloud.github.io/facets-schemas/schemas/log_collector/log-collector.schema.json         | [Sample](schemas/log_collector/loki-sample.json)                  | [Readme](schemas/log_collector/README.md)                       |
| log_collector        | loki_s3          | 0.1     | https://facets-cloud.github.io/facets-schemas/schemas/log_collector/log-collector.schema.json         | [Sample](schemas/log_collector/loki-s3-sample.json)               | [Readme](schemas/log_collector/README.md)                       |
| sqs                  | default          | 0.2     | https://facets-cloud.github.io/facets-schemas/schemas/sqs/sqs.schema.json                             | [Sample](schemas/sqs/sqs.sample.json)                             | [Readme](schemas/sqs/README.md)                                 |
| helm                 | default          | 0.1     | https://facets-cloud.github.io/facets-schemas/schemas/helm/helm.schema.json                           | [Sample](schemas/helm/sample.json)                                | [Readme](schemas/helm/README.md)                                |
| redis                | k8s              | 0.1     | https://facets-cloud.github.io/facets-schemas/schemas/redis/redis.schema.json                         | [Sample](schemas/redis/)                                          | [Readme](schemas/redis/README.md)                               |
| mongo                | k8s              | 0.2     | https://facets-cloud.github.io/facets-schemas/schemas/redis/redis.schema.json                         | [Sample](schemas/mongo/)                                          | [Readme](schemas/mongo/mongo.schema.md)                         |
| elasticsearch        | k8s              | 0.2     | https://facets-cloud.github.io/facets-schemas/schemas/elasticsearch/elasticsearch.schema.json         | [Sample](schemas/elasticsearch/elasticsearch.sample.json)         | [Readme](schemas/elasticsearch/elasticsearch.schema.md)         |
| rabbitmq             | k8s              | 0.1     | https://facets-cloud.github.io/facets-schemas/schemas/rabbitmq/rabbitmq.schema.json                   | [Sample](schemas/rabbitmq/rabbitmq.k8s.sample.json)               | [Readme](schemas/rabbitmq/rabbitmq.schema.md)                   |
| kafka                | k8s              | 0.2     | https://facets-cloud.github.io/facets-schemas/schemas/kafka/kafka.schema.json                         | [Sample](schemas/kafka/sample-kafka.json)                         | [Readme](schemas/kafka/kafka.schema.md)                         |
| grafana_dashboard    | default          | 0.1     | https://facets-cloud.github.io/facets-schemas/schemas/grafana_dashboard/grafana-dashboard.schema.json | [Sample](schemas/grafana_dashboard/grafana-dashboard.sample.json) | [Readme](schemas/grafana_dashboard/grafana-dashboard.schema.md) |
