# Introduction

This document describes the schema for configuring AWS DocumetDB (with MongoDB compatibility).

## Properties

| Property     | Type            | Required | Description                                                                                                                                    |
| ------------ | --------------- | -------- | ---------------------------------------------------------------------------------------------------------------------------------------------- |
| `flavor`     | string          | **Yes**  | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc.                                     |
| `kind`       | string          | **Yes**  | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances                |
| `metadata`   | object          | **Yes**  | Metadata related to the resource                                                                                                               |
| `spec`       | [object](#spec) | **Yes**  | Specification as per resource types schema                                                                                                     |
| `version`    | string          | **Yes**  | This field can be used to pin to a particular version                                                                                          |
| `advanced`   | object          | No       | Additional fields if any for a particular implementation of a resource                                                                         |
| `depends_on` |                 | No       | Dependencies on other resources. e.g. application x may depend on mysql                                                                        |
| `disabled`   | boolean         | No       | Flag to disable the resource                                                                                          
| `out`        | [object](#out)  | No       | Output given by the resource for others to refer.                                                                                              |
| `provided`   | boolean         | No       | Flag to tell if the resource should not be provisioned by facets   |
                                        

## Spec

Specification as per resource types schema

### Properties

| Property           | Type   | Required | Description                                                                                                                                                                                                                              |
| ------------------ | ------ | -------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `size` | [object](#size) | **Yes**  | The cluster instance configurations.        |

## size

### Properties

| Property           | Type   | Required | Description                                                                                                                                                                                                                              |
| ------------------ | ------ | -------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `instance_count` | integer | **Yes**  | The number of instance to be created. Defaults to 0       |
| `instance` | string | **Yes**  | The instance class of instances to be created.       |

## Advanced

### Properties

| Property           | Type   | Required | Description                                                                                                                                                                                                                              |
| ------------------ | ------ | -------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `additional_tag_map` | object     |   **No**         |    Additional key-value pairs to add to each map in tags_as_list_of_maps                 |
| `allow_ingress_from_self` | boolean     |   **No**         |  Adds the Document DB security group itself as a source for ingress rules                   |
| `allowed_cidr_blocks` | array     |   **No**         |  List of CIDR blocks to be allowed to connect to the DocumentDB cluster                   |
| `allowed_egress_cidr_blocks` |  array    |   **No**         |   List of CIDR blocks to be allowed to send traffic outside of the DocumentDB cluster                  |
| `allowed_security_groups` | array     |   **No**         |  List of existing Security Groups to be allowed to connect to the DocumentDB cluster                   |
| `apply_immediately` | boolean     |   **No**         |   Specifies whether any cluster modifications are applied immediately, or during the next maintenance window                 |
| `attributes` | array    |   **No**         |  ID element. Additional attributes (e.g. workers or cluster) to add to id                    |
| `auto_minor_version_upgrade` | boolean     |   **No**         |   Specifies whether any minor engine upgrades will be applied automatically to the DB instance during the maintenance window or not                  |
| `ca_cert_identifier` | string    |   **No**         |   The identifier of the CA certificate for the DB instance                  |
| `cluster_dns_name` |  string    |   **No**         |   Name of the cluster CNAME record to create in the parent DNS zone specified by zone_id                  |
| `cluster_parameters` |  array    |   **No**         |   List of DB parameters to apply                  |
| `context` |  object    |   **No**         |   Single object for setting entire context at once                  |
| `db_port` | integer     |   **No**         |  DocumentDB port                   |
| `deletion_protection` | boolean     |   **No**         |  A value that indicates whether the DB cluster has deletion protection enabled                   |
| `delimiter` | string    |   **No**         | Delimiter to be used between ID elements.
Defaults to - (hyphen)                    |
| `descriptor_formats` |  object    |   **No**         |  Describe additional descriptors to be output in the descriptors output map.                   |
| `egress_from_port` | integer     |   **No**         |  [from_port]DocumentDB initial port range for egress (e.g. 0)                   |
| `egress_protocol` | string     |   **No**         |  DocumentDB protocol for egress (e.g. -1, tcp)                    |
| `egress_to_port` |  integer    |   **No**         |   [to_port]DocumentDB initial port range for egress (e.g. 65535)                  |
| `enable_performance_insights` | boolean     |   **No**         |  Specifies whether to enable Performance Insights for the DB Instance.                   |
| `enabled` | boolean     |   **No**         |  Set to false to prevent the module from creating any resources                   |
| `enabled_cloudwatch_logs_exports` | array     |   **No**         |  List of log types to export to cloudwatch. The following log types are supported: audit, error, general, slowquery                   |
| `environment` | string     |   **No**         |  ID element. Usually used for region e.g. 'uw2', 'us-west-2', OR role 'prod', 'staging', 'dev', 'UAT'                   |
| `external_security_group_id_list` | array     |   **No**         |   List of external security group IDs to attach to the Document DB                  |
| `id_length_limit` | integer     |   **No**         |  Limit id to this many characters (minimum 6)                   |
| `kms_key_id` | string     |   **No**         |  The ARN for the KMS encryption key                   |
| `label_key_case` | string     |   **No**         |  Controls the letter case of the tags keys (label names) for tags generated by this module                   |
| `label_order` | array     |   **No**         |  The order in which the labels (ID elements) appear in the id.                   |
| `label_value_case` | string     |   **No**         |   Controls the letter case of ID elements (labels) as included in id,
set as tag values, and output by this module individually.                  |
| `labels_as_tags` | array     |   **No**         |  Set of labels (ID elements) to include as tags in the tags output.
Default is to include all labels.                   |
| `master_password` | string     |   **No**         |  Password for the master DB user.                   |
| `master_username` | string     |   **No**         |  Username for the master DB user.                   |
| `name` | string     |   **No**         |  ID element                   |
| `namespace` | string     |   **No**         |  ID element. Usually an abbreviation of your organization name                   |
| `preferred_backup_window` | string     |   **No**         |  Daily time range during which the backups happen                   |
| `preferred_maintenance_window` | string     |   **No**         |  The window to perform maintenance in                   |
| `reader_dns_name` | string     |   **No**         |  Name of the reader endpoint CNAME record to create in the parent DNS zone specified by zone_id                   |
| `regex_replace_chars` | string     |   **No**         |  Terraform regular expression (regex) string                   |
| `retention_period` | number     |   **No**         |  Number of days to retain backups for.                   |
| `skip_final_snapshot` |  boolean    |   **No**         |  Determines whether a final DB snapshot is created before the DB cluster is deleted                   |
| `snapshot_identifier` | string     |   **No**         |  Specifies whether or not to create this cluster from a snapshot                   |
| `ssm_parameter_enabled` | boolean     |   **No**         |   Whether an SSM parameter store value is created to store the database password.                  |
| `ssm_parameter_path_prefix` | string     |   **No**         |   The path prefix for the created SSM parameter e.g. '/docdb/master-password/dev'.                  |
| `stage` | string     |   **No**         |  ID element. Usually used to indicate role, e.g. 'prod', 'staging', 'source', 'build', 'test', 'deploy', 'release'                   |
| `storage_encrypted` | boolean     |   **No**         |  Specifies whether the DB cluster is encrypted                   |
| `storage_type` | string     |   **No**         |  The storage type to associate with the DB cluster. Valid values: standard, iopt1                   |
| `tenant` | string     |   **No**         |  ID element _(Rarely used, not included by default)_. A customer identifier, indicating who this instance of a resource is for                   |
| `zone_id` | string     |   **No**         |   Route53 parent zone ID. If provided (not empty), the module will create sub-domain DNS records for the DocumentDB master and replicas                  |