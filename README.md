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

#### Kind

The kind property specifies the type of resource that the JSON file represents. For example, it could be an ingress, an application, a MySQL database, etc. If this property is not specified, the default value is the folder name/instances.

#### Flavor

The flavor property is used to select a specific implementation of the resource type. For example, for a resource type of ingress, a flavor of default, aws_alb, or gcp_alb could be specified. This property allows for flexibility in choosing the implementation that best fits the needs of the environment.

#### Version

The version property is used to specify a particular version of the resource implementation. This is useful when there are multiple versions of an implementation available, and you want to pin the resource to a specific version. The default version is the latest version available.

#### Disabled

The disabled property is a boolean flag that allows the user to disable the resource. This is useful when a resource is not needed or is temporarily unavailable.

#### Provided

The provided property is a boolean flag that specifies whether the resource should be provisioned by Facets or not. For example, a MySQL database may be provisioned outside of Facets, but can still exist within the blueprint for other resources to refer to its URL, username, etc. In this case, the provided property would be set to true, and the out section would be populated by the user.

#### Depends On

The depends_on property lists any dependencies that the resource has on other resources. For example, an application may depend on a MySQL database. The depends_on property would be set to ["mysql.y"].

#### Metadata

The metadata property contains metadata related to the resource. This includes the name of the resource and any other relevant information. If the name property is not specified, the default value is the file name.

#### Spec

The spec property contains the specification for the resource. This is where the specific details of the resource are defined, and it follows the schema for the specific resource type.

#### Out

The out property contains the output given by the resource for others to refer. This includes any relevant information that other resources may need to use, such as URLs, usernames, and passwords. The out section follows the schema for the specific resource type.

#### Advanced

The advanced property is an optional field that contains additional fields that are specific to a particular implementation of a resource. This allows for greater customization and configuration of the resource beyond the standard fields.

# Supported Services

