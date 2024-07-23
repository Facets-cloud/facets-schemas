# MongoDB

A MongoDB Instance in clustering mode.

## Properties

| Property     | Type                | Required | Description                                                                                                                                    |
| ------------ | ------------------- | -------- | ---------------------------------------------------------------------------------------------------------------------------------------------- |
| `kind`       | string              | **Yes**  | Describes the type of resource. e.g., ingress, application, mongodb, etc. If not specified, fallback is the `folder_name`/instances            |
| `flavor`     | string              | **Yes**  | Implementation selector for the resource.                                                                                                       |
| `version`    | string              | **Yes**  | This field can be used to pin to a particular version                                                                                          |
| `metadata`   | [object](#metadata) | **Yes**  | Metadata related to the resource                                                                                                               |
| `depends_on` | array of strings    | No       | Dependencies on other resources. e.g., application x may depend on mysql                                                                        |
| `provided`   | boolean             | No       | Flag to indicate if the resource should not be provisioned by facets                                                                            |
| `disabled`   | boolean             | No       | Flag to disable the resource                                                                                                                   |
| `lifecycle`  | string              | No       | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`). Possible values are: `ENVIRONMENT_BOOTSTRAP`, `ENVIRONMENT`. |
| `spec`       | [object](#spec)     | **Yes**  | Specification as per resource types schema                                                                                                     |
| `inputs`     | [object](#inputs)   | **Yes**  | Inputs required for the resource                                                                                                               |
| `out`        | [object](#out)      | No       | Output given by the resource for others to refer.                                                                                              |
| `advanced`   | [object](#advanced) | No       | Additional fields if any for a particular implementation of a resource                                                                         |

## metadata

Metadata related to the resource

| Property | Type   | Required | Description           |
| -------- | ------ | -------- | --------------------- |
| `name`   | string | **No**  | Name of the resource  |

## spec

Specification as per resource types schema

| Property            | Type                | Required | Description                                                                                 |
| ------------------- | ------------------- | -------- | ------------------------------------------------------------------------------------------- |
| `mongodb_version`   | string              | **Yes**  | Version of MongoDB e.g., 7.0                                                                |
| `size`              | [object](#size)     | **Yes**  | Details about the size of the MongoDB instance                                              |
| `region`            | string              | **Yes**  | Region for the MongoDB instance                                                             |
| `replication_specs` | array of [object](#replication_specs) | **Yes**  | Replication specifications for the MongoDB instance                                         |

### size

Details about the size of the MongoDB instance

| Property         | Type    | Required | Description                         |
| ---------------- | ------- | -------- | ----------------------------------- |
| `instance`       | string  | **Yes**  | The instance type of the node. e.g., 'M10', 'M20' |
| `instance_count` | integer | **Yes**  | The number of instances to create.  |
| `volume`         | integer | **Yes**  | The size of the volume in GB.       |

### replication_specs

Replication specifications for the MongoDB instance

| Property            | Type                | Required | Description                                                                                 |
| ------------------- | ------------------- | -------- | ------------------------------------------------------------------------------------------- |
| `num_shards`        | integer             | **Yes**  | Number of shards in the cluster                                                             |
| `zone_name`         | string              | No       | Zone name for the replication spec                                                          |
| `region_configs`    | array of [object](#region_configs) | **Yes**  | Region configurations for the replication spec                                              |

### region_configs

Region configurations for the replication spec

| Property               | Type                | Required | Description                                                                                 |
| ---------------------- | ------------------- | -------- | ------------------------------------------------------------------------------------------- |
| `backing_provider_name` | string              | No       | Name of the backing provider                                                                |
| `priority`             | integer             | No       | Priority of the region config (1-7)                                                         |
| `provider_name`        | string              | **Yes**  | Name of the provider                                                                        |
| `region_name`          | string              | **Yes**  | Name of the region                                                                          |
| `electable_specs`      | [object](#electable_specs) | No       | Electable specifications for the region                                                     |
| `read_only_specs`      | [object](#read_only_specs) | No       | Read-only specifications for the region                                                     |
| `analytics_specs`      | [object](#analytics_specs) | No       | Analytics specifications for the region                                                     |
| `auto_scaling`         | [object](#auto_scaling) | No       | Auto-scaling configurations for the region                                                  |
| `analytics_auto_scaling` | [object](#analytics_auto_scaling) | No       | Auto-scaling configurations for analytics in the region                                     |

### electable_specs

Electable specifications for the region

| Property        | Type    | Required | Description                   |
| --------------- | ------- | -------- | ----------------------------- |
| `instance_size` | string  | **Yes**  | Size of the instance          |
| `node_count`    | integer | **Yes**  | Number of electable nodes     |
| `disk_iops`     | integer | No       | Disk IOPS                     |
| `ebs_volume_type` | string | No       | Type of EBS volume            |

### read_only_specs

Read-only specifications for the region

| Property        | Type    | Required | Description                   |
| --------------- | ------- | -------- | ----------------------------- |
| `instance_size` | string  | **Yes**  | Size of the instance          |
| `node_count`    | integer | **Yes**  | Number of read-only nodes     |
| `disk_iops`     | integer | No       | Disk IOPS                     |
| `ebs_volume_type` | string | No       | Type of EBS volume            |

### analytics_specs

Analytics specifications for the region

| Property        | Type    | Required | Description                   |
| --------------- | ------- | -------- | ----------------------------- |
| `instance_size` | string  | **Yes**  | Size of the instance          |
| `node_count`    | integer | **Yes**  | Number of analytics nodes     |
| `disk_iops`     | integer | No       | Disk IOPS                     |
| `ebs_volume_type` | string | No       | Type of EBS volume            |

### auto_scaling

Auto-scaling configurations for the region

| Property                  | Type    | Required | Description                                     |
| ------------------------- | ------- | -------- | ----------------------------------------------- |
| `disk_gb_enabled`         | boolean | No       | Enable disk GB auto-scaling                     |
| `compute_enabled`         | boolean | No       | Enable compute auto-scaling                     |
| `compute_scale_down_enabled` | boolean | No       | Enable compute scale-down                       |
| `compute_min_instance_size` | string  | No       | Minimum instance size for compute auto-scaling  |
| `compute_max_instance_size` | string  | No       | Maximum instance size for compute auto-scaling  |

### analytics_auto_scaling

Auto-scaling configurations for analytics in the region

| Property                  | Type    | Required | Description                                     |
| ------------------------- | ------- | -------- | ----------------------------------------------- |
| `disk_gb_enabled`         | boolean | No       | Enable disk GB auto-scaling for analytics       |
| `compute_enabled`         | boolean | No       | Enable compute auto-scaling for analytics       |
| `compute_scale_down_enabled` | boolean | No       | Enable compute scale-down for analytics         |
| `compute_min_instance_size` | string  | No       | Minimum instance size for compute auto-scaling for analytics |
| `compute_max_instance_size` | string  | No       | Maximum instance size for compute auto-scaling for analytics |

## inputs

Inputs required for the resource

| Property                | Type                | Required | Description                                                                                 |
| ----------------------- | ------------------- | -------- | ------------------------------------------------------------------------------------------- |
| `atlas_account_details` | [object](#atlas_account_details) | **Yes**  | Details about the Atlas account                                                             |

### atlas_account_details

Details about the Atlas account

| Property        | Type    | Required | Description                     |
| --------------- | ------- | -------- | ------------------------------- |
| `resource_type` | string  | **Yes**  | Type of the resource            |
| `resource_name` | string  | **Yes**  | Name of the resource            |
| `output_name`   | string  | **Yes**  | Output name                     |

## advanced

Additional fields if any for a particular implementation of a resource

| Property     | Type                | Required | Description                                                                                 |
| ------------ | ------------------- | -------- | ------------------------------------------------------------------------------------------- |
| `atlas`      | [object](#atlas)    | No       | Atlas-specific advanced configurations                                                      |

### atlas

Atlas-specific advanced configurations

| Property                | Type                | Required | Description                                                                                 |
| ----------------------- | ------------------- | -------- | ------------------------------------------------------------------------------------------- |
| `atlas_mongo_cidr`         | string | No       | CIDR block for MongoDB Atlas                                                                 |
| `cluster_name`          | string              | No       | Name of the MongoDB cluster                                                                 |
| `cluster_type`          | string              | No       | Type of the MongoDB cluster, possible values are: `REPLICASET`, `SHARDED`                   |
| `backup_enabled`        | boolean             | No       | Enable backups for MongoDB                                                                  |
| `retain_backups_enabled` | boolean            | No       | Enable retaining backups for MongoDB                                                        |
| `bi_connector_config`   | [object](#bi_connector_config) | No       | BI connector configurations                                                                 |
| `advanced_configuration` | [object](#advanced_configuration) | No       | Advanced configurations for MongoDB                                                         |
| `encryption_at_rest_provider` | string        | No       | Encryption at rest provider                                                                 |
| `pit_enabled`           | boolean             | No       | Enable point-in-time recovery                                                               |
| `root_cert_type`        | string              | No       | Root certificate type                                                                       |
| `termination_protection_enabled` | boolean    | No       | Enable termination protection                                                               |
| `version_release_system` | string             | No       | Version release system                                                                      |
| `paused`                | boolean             | No       | Pause the MongoDB cluster                                                                   |
| `accept_data_risks_and_force_replica_set_reconfig` | boolean | No       | Accept data risks and force replica set reconfiguration                                     |
| `backup_schedule`       | [object](#backup_schedule) | No       | Backup schedule configurations                                                              |


### bi_connector_config

BI connector configurations

| Property              | Type    | Required | Description                     |
| --------------------- | ------- | -------- | ------------------------------- |
| `enabled`             | boolean | No       | Enable BI connector             |
| `read_preference`     | string  | No       | BI connector read preference, possible values are: `primary`, `secondary` |

### advanced_configuration

Advanced configurations for MongoDB

| Property                         | Type    | Required | Description                     |
| -------------------------------- | ------- | -------- | ------------------------------- |
| `default_read_concern`           | string  | No       | Default read concern            |
| `default_write_concern`          | string  | No       | Default write concern           |
| `fail_index_key_too_long`        | boolean | No       | Fail index key too long         |
| `javascript_enabled`             | boolean | No       | Enable JavaScript               |
| `minimum_enabled_tls_protocol`   | string  | No       | Minimum enabled TLS protocol    |
| `no_table_scan`                  | boolean | No       | Disable table scan              |
| `oplog_size_mb`                  | integer | No       | Oplog size in MB                |
| `oplog_min_retention_hours`      | integer | No       | Oplog minimum retention hours   |
| `sample_size_bi_connector`       | integer | No       | Sample size for BI connector    |
| `sample_refresh_interval_bi_connector` | integer | No | Sample refresh interval for BI connector |
| `transaction_lifetime_limit_seconds` | integer | No | Transaction lifetime limit in seconds |

### backup_schedule

Backup schedule configurations

| Property                             | Type    | Required | Description                     |
| ------------------------------------ | ------- | -------- | ------------------------------- |
| `enabled`                            | boolean | No       | Enable backup schedule          |
| `reference_hour_of_day`              | integer | No       | Reference hour of day for backups |
| `reference_minute_of_hour`           | integer | No       | Reference minute of hour for backups |
| `restore_window_days`                | integer | No       | Restore window in days          |
| `update_snapshots`                   | boolean | No       | Update snapshots                |
| `use_org_and_group_names_in_export_prefix` | boolean | No | Use org and group names in export prefix |
| `auto_export_enabled`                | boolean | No       | Enable auto-export              |
| `policy_item_hourly`                 | [object](#policy_item_hourly) | No | Hourly backup policy item       |
| `policy_item_daily`                  | [object](#policy_item_daily) | No | Daily backup policy item        |
| `policy_item_weekly`                 | [object](#policy_item_weekly) | No | Weekly backup policy item       |
| `policy_item_monthly`                | [object](#policy_item_monthly) | No | Monthly backup policy item      |
| `policy_item_yearly`                 | [object](#policy_item_yearly) | No | Yearly backup policy item       |

### policy_item_hourly

Hourly backup policy item

| Property            | Type    | Required | Description                     |
| ------------------- | ------- | -------- | ------------------------------- |
| `frequency_type`    | string  | No       | Frequency type for hourly backup policy |
| `frequency_interval` | integer | No      | Frequency interval for hourly backup policy |
| `retention_unit`    | string  | No       | Retention unit for hourly backup policy |
| `retention_value`   | integer | No       | Retention value for hourly backup policy |

### policy_item_daily

Daily backup policy item

| Property            | Type    | Required | Description                     |
| ------------------- | ------- | -------- | ------------------------------- |
| `frequency_type`    | string  | No       | Frequency type for daily backup policy |
| `frequency_interval` | integer | No      | Frequency interval for daily backup policy |
| `retention_unit`    | string  | No       | Retention unit for daily backup policy |
| `retention_value`   | integer | No       | Retention value for daily backup policy |

### policy_item_weekly

Weekly backup policy item

| Property            | Type    | Required | Description                     |
| ------------------- | ------- | -------- | ------------------------------- |
| `frequency_type`    | string  | No       | Frequency type for weekly backup policy |
| `frequency_interval` | integer | No      | Frequency interval for weekly backup policy |
| `retention_unit`    | string  | No       | Retention unit for weekly backup policy |
| `retention_value`   | integer | No       | Retention value for weekly backup policy |

### policy_item_monthly

Monthly backup policy item

| Property            | Type    | Required | Description                     |
| ------------------- | ------- | -------- | ------------------------------- |
| `frequency_type`    | string  | No       | Frequency type for monthly backup policy |
| `frequency_interval` | integer | No      | Frequency interval for monthly backup policy |
| `retention_unit`    | string  | No       | Retention unit for monthly backup policy |
| `retention_value`   | integer | No       | Retention value for monthly backup policy |

### policy_item_yearly

Yearly backup policy item

| Property            | Type    | Required | Description                     |
| ------------------- | ------- | -------- | ------------------------------- |
| `frequency_type`    | string  | No       | Frequency type for yearly backup policy |
| `frequency_interval` | integer | No      | Frequency interval for yearly backup policy |
| `retention_unit`    | string  | No       | Retention unit for yearly backup policy |
| `retention_value`   | integer | No       | Retention value for yearly backup policy |

## out

Output given by the resource for others to refer.

| Property     | Type               | Required | Description                                    |
| ------------ |--------------------| -------- |------------------------------------------------|
| `interfaces` | [object](#interfaces) | **Yes**  | MongoDB broker interface details of type cluster |

### interfaces

MongoDB broker details

| Property            | Type   | Required | Description                    |
| ------------------- | ------ | -------- | ------------------------------ |
| `connection_string` | string | No       | Connection string to connect   |
| `host`              | string | No       | Host for service discovery     |
| `name`              | string | No       | Name of interface, same as key |
| `password`          | string | No       | Password to connect (if any)   |
| `port`              | string | No       | Port for service discovery     |
| `username`          | string | No       | Username to connect (if any)   |

### cluster

MongoDB cluster details

| Property            | Type   | Required | Description                                                                                                                                |
| ------------------- | ------ | -------- | ------------------------------------------------------------------------------------------------------------------------------------------ |
| `endpoint`          | string | Yes      | A comma-separated string of endpoints in the format 'host1:port1,host2:port2'. The number of endpoints must be greater than or equal to 1. |
| `username`          | string | No       | The username to use for authentication when connecting to the datastore.                                                                   |
| `password`          | string | No       | The password to use for authentication when connecting to the datastore.                                                                   |
| `connection_string` | string | Yes      | The connection_string to use when connecting to the datastore.                                                                             |