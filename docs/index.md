# Index

- [Redis](#redis)
- [Kubernetes_node_pool](#kubernetes_node_pool)
- [Helm](#helm)
- [Schemahero_table](#schemahero_table)
- [Log_collector](#log_collector)
- [Mysql](#mysql)
- [Schemahero_database](#schemahero_database)
- [Service](#service)
- [Alert_group](#alert_group)
- [K8s_resource](#k8s_resource)
- [Rabbitmq](#rabbitmq)
- [Mongo](#mongo)
- [Kafka](#kafka)
- [Grafana_dashboard](#grafana_dashboard)
- [Ingress](#ingress)
- [Snapshot_schedule](#snapshot_schedule)
- [Peering](#peering)
- [Azure_storage_container](#azure_storage_container)
- [Postgres](#postgres)
- [Elasticsearch](#elasticsearch)
- [Iam_policy](#iam_policy)
- [Config_map](#config_map)
- [Sqs](#sqs)
- [S3](#s3)


## Redis

### Schema Properties

|                                                        | Type                                     | Description                                                                                                                                                                            | Required   |
|:-------------------------------------------------------|:-----------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                                                   | enum: redis                              |                                                                                                                                                                                        | Yes        |
| flavor                                                 | enum: k8s, memorystore, elasticache      |                                                                                                                                                                                        | Yes        |
| version                                                | enum: 0.1, 0.2, latest                   |                                                                                                                                                                                        | Yes        |
| lifecycle                                              | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                                      | No         |
| disabled                                               | boolean                                  | Flag to disable the resource                                                                                                                                                           | No         |
| conditional_on_intent                                  | string                                   | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail.         | No         |
| provided                                               | boolean                                  | Flag to tell if the resource should not be provisioned by facets                                                                                                                       | No         |
| depends_on                                             | array                                    | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                                | No         |
| metadata.name                                          | string                                   | Name of the resource                                                                                                                                                                   | No         |
|                                                        |                                          |     - if not specified, fallback is the `filename`                                                                                                                                     |            |
| spec                                                   | object                                   | Specification as per resource types schema                                                                                                                                             | Yes        |
| advanced                                               | object                                   | Additional fields if any for a particular implementation of a resource                                                                                                                 | No         |
| spec.authenticated                                     | boolean                                  | Make this redis Password protected                                                                                                                                                     | No         |
| spec.redis_version                                     | string                                   | Version of redis e.g. 6.3                                                                                                                                                              | No         |
| spec.persistence_enabled                               | boolean                                  | Enable Persistence for this redis                                                                                                                                                      | No         |
| spec.size.writer.volume                                | string                                   | The size of the volume. e.g '10G' or '5Gi'                                                                                                                                             | No         |
| spec.size.reader.instance_count                        | integer                                  | The number of instances to create.                                                                                                                                                     | No         |
| spec.size.reader.replica_count                         | integer                                  | [Deprecated] The number of instances to create.                                                                                                                                        | No         |
| advanced.k8s.redis.values                              | object                                   | Helm values as per the Bitnami redis chart https://github.com/bitnami/charts/tree/master/bitnami/redis                                                                                 | No         |
| advanced.elasticache.aws_elasticache_replication_group | object                                   | Resource values as per the terraform resource documentation https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/elasticache_replication_group                  | No         |
| advanced.elasticache.aws_elasticache_parameter_group   | object                                   | Resource values as per the terraform resource documentation https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/elasticache_parameter_group                    | No         |
| advanced.elasticache.aws_security_group                | object                                   | Resource values as per the terraform resource documentation https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/security_group                                 | No         |
| advanced.memorystore.google_redis_instance             | object                                   | Resource values as per the terraform resource documentation for redis instance creation  https://registry.terraform.io/providers/hashicorp/google/latest/docs/resources/redis_instance | No         |

### Outputs

|                                                   | Type    | Description                                       | Required   |
|:--------------------------------------------------|:--------|:--------------------------------------------------|:-----------|
| out                                               | object  | Output given by the resource for others to refer. | No         |
| out.spec.authenticated                            | boolean | Make this redis Password protected                | No         |
| out.spec.redis_version                            | string  | Version of redis e.g. 6.3                         | No         |
| out.spec.persistence_enabled                      | boolean | Enable Persistence for this redis                 | No         |
| out.spec.size.writer.volume                       | string  | The size of the volume. e.g '10G' or '5Gi'        | No         |
| out.spec.size.reader.instance_count               | integer | The number of instances to create.                | No         |
| out.spec.size.reader.replica_count                | integer | [Deprecated] The number of instances to create.   | No         |
| out.interfaces.reader.username                    | string  | Username to connect (if any)                      | No         |
| out.interfaces.reader.password                    | string  | Password to connect (if any)                      | No         |
| out.interfaces.reader.host                        | string  | Host for service discovery                        | No         |
| out.interfaces.reader.port                        | string  | Port for service discovery                        | No         |
| out.interfaces.reader.name                        | string  | Name of interface, same as key                    | No         |
| out.interfaces.reader.connection_string           | string  | Connection string to connect                      | No         |
| out.interfaces.writer.username                    | string  | Username to connect (if any)                      | No         |
| out.interfaces.writer.password                    | string  | Password to connect (if any)                      | No         |
| out.interfaces.writer.host                        | string  | Host for service discovery                        | No         |
| out.interfaces.writer.port                        | string  | Port for service discovery                        | No         |
| out.interfaces.writer.name                        | string  | Name of interface, same as key                    | No         |
| out.interfaces.writer.connection_string           | string  | Connection string to connect                      | No         |
| out.instances.^([a-z]*-[0-9]+)$.username          | string  | Username to connect (if any)                      | No         |
| out.instances.^([a-z]*-[0-9]+)$.password          | string  | Password to connect (if any)                      | No         |
| out.instances.^([a-z]*-[0-9]+)$.host              | string  | Host for service discovery                        | No         |
| out.instances.^([a-z]*-[0-9]+)$.port              | string  | Port for service discovery                        | No         |
| out.instances.^([a-z]*-[0-9]+)$.name              | string  | Name of interface, same as key                    | No         |
| out.instances.^([a-z]*-[0-9]+)$.connection_string | string  | Connection string to connect                      | No         |



## Kubernetes_node_pool

### Schema Properties

|                                                             | Type                                          | Description                                                                                                                                                                                      | Required   |
|:------------------------------------------------------------|:----------------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                                                        | enum: kubernetes_node_pool                    |                                                                                                                                                                                                  | Yes        |
| flavor                                                      | enum: eks_self_managed, aks, eks_managed      |                                                                                                                                                                                                  | Yes        |
| version                                                     | enum: 0.1, latest                             |                                                                                                                                                                                                  | Yes        |
| lifecycle                                                   | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP      | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                                                | No         |
| disabled                                                    | boolean                                       | Flag to disable the resource                                                                                                                                                                     | No         |
| conditional_on_intent                                       | string                                        | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail.                   | No         |
| provided                                                    | boolean                                       | Flag to tell if the resource should not be provisioned by facets                                                                                                                                 | No         |
| depends_on                                                  | array                                         | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                                          | No         |
| metadata.name                                               | string                                        | Name of the resource                                                                                                                                                                             | No         |
|                                                             |                                               |     - if not specified, fallback is the `filename`                                                                                                                                               |            |
| spec                                                        | object                                        | Specification as per resource types schema                                                                                                                                                       | Yes        |
| advanced                                                    | object                                        | Additional fields if any for a particular implementation of a resource                                                                                                                           | No         |
| spec.instance_type                                          | string                                        | Instance type associated with the Node Pool                                                                                                                                                      | No         |
| spec.min_node_count                                         | number                                        | Minimum number of worker nodes                                                                                                                                                                   | No         |
| spec.max_node_count                                         | number                                        | Maximum number of worker nodes                                                                                                                                                                   | No         |
| spec.disk_size                                              | number                                        | Disk size in GiB for worker nodes                                                                                                                                                                | No         |
| spec.azs                                                    | string                                        | Comma separated string of one or more availability zones for the Node Pool                                                                                                                       | No         |
| spec.is_public                                              | boolean                                       | Whether to launch nodes in Public or Private network                                                                                                                                             | No         |
| spec.taints.^[a-zA-Z]+\d+$.key                              | string                                        | The key of the taint                                                                                                                                                                             | No         |
| spec.taints.^[a-zA-Z]+\d+$.value                            | string                                        | The value of the taint                                                                                                                                                                           | No         |
| spec.taints.^[a-zA-Z]+\d+$.effect                           | enum: NoSchedule, NoExecute, PreferNoSchedule | The effect of the taint                                                                                                                                                                          | No         |
| spec.labels                                                 | object                                        | Key-value map of Kubernetes labels                                                                                                                                                               | No         |
| advanced.aks.node_pool.values.upgrade_settings.max_surge    | string                                        |                                                                                                                                                                                                  | No         |
| advanced.aks.node_pool.values.kubelet_config                | object                                        | Block for all Linux configuration                                                                                                                                                                | No         |
| advanced.aks.node_pool.values.linux_os_config.sysctl_config | object                                        | Block for all System control configurations which should be under linux_os_config                                                                                                                | No         |
| advanced.aks.node_pool.values.tags.test                     | string                                        |                                                                                                                                                                                                  | No         |
| advanced.eks.managed_node_group                             | object                                        | Advanced values as per Terraform AWS EKS Managed Node Group resource https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/eks_node_group                                  | No         |
|                                                             |                                               | Note: If you want to pass multiple intsnace types then you can use `instance_types` attribute in advanced along with spec.instance_type                                                          |            |
| advanced.eks.self_managed_node_group                        | object                                        | Advanced values as per Terraform AWS EKS Self Managed Node Group module https://registry.terraform.io/modules/terraform-aws-modules/eks/aws/latest/submodules/self-managed-node-group?tab=inputs | No         |

### Outputs

|                                       | Type                                          | Description                                                                | Required   |
|:--------------------------------------|:----------------------------------------------|:---------------------------------------------------------------------------|:-----------|
| out                                   | object                                        | Output given by the resource for others to refer.                          | No         |
| out.spec.instance_type                | string                                        | Instance type associated with the Node Pool                                | No         |
| out.spec.min_node_count               | number                                        | Minimum number of worker nodes                                             | No         |
| out.spec.max_node_count               | number                                        | Maximum number of worker nodes                                             | No         |
| out.spec.disk_size                    | number                                        | Disk size in GiB for worker nodes                                          | No         |
| out.spec.azs                          | string                                        | Comma separated string of one or more availability zones for the Node Pool | No         |
| out.spec.is_public                    | boolean                                       | Whether to launch nodes in Public or Private network                       | No         |
| out.spec.taints.^[a-zA-Z]+\d+$.key    | string                                        | The key of the taint                                                       | No         |
| out.spec.taints.^[a-zA-Z]+\d+$.value  | string                                        | The value of the taint                                                     | No         |
| out.spec.taints.^[a-zA-Z]+\d+$.effect | enum: NoSchedule, NoExecute, PreferNoSchedule | The effect of the taint                                                    | No         |
| out.spec.labels                       | object                                        | Key-value map of Kubernetes labels                                         | No         |
| out.name                              | string                                        |                                                                            | No         |
| out.labels                            | object                                        |                                                                            | No         |
| out.taints                            | list                                          |                                                                            | No         |



## Helm

### Schema Properties

|                     | Type              | Description                                                                          | Required   |
|:--------------------|:------------------|:-------------------------------------------------------------------------------------|:-----------|
| kind                | const: helm       | Kind of the intent, always set to 'helm'                                             | Yes        |
| flavor              | const: default    | Flavor for the Helm chart implementation, always set to 'default'                    | Yes        |
| spec.helm.chart     | string            | URL or local path to the Helm chart package                                          | No         |
| spec.helm.namespace | string            | Namespace to deploy the Helm chart in                                                | No         |
| spec.helm.wait      | boolean           | Whether to wait for the Helm chart deployment to complete                            | No         |
| spec.env            | array             | List of dynamic values to be passed to the chart, i.e to reference a variable/secret | No         |
| spec.values         | object            | The values to be passed on to the chart in the form of a json object                 | No         |
| version             | enum: 0.1, latest |                                                                                      | Yes        |
| disabled            | boolean           | Flag to disable the resource                                                         | No         |
| provided            | boolean           | Flag to tell if the resource should not be provisioned by facets                     | No         |
| depends_on          | array             | Dependencies on other resources. e.g. application x may depend on y                  | No         |
| metadata.name       | string            | Name of the resource                                                                 | No         |
|                     |                   |     - if not specified, fallback is the `filename`                                   |            |

### Outputs

| Type   | Description   | Required   |
|--------|---------------|------------|



## Schemahero_table

### Schema Properties

|                                         | Type                                     | Description                                                                                                                                                                    | Required   |
|:----------------------------------------|:-----------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                                    | string                                   | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances                                                | Yes        |
| flavor                                  | enum: default                            | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc.                                                                     | Yes        |
| version                                 | enum: 0.1                                | This field can be used to pin to a particular version                                                                                                                          | Yes        |
| lifecycle                               | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                              | No         |
| disabled                                | boolean                                  | Flag to disable the resource                                                                                                                                                   | No         |
| conditional_on_intent                   | string                                   | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| provided                                | boolean                                  | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| depends_on                              | array                                    | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| metadata.name                           | string                                   | Name of the resource                                                                                                                                                           | No         |
| spec                                    | object                                   | Specification as per resource types schema                                                                                                                                     | Yes        |
| advanced                                | object                                   | Additional fields if any for a particular implementation of a resource                                                                                                         | No         |
| spec.connection                         | enum: mysql                              | Database connection type                                                                                                                                                       | No         |
| spec.database                           | string                                   | Name of the schemahero database object                                                                                                                                         | No         |
| spec.primary_key                        | array                                    | List of column names as the primary key of the table                                                                                                                           | No         |
| spec.indexes..*.columns                 | array                                    | Define columns in the index                                                                                                                                                    | No         |
| spec.indexes..*.name                    | string                                   | Name of the index                                                                                                                                                              | No         |
| spec.indexes..*.is_unique               | boolean                                  | Bool indicating a unique index                                                                                                                                                 | No         |
| spec.foreign_keys..*.columns            | array                                    | Columns in the foreign key                                                                                                                                                     | No         |
| spec.foreign_keys..*.references.table   | string                                   | Table name of the reference table                                                                                                                                              | No         |
| spec.foreign_keys..*.references.columns | array                                    | Foreign table columns                                                                                                                                                          | No         |
| spec.columns..*.name                    | string                                   | Name of the column                                                                                                                                                             | No         |
| spec.columns..*.type                    | string                                   | Data type of the column                                                                                                                                                        | No         |
| spec.columns..*.constraints             | object                                   | Adding constraints to the table                                                                                                                                                | No         |
| spec.columns..*.attributes              | object                                   | Adding attributes to the column                                                                                                                                                | No         |
| advanced.default                        | object                                   | Spec values as per the API reference https://schemahero.io/reference/v1alpha4/table/                                                                                           | No         |

### Outputs

|     | Type   | Description                                       | Required   |
|:----|:-------|:--------------------------------------------------|:-----------|
| out | object | Output given by the resource for others to refer. | No         |



## Log_collector

### Schema Properties

|                                                   | Type                                     | Description                                                                                                                                                                                                                              | Required   |
|:--------------------------------------------------|:-----------------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                                              | enum: log_collector                      |                                                                                                                                                                                                                                          | Yes        |
| flavor                                            | enum: loki, loki_s3, loki_blob           |                                                                                                                                                                                                                                          | Yes        |
| version                                           | enum: 0.1, latest                        |                                                                                                                                                                                                                                          | Yes        |
| lifecycle                                         | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                                                                                        | No         |
| disabled                                          | boolean                                  | Flag to disable the resource                                                                                                                                                                                                             | No         |
| conditional_on_intent                             | string                                   | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail.                                                           | No         |
| provided                                          | boolean                                  | Flag to tell if the resource should not be provisioned by facets                                                                                                                                                                         | No         |
| depends_on                                        | array                                    | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                                                                                  | No         |
| metadata.name                                     | string                                   | Name of the resource                                                                                                                                                                                                                     | No         |
|                                                   |                                          |     - if not specified, fallback is the `filename`                                                                                                                                                                                       |            |
| spec                                              | object                                   | Specification as per resource types schema                                                                                                                                                                                               | Yes        |
| advanced                                          | object                                   | Additional fields if any for a particular implementation of a resource                                                                                                                                                                   | No         |
| spec.retentation_days                             | number                                   | The number of days to retain an object after being created. Specify the number of days as an integer, e.g. 30 for 30 days                                                                                                                | No         |
| spec.storage_size                                 | string                                   | Storage capacity to store logs. You can express storage size as a plain integer or as a fixed-point number using one of these quantity suffixes: E, P, T, G, M, k. You can also use the power-of-two equivalents: Ei, Pi, Ti, Gi, Mi, Ki | No         |
| advanced.loki.loki.values                         | object                                   | Helm values as per the chart https://artifacthub.io/packages/helm/grafana/loki-distributed                                                                                                                                               | No         |
|                                                   |                                          |  Note: By default `loki.structuredConfig.storage_config` is configured to use minio storage backend                                                                                                                                      |            |
| advanced.loki.minio.values                        | object                                   | Helm values as per the chart https://artifacthub.io/packages/helm/bitnami/minio                                                                                                                                                          | No         |
| advanced.loki.promtail.values                     | object                                   | Helm values as per the chart https://artifacthub.io/packages/helm/grafana/promtail                                                                                                                                                       | No         |
|                                                   |                                          |  Note: By default, loki push endpoint will be configured automatically to send log entries to Loki                                                                                                                                       |            |
| advanced.loki.loki_canary.enable_loki_canary      | boolean                                  | Whether to enable loki canary or not                                                                                                                                                                                                     | No         |
| advanced.loki.loki_canary.values                  | object                                   | Helm values as per the chart https://artifacthub.io/packages/helm/grafana/loki-canary                                                                                                                                                    | No         |
|                                                   |                                          |  Note: By default, `lokiAddress` will be set with loki endpoint and `serviceMonitor` is enabled                                                                                                                                          |            |
| advanced.loki_s3.bucket_name                      | string                                   | AWS S3 bucket name to distribute chunks.                                                                                                                                                                                                 | No         |
|                                                   |                                          |  Note: You can use AWS S3 module (https://facets-cloud.github.io/facets-schemas/schemas/s3/s3.schema.json) to create bucket and map the name using `${s3.<name-of-your-resource>.bucket_name}`                                           |            |
| advanced.loki_s3.loki.values                      | object                                   | Helm values as per the chart https://artifacthub.io/packages/helm/grafana/loki-distributed                                                                                                                                               | No         |
|                                                   |                                          |  Note: By default `loki.structuredConfig.storage_config` is configured to use minio storage backend                                                                                                                                      |            |
| advanced.loki_s3.minio.values                     | object                                   | Helm values as per the chart https://artifacthub.io/packages/helm/bitnami/minio                                                                                                                                                          | No         |
| advanced.loki_s3.promtail.values                  | object                                   | Helm values as per the chart https://artifacthub.io/packages/helm/grafana/promtail                                                                                                                                                       | No         |
|                                                   |                                          |  Note: By default, loki push endpoint will be configured automatically to send log entries to Loki                                                                                                                                       |            |
| advanced.loki_s3.loki_canary.enable_loki_canary   | boolean                                  | Whether to enable loki canary or not                                                                                                                                                                                                     | No         |
| advanced.loki_s3.loki_canary.values               | object                                   | Helm values as per the chart https://artifacthub.io/packages/helm/grafana/loki-canary                                                                                                                                                    | No         |
|                                                   |                                          |  Note: By default, `lokiAddress` will be set with loki endpoint and `serviceMonitor` is enabled                                                                                                                                          |            |
| advanced.loki_blob.container_name                 | string                                   | the container name which you created via azure_storage_container `${azure_storage_container.<name-of-your-resource>.out.attributes.container_name}`                                                                                      | No         |
| advanced.loki_blob.storage_account_name           | string                                   | the storage account name which is part of the container that is  created via azure_storage_container `${azure_storage_container.<name-of-your-resource>.out.attributes.storage_account_name}`                                            | No         |
| advanced.loki_blob.primary_access_key             | string                                   | the storage account primary access key that is created for the storage account  `${azure_storage_container.<name-of-your-resource>.out.attributes.primary_access_key}`                                                                   | No         |
| advanced.loki_blob.loki.values                    | object                                   | Helm values as per the chart https://artifacthub.io/packages/helm/grafana/loki-distributed                                                                                                                                               | No         |
|                                                   |                                          |  Note: By default `loki.structuredConfig.storage_config` is configured to use minio storage backend                                                                                                                                      |            |
| advanced.loki_blob.minio.values                   | object                                   | Helm values as per the chart https://artifacthub.io/packages/helm/bitnami/minio                                                                                                                                                          | No         |
| advanced.loki_blob.promtail.values                | object                                   | Helm values as per the chart https://artifacthub.io/packages/helm/grafana/promtail                                                                                                                                                       | No         |
|                                                   |                                          |  Note: By default, loki push endpoint will be configured automatically to send log entries to Loki                                                                                                                                       |            |
| advanced.loki_blob.loki_canary.enable_loki_canary | boolean                                  | Whether to enable loki canary or not                                                                                                                                                                                                     | No         |
| advanced.loki_blob.loki_canary.values             | object                                   | Helm values as per the chart https://artifacthub.io/packages/helm/grafana/loki-canary                                                                                                                                                    | No         |
|                                                   |                                          |  Note: By default, `lokiAddress` will be set with loki endpoint and `serviceMonitor` is enabled                                                                                                                                          |            |

### Outputs

|                           | Type   | Description                                                                                                                                                                                                                              | Required   |
|:--------------------------|:-------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| out                       | object | Output given by the resource for others to refer.                                                                                                                                                                                        | No         |
| out.spec.retentation_days | number | The number of days to retain an object after being created. Specify the number of days as an integer, e.g. 30 for 30 days                                                                                                                | No         |
| out.spec.storage_size     | string | Storage capacity to store logs. You can express storage size as a plain integer or as a fixed-point number using one of these quantity suffixes: E, P, T, G, M, k. You can also use the power-of-two equivalents: Ei, Pi, Ti, Gi, Mi, Ki | No         |



## Mysql

### Schema Properties

|                                 | Type                                         | Description                                                                                                                                                                    | Required   |
|:--------------------------------|:---------------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                            | enum: mysql                                  | Describes the type of resource mysql. If not specified, fallback is the `folder_name`/instances                                                                                | Yes        |
| flavor                          | enum: aurora, cloudsql, rds, flexible_server | Implementation selector for the resource. e.g. for a resource type mysql.                                                                                                      | Yes        |
| version                         | enum: 0.1, latest                            | This field can be used to pin to a particular version                                                                                                                          | Yes        |
| lifecycle                       | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP     | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                              | No         |
| disabled                        | boolean                                      | Flag to disable the resource                                                                                                                                                   | No         |
| conditional_on_intent           | string                                       | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| provided                        | boolean                                      | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| depends_on                      | array                                        | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| metadata.name                   | string                                       | Name of the resource                                                                                                                                                           | No         |
|                                 |                                              |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                            | object                                       | Specification as per resource types schema                                                                                                                                     | Yes        |
| advanced                        |                                              |                                                                                                                                                                                | No         |
| spec.mysql_version              | string                                       | Aurora/CloudSql/Rds compatible MySQL version (eg: 8.0.mysql_aurora.3.02.0, 8.0.31, 8.0)                                                                                        | No         |
| spec.size.writer.volume         | string                                       | The size of the volume. e.g '10G' or '5Gi'                                                                                                                                     | No         |
| spec.size.reader.instance_count | integer                                      | The number of instances to create.                                                                                                                                             | No         |
| spec.size.reader.replica_count  | integer                                      | [Deprecated] The number of instances to create.                                                                                                                                | No         |

### Outputs

|                                                   | Type    | Description                                                                             | Required   |
|:--------------------------------------------------|:--------|:----------------------------------------------------------------------------------------|:-----------|
| out                                               | object  | Output given by the resource for others to refer.                                       | No         |
| out.spec.mysql_version                            | string  | Aurora/CloudSql/Rds compatible MySQL version (eg: 8.0.mysql_aurora.3.02.0, 8.0.31, 8.0) | No         |
| out.spec.size.writer.volume                       | string  | The size of the volume. e.g '10G' or '5Gi'                                              | No         |
| out.spec.size.reader.instance_count               | integer | The number of instances to create.                                                      | No         |
| out.spec.size.reader.replica_count                | integer | [Deprecated] The number of instances to create.                                         | No         |
| out.interfaces.reader.username                    | string  | Username to connect (if any)                                                            | No         |
| out.interfaces.reader.password                    | string  | Password to connect (if any)                                                            | No         |
| out.interfaces.reader.host                        | string  | Host for service discovery                                                              | No         |
| out.interfaces.reader.port                        | string  | Port for service discovery                                                              | No         |
| out.interfaces.reader.name                        | string  | Name of interface, same as key                                                          | No         |
| out.interfaces.reader.connection_string           | string  | Connection string to connect                                                            | No         |
| out.interfaces.writer.username                    | string  | Username to connect (if any)                                                            | No         |
| out.interfaces.writer.password                    | string  | Password to connect (if any)                                                            | No         |
| out.interfaces.writer.host                        | string  | Host for service discovery                                                              | No         |
| out.interfaces.writer.port                        | string  | Port for service discovery                                                              | No         |
| out.interfaces.writer.name                        | string  | Name of interface, same as key                                                          | No         |
| out.interfaces.writer.connection_string           | string  | Connection string to connect                                                            | No         |
| out.instances.^([a-z]*-[0-9]+)$.username          | string  | Username to connect (if any)                                                            | No         |
| out.instances.^([a-z]*-[0-9]+)$.password          | string  | Password to connect (if any)                                                            | No         |
| out.instances.^([a-z]*-[0-9]+)$.host              | string  | Host for service discovery                                                              | No         |
| out.instances.^([a-z]*-[0-9]+)$.port              | string  | Port for service discovery                                                              | No         |
| out.instances.^([a-z]*-[0-9]+)$.name              | string  | Name of interface, same as key                                                          | No         |
| out.instances.^([a-z]*-[0-9]+)$.connection_string | string  | Connection string to connect                                                            | No         |



## Schemahero_database

### Schema Properties

|                       | Type                                     | Description                                                                                                                                                                    | Required   |
|:----------------------|:-----------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                  | string                                   | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances                                                | Yes        |
| flavor                | enum: default                            | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc.                                                                     | Yes        |
| version               | enum: 0.1                                | This field can be used to pin to a particular version                                                                                                                          | Yes        |
| lifecycle             | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                              | No         |
| disabled              | boolean                                  | Flag to disable the resource                                                                                                                                                   | No         |
| conditional_on_intent | string                                   | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| provided              | boolean                                  | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| depends_on            | array                                    | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| metadata.name         | string                                   | Name of the resource                                                                                                                                                           | No         |
|                       |                                          |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                  | object                                   | Specification as per resource types schema                                                                                                                                     | Yes        |
| advanced              | object                                   | Additional fields if any for a particular implementation of a resource                                                                                                         | No         |
| spec.connection       | enum: mysql                              | Database connection type                                                                                                                                                       | No         |
| spec.uri              | string                                   | Connection uri of the database.                                                                                                                                                | No         |
| advanced.default      | object                                   | Spec values as per the API reference https://schemahero.io/reference/v1alpha4/database/                                                                                        | No         |

### Outputs

|          | Type   | Description                                       | Required   |
|:---------|:-------|:--------------------------------------------------|:-----------|
| out      | object | Output given by the resource for others to refer. | No         |
| out.name | string | Name of the schemahero resource                   | No         |



## Service

### Schema Properties

|                                                               | Type                                         | Description                                                                                                                                                                                                                                                                                        | Required   |
|:--------------------------------------------------------------|:---------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                                                          | enum: service                                | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances                                                                                                                                                                    | No         |
| flavor                                                        | enum: default                                | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc.                                                                                                                                                                                         | No         |
| version                                                       | enum: 0.1                                    | This field can be used to pin to a particular version                                                                                                                                                                                                                                              | No         |
| lifecycle                                                     | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP     | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                                                                                                                                                  | No         |
| disabled                                                      | boolean                                      | Flag to disable the resource                                                                                                                                                                                                                                                                       | No         |
| conditional_on_intent                                         | string                                       | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail.                                                                                                                     | No         |
| provided                                                      | boolean                                      | Flag to tell if the resource should not be provisioned by facets                                                                                                                                                                                                                                   | No         |
| depends_on                                                    | array                                        | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                                                                                                                                            | No         |
| metadata.name                                                 | string                                       | Name of the resource                                                                                                                                                                                                                                                                               | No         |
|                                                               |                                              |     - if not specified, fallback is the `filename`                                                                                                                                                                                                                                                 |            |
| spec                                                          | object                                       | Specification as per resource types schema                                                                                                                                                                                                                                                         | No         |
| advanced                                                      | object                                       | Additional fields if any for a particular implementation of a resource                                                                                                                                                                                                                             | No         |
| spec.release.image                                            | string                                       | The docker image of the application that you want to run                                                                                                                                                                                                                                           | No         |
| spec.release.build.artifactory                                | string                                       | Parent artifactory name                                                                                                                                                                                                                                                                            | No         |
| spec.release.build.name                                       | string                                       | Name of the artifactory                                                                                                                                                                                                                                                                            | No         |
| spec.release.strategy.type                                    | enum: RollingUpdate, Recreate                | Your kubernetes rollout type eg: RollingUpdate or Recreate                                                                                                                                                                                                                                         | No         |
| spec.release.strategy.max_available                           | integer                                      | If type RollingUpdate , this is the max number of pods that can be created from the default replicas                                                                                                                                                                                               | No         |
| spec.release.strategy.max_unavailable                         | integer                                      | If type RollingUpdate , this is the max number of pods that can be unavailable from the default replicas                                                                                                                                                                                           | No         |
| spec.release.disruption_policy.min_available                  | integer                                      | This is the min number of pods should be available in case of failures                                                                                                                                                                                                                             | No         |
| spec.release.disruption_policy.max_unavailable                | integer                                      | This is the max number of pods that can be unavailable during a failure.                                                                                                                                                                                                                           | No         |
| spec.type                                                     | enum: application, statefulset, cronjob, job | This will specify the type of service you want to create. eg: application, statefulset,cronjob etc                                                                                                                                                                                                 | No         |
| spec.enable_host_anti_affinity                                | boolean                                      | boolean to enable or disable host anti affinity                                                                                                                                                                                                                                                    | No         |
| spec.env                                                      | object                                       | The key value pairs of all the environment variables that needs to be passed to the pod                                                                                                                                                                                                            | No         |
| spec.runtime.command                                          | array                                        | The list of commands you want to perform before starting the container                                                                                                                                                                                                                             | No         |
| spec.runtime.args                                             | array                                        | The list of arguments you want to pass for the above mentioned command                                                                                                                                                                                                                             | No         |
| spec.runtime.ports.^[a-zA-Z0-9_.-]*$.protocol                 | enum: tcp, udp                               | the protocol of the port , either tcp or udp                                                                                                                                                                                                                                                       | No         |
| spec.runtime.ports.^[a-zA-Z0-9_.-]*$.port                     | string                                       | the port number where the pod is exposed                                                                                                                                                                                                                                                           | No         |
| spec.runtime.ports.^[a-zA-Z0-9_.-]*$.service_port             | string                                       | the port number where the service is exposed, this is optional                                                                                                                                                                                                                                     | No         |
| spec.runtime.metrics.^[a-zA-Z0-9_.-]*$.path                   | string                                       | the path where the service monitor should scrape metrics                                                                                                                                                                                                                                           | No         |
| spec.runtime.metrics.^[a-zA-Z0-9_.-]*$.port_name              | string                                       | name of the service monitor                                                                                                                                                                                                                                                                        | No         |
| spec.runtime.size.cpu                                         | string                                       | The number of CPU cores required, e.g '1' or '1000m'                                                                                                                                                                                                                                               | No         |
| spec.runtime.size.memory                                      | string                                       | The amount of memory required, e.g '800Mi' or '1.5Gi'                                                                                                                                                                                                                                              | No         |
| spec.runtime.volumes.config_maps.^[a-zA-Z0-9_.-]*$.name       | string                                       | The name of the config map that has been created in the kubernetes cluster                                                                                                                                                                                                                         | No         |
| spec.runtime.volumes.config_maps.^[a-zA-Z0-9_.-]*$.mount_path | string                                       | The mount path for the config map                                                                                                                                                                                                                                                                  | No         |
| spec.runtime.health_checks.start_up_time                      | integer                                      | The time kubernetes api needs to wait until the application is ready                                                                                                                                                                                                                               | No         |
| spec.runtime.health_checks.timeout                            | integer                                      | Timeout for the health check                                                                                                                                                                                                                                                                       | No         |
| spec.runtime.health_checks.period                             | integer                                      | This is the repeated interval in which kubelet does a health check                                                                                                                                                                                                                                 | No         |
| spec.runtime.health_checks.port                               | integer                                      | Specify the port in which the health checks should be made                                                                                                                                                                                                                                         | No         |
| spec.runtime.health_checks.liveness_url                       | string                                       | URL to make the liveness check                                                                                                                                                                                                                                                                     | No         |
| spec.runtime.health_checks.readiness_url                      | string                                       | URL to make the readiness check                                                                                                                                                                                                                                                                    | No         |
| spec.runtime.health_checks.liveness_exec_command              | array                                        | The list of commands to make liveness check                                                                                                                                                                                                                                                        | No         |
| spec.runtime.health_checks.readiness_exec_command             | array                                        | The list of commands to make readiness check                                                                                                                                                                                                                                                       | No         |
| spec.runtime.autoscaling.min                                  | integer                                      | This is the min replicas where the hpa downscales to                                                                                                                                                                                                                                               | No         |
| spec.runtime.autoscaling.max                                  | integer                                      | This is the max replicas where the hpa upscales to                                                                                                                                                                                                                                                 | No         |
| spec.runtime.autoscaling.cpu_threshold                        | integer                                      | The max cpu threshold that the hpa waits until it upscales                                                                                                                                                                                                                                         | No         |
| spec.runtime.autoscaling.ram_threshold                        | integer                                      | The max ram threshold that the hpa waits until it upscales                                                                                                                                                                                                                                         | No         |
| spec.runtime.autoscaling.packets_per_second                   | string                                       | The max number of packets that can be sent to the pod, once exceeded it autoscales. eg: 10k                                                                                                                                                                                                        | No         |
| spec.runtime.autoscaling.requests_per_second.threshold        | string                                       | The rps threshold that hpa looks for , once exceeds it autoscales eg: 10k                                                                                                                                                                                                                          | No         |
| spec.runtime.autoscaling.requests_per_second.ingress_name     | string                                       | Name of the ingress object that the hpa should watch for autoscaling                                                                                                                                                                                                                               | No         |
| spec.restart_policy                                           | enum: Always, OnFailure, Never               | Can be of type Always , OnFailure or Never                                                                                                                                                                                                                                                         | No         |
| advanced.aws.iam.^[a-zA-Z0-9_.-]*$.arn                        | string                                       | The arn of the policy that you want to create a role and attach it to the deployment                                                                                                                                                                                                               | No         |
| advanced.gcp.iam.^[a-zA-Z0-9_.-]*$.role                       | string                                       | The id of the role that you want to attach to the deployment, you can check out this link on how to create roles in gcp https://cloud.google.com/iam/docs/creating-custom-roles#iam-custom-roles-get-metadata-console                                                                              | No         |
| advanced.chart.image_pull_secrets                             | object                                       | Map of all the image pull secrets for the container image to be pulled from the repository. Each map should have name specified within it. For more information refer to: https://kubernetes.io/docs/tasks/configure-pod-container/pull-image-private-registry/#create-a-pod-that-uses-your-secret | No         |
| advanced.chart.node_selector                                  | object                                       | Map of all node selectors for the application                                                                                                                                                                                                                                                      | No         |

### Outputs

|                                                    | Type    | Description                                                                             | Required   |
|:---------------------------------------------------|:--------|:----------------------------------------------------------------------------------------|:-----------|
| out                                                | object  | Output given by the resource for others to refer.                                       | No         |
| out.interfaces.^[a-zA-Z0-9_.-]*$.host              | string  | The hostname or IP address of the database or cache server.                             | No         |
| out.interfaces.^[a-zA-Z0-9_.-]*$.port              | integer | The port number to use for connecting to the database or cache server.                  | No         |
| out.interfaces.^[a-zA-Z0-9_.-]*$.username          | string  | The username to use for authentication when connecting to the database or cache server. | No         |
| out.interfaces.^[a-zA-Z0-9_.-]*$.password          | string  | The password to use for authentication when connecting to the database or cache server. | No         |
| out.interfaces.^[a-zA-Z0-9_.-]*$.connection_string | string  | The connection_string to use when connecting to the datastore.                          | No         |



## Alert_group

### Schema Properties

|                                                        | Type                                     | Description                                                                                                                                                                                   | Required   |
|:-------------------------------------------------------|:-----------------------------------------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                                                   | enum: alert_group                        |                                                                                                                                                                                               | Yes        |
| flavor                                                 | enum: default                            |                                                                                                                                                                                               | Yes        |
| version                                                | enum: 0.1, latest                        |                                                                                                                                                                                               | Yes        |
| lifecycle                                              | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                                             | No         |
| disabled                                               | boolean                                  | Flag to disable the resource                                                                                                                                                                  | No         |
| conditional_on_intent                                  | string                                   |                                                                                                                                                                                               | No         |
| provided                                               | boolean                                  | Flag to tell if the resource should not be provisioned by facets                                                                                                                              | No         |
| depends_on                                             | array                                    | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                                       | No         |
| metadata.name                                          | string                                   | Name of the resource                                                                                                                                                                          | No         |
|                                                        |                                          |     - if not specified, fallback is the `filename`                                                                                                                                            |            |
| spec                                                   | object                                   | Specification as per resource types schema                                                                                                                                                    | Yes        |
| advanced                                               | object                                   | Additional fields if any for a particular implementation of a resource                                                                                                                        | No         |
| spec.rules.^[a-zA-Z]+\d+$.expr                         | string                                   | alert expression                                                                                                                                                                              | No         |
| spec.rules.^[a-zA-Z]+\d+$.for                          | string                                   | The optional `for` clause causes Prometheus to wait for a certain duration between first encountering a new expression output vector element and counting an alert as firing for this element | No         |
| spec.rules.^[a-zA-Z]+\d+$.summary                      | string                                   | The summary of the alert rule                                                                                                                                                                 | No         |
| spec.rules.^[a-zA-Z]+\d+$.message                      | string                                   | The message for the alert rule                                                                                                                                                                | No         |
| spec.rules.^[a-zA-Z]+\d+$.severity                     | string                                   | The alert rule severity                                                                                                                                                                       | No         |
| spec.rules.^[a-zA-Z]+\d+$.resource_name                | string                                   | The resource name. Eg: {{ $labels.pod }}                                                                                                                                                      | No         |
| spec.rules.^[a-zA-Z]+\d+$.resource_type                | string                                   | The resource type. Eg: pod                                                                                                                                                                    | No         |
| spec.rules.^[a-zA-Z]+\d+$.labels.^[a-zA-Z0-9-_]*$      | string                                   | Label value                                                                                                                                                                                   | No         |
| spec.rules.^[a-zA-Z]+\d+$.annotations.^[a-zA-Z0-9-_]+$ | string                                   | Annotation value                                                                                                                                                                              | No         |

### Outputs

|     | Type   | Description                                       | Required   |
|:----|:-------|:--------------------------------------------------|:-----------|
| out | object | Output given by the resource for others to refer. | No         |



## K8s_resource

### Schema Properties

|                                       | Type                                     | Description                                                                                                                                                                    | Required   |
|:--------------------------------------|:-----------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                                  | enum: k8s_resource                       |                                                                                                                                                                                | Yes        |
| flavor                                | enum: default                            |                                                                                                                                                                                | Yes        |
| version                               | enum: 0.1, latest                        |                                                                                                                                                                                | Yes        |
| lifecycle                             | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                              | No         |
| disabled                              | boolean                                  | Flag to disable the resource                                                                                                                                                   | No         |
| conditional_on_intent                 | string                                   | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| provided                              | boolean                                  | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| depends_on                            | array                                    | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| metadata.name                         | string                                   | Name of the resource                                                                                                                                                           | No         |
|                                       |                                          |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                                  | object                                   | Specification as per resource types schema                                                                                                                                     | Yes        |
| advanced                              | object                                   | Additional fields if any for a particular implementation of a resource                                                                                                         | No         |
| advanced.k8s_resource.timeout         | integer                                  | Timeout for the resource                                                                                                                                                       | No         |
| advanced.k8s_resource.cleanup_on_fail | boolean                                  | Whether to clean up when the resource installation fails                                                                                                                       | No         |
| advanced.k8s_resource.wait            | boolean                                  | Whether to wait until all the resources has been successfully created                                                                                                          | No         |
| spec.resource                         | object                                   | This should contain the kubernetes YAML manifest converted to JSON and pasted in a block                                                                                       | No         |

### Outputs

|                                                    | Type   | Description                                       | Required   |
|:---------------------------------------------------|:-------|:--------------------------------------------------|:-----------|
| out                                                | object | Output given by the resource for others to refer. | No         |
| out.interfaces.^[a-zA-Z0-9_.-]*$.username          | string | Username to connect (if any)                      | No         |
| out.interfaces.^[a-zA-Z0-9_.-]*$.password          | string | Password to connect (if any)                      | No         |
| out.interfaces.^[a-zA-Z0-9_.-]*$.host              | string | Host for service discovery                        | No         |
| out.interfaces.^[a-zA-Z0-9_.-]*$.port              | string | Port for service discovery                        | No         |
| out.interfaces.^[a-zA-Z0-9_.-]*$.name              | string | Name of interface, same as key                    | No         |
| out.interfaces.^[a-zA-Z0-9_.-]*$.connection_string | string | Connection string to connect                      | No         |



## Rabbitmq

### Schema Properties

|                       | Type                                     | Description                                                                                                                                                                    | Required   |
|:----------------------|:-----------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                  | enum: rabbitmq                           |                                                                                                                                                                                | Yes        |
| flavor                | enum: k8s                                |                                                                                                                                                                                | Yes        |
| version               | enum: 0.1, latest                        |                                                                                                                                                                                | Yes        |
| lifecycle             | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                              | No         |
| disabled              | boolean                                  | Flag to disable the resource                                                                                                                                                   | No         |
| conditional_on_intent | string                                   | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| provided              | boolean                                  | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| depends_on            | array                                    | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| metadata.name         | string                                   | Name of the resource                                                                                                                                                           | No         |
|                       |                                          |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                  | object                                   | Specification as per resource types schema                                                                                                                                     | Yes        |
| advanced              | object                                   | Advanced values to be used by different flavours of RabbitMQ                                                                                                                   | No         |
| spec.rabbitmq_version | string                                   | Version of RabbitMQ e.g. 3.9.23                                                                                                                                                | No         |
| spec.size.volume      | string                                   | The size of the volume. e.g '10G' or '5Gi'                                                                                                                                     | No         |

### Outputs

|                                          | Type    | Description                                                                                                                                | Required   |
|:-----------------------------------------|:--------|:-------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| out                                      | object  | Output given by the resource for others to refer.                                                                                          | No         |
| out.interfaces.cluster.endpoint          | string  | A comma-separated string of endpoints in the format 'host1:port1,host2:port2'. The number of endpoints must be greater than or equal to 1. | No         |
| out.interfaces.cluster.username          | string  | The username to use for authentication when connecting to the datastore.                                                                   | No         |
| out.interfaces.cluster.password          | string  | The password to use for authentication when connecting to the datastore.                                                                   | No         |
| out.interfaces.cluster.connection_string | string  | The connection_string to use when connecting to the datastore.                                                                             | No         |
| out.interfaces.tcp.host                  | string  | The hostname or IP address of the database or cache server.                                                                                | No         |
| out.interfaces.tcp.port                  | integer | The port number to use for connecting to the database or cache server.                                                                     | No         |
| out.interfaces.tcp.username              | string  | The username to use for authentication when connecting to the database or cache server.                                                    | No         |
| out.interfaces.tcp.password              | string  | The password to use for authentication when connecting to the database or cache server.                                                    | No         |
| out.interfaces.tcp.connection_string     | string  | The connection_string to use when connecting to the datastore.                                                                             | No         |
| out.interfaces.http.host                 | string  | The hostname or IP address of the database or cache server.                                                                                | No         |
| out.interfaces.http.port                 | integer | The port number to use for connecting to the database or cache server.                                                                     | No         |
| out.interfaces.http.username             | string  | The username to use for authentication when connecting to the database or cache server.                                                    | No         |
| out.interfaces.http.password             | string  | The password to use for authentication when connecting to the database or cache server.                                                    | No         |
| out.interfaces.http.connection_string    | string  | The connection_string to use when connecting to the datastore.                                                                             | No         |



## Mongo

### Schema Properties

|                             | Type                                     | Description                                                                                                                                                                    | Required   |
|:----------------------------|:-----------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                        | enum: mongo                              | Describes the type of resource.                                                                                                                                                | Yes        |
| flavor                      | enum: k8s                                | Implementation selector for the Mongo resource. e.g. for a resource type k8s                                                                                                   | Yes        |
| version                     | enum: 0.2, latest                        | This field can be used to pin to a particular version                                                                                                                          | Yes        |
| lifecycle                   | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                              | No         |
| disabled                    | boolean                                  | Flag to disable the resource                                                                                                                                                   | No         |
| conditional_on_intent       | string                                   | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| provided                    | boolean                                  | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| depends_on                  | array                                    | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| metadata.name               | string                                   | Name of the resource                                                                                                                                                           | No         |
|                             |                                          |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                        | object                                   | Specification as per resource types schema                                                                                                                                     | Yes        |
| advanced                    | object                                   | Additional fields if any for a particular implementation of a resource                                                                                                         | No         |
| spec.authenticated          | boolean                                  | Make this mongodb instance Password protected                                                                                                                                  | No         |
| spec.mongo_version          | string                                   | Version of mongodb e.g. 6.0, see valid tags here: https://hub.docker.com/_/mongo/tags?page=1                                                                                   | No         |
| spec.size.instance_count    | integer                                  | The number of instances to create.                                                                                                                                             | No         |
| spec.size.replica_count     | integer                                  | [Deprecated] The number of instances to create.                                                                                                                                | No         |
| advanced.k8s.mongodb.values | object                                   | Helm values as per bitnami mongodb helm chart https://artifacthub.io/packages/helm/bitnami/mongodb                                                                             | No         |

### Outputs

|                                                   | Type    | Description                                                                                                                                | Required   |
|:--------------------------------------------------|:--------|:-------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| out                                               | object  | Output given by the resource for others to refer.                                                                                          | No         |
| out.spec.authenticated                            | boolean | Make this mongodb instance Password protected                                                                                              | No         |
| out.spec.mongo_version                            | string  | Version of mongodb e.g. 6.0, see valid tags here: https://hub.docker.com/_/mongo/tags?page=1                                               | No         |
| out.spec.size.instance_count                      | integer | The number of instances to create.                                                                                                         | No         |
| out.spec.size.replica_count                       | integer | [Deprecated] The number of instances to create.                                                                                            | No         |
| out.interfaces.cluster.endpoint                   | string  | A comma-separated string of endpoints in the format 'host1:port1,host2:port2'. The number of endpoints must be greater than or equal to 1. | No         |
| out.interfaces.cluster.username                   | string  | The username to use for authentication when connecting to the datastore.                                                                   | No         |
| out.interfaces.cluster.password                   | string  | The password to use for authentication when connecting to the datastore.                                                                   | No         |
| out.interfaces.cluster.connection_string          | string  | The connection_string to use when connecting to the datastore.                                                                             | No         |
| out.instances.^([a-z]*-[0-9]+)$.host              | string  | The hostname or IP address of the database or cache server.                                                                                | No         |
| out.instances.^([a-z]*-[0-9]+)$.port              | integer | The port number to use for connecting to the database or cache server.                                                                     | No         |
| out.instances.^([a-z]*-[0-9]+)$.username          | string  | The username to use for authentication when connecting to the database or cache server.                                                    | No         |
| out.instances.^([a-z]*-[0-9]+)$.password          | string  | The password to use for authentication when connecting to the database or cache server.                                                    | No         |
| out.instances.^([a-z]*-[0-9]+)$.connection_string | string  | The connection_string to use when connecting to the datastore.                                                                             | No         |



## Kafka

### Schema Properties

|                                   | Type                                     | Description                                                                                                                                                                    | Required   |
|:----------------------------------|:-----------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                              | enum: kafka                              |                                                                                                                                                                                | Yes        |
| flavor                            | enum: k8s                                |                                                                                                                                                                                | Yes        |
| version                           | enum: 0.2                                |                                                                                                                                                                                | Yes        |
| lifecycle                         | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                              | No         |
| disabled                          | boolean                                  | Flag to disable the resource                                                                                                                                                   | No         |
| conditional_on_intent             | string                                   | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| provided                          | boolean                                  | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| depends_on                        | array                                    | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| metadata.name                     | string                                   | Name of the resource                                                                                                                                                           | No         |
|                                   |                                          |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                              | object                                   | Specification as per resource types schema                                                                                                                                     | Yes        |
| advanced                          | object                                   | Additional fields if any for a particular implementation of a resource                                                                                                         | No         |
| spec.authenticated                | boolean                                  | Make this kafka is Password protected                                                                                                                                          | No         |
| spec.kafka_version                | string                                   | Version of kafka e.g. 3.2.3                                                                                                                                                    | No         |
| spec.persistence_enabled          | boolean                                  | Enable Persistence for this redis                                                                                                                                              | No         |
| spec.size.zookeeper.cpu           | string                                   | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu                                                | No         |
| spec.size.zookeeper.memory        | string                                   | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory                                          | No         |
| spec.size.zookeeper.volume        | string                                   | Volume request in kubernetes persistent volumes                                                                                                                                | No         |
| spec.size.zookeeper.replica_count | integer                                  | Number of zookeeper instances needs to be deployed                                                                                                                             | No         |
| spec.size.kafka.cpu               | string                                   | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu                                                | No         |
| spec.size.kafka.memory            | string                                   | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory                                          | No         |
| spec.size.kafka.volume            | string                                   | Volume request in kubernetes persistent volumes                                                                                                                                | No         |
| spec.size.kafka.replica_count     | integer                                  | Number of kafka instances needs to be deployed                                                                                                                                 | No         |
| advanced.k8s.kafka.values         | object                                   | Helm values as per the Bitnami kafka chart https://github.com/bitnami/charts/tree/master/bitnami/kafka                                                                         | No         |

### Outputs

|                                                  | Type                            | Description                                                                                                                                | Required   |
|:-------------------------------------------------|:--------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| out                                              | object                          | Output given by the resource for others to refer.                                                                                          | No         |
| out.interfaces.endpoint                          | string                          | A comma-separated string of endpoints in the format 'host1:port1,host2:port2'. The number of endpoints must be greater than or equal to 1. | No         |
| out.interfaces.username                          | string                          | The username to use for authentication when connecting to the datastore.                                                                   | No         |
| out.interfaces.password                          | string                          | The password to use for authentication when connecting to the datastore.                                                                   | No         |
| out.interfaces.connection_string                 | string                          | The connection_string to use when connecting to the datastore.                                                                             | No         |
| out.instances.^(kafka_[0-9]+)$.host              | string                          | The hostname or IP address of the database or cache server.                                                                                | No         |
| out.instances.^(kafka_[0-9]+)$.port              | integer                         | The port number to use for connecting to the database or cache server.                                                                     | No         |
| out.instances.^(kafka_[0-9]+)$.username          | string                          | The username to use for authentication when connecting to the database or cache server.                                                    | No         |
| out.instances.^(kafka_[0-9]+)$.password          | string                          | The password to use for authentication when connecting to the database or cache server.                                                    | No         |
| out.instances.^(kafka_[0-9]+)$.connection_string | string                          | The connection_string to use when connecting to the datastore.                                                                             | No         |
| out.extra_settings.kafka.security_protocol       | enum: SASL_PLAINTEXT, PLAINTEXT | The security protocol required to connect to the kafka broker                                                                              | No         |
| out.extra_settings.kafka.sasl_mechanism          | enum: SCRAM-SHA-256, PLAIN      | The sasl mechanism required to connect to the kafka broker                                                                                 | No         |



## Grafana_dashboard

### Schema Properties

|         | Type                    | Description   | Required   |
|:--------|:------------------------|:--------------|:-----------|
| kind    | enum: grafana_dashboard |               | Yes        |
| flavor  | enum: default           |               | Yes        |
| version | enum: 0.1, latest       |               | Yes        |

### Outputs

| Type   | Description   | Required   |
|--------|---------------|------------|



## Ingress

### Schema Properties

|                                                      | Type                                                         | Description                                                                                                                                                                                                                                                                                                                                                               | Required   |
|:-----------------------------------------------------|:-------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                                                 | enum: ingress                                                | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances                                                                                                                                                                                                                                           | Yes        |
| flavor                                               | enum: nginx_ingress_controller, aws_alb, azure_agic, gcp_alb | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc.                                                                                                                                                                                                                                                                | Yes        |
| version                                              | string                                                       | This field can be used to pin to a particular version                                                                                                                                                                                                                                                                                                                     | Yes        |
| lifecycle                                            | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP                     | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                                                                                                                                                                                                                         | No         |
| disabled                                             | boolean                                                      | Flag to disable the resource                                                                                                                                                                                                                                                                                                                                              | No         |
| conditional_on_intent                                | string                                                       | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail.                                                                                                                                                                                            | No         |
| provided                                             | boolean                                                      | Flag to tell if the resource should not be provisioned by facets                                                                                                                                                                                                                                                                                                          | No         |
| depends_on                                           | array                                                        | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                                                                                                                                                                                                                   | No         |
| metadata.name                                        | string                                                       | Name of the resource                                                                                                                                                                                                                                                                                                                                                      | No         |
|                                                      |                                                              |     - if not specified, fallback is the `filename`                                                                                                                                                                                                                                                                                                                        |            |
| spec                                                 | object                                                       | Specification as per resource types schema                                                                                                                                                                                                                                                                                                                                | Yes        |
| advanced                                             |                                                              |                                                                                                                                                                                                                                                                                                                                                                           | No         |
| spec.basicAuth                                       | boolean                                                      | Enable or disable basic auth                                                                                                                                                                                                                                                                                                                                              | No         |
| spec.grpc                                            | boolean                                                      | Enable or Disable grpc support in nginx_ingress_controller                                                                                                                                                                                                                                                                                                                | No         |
| spec.private                                         | boolean                                                      | Make this load balancer private                                                                                                                                                                                                                                                                                                                                           | No         |
| spec.domains.^[a-zA-Z0-9_.-]*$.domain                | string                                                       | The host name of the domain                                                                                                                                                                                                                                                                                                                                               | No         |
| spec.domains.^[a-zA-Z0-9_.-]*$.alias                 | string                                                       | Alias naming for the domain                                                                                                                                                                                                                                                                                                                                               | No         |
| spec.domains.^[a-zA-Z0-9_.-]*$.certificate_reference | string                                                       | Certificate reference name, if flavor is `aws_alb` then its the ACM ARN certificate reference else `gcp_alb` its the name of the managed certificate that you want facets to create in gcp. In case of `nginx_ingress_controller` its the uploaded k8s certificate referenced as secret. In case of `azure_agic` it is the secret key id of the secret in azure key vault | No         |
| spec.rules.^[a-zA-Z0-9_.-]*$.service_name            | string                                                       | The Kubernetes service name of the application                                                                                                                                                                                                                                                                                                                            | No         |
| spec.rules.^[a-zA-Z0-9_.-]*$.path                    | string                                                       | path of the application                                                                                                                                                                                                                                                                                                                                                   | No         |
| spec.rules.^[a-zA-Z0-9_.-]*$.port_name               | string                                                       | Port name of the service                                                                                                                                                                                                                                                                                                                                                  | No         |
| spec.rules.^[a-zA-Z0-9_.-]*$.port                    | string                                                       | Port number of the service                                                                                                                                                                                                                                                                                                                                                | No         |
| spec.rules.^[a-zA-Z0-9_.-]*$.domain_prefix           | string                                                       | Subdomain prefix for the service                                                                                                                                                                                                                                                                                                                                          | No         |
| spec.rules.^[a-zA-Z0-9_.-]*$.priority                | integer                                                      | Priority number for the above rule ( this can be from 1 - 1000 ) and it should be unique for each rule - applicable only for `aws_alb` version `0.2`                                                                                                                                                                                                                      | No         |
| spec.force_ssl_redirection                           | boolean                                                      | Force SSL redirection from http to https                                                                                                                                                                                                                                                                                                                                  | No         |
| spec.ipv6_enabled                                    | boolean                                                      | Enable/disable ipv6, this is a cloud specific check if your vpc has ipv6 support enabled                                                                                                                                                                                                                                                                                  | No         |

### Outputs

|                                                    | Type   | Description                                       | Required   |
|:---------------------------------------------------|:-------|:--------------------------------------------------|:-----------|
| out                                                | object | Output given by the resource for others to refer. | No         |
| out.interfaces.^[a-zA-Z0-9_.-]*$.username          | string | Username to connect (if any)                      | No         |
| out.interfaces.^[a-zA-Z0-9_.-]*$.password          | string | Password to connect (if any)                      | No         |
| out.interfaces.^[a-zA-Z0-9_.-]*$.host              | string | Host for service discovery                        | No         |
| out.interfaces.^[a-zA-Z0-9_.-]*$.port              | string | Port for service discovery                        | No         |
| out.interfaces.^[a-zA-Z0-9_.-]*$.name              | string | Name of interface, same as key                    | No         |
| out.interfaces.^[a-zA-Z0-9_.-]*$.connection_string | string | Connection string to connect                      | No         |



## Snapshot_schedule

### Schema Properties

|                              | Type                                     | Description                                                                                                                                                                                                                                       | Required   |
|:-----------------------------|:-----------------------------------------|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                         | enum: snapshot_schedule                  |                                                                                                                                                                                                                                                   | Yes        |
| flavor                       | enum: default                            |                                                                                                                                                                                                                                                   | Yes        |
| version                      | enum: 0.1, latest                        |                                                                                                                                                                                                                                                   | Yes        |
| lifecycle                    | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                                                                                                 | No         |
| disabled                     | boolean                                  | Flag to disable the resource                                                                                                                                                                                                                      | No         |
| conditional_on_intent        | string                                   | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail.                                                                    | No         |
| provided                     | boolean                                  | Flag to tell if the resource should not be provisioned by facets                                                                                                                                                                                  | No         |
| depends_on                   | array                                    | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                                                                                           | No         |
| metadata.name                | string                                   | Name of the resource                                                                                                                                                                                                                              | No         |
|                              |                                          |     - if not specified, fallback is the `filename`                                                                                                                                                                                                |            |
| spec                         | object                                   | Specification as per resource types schema                                                                                                                                                                                                        | Yes        |
| advanced                     | object                                   | Additional fields if any for a particular implementation of a resource                                                                                                                                                                            | No         |
| spec.schedule                | string                                   | The cronspec (https://en.wikipedia.org/wiki/Cron#Overview) that defines the schedule. It is interpreted with respect to the UTC timezone. The following pre-defined shortcuts are also supported: @hourly, @daily, @weekly, @monthly, and @yearly | No         |
| spec.resource_name           | string                                   | The name of the resource that you want to create a volume snapshot backup, if this is left blank, it will create volume backups for all resource name in the resource type                                                                        | No         |
| spec.resource_type           | string                                   | The name of the resource type which you need to create volume snapshot backup                                                                                                                                                                     | No         |
| spec.retention_policy.expiry | string                                   | The length of time a given snapshot should be retained, specified in hours. (168h = 1 week)                                                                                                                                                       | No         |
| spec.retention_policy.count  | integer                                  | The maximum number of snapshots per PVC to keep                                                                                                                                                                                                   | No         |

### Outputs

|     | Type   | Description                                       | Required   |
|:----|:-------|:--------------------------------------------------|:-----------|
| out | object | Output given by the resource for others to refer. | No         |



## Peering

### Schema Properties

|                       | Type                                     | Description                                                                                                                                                                    | Required   |
|:----------------------|:-----------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                  | string                                   | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances                                                | Yes        |
| flavor                | string                                   | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc.                                                                     | Yes        |
| version               | string                                   | This field can be used to pin to a particular version                                                                                                                          | Yes        |
| lifecycle             | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                              | No         |
| disabled              | boolean                                  | Flag to disable the resource                                                                                                                                                   | No         |
| conditional_on_intent | string                                   | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| provided              | boolean                                  | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| depends_on            | array                                    | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| metadata.name         | string                                   | Name of the resource                                                                                                                                                           | No         |
|                       |                                          |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                  | object                                   | Specification as per resource types schema                                                                                                                                     | Yes        |
| advanced              |                                          |                                                                                                                                                                                | No         |
| spec.account_id       | string                                   | The account id of the cloud that you want to peer vpc with                                                                                                                     | No         |
| spec.vpc_id           | string                                   | The accepter vpc id of the account id                                                                                                                                          | No         |
| spec.cidr             | string                                   | The CIDR range of the vpc that you want to peered                                                                                                                              | No         |
| spec.region           | string                                   | The region where the vpc exists in your account                                                                                                                                | No         |

### Outputs

|                                  | Type   | Description                                       | Required   |
|:---------------------------------|:-------|:--------------------------------------------------|:-----------|
| out                              | object | Output given by the resource for others to refer. | No         |
| out.interfaces.username          | string | Username to connect (if any)                      | No         |
| out.interfaces.password          | string | Password to connect (if any)                      | No         |
| out.interfaces.host              | string | Host for service discovery                        | No         |
| out.interfaces.port              | string | Port for service discovery                        | No         |
| out.interfaces.name              | string | Name of interface, same as key                    | No         |
| out.interfaces.connection_string | string | Connection string to connect                      | No         |



## Azure_storage_container

### Schema Properties

|                                                                                          | Type                                     | Description                                                                                                                                                                    | Required   |
|:-----------------------------------------------------------------------------------------|:-----------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                                                                                     | enum: azure_storage_container            |                                                                                                                                                                                | Yes        |
| flavor                                                                                   | enum: default                            |                                                                                                                                                                                | Yes        |
| version                                                                                  | enum: 0.1, latest                        |                                                                                                                                                                                | Yes        |
| lifecycle                                                                                | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                              | No         |
| disabled                                                                                 | boolean                                  | Flag to disable the resource                                                                                                                                                   | No         |
| conditional_on_intent                                                                    | string                                   | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| provided                                                                                 | boolean                                  | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| depends_on                                                                               | array                                    | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| metadata.name                                                                            | string                                   | Name of the resource                                                                                                                                                           | No         |
|                                                                                          |                                          |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                                                                                     | object                                   |                                                                                                                                                                                | Yes        |
| advanced                                                                                 | object                                   | Additional fields if any for a particular implementation of a resource                                                                                                         | No         |
| advanced.azure_storage_container.az_storage_account_name                                 | string                                   | The name of the storage account which you want this container to be added                                                                                                      | No         |
| advanced.azure_storage_container.az_storage_account_id                                   | string                                   | The id of the storage account which you want this container to be added                                                                                                        | No         |
| advanced.azure_storage_container.access_type                                             | enum: blob, container, private           | The Access Level configured for this Container. Possible values are blob, container or private. Defaults to private                                                            | No         |
| advanced.azure_storage_container.lifecycles.^[a-zA-Z0-9_.-]*$.prefix_match               | array                                    | An array of strings for prefixes to be matched.                                                                                                                                | No         |
| advanced.azure_storage_container.lifecycles.^[a-zA-Z0-9_.-]*$.tier_to_cool_after_days    | integer                                  | The age in days after last modification to tier blobs to cool storage. Supports blob currently at Hot tier. Must be between 0 and 99999. Defaults to -1                        | No         |
| advanced.azure_storage_container.lifecycles.^[a-zA-Z0-9_.-]*$.tier_to_archive_after_days | integer                                  | The age in days after last modification to tier blobs to archive storage. Supports blob currently at Hot or Cool tier. Must be between 0 and 99999. Defaults to -1             | No         |
| advanced.azure_storage_container.lifecycles.^[a-zA-Z0-9_.-]*$.delete_after_days          | integer                                  | The age in days after last modification to delete the blob. Must be between 0 and 99999. Defaults to -1                                                                        | No         |
| advanced.azure_storage_container.lifecycles.^[a-zA-Z0-9_.-]*$.snapshot_delete_after_days | integer                                  | The age in days after creation to delete the blob snapshot. Must be between 0 and 99999. Defaults to -1                                                                        | No         |

### Outputs

|                                     | Type   | Description                                                                            | Required   |
|:------------------------------------|:-------|:---------------------------------------------------------------------------------------|:-----------|
| out                                 | object | Output given by the resource for others to refer.                                      | No         |
| out.attributes.storage_account_name | string | The storage account name where the containers are created                              | No         |
| out.attributes.storage_account_id   | string | The storage account id where the containers are created                                | No         |
| out.attributes.primary_access_key   | string | The primary access key for the storage account                                         | No         |
| out.attributes.container_name       | string | The name of the container that is created and will be referenced by other applications | No         |



## Postgres

### Schema Properties

|                                 | Type                                     | Description                                                                                                                                                                    | Required   |
|:--------------------------------|:-----------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                            | enum: postgres                           | Describes the type of resource. e.g. cloudsql or aurora                                                                                                                        | Yes        |
| flavor                          | enum: cloudsql, aurora                   | Implementation selector for the Postgres resource. e.g. for a resource type cloudsql or aurora.                                                                                | Yes        |
| version                         | enum: 0.1, latest                        | This field can be used to pin to a particular version                                                                                                                          | Yes        |
| lifecycle                       | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                              | No         |
| disabled                        | boolean                                  | Flag to disable the resource                                                                                                                                                   | No         |
| conditional_on_intent           | string                                   | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| provided                        | boolean                                  | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| depends_on                      | array                                    | Dependencies on other resources. e.g. application x may depend on y                                                                                                            | No         |
| metadata.name                   | string                                   | Name of the resource                                                                                                                                                           | No         |
|                                 |                                          |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                            | object                                   | Specification as per resource types schema                                                                                                                                     | Yes        |
| advanced                        | object                                   | Advanced values to be used by different flavours of postgres between Cloudsql or Aurora                                                                                        | No         |
| spec.postgres_version           | string                                   | Version of postgres e.g. 12.11                                                                                                                                                 | No         |
| spec.size.writer.volume         | string                                   | The size of the volume. e.g '10G' or '5Gi'                                                                                                                                     | No         |
| spec.size.reader.instance_count | integer                                  | The number of instances to create.                                                                                                                                             | No         |
| spec.size.reader.replica_count  | integer                                  | [Deprecated] The number of instances to create.                                                                                                                                | No         |

### Outputs

|                                         | Type    | Description                                                                | Required   |
|:----------------------------------------|:--------|:---------------------------------------------------------------------------|:-----------|
| out                                     | object  | Output given by the resource for others to refer.                          | No         |
| out.spec.postgres_version               | string  | Version of postgres e.g. 12.11                                             | No         |
| out.spec.size.writer.volume             | string  | The size of the volume. e.g '10G' or '5Gi'                                 | No         |
| out.spec.size.reader.instance_count     | integer | The number of instances to create.                                         | No         |
| out.spec.size.reader.replica_count      | integer | [Deprecated] The number of instances to create.                            | No         |
| out.interfaces.reader.username          | string  | Username to connect (if any)                                               | No         |
| out.interfaces.reader.password          | string  | Password to connect (if any)                                               | No         |
| out.interfaces.reader.host              | string  | Host for service discovery                                                 | No         |
| out.interfaces.reader.port              | string  | Port for service discovery                                                 | No         |
| out.interfaces.reader.name              | string  | Name of interface, same as key                                             | No         |
| out.interfaces.reader.connection_string | string  | Connection string to connect                                               | No         |
| out.interfaces.writer.username          | string  | Username to connect (if any)                                               | No         |
| out.interfaces.writer.password          | string  | Password to connect (if any)                                               | No         |
| out.interfaces.writer.host              | string  | Host for service discovery                                                 | No         |
| out.interfaces.writer.port              | string  | Port for service discovery                                                 | No         |
| out.interfaces.writer.name              | string  | Name of interface, same as key                                             | No         |
| out.interfaces.writer.connection_string | string  | Connection string to connect                                               | No         |
| out.postgresVersion                     | string  | Version of postgres e.g. 12.11                                             | No         |
| out.excludedDatabases                   | list    | List of databases to be excluded for prometheus-postgres-exporter          | No         |
| out.defaultDatabase                     | string  | Default database for the connection string of prometheus-postgres-exporter | No         |



## Elasticsearch

### Schema Properties

|                            | Type                                     | Description                                                                                                                                                                    | Required   |
|:---------------------------|:-----------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                       | enum: elasticsearch                      |                                                                                                                                                                                | Yes        |
| flavor                     | enum: k8s                                |                                                                                                                                                                                | Yes        |
| version                    | enum: 0.1, latest                        |                                                                                                                                                                                | Yes        |
| lifecycle                  | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                              | No         |
| disabled                   | boolean                                  | Flag to disable the resource                                                                                                                                                   | No         |
| conditional_on_intent      | string                                   | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| provided                   | boolean                                  | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| depends_on                 | array                                    | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| metadata.name              | string                                   | Name of the resource                                                                                                                                                           | No         |
|                            |                                          |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                       | object                                   | Specification as per resource types schema                                                                                                                                     | Yes        |
| advanced                   | object                                   | Advanced values to be used by different flavours of Elasticsearch                                                                                                              | No         |
| spec.elasticsearch_version | string                                   | Version of Elasticsearch e.g. 8.5.1                                                                                                                                            | No         |
| spec.size.instance_count   | integer                                  | The number of instances to create.                                                                                                                                             | No         |
| spec.size.replica_count    | integer                                  | [Deprecated] The number of instances to create.                                                                                                                                | No         |

### Outputs

|                                          | Type    | Description                                                                                                                                | Required   |
|:-----------------------------------------|:--------|:-------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| out                                      | object  | Output given by the resource for others to refer.                                                                                          | No         |
| out.spec.elasticsearch_version           | string  | Version of Elasticsearch e.g. 8.5.1                                                                                                        | No         |
| out.spec.size.instance_count             | integer | The number of instances to create.                                                                                                         | No         |
| out.spec.size.replica_count              | integer | [Deprecated] The number of instances to create.                                                                                            | No         |
| out.interfaces.cluster.endpoint          | string  | A comma-separated string of endpoints in the format 'host1:port1,host2:port2'. The number of endpoints must be greater than or equal to 1. | No         |
| out.interfaces.cluster.username          | string  | The username to use for authentication when connecting to the datastore.                                                                   | No         |
| out.interfaces.cluster.password          | string  | The password to use for authentication when connecting to the datastore.                                                                   | No         |
| out.interfaces.cluster.connection_string | string  | The connection_string to use when connecting to the datastore.                                                                             | No         |



## Iam_policy

### Schema Properties

|                      | Type    | Description   | Required   |
|:---------------------|:--------|:--------------|:-----------|
| $schema              | string  |               | No         |
| kind                 | string  |               | Yes        |
| flavor               | string  |               | Yes        |
| lifecycle            | string  |               | Yes        |
| disabled             | boolean |               | Yes        |
| provided             | boolean |               | Yes        |
| depends_on           | array   |               | Yes        |
| metadata.annotations | object  |               | No         |

### Outputs

| Type   | Description   | Required   |
|--------|---------------|------------|



## Config_map

### Schema Properties

|                             | Type                                     | Description                                                                                                                                                                    | Required   |
|:----------------------------|:-----------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                        | enum: config_map                         |                                                                                                                                                                                | Yes        |
| flavor                      | enum: default                            |                                                                                                                                                                                | Yes        |
| version                     | enum: 0.1, latest                        |                                                                                                                                                                                | Yes        |
| lifecycle                   | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                              | No         |
| disabled                    | boolean                                  | Flag to disable the resource                                                                                                                                                   | No         |
| conditional_on_intent       | string                                   | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| provided                    | boolean                                  | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| depends_on                  | array                                    | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| metadata.name               | string                                   | Name of the resource                                                                                                                                                           | No         |
|                             |                                          |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                        | object                                   | Specification as per resource types schema                                                                                                                                     | Yes        |
| advanced                    | object                                   | Additional fields if any for a particular implementation of a resource                                                                                                         | No         |
| spec.data.^[a-zA-Z0-9_.-]*$ | string                                   | The contents of the file                                                                                                                                                       | No         |
| advanced.k8s                | object                                   | The advanced section of all the k8s config map module                                                                                                                          | No         |

### Outputs

|                       | Type   | Description                                                                                                                  | Required   |
|:----------------------|:-------|:-----------------------------------------------------------------------------------------------------------------------------|:-----------|
| out                   | object | Output given by the resource for others to refer.                                                                            | No         |
| out.^[a-zA-Z0-9_.-]*$ | string | This is the name of the output interface this can be any name depending on the number of prefix domains in the domains block | No         |



## Sqs

### Schema Properties

|                       | Type                                     | Description                                                                                                                                                                    | Required   |
|:----------------------|:-----------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                  | enum: sqs                                |                                                                                                                                                                                | Yes        |
| flavor                | enum: default                            |                                                                                                                                                                                | Yes        |
| version               | enum: 0.2                                |                                                                                                                                                                                | Yes        |
| lifecycle             | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                              | No         |
| disabled              | boolean                                  | Flag to disable the resource                                                                                                                                                   | No         |
| conditional_on_intent | string                                   | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| provided              | boolean                                  | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| depends_on            | array                                    | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| metadata.name         | string                                   | Name of the resource                                                                                                                                                           | No         |
|                       |                                          |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                  | object                                   |                                                                                                                                                                                | No         |
| advanced              | object                                   | Advanced values for SQS                                                                                                                                                        | No         |
| sqs_queue_name        | string                                   | Name of the SQS                                                                                                                                                                | No         |
| sqs_queue_arn         | string                                   | ARN of the SQS                                                                                                                                                                 | No         |
| sqs_queue_url         | string                                   | URL of the SQS                                                                                                                                                                 | No         |

### Outputs

|     | Type   | Description                                       | Required   |
|:----|:-------|:--------------------------------------------------|:-----------|
| out | object | Output given by the resource for others to refer. | No         |



## S3

### Schema Properties

|                       | Type                                     | Description                                                                                                                                                                    | Required   |
|:----------------------|:-----------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                  | enum: s3                                 |                                                                                                                                                                                | Yes        |
| flavor                | enum: default                            |                                                                                                                                                                                | Yes        |
| version               | enum: 0.1, 0.2, latest                   |                                                                                                                                                                                | Yes        |
| lifecycle             | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                              | No         |
| disabled              | boolean                                  | Flag to disable the resource                                                                                                                                                   | No         |
| conditional_on_intent | string                                   | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| provided              | boolean                                  | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| depends_on            | array                                    | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| metadata.name         | string                                   | Name of the resource                                                                                                                                                           | No         |
|                       |                                          |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                  | object                                   | Specification as per resource types schema                                                                                                                                     | No         |
| advanced              | object                                   | Advanced values for s3                                                                                                                                                         | No         |

### Outputs

|                             | Type   | Description                                       | Required   |
|:----------------------------|:-------|:--------------------------------------------------|:-----------|
| out                         | object | Output given by the resource for others to refer. | No         |
| out.spec                    | object |                                                   | No         |
| out.bucket_name             | string | Name of the s3 bucket                             | No         |
| out.bucket_arn              | string | ARN of the bucket                                 | No         |
| out.bucket_region           | string | Region of the Bucket                              | No         |
| out.iam_policies.READ_ONLY  | string | ARN of the read_only policy of the bucket         | No         |
| out.iam_policies.READ_WRITE | string | ARN of the read_write policy of the bucket        | No         |