| Kind | Flavor | Version | Schema | Sample | Readme |
|---|---|---|---|---|---|
| s3 | default | 0.2 | <https://facets-cloud.github.io/facets-schemas/schemas/s3/s3.schema.json> | [Sample](schemas/s3/s3.default.sample.json) | [Readme](schemas/s3/s3.schema.md) |
| postgres | cloudsql | 0.2 | <https://facets-cloud.github.io/facets-schemas/schemas/postgres/postgres.schema.json> | [Sample](schemas/postgres/postgres.cloudsql.sample.json) | [Readme](schemas/postgres/postgres.schema.md) |
| postgres | k8s | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/postgres/postgres.schema.json> | [Sample](schemas/postgres/postgres.k8s.sample.json) | [Readme](schemas/postgres/postgres.schema.md) |
| postgres | rds | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/postgres/postgres.schema.json> | [Sample](schemas/postgres/postgres.rds.sample.json) | [Readme](schemas/postgres/postgres.schema.md) |
| postgres | aurora | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/postgres/postgres.schema.json> | [Sample](schemas/postgres/postgres.aurora.sample.json) | [Readme](schemas/postgres/postgres.schema.md) |
| postgres | alloydb | 0.2 | <https://facets-cloud.github.io/facets-schemas/schemas/postgres/postgres.schema.json> | [Sample](schemas/postgres/postgres.alloydb.sample.json) | [Readme](schemas/postgres/postgres.schema.md) |
| mysql | aurora | 0.3 | <https://facets-cloud.github.io/facets-schemas/schemas/mysql/mysql.schema.json> | [Sample](schemas/mysql/mysql.aurora.sample.json) | [Readme](schemas/mysql/mysql.schema.md) |
| mysql | k8s | 0.2 | <https://facets-cloud.github.io/facets-schemas/schemas/mysql/mysql.schema.json> | [Sample](schemas/mysql/mysql.k8s.sample.json) | [Readme](schemas/mysql/mysql.schema.md) |
| mysql | rds | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/mysql/mysql.schema.json> | [Sample](schemas/mysql/mysql.rds.sample.json) | [Readme](schemas/mysql/mysql.schema.md) |
| mysql | flexible_server | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/mysql/mysql.schema.json> | [Sample](schemas/mysql/mysql.flexible_server.sample.json) | [Readme](schemas/mysql/mysql.schema.md) |
| mysql | cloudsql | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/mysql/mysql.schema.json> | [Sample](schemas/mysql/mysql.cloudsql.sample.json) | [Readme](schemas/mysql/mysql.schema.md) |
| redis | memorystore | 0.2 | <https://facets-cloud.github.io/facets-schemas/schemas/redis/redis.schema.json> | [Sample](schemas/redis/redis.memorystore.sample.json) | [Readme](schemas/redis/redis.schema.md) |
| redis | azure_cache | 0.2 | <https://facets-cloud.github.io/facets-schemas/schemas/redis/redis.schema.json> | [Sample](schemas/redis/redis.azure_cache.sample.json) | [Readme](schemas/redis/redis.schema.md) |
| redis | k8s | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/redis/redis.schema.json> | [Sample](schemas/redis/redis.k8s.sample.json) | [Readme](schemas/redis/redis.schema.md) |
| redis | elasticache | 0.2 | <https://facets-cloud.github.io/facets-schemas/schemas/redis/redis.schema.json> | [Sample](schemas/redis/redis.elasticache.sample.json) | [Readme](schemas/redis/redis.schema.md) |
| helm | k8s | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/helm/helm.schema.json> | [Sample](schemas/helm/helm.k8s.sample.json) | [Readme](schemas/helm/helm.schema.md) |
| ingress | gcp_alb | 0.2 | <https://facets-cloud.github.io/facets-schemas/schemas/ingress/ingress.schema.json> | [Sample](schemas/ingress/ingress.gcp_alb.sample.json) | [Readme](schemas/ingress/ingress.schema.md) |
| ingress | aws_alb | 0.3 | <https://facets-cloud.github.io/facets-schemas/schemas/ingress/ingress.schema.json> | [Sample](schemas/ingress/ingress.aws_alb.sample.json) | [Readme](schemas/ingress/ingress.schema.md) |
| ingress | nginx_k8s_native | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/ingress/ingress.schema.json> | [Sample](schemas/ingress/ingress.nginx_k8s_native.sample.json) | [Readme](schemas/ingress/ingress.schema.md) |
| ingress | nginx_ingress_controller | 0.2 | <https://facets-cloud.github.io/facets-schemas/schemas/ingress/ingress.schema.json> | [Sample](schemas/ingress/ingress.nginx_ingress_controller.sample.json) | [Readme](schemas/ingress/ingress.schema.md) |
| log_collector | loki_aws_s3 | 0.2 | <https://facets-cloud.github.io/facets-schemas/schemas/log_collector/log_collector.schema.json> | [Sample](schemas/log_collector/log_collector.loki_aws_s3.sample.json) | [Readme](schemas/log_collector/log_collector.schema.md) |
| log_collector | loki_azure_blob | 0.2 | <https://facets-cloud.github.io/facets-schemas/schemas/log_collector/log_collector.schema.json> | [Sample](schemas/log_collector/log_collector.loki_azure_blob.sample.json) | [Readme](schemas/log_collector/log_collector.schema.md) |
| log_collector | loki_gcp_gcs | 0.2 | <https://facets-cloud.github.io/facets-schemas/schemas/log_collector/log_collector.schema.json> | [Sample](schemas/log_collector/log_collector.loki_gcp_gcs.sample.json) | [Readme](schemas/log_collector/log_collector.schema.md) |
| log_collector | k8s | 0.2 | <https://facets-cloud.github.io/facets-schemas/schemas/log_collector/log_collector.schema.json> | [Sample](schemas/log_collector/log_collector.k8s.sample.json) | [Readme](schemas/log_collector/log_collector.schema.md) |
| aws_efs | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/aws_efs/aws_efs.schema.json> | [Sample](schemas/aws_efs/aws_efs.default.sample.json) | [Readme](schemas/aws_efs/aws_efs.schema.md) |
| loki_alerting_rules | k8s | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/loki_alerting_rules/loki_alerting_rules.schema.json> | [Sample](schemas/loki_alerting_rules/loki_alerting_rules.k8s.sample.json) | [Readme](schemas/loki_alerting_rules/loki_alerting_rules.schema.md) |
| rabbitmq | k8s | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/rabbitmq/rabbitmq.schema.json> | [Sample](schemas/rabbitmq/rabbitmq.k8s.sample.json) | [Readme](schemas/rabbitmq/rabbitmq.schema.md) |
| kms | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/kms/kms.schema.json> | [Sample](schemas/kms/kms.default.sample.json) | [Readme](schemas/kms/kms.schema.md) |
| aws_dlm_lifecycle_policy | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/aws_dlm_lifecycle_policy/aws_dlm_lifecycle_policy.schema.json> | [Sample](schemas/aws_dlm_lifecycle_policy/aws_dlm_lifecycle_policy.default.sample.json) | [Readme](schemas/aws_dlm_lifecycle_policy/aws_dlm_lifecycle_policy.schema.md) |
| cloudflare | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/cloudflare/cloudflare.schema.json> | [Sample](schemas/cloudflare/cloudflare.default.sample.json) | [Readme](schemas/cloudflare/cloudflare.schema.md) |
| pvc | k8s | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/pvc/pvc.schema.json> | [Sample](schemas/pvc/pvc.k8s.sample.json) | [Readme](schemas/pvc/pvc.schema.md) |
| elasticsearch | opensearch | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/elasticsearch/elasticsearch.schema.json> | [Sample](schemas/elasticsearch/elasticsearch.opensearch.sample.json) | [Readme](schemas/elasticsearch/elasticsearch.schema.md) |
| elasticsearch | k8s | 0.2 | <https://facets-cloud.github.io/facets-schemas/schemas/elasticsearch/elasticsearch.schema.json> | [Sample](schemas/elasticsearch/elasticsearch.k8s.sample.json) | [Readme](schemas/elasticsearch/elasticsearch.schema.md) |
| google_cloud_storage | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/google_cloud_storage/google_cloud_storage.schema.json> | [Sample](schemas/google_cloud_storage/google_cloud_storage.default.sample.json) | [Readme](schemas/google_cloud_storage/google_cloud_storage.schema.md) |
| grafana_dashboard | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/grafana_dashboard/grafana_dashboard.schema.json> | [Sample](schemas/grafana_dashboard/grafana_dashboard.default.sample.json) | [Readme](schemas/grafana_dashboard/grafana_dashboard.schema.md) |
| kafka | k8s | 0.3 | <https://facets-cloud.github.io/facets-schemas/schemas/kafka/kafka.schema.json> | [Sample](schemas/kafka/kafka.k8s.sample.json) | [Readme](schemas/kafka/kafka.schema.md) |
| kafka | msk | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/kafka/kafka.schema.json> | [Sample](schemas/kafka/kafka.msk.sample.json) | [Readme](schemas/kafka/kafka.schema.md) |
| kafka | external | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/kafka/kafka.schema.json> | [Sample](schemas/kafka/kafka.external.sample.json) | [Readme](schemas/kafka/kafka.schema.md) |
| kubernetes_node_pool | node_fleet | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/kubernetes_node_pool/kubernetes_node_pool.schema.json> | [Sample](schemas/kubernetes_node_pool/kubernetes_node_pool.node_fleet.sample.json) | [Readme](schemas/kubernetes_node_pool/kubernetes_node_pool.schema.md) |
| kubernetes_node_pool | eks_managed | 0.2 | <https://facets-cloud.github.io/facets-schemas/schemas/kubernetes_node_pool/kubernetes_node_pool.schema.json> | [Sample](schemas/kubernetes_node_pool/kubernetes_node_pool.eks_managed.sample.json) | [Readme](schemas/kubernetes_node_pool/kubernetes_node_pool.schema.md) |
| kubernetes_node_pool | eks_self_managed | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/kubernetes_node_pool/kubernetes_node_pool.schema.json> | [Sample](schemas/kubernetes_node_pool/kubernetes_node_pool.eks_self_managed.sample.json) | [Readme](schemas/kubernetes_node_pool/kubernetes_node_pool.schema.md) |
| kubernetes_node_pool | aks | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/kubernetes_node_pool/kubernetes_node_pool.schema.json> | [Sample](schemas/kubernetes_node_pool/kubernetes_node_pool.aks.sample.json) | [Readme](schemas/kubernetes_node_pool/kubernetes_node_pool.schema.md) |
| kubernetes_node_pool | gke_node_pool | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/kubernetes_node_pool/kubernetes_node_pool.schema.json> | [Sample](schemas/kubernetes_node_pool/kubernetes_node_pool.gke_node_pool.sample.json) | [Readme](schemas/kubernetes_node_pool/kubernetes_node_pool.schema.md) |
| mongo | k8s | 0.3 | <https://facets-cloud.github.io/facets-schemas/schemas/mongo/mongo.schema.json> | [Sample](schemas/mongo/mongo.k8s.sample.json) | [Readme](schemas/mongo/mongo.schema.md) |
| mongo | atlas | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/mongo/mongo.schema.json> | [Sample](schemas/mongo/mongo.atlas.sample.json) | [Readme](schemas/mongo/mongo.schema.md) |
| mongo | external | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/mongo/mongo.schema.json> | [Sample](schemas/mongo/mongo.external.sample.json) | [Readme](schemas/mongo/mongo.schema.md) |
| mongo | documentdb | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/mongo/mongo.schema.json> | [Sample](schemas/mongo/mongo.documentdb.sample.json) | [Readme](schemas/mongo/mongo.schema.md) |
| externaldns | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/externaldns/externaldns.schema.json> | [Sample](schemas/externaldns/externaldns.default.sample.json) | [Readme](schemas/externaldns/externaldns.schema.md) |
| kafka_topic | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/kafka_topic/kafka_topic.schema.json> | [Sample](schemas/kafka_topic/kafka_topic.default.sample.json) | [Readme](schemas/kafka_topic/kafka_topic.schema.md) |
| aws_iam_role | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/aws_iam_role/aws_iam_role.schema.json> | [Sample](schemas/aws_iam_role/aws_iam_role.default.sample.json) | [Readme](schemas/aws_iam_role/aws_iam_role.schema.md) |
| mysql_user | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/mysql_user/mysql_user.schema.json> | [Sample](schemas/mysql_user/mysql_user.default.sample.json) | [Readme](schemas/mysql_user/mysql_user.schema.md) |
| mysql_user | sharded_user | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/mysql_user/mysql_user.schema.json> | [Sample](schemas/mysql_user/mysql_user.sharded_user.sample.json) | [Readme](schemas/mysql_user/mysql_user.schema.md) |
| mongo_user | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/mongo_user/mongo_user.schema.json> | [Sample](schemas/mongo_user/mongo_user.default.sample.json) | [Readme](schemas/mongo_user/mongo_user.schema.md) |
| postgres_user | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/postgres_user/postgres_user.schema.json> | [Sample](schemas/postgres_user/postgres_user.default.sample.json) | [Readme](schemas/postgres_user/postgres_user.schema.md) |
| aws_lambda_permission | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/aws_lambda_permission/aws_lambda_permission.schema.json> | [Sample](schemas/aws_lambda_permission/aws_lambda_permission.default.sample.json) | [Readme](schemas/aws_lambda_permission/aws_lambda_permission.schema.md) |
| zookeeper | k8s | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/zookeeper/zookeeper.schema.json> | [Sample](schemas/zookeeper/zookeeper.k8s.sample.json) | [Readme](schemas/zookeeper/zookeeper.schema.md) |
| tcp_lb | nlb | 0.2 | <https://facets-cloud.github.io/facets-schemas/schemas/tcp_lb/tcp_lb.schema.json> | [Sample](schemas/tcp_lb/tcp_lb.nlb.sample.json) | [Readme](schemas/tcp_lb/tcp_lb.schema.md) |
| service | deployment | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/service/service.schema.json> | [Sample](schemas/service/service.deployment.sample.json) | [Readme](schemas/service/service.schema.md) |
| service | vm | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/service/service.schema.json> | [Sample](schemas/service/service.vm.sample.json) | [Readme](schemas/service/service.schema.md) |
| service | ecs_service | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/service/service.schema.json> | [Sample](schemas/service/service.ecs_service.sample.json) | [Readme](schemas/service/service.schema.md) |
| schemahero_table | k8s | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/schemahero_table/schemahero_table.schema.json> | [Sample](schemas/schemahero_table/schemahero_table.k8s.sample.json) | [Readme](schemas/schemahero_table/schemahero_table.schema.md) |
| alert_group | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/alert_group/alert_group.schema.json> | [Sample](schemas/alert_group/alert_group.default.sample.json) | [Readme](schemas/alert_group/alert_group.schema.md) |
| aws_event_bus | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/aws_event_bus/aws_event_bus.schema.json> | [Sample](schemas/aws_event_bus/aws_event_bus.default.sample.json) | [Readme](schemas/aws_event_bus/aws_event_bus.schema.md) |
| cloudfront | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/cloudfront/cloudfront.schema.json> | [Sample](schemas/cloudfront/cloudfront.default.sample.json) | [Readme](schemas/cloudfront/cloudfront.schema.md) |
| atlas_account | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/atlas_account/atlas_account.schema.json> | [Sample](schemas/atlas_account/atlas_account.default.sample.json) | [Readme](schemas/atlas_account/atlas_account.schema.md) |
| aws_waf | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/aws_waf/aws_waf.schema.json> | [Sample](schemas/aws_waf/aws_waf.default.sample.json) | [Readme](schemas/aws_waf/aws_waf.schema.md) |
| aws_lambda | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/aws_lambda/aws_lambda.schema.json> | [Sample](schemas/aws_lambda/aws_lambda.default.sample.json) | [Readme](schemas/aws_lambda/aws_lambda.schema.md) |
| k8s_resource | k8s | 0.3 | <https://facets-cloud.github.io/facets-schemas/schemas/k8s_resource/k8s_resource.schema.json> | [Sample](schemas/k8s_resource/k8s_resource.k8s.sample.json) | [Readme](schemas/k8s_resource/k8s_resource.schema.md) |
| aws_api_gateway | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/aws_api_gateway/aws_api_gateway.schema.json> | [Sample](schemas/aws_api_gateway/aws_api_gateway.default.sample.json) | [Readme](schemas/aws_api_gateway/aws_api_gateway.schema.md) |
| aws_eventbridge | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/aws_eventbridge/aws_eventbridge.schema.json> | [Sample](schemas/aws_eventbridge/aws_eventbridge.default.sample.json) | [Readme](schemas/aws_eventbridge/aws_eventbridge.schema.md) |
| artifactories | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/artifactories/artifactories.schema.json> | [Sample](schemas/artifactories/artifactories.default.sample.json) | [Readme](schemas/artifactories/artifactories.schema.md) |
| loki_recording_rules | k8s | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/loki_recording_rules/loki_recording_rules.schema.json> | [Sample](schemas/loki_recording_rules/loki_recording_rules.k8s.sample.json) | [Readme](schemas/loki_recording_rules/loki_recording_rules.schema.md) |
| azure_storage_container | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/azure_storage_container/azure_storage_container.schema.json> | [Sample](schemas/azure_storage_container/azure_storage_container.default.sample.json) | [Readme](schemas/azure_storage_container/azure_storage_container.schema.md) |
| cassandra | k8s | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/cassandra/cassandra.schema.json> | [Sample](schemas/cassandra/cassandra.k8s.sample.json) | [Readme](schemas/cassandra/cassandra.schema.md) |
| azure_functions | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/azure_functions/azure_functions.schema.json> | [Sample](schemas/azure_functions/azure_functions.default.sample.json) | [Readme](schemas/azure_functions/azure_functions.schema.md) |
| aws_static_website | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/aws_static_website/aws_static_website.schema.json> | [Sample](schemas/aws_static_website/aws_static_website.default.sample.json) | [Readme](schemas/aws_static_website/aws_static_website.schema.md) |
| dynamodb | default | 0.2 | <https://facets-cloud.github.io/facets-schemas/schemas/dynamodb/dynamodb.schema.json> | [Sample](schemas/dynamodb/dynamodb.default.sample.json) | [Readme](schemas/dynamodb/dynamodb.schema.md) |
| snapshot_schedule | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/snapshot_schedule/snapshot_schedule.schema.json> | [Sample](schemas/snapshot_schedule/snapshot_schedule.default.sample.json) | [Readme](schemas/snapshot_schedule/snapshot_schedule.schema.md) |
| status_check | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/status_check/status_check.schema.json> | [Sample](schemas/status_check/status_check.default.sample.json) | [Readme](schemas/status_check/status_check.schema.md) |
| schemahero_database | k8s | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/schemahero_database/schemahero_database.schema.json> | [Sample](schemas/schemahero_database/schemahero_database.k8s.sample.json) | [Readme](schemas/schemahero_database/schemahero_database.schema.md) |
| alb | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/alb/alb.schema.json> | [Sample](schemas/alb/alb.default.sample.json) | [Readme](schemas/alb/alb.schema.md) |
| vault | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/vault/vault.schema.json> | [Sample](schemas/vault/vault.default.sample.json) | [Readme](schemas/vault/vault.schema.md) |
| gcp_iam_role | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/gcp_iam_role/gcp_iam_role.schema.json> | [Sample](schemas/gcp_iam_role/gcp_iam_role.default.sample.json) | [Readme](schemas/gcp_iam_role/gcp_iam_role.schema.md) |
| peering | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/peering/peering.schema.json> | [Sample](schemas/peering/peering.default.sample.json) | [Readme](schemas/peering/peering.schema.md) |
| kubernetes_secret | k8s | 0.3 | <https://facets-cloud.github.io/facets-schemas/schemas/kubernetes_secret/kubernetes_secret.schema.json> | [Sample](schemas/kubernetes_secret/kubernetes_secret.k8s.sample.json) | [Readme](schemas/kubernetes_secret/kubernetes_secret.schema.md) |
| service_bus | azure_service_bus | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/service_bus/service_bus.schema.json> | [Sample](schemas/service_bus/service_bus.azure_service_bus.sample.json) | [Readme](schemas/service_bus/service_bus.schema.md) |
| sns | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/sns/sns.schema.json> | [Sample](schemas/sns/sns.default.sample.json) | [Readme](schemas/sns/sns.schema.md) |
| sqs | default | 0.2 | <https://facets-cloud.github.io/facets-schemas/schemas/sqs/sqs.schema.json> | [Sample](schemas/sqs/sqs.default.sample.json) | [Readme](schemas/sqs/sqs.schema.md) |
| config_map | k8s | 0.3 | <https://facets-cloud.github.io/facets-schemas/schemas/config_map/config_map.schema.json> | [Sample](schemas/config_map/config_map.k8s.sample.json) | [Readme](schemas/config_map/config_map.schema.md) |
| dax_cluster | default | 0.1 | <https://facets-cloud.github.io/facets-schemas/schemas/dax_cluster/dax_cluster.schema.json> | [Sample](schemas/dax_cluster/dax_cluster.default.sample.json) | [Readme](schemas/dax_cluster/dax_cluster.schema.md) |
| iam_policy | aws_iam_policy | 0.2 | <https://facets-cloud.github.io/facets-schemas/schemas/iam_policy/iam_policy.schema.json> | [Sample](schemas/iam_policy/iam_policy.aws_iam_policy.sample.json) | [Readme](schemas/iam_policy/iam_policy.schema.md) |

