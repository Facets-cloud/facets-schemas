# AWS DLM Lifecycle Policy Configuration Schema

This document describes the schema for configuring AWS DLM (Data Lifecycle Manager) lifecycle policies for automated snapshot and volume management.

## Schema Properties Overview

Below is a table outlining the properties defined in the schema, their types, whether they are required, and a brief description of each.

| Property                                             | Type     | Required | Description                                                                                           |
|------------------------------------------------------|----------|----------|-------------------------------------------------------------------------------------------------------|
| kind                                                 | const    | Yes      | Specifies the configuration object type for AWS DLM Lifecycle policies.                               |
| flavor                                               | const    | Yes      | Identifies the variant of the policy configuration, set as 'default' for the standard configuration. |
| version                                              | const    | Yes      | The version of the configuration schema, indicating the schema version or iteration.                 |
| spec                                                 | object   | Yes      | The specification section containing detailed configuration settings for the DLM lifecycle policy.   |
| spec.schedules                                       | object   | Yes      | Defines the schedules for snapshot management operations, allowing multiple named schedules.         |
| spec.schedules.[schedule_name]                       | object   | Yes      | A schedule configuration for managing the lifecycle of snapshots, identified by a unique name.       |
| spec.schedules.[schedule_name].copy_tags             | boolean  | Yes      | Indicates whether tags should be copied from the source volume to the snapshots.                     |
| spec.schedules.[schedule_name].create_rule           | object   | Yes      | Defines the rules for creating snapshots, including timing and frequency.                            |
| spec.schedules.[schedule_name].create_rule.interval           | number   | Conditional      | The interval at which snapshots are taken.                                                           |
| spec.schedules.[schedule_name].create_rule.interval_unit     | string   | Conditional      | The unit of measurement for the interval, such as hours or days.                                     |
| spec.schedules.[schedule_name].create_rule.times             | array    | Conditional      | Specific times of the day when snapshots should be created.                                          |
| spec.schedules.[schedule_name].create_rule.cron_expression   | string   | Conditional      | A CRON expression specifying the snapshot creation schedule.                                         |
| spec.schedules.[schedule_name].cross_region_copy_rules       | object   | Yes      | Defines rules for copying snapshots across AWS regions.                                              |
| spec.schedules.[schedule_name].cross_region_copy_rules.[rule_name] | object | Yes | A rule specifying how snapshots should be copied to another region.                                 |
| spec.schedules.[schedule_name].cross_region_copy_rules.[rule_name].copy_tags | boolean | Yes | Indicates whether tags should be copied along with the snapshot to the target region.               |
| spec.schedules.[schedule_name].cross_region_copy_rules.[rule_name].encrypted | boolean | Yes | Specifies whether the copied snapshot should be encrypted.                                           |
| spec.schedules.[schedule_name].cross_region_copy_rules.[rule_name].retain_rule | object | Yes | Defines how long the copied snapshots should be retained in the target region.                       |
| spec.schedules.[schedule_name].cross_region_copy_rules.[rule_name].retain_rule.interval | number | Yes | The retention interval for the copied snapshots.                                                     |
| spec.schedules.[schedule_name].cross_region_copy_rules.[rule_name].retain_rule.interval_unit | string | Yes | The unit of measurement for the retention interval, such as months.                                  |
| spec.schedules.[schedule_name].cross_region_copy_rules.[rule_name].target | string | Yes | The target AWS region where the snapshot will be copied.                                             |
| spec.schedules.[schedule_name].retain_rule                    | object   | Yes      | Specifies the retention rule for how long snapshots should be kept.                                  |
| spec.schedules.[schedule_name].retain_rule.count              | number   | Yes      | The number of snapshots to retain.                                                                   |
| spec.schedules.[schedule_name].tags_to_add                    | object   | Yes      | Defines tags to be added to the snapshots created by this policy.                                    |
| spec.target_tags                                      | object   | Yes      | Defines the tags used to identify which volumes should be managed by this lifecycle policy.          |

Each schedule is identified by a unique name (represented as `[schedule_name]`), and each cross-region copy rule within a schedule is also identified by a unique name (represented as `[rule_name]`).
