## Properties

| Property   | Type                | Required | Description                                                                                                                                                     |
|------------|---------------------|----------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `advanced` | [object](#advanced) | No       | Advanced section of kind service                                                                                                                                |
| `flavor`   | string              | No       | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc. Possible values are: `default`, `k8s`.               |
| `kind`     | string              | No       | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances Possible values are: `service`. |
| `out`      | [object](#out)      | No       |                                                                                                                                                                 |
| `spec`     | [object](#spec)     | No       |                                                                                                                                                                 |
| `version`  | string              | No       | This field can be used to pin to a particular version Possible values are: `0.1`, `latest`.                                                                     |

## advanced

Advanced section of kind service

### Properties

| Property | Type              | Required | Description                                        |
|----------|-------------------|----------|----------------------------------------------------|
| `aws`    | [object](#aws)    | No       | Map of all aws advanced keys                       |
| `chart`  | [object](#chart)  | No       | Map of all chart advanced keys                     |
| `common` | [object](#common) | No       | Map of all the advanced values passed to app-chart |
| `gcp`    | [object](#gcp)    | No       | Map of all gcp advanced keys                       |

### aws

Map of all aws advanced keys

#### Properties

| Property | Type           | Required | Description                                                                   |
|----------|----------------|----------|-------------------------------------------------------------------------------|
| `iam`    | [object](#iam) | No       | This is the iam key where all iam permissions are attached for the deployment |
| `vm`     | [object](#vm)  | No       | This is the advanced block where we define vm implementation for aws          |

#### iam

This is the iam key where all iam permissions are attached for the deployment

| Property | Type | Required | Description |
|----------|------|----------|-------------|

#### vm

This is the advanced block where we define vm implementation for aws

##### Properties

| Property            | Type                         | Required | Description                                               |
|---------------------|------------------------------|----------|-----------------------------------------------------------|
| `alb`               | [object](#alb)               | No       | Define the status of alb                                  |
| `autoscaling_group` | [object](#autoscaling_group) | No       | Define autoscaling group for the vm's                     |
| `is_public`         | boolean                      | No       | specify if the vm is public                               |
| `is_vm_image`       | boolean                      | No       | Boolean value to specify if the artifact is vm ami or not |
| `security_group`    | [object](#security_group)    | No       | Define securrity groups for the vm                        |
| `vm_image_id`       | string                       | No       | Image id of the vm                                        |

##### alb

Define the status of alb

###### Properties

| Property                        | Type                                     | Required | Description                                                                                                                                                                                                                                            |
|---------------------------------|------------------------------------------|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `additional_http_tcp_listeners` | [object](#additional_http_tcp_listeners) | No       | Additional http tcp listeners that you want to add to the loadbalancer                                                                                                                                                                                 |
| `additional_subnets`            | [object](#additional_subnets)            | No       | Define additional subnets to attach                                                                                                                                                                                                                    |
| `additional_target_groups`      | [object](#additional_target_groups)      | No       | A list of maps containing key/value pairs that define the target groups to be created. Order of these maps is important and the index of these are to be referenced in listener definitions. Required key/values: name, backend_protocol, backend_port |
| `enabled`                       | boolean                                  | No       | Controls if the Load Balancer should be created                                                                                                                                                                                                        |
| `internal`                      | boolean                                  | No       | Boolean determining if the load balancer is internal or externally facing                                                                                                                                                                              |
| `tags`                          | [object](#tags)                          | No       | Tags you want to add to the security group                                                                                                                                                                                                             |
| `target_group_arns`             | [object](#target_group_arns)             | No       | Define your target group arn's                                                                                                                                                                                                                         |

###### additional_http_tcp_listeners

Additional http tcp listeners that you want to add to the loadbalancer

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### additional_subnets

Define additional subnets to attach

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### additional_target_groups

A list of maps containing key/value pairs that define the target groups to be created. Order of these maps is important and the index of these are to be referenced in listener definitions. Required key/values: name, backend_protocol, backend_port

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### tags

Tags you want to add to the security group

**Properties**

| Property | Type   | Required | Description                           |
|----------|--------|----------|---------------------------------------|
| `Name`   | string | No       | A map of tags to add to all resources |

###### target_group_arns

Define your target group arn's

| Property | Type | Required | Description |
|----------|------|----------|-------------|

##### autoscaling_group

Define autoscaling group for the vm's

###### Properties

| Property                     | Type                               | Required | Description                                                                                    |
|------------------------------|------------------------------------|----------|------------------------------------------------------------------------------------------------|
| `block_device_mappings`      | [object](#block_device_mappings)   | No       | Specify volumes to attach to the instance besides the volumes specified by the AMI             |
| `cpu_options`                | [object](#cpu_options)             | No       | The CPU options for the instance                                                               |
| `credit_specification`       | [object](#credit_specification)    | No       | Customize the credit specification of the instance                                             |
| `instance_market_options`    | [object](#instance_market_options) | No       | The market (purchasing) option for the instance                                                |
| `instance_refresh`           | [object](#instance_refresh)        | No       | If this block is configured, start an Instance Refresh when this Auto Scaling Group is updated |
| `maintenance_options`        | string                             | No       | Define maintenance options                                                                     |
| `metadata_options`           | [object](#metadata_options)        | No       | Customize the metadata options for the instance                                                |
| `mixed_instances_policy`     | [object](#mixed_instances_policy)  | No       | Configuration block containing settings to define launch targets for Auto Scaling groups       |
| `network_interfaces`         | [object](#network_interfaces)      | No       | Customize network interfaces to be attached at instance boot time                              |
| `placement`                  | [object](#placement)               | No       | The placement of the instance                                                                  |
| `scaling_policies`           | [object](#scaling_policies)        | No       | Map of target scaling policy schedule to create                                                |
| `schedules`                  | [object](#schedules)               | No       | Map of autoscaling group schedule to create                                                    |
| `use_mixed_instances_policy` | boolean                            | No       | Specify if you want to use use mixed instances policy                                          |

###### block_device_mappings

Specify volumes to attach to the instance besides the volumes specified by the AMI

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### cpu_options

The CPU options for the instance

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### credit_specification

Customize the credit specification of the instance

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### instance_market_options

The market (purchasing) option for the instance

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### instance_refresh

If this block is configured, start an Instance Refresh when this Auto Scaling Group is updated

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### metadata_options

Customize the metadata options for the instance

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### mixed_instances_policy

Configuration block containing settings to define launch targets for Auto Scaling groups

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### network_interfaces

Customize network interfaces to be attached at instance boot time

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### placement

The placement of the instance

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### scaling_policies

Map of target scaling policy schedule to create

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### schedules

Map of autoscaling group schedule to create

| Property | Type | Required | Description |
|----------|------|----------|-------------|

##### security_group

Define securrity groups for the vm

###### Properties

| Property                              | Type                                           | Required | Description                                                 |
|---------------------------------------|------------------------------------------------|----------|-------------------------------------------------------------|
| `additional_ingress_cidr_blocks`      | [object](#additional_ingress_cidr_blocks)      | No       | List of IPv4 CIDR ranges to use on all ingress rules        |
| `additional_ingress_with_cidr_blocks` | [object](#additional_ingress_with_cidr_blocks) | No       | List of ingress rules to create where 'cidr_blocks' is used |
| `tags`                                | [object](#tags)                                | No       | Tags you want to add to the security group                  |

###### additional_ingress_cidr_blocks

List of IPv4 CIDR ranges to use on all ingress rules

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### additional_ingress_with_cidr_blocks

List of ingress rules to create where 'cidr_blocks' is used

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### tags

Tags you want to add to the security group

**Properties**

| Property | Type   | Required | Description                                   |
|----------|--------|----------|-----------------------------------------------|
| `Name`   | string | No       | A mapping of tags to assign to security group |

### chart

Map of all chart advanced keys

#### Properties

| Property             | Type                          | Required | Description                                                                                                                                                                                                                                                                                        |
|----------------------|-------------------------------|----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `image_pull_secrets` | [object](#image_pull_secrets) | No       | Map of all the image pull secrets for the container image to be pulled from the repository. Each map should have name specified within it. For more information refer to: https://kubernetes.io/docs/tasks/configure-pod-container/pull-image-private-registry/#create-a-pod-that-uses-your-secret |
| `node_selector`      | [object](#node_selector)      | No       | Map of all node selectors for the application                                                                                                                                                                                                                                                      |

#### image_pull_secrets

Map of all the image pull secrets for the container image to be pulled from the repository. Each map should have name specified within it. For more information refer to: https://kubernetes.io/docs/tasks/configure-pod-container/pull-image-private-registry/#create-a-pod-that-uses-your-secret

| Property | Type | Required | Description |
|----------|------|----------|-------------|

#### node_selector

Map of all node selectors for the application

| Property | Type | Required | Description |
|----------|------|----------|-------------|

### common

Map of all the advanced values passed to app-chart

#### Properties

| Property    | Type                 | Required | Description                                              |
|-------------|----------------------|----------|----------------------------------------------------------|
| `app_chart` | [object](#app_chart) | No       | Map of all advanced values to be configured to app-chart |

#### app_chart

Map of all advanced values to be configured to app-chart

##### Properties

| Property | Type              | Required | Description                                 |
|----------|-------------------|----------|---------------------------------------------|
| `values` | [object](#values) | No       | Values to be passed to app-chart helm chart |

##### values

Values to be passed to app-chart helm chart

###### Properties

| Property                           | Type                        | Required | Description                                                                                                                                                                                                                                                                                                                               |
|------------------------------------|-----------------------------|----------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `additional_k8s_env_from`          | array                       | No       | Allows you to set additional environment variables for a container from sources (ConfigMap, Secret), adhering to the Kubernetes schema for environment variables. For more details on the schema, refer to the Kubernetes API documentation at https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.25/#envfromsource-v1-core |
| `additional_k8s_env`               | array                       | No       | Allows you to set additional environment variables for a container, adhering to the Kubernetes schema for environment variables. For more details on the schema, refer to the Kubernetes API documentation at https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.25/#envvar-v1-core                                         |
| `additional_volume_mounts`         | array                       | No       | Allows you to set the volume mounts                                                                                                                                                                                                                                                                                                       |
| `additional_volume`                | array                       | No       | Allows you to set additional volumes for an application/statefulset, adhering to the Kubernetes schema for volumes. For more details on the schema, refer to the Kubernetes API documentation at https://kubernetes.io/docs/concepts/storage/volumes/                                                                                     |
| `enable_service_links`             | boolean                     | No       | Enable service links                                                                                                                                                                                                                                                                                                                      |
| `hpa`                              | [object](#hpa)              | No       | Configurations for horizontal pod autoscaler                                                                                                                                                                                                                                                                                              |
| `init_containers`                  | [object](#init_containers)  | No       | init containers configurations                                                                                                                                                                                                                                                                                                            |
| `lifecycle`                        | [object](#lifecycle)        | No       |                                                                                                                                                                                                                                                                                                                                           |
| `node`                             | [object](#node)             | No       | Node configurations                                                                                                                                                                                                                                                                                                                       |
| `pod_distribution`                 | [object](#pod_distribution) | No       | Pod topology for distributing pods across nodes                                                                                                                                                                                                                                                                                           |
| `pod`                              | [object](#pod)              | No       | Pod configurations                                                                                                                                                                                                                                                                                                                        |
| `security_context`                 | [object](#security_context) | No       | security context for container                                                                                                                                                                                                                                                                                                            |
| `sidecars`                         | [object](#sidecars)         | No       | Sidecar containers configurations                                                                                                                                                                                                                                                                                                         |
| `termination_grace_period_seconds` | number                      | No       | Specify the number of seconds for a pod wait for shut down after it has received the SIGTERM signal                                                                                                                                                                                                                                       |
| `tolerations`                      | [object](#tolerations)      | No       | Tolerations to be added to service                                                                                                                                                                                                                                                                                                        |

###### hpa

Configurations for horizontal pod autoscaler

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### init_containers

init containers configurations

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### lifecycle

**Properties**

| Property    | Type                 | Required | Description |
|-------------|----------------------|----------|-------------|
| `postStart` | [object](#poststart) | No       |             |
| `preStop`   | [object](#prestop)   | No       |             |

**postStart**

**Properties**

| Property | Type            | Required | Description |
|----------|-----------------|----------|-------------|
| `exec`   | [object](#exec) | No       |             |

**exec**

**Properties**

| Property  | Type  | Required | Description                 |
|-----------|-------|----------|-----------------------------|
| `command` | array | No       | The commands to be executed |

**preStop**

**Properties**

| Property | Type            | Required | Description |
|----------|-----------------|----------|-------------|
| `exec`   | [object](#exec) | No       |             |

**exec**

**Properties**

| Property  | Type  | Required | Description                 |
|-----------|-------|----------|-----------------------------|
| `command` | array | No       | The commands to be executed |

###### node

Node configurations

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### pod

Pod configurations

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### pod_distribution

Pod topology for distributing pods across nodes

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### security_context

security context for container

**Properties**

| Property                 | Type                       | Required | Description |
|--------------------------|----------------------------|----------|-------------|
| `comp_profile`           | string                     | No       |             |
| `fs_group_change_policy` | string                     | No       |             |
| `fsgroup`                | string                     | No       |             |
| `linux_options`          | [object](#linux_options)   | No       |             |
| `run_as_group`           | string                     | No       |             |
| `run_as_non_root`        | boolean                    | No       |             |
| `run_as_user`            | string                     | No       |             |
| `supplemental_groups`    | string                     | No       |             |
| `sysctls`                | string                     | No       |             |
| `windows_options`        | [object](#windows_options) | No       |             |

**linux_options**

| Property | Type | Required | Description |
|----------|------|----------|-------------|

**windows_options**

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### sidecars

Sidecar containers configurations

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### tolerations

Tolerations to be added to service

| Property | Type | Required | Description |
|----------|------|----------|-------------|

### gcp

Map of all gcp advanced keys

#### Properties

| Property | Type           | Required | Description                                                                   |
|----------|----------------|----------|-------------------------------------------------------------------------------|
| `iam`    | [object](#iam) | No       | This is the iam key where all iam permissions are attached for the deployment |

#### iam

This is the iam key where all iam permissions are attached for the deployment

| Property | Type | Required | Description |
|----------|------|----------|-------------|

## out

### Properties

| Property     | Type                  | Required | Description                                                           |
|--------------|-----------------------|----------|-----------------------------------------------------------------------|
| `interfaces` | [object](#interfaces) | No       | The output for your service module, this can be generated or provided |

### interfaces

The output for your service module, this can be generated or provided

| Property | Type | Required | Description |
|----------|------|----------|-------------|

## spec

### Properties

| Property                    | Type               | Required | Description                                                                                                                                                             |
|-----------------------------|--------------------|----------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `enable_host_anti_affinity` | boolean            | **Yes**  | boolean to enable or disable host anti affinity                                                                                                                         |
| `env`                       | [object](#env)     | **Yes**  | The key value pairs of all the environment variables that needs to be passed to the pod                                                                                 |
| `release`                   | [object](#release) | **Yes**  | Map of all release keys                                                                                                                                                 |
| `runtime`                   | [object](#runtime) | **Yes**  | Map of all runtime keys                                                                                                                                                 |
| `type`                      | string             | **Yes**  | This will specify the type of service you want to create. eg: application, statefulset,cronjob etc Possible values are: `application`, `statefulset`, `cronjob`, `job`. |
| `restart_policy`            | string             | No       | Can be of type Always , OnFailure or Never Possible values are: `Always`, `OnFailure`, `Never`.                                                                         |

### env

The key value pairs of all the environment variables that needs to be passed to the pod

| Property | Type | Required | Description |
|----------|------|----------|-------------|

### release

Map of all release keys

#### Properties

| Property            | Type                         | Required | Description                                                             |
|---------------------|------------------------------|----------|-------------------------------------------------------------------------|
| `build`             | [object](#build)             | No       | These contains the build objects required for running the containers    |
| `disruption_policy` | [object](#disruption_policy) | No       | The disruption policy for the service                                   |
| `image_pull_policy` | string                       | No       | ImagePullPolicy Possible values are: `IfNotPresent`, `Always`, `Never`. |
| `image`             | string                       | No       | The docker image of the application that you want to run                |
| `strategy`          | [object](#strategy)          | No       | The type of upgrade strategy to be followed by this service             |

#### build

These contains the build objects required for running the containers

##### Properties

| Property      | Type   | Required | Description             |
|---------------|--------|----------|-------------------------|
| `artifactory` | string | **Yes**  | Parent artifactory name |
| `name`        | string | **Yes**  | Name of the artifactory |

#### disruption_policy

The disruption policy for the service

##### Properties

| Property          | Type   | Required | Description                                                              |
|-------------------|--------|----------|--------------------------------------------------------------------------|
| `max_unavailable` | string | No       | This is the max number of pods that can be unavailable during a failure. |
| `min_available`   | string | No       | This is the min number of pods should be available in case of failures   |

#### strategy

The type of upgrade strategy to be followed by this service

##### Properties

| Property              | Type                           | Required | Description                                                                                                                                             |
|-----------------------|--------------------------------|----------|---------------------------------------------------------------------------------------------------------------------------------------------------------|
| `blue_green_strategy` | [object](#blue_green_strategy) | No       | Configuration for BlueGreen strategy                                                                                                                    |
| `canary_strategy`     | [object](#canary_strategy)     | No       | Configuration for Canary strategy                                                                                                                       |
| `max_available`       | string                         | No       | If type RollingUpdate , this is the max number of pods that can be created from the default replicas                                                    |
| `max_unavailable`     | string                         | No       | If type RollingUpdate , this is the max number of pods that can be unavailable from the default replicas                                                |
| `type`                | string                         | No       | Your kubernetes rollout type eg: RollingUpdate / Recreate / BlueGreen / Canary Possible values are: `RollingUpdate`, `Recreate`, `BlueGreen`, `Canary`. |

##### blue_green_strategy

Configuration for BlueGreen strategy

###### Properties

| Property                          | Type    | Required | Description                                                                 |
|-----------------------------------|---------|----------|-----------------------------------------------------------------------------|
| `abort_scale_down_delay_seconds`  | integer | No       | The time in seconds to delay scaling down the old ReplicaSet after an abort |
| `auto_promotion_seconds`          | integer | No       | The time in seconds before automatic promotion                              |
| `auto_promotion`                  | boolean | No       | Flag to enable automatic promotion                                          |
| `enable_manual_rollout`           | boolean | No       | Flag to enable manual rollout                                               |
| `min_ready_seconds`               | integer | No       | The minimum number of seconds a new pod should be ready                     |
| `preview_replicas`                | integer | No       | The number of replicas for the preview service                              |
| `progress_deadline_abort`         | boolean | No       | Flag to abort the rollout if the progress deadline is exceeded              |
| `progress_deadline_seconds`       | integer | No       | The maximum time in seconds for a rollout to make progress                  |
| `restart_at`                      | string  | No       | Timestamp to restart the rollout                                            |
| `revision_history_limit`          | integer | No       | The number of old ReplicaSets to retain                                     |
| `rollback_window_revisions`       | integer | No       | The number of revisions to keep for rollback                                |
| `scale_down_delay_revision_limit` | integer | No       | The number of old ReplicaSets to retain for scale down delay                |
| `scale_down_delay_seconds`        | integer | No       | The time in seconds to delay scaling down the old ReplicaSet                |
| `successful_run_history_limit`    | integer | No       | The number of successful runs to keep in history                            |
| `unsuccessful_run_history_limit`  | integer | No       | The number of unsuccessful runs to keep in history                          |

##### canary_strategy

Configuration for Canary strategy

###### Properties

| Property                          | Type                       | Required | Description                                                                    |
|-----------------------------------|----------------------------|----------|--------------------------------------------------------------------------------|
| `abort_scale_down_delay_seconds`  | integer                    | No       | The time in seconds to delay scaling down the old ReplicaSet after an abort    |
| `analysis`                        | [object](#analysis)        | No       | Analysis configuration for the canary strategy                                 |
| `anti_affinity`                   | [object](#anti_affinity)   | No       | Anti-affinity configuration for the canary strategy                            |
| `canary_metadata`                 | [object](#canary_metadata) | No       | Metadata for the canary strategy                                               |
| `enable_manual_rollout`           | boolean                    | No       | Flag to enable manual rollout                                                  |
| `max_surge`                       | string                     | No       | The maximum number of pods that can be created over the desired number of pods |
| `max_unavailable`                 | integer                    | No       | The maximum number of pods that can be unavailable during the update           |
| `min_pods_per_replicaset`         | integer                    | No       | The minimum number of pods per ReplicaSet                                      |
| `min_ready_seconds`               | integer                    | No       | The minimum number of seconds a new pod should be ready                        |
| `progress_deadline_abort`         | boolean                    | No       | Flag to abort the rollout if the progress deadline is exceeded                 |
| `progress_deadline_seconds`       | integer                    | No       | The maximum time in seconds for a rollout to make progress                     |
| `restart_at`                      | string                     | No       | Timestamp to restart the rollout                                               |
| `revision_history_limit`          | integer                    | No       | The number of old ReplicaSets to retain                                        |
| `rollback_window_revisions`       | integer                    | No       | The number of revisions to keep for rollback                                   |
| `scale_down_delay_revision_limit` | integer                    | No       | The number of old ReplicaSets to retain for scale down delay                   |
| `scale_down_delay_seconds`        | integer                    | No       | The time in seconds to delay scaling down the old ReplicaSet                   |
| `stable_metadata`                 | [object](#stable_metadata) | No       | Metadata for the Stable strategy                                               |
| `steps`                           | array                      | No       | Steps configuration for the canary strategy                                    |
| `successful_run_history_limit`    | integer                    | No       | The number of successful runs to keep in history                               |
| `traffic_routing`                 | [object](#traffic_routing) | No       | Traffic routing configuration for the canary strategy                          |
| `unsuccessful_run_history_limit`  | integer                    | No       | The number of unsuccessful runs to keep in history                             |

###### analysis

Analysis configuration for the canary strategy

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### anti_affinity

Anti-affinity configuration for the canary strategy

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### canary_metadata

Metadata for the canary strategy

**Properties**

| Property      | Type                   | Required | Description                         |
|---------------|------------------------|----------|-------------------------------------|
| `annotations` | [object](#annotations) | No       | Annotations for the canary strategy |
| `labels`      | [object](#labels)      | No       | Labels for the canary strategy      |

**annotations**

Annotations for the canary strategy

| Property | Type | Required | Description |
|----------|------|----------|-------------|

**labels**

Labels for the canary strategy

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### stable_metadata

Metadata for the Stable strategy

**Properties**

| Property      | Type                   | Required | Description                         |
|---------------|------------------------|----------|-------------------------------------|
| `annotations` | [object](#annotations) | No       | Annotations for the Stable strategy |
| `labels`      | [object](#labels)      | No       | Labels for the Stable strategy      |

**annotations**

Annotations for the Stable strategy

| Property | Type | Required | Description |
|----------|------|----------|-------------|

**labels**

Labels for the Stable strategy

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### traffic_routing

Traffic routing configuration for the canary strategy

| Property | Type | Required | Description |
|----------|------|----------|-------------|

### runtime

Map of all runtime keys

#### Properties

| Property        | Type                     | Required | Description                                                            |
|-----------------|--------------------------|----------|------------------------------------------------------------------------|
| `health_checks` | [object](#health_checks) | **Yes**  | All the health check related key value pairs                           |
| `ports`         | [object](#ports)         | **Yes**  | Maps of all the ports that you want to expose in the service           |
| `size`          | [object](#size)          | **Yes**  | Size of the deployment pods                                            |
| `args`          | array                    | No       | The list of arguments you want to pass for the above mentioned command |
| `autoscaling`   | [object](#autoscaling)   | No       |                                                                        |
| `command`       | array                    | No       | The list of commands you want to perform before starting the container |
| `metrics`       | [object](#metrics)       | No       | Maps of all the metrics port that you want to expose to prometheus     |
| `volumes`       | [object](#volumes)       | No       | All the volumes you want to attach to the service                      |

#### autoscaling

##### Properties

| Property              | Type                           | Required | Description                                                                                 |
|-----------------------|--------------------------------|----------|---------------------------------------------------------------------------------------------|
| `cpu_threshold`       | string                         | No       | The max cpu threshold that the hpa waits until it upscales                                  |
| `max`                 | string                         | No       | This is the max replicas where the hpa upscales to                                          |
| `min`                 | string                         | No       | This is the min replicas where the hpa downscales to                                        |
| `packets_per_second`  | string                         | No       | The max number of packets that can be sent to the pod, once exceeded it autoscales. eg: 10k |
| `ram_threshold`       | string                         | No       | The max ram threshold that the hpa waits until it upscales                                  |
| `requests_per_second` | [object](#requests_per_second) | No       | Requests per second RPS object keys                                                         |

##### requests_per_second

Requests per second RPS object keys

###### Properties

| Property       | Type   | Required | Description                                                               |
|----------------|--------|----------|---------------------------------------------------------------------------|
| `ingress_name` | string | **Yes**  | Name of the ingress object that the hpa should watch for autoscaling      |
| `threshold`    | string | **Yes**  | The rps threshold that hpa looks for , once exceeds it autoscales eg: 10k |

#### health_checks

All the health check related key value pairs

##### Properties

| Property                  | Type   | Required | Description                                                                                                                           |
|---------------------------|--------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `liveness_exec_command`   | array  | No       | The list of commands to make liveness check                                                                                           |
| `liveness_period`         | string | No       | This is the repeated interval in which kubelet does a health check, this is takes precedence from period                              |
| `liveness_port`           | string | No       | Specify the port in which the health checks should be made, this is takes precedence from port                                        |
| `liveness_start_up_time`  | string | No       | The time kubernetes api needs to wait until the application is ready for liveness check, this is takes precedence from start_up_time  |
| `liveness_timeout`        | string | No       | Timeout for the health check liveness, this is takes precedence from timeout                                                          |
| `liveness_url`            | string | No       | URL to make the liveness check                                                                                                        |
| `period`                  | string | No       | This is the repeated interval in which kubelet does a health check                                                                    |
| `port`                    | string | No       | Specify the port in which the health checks should be made                                                                            |
| `readiness_exec_command`  | array  | No       | The list of commands to make readiness check                                                                                          |
| `readiness_period`        | string | No       | This is the repeated interval in which kubelet does a health check, this is takes precedence from period                              |
| `readiness_port`          | string | No       | Specify the port in which the health checks should be made, this is takes precedence from port                                        |
| `readiness_start_up_time` | string | No       | The time kubernetes api needs to wait until the application is ready for readiness check, this is takes precedence from start_up_time |
| `readiness_timeout`       | string | No       | Timeout for the health check readiness, this is takes precedence from timeout                                                         |
| `readiness_url`           | string | No       | URL to make the readiness check                                                                                                       |
| `start_up_time`           | string | No       | The time kubernetes api needs to wait until the application is ready                                                                  |
| `timeout`                 | string | No       | Timeout for the health check                                                                                                          |

#### metrics

Maps of all the metrics port that you want to expose to prometheus

| Property | Type | Required | Description |
|----------|------|----------|-------------|

#### ports

Maps of all the ports that you want to expose in the service

| Property | Type | Required | Description |
|----------|------|----------|-------------|

#### size

Size of the deployment pods

##### Properties

| Property       | Type   | Required | Description                                                                                                   |
|----------------|--------|----------|---------------------------------------------------------------------------------------------------------------|
| `cpu`          | string | **Yes**  | The number of CPU cores required, e.g '1' or '1000m'                                                          |
| `memory`       | string | **Yes**  | The amount of memory required, e.g '800Mi' or '1.5Gi'                                                         |
| `cpu_limit`    | string | No       | The CPU limit to set a maximum limit on the amount of CPU resources utilization, e.g '1' or '1000m'           |
| `memory_limit` | string | No       | The memory limit to set a maximum limit on the amount of memory resources utilization, e.g '800Mi' or '1.5Gi' |

#### volumes

All the volumes you want to attach to the service

##### Properties

| Property      | Type                   | Required | Description                                                              |
|---------------|------------------------|----------|--------------------------------------------------------------------------|
| `config_maps` | [object](#config_maps) | No       | The map of all the ConfigMaps that you want to mount.                    |
| `host_path`   | [object](#host_path)   | No       | The map of all the HostPaths that you want to mount.                     |
| `pvc`         | [object](#pvc)         | No       | The map of all the PVCs (PersistentVolumeClaims) that you want to mount. |
| `secrets`     | [object](#secrets)     | No       | The map of all the Secrets that you want to mount.                       |

##### config_maps

The map of all the ConfigMaps that you want to mount.

| Property | Type | Required | Description |
|----------|------|----------|-------------|

##### host_path

The map of all the HostPaths that you want to mount.

| Property | Type | Required | Description |
|----------|------|----------|-------------|

##### pvc

The map of all the PVCs (PersistentVolumeClaims) that you want to mount.

| Property | Type | Required | Description |
|----------|------|----------|-------------|

##### secrets

The map of all the Secrets that you want to mount.

| Property | Type | Required | Description |
|----------|------|----------|-------------|

