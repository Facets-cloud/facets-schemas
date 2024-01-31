## Introduction

A service for storing your objects in Google Cloud as objects.

## Properties

| Property     | Type                | Required | Description                                                                                                                                    |
|--------------|---------------------|----------|------------------------------------------------------------------------------------------------------------------------------------------------|
| `flavor`     | string              | **Yes**  | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc.                                     |
| `kind`       | string              | **Yes**  | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances                |
| `metadata`   | [object](#metadata) | **Yes**  | Metadata related to the resource                                                                                                               |
| `spec`       | [object](#spec)     | **Yes**  | Specification as per resource types schema                                                                                                     |
| `version`    | string              | **Yes**  | This field can be used to pin to a particular version                                                                                          |
| `advanced`   | [object](#advanced) | No       | Additional fields if any for a particular implementation of a resource                                                                         |
| `depends_on` |                     | No       | Dependencies on other resources. e.g. application x may depend on mysql                                                                        |
| `disabled`   | boolean             | No       | Flag to disable the resource                                                                                                                   |
| `lifecycle`  | string              | No       | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`) Possible values are: `ENVIRONMENT_BOOTSTRAP`. |
| `out`        | [object](#out)      | No       | The output for your configmap module, this can be generated or provided                                                                        |
| `provided`   | boolean             | No       | Flag to tell if the resource should not be provisioned by facets                                                                               |

## spec

Specification as per resource types schema


## advanced

Advanced configurations for GCS bucket

### Properties

| Property | Type            | Required | Description                                |
|----------|-----------------|----------|--------------------------------------------|
| `iam_members`   | [list(map)](#iam_members) | **No**  | The list of IAM members to grant permissions on the bucket. |
| `bucket_policy_only`   | boolean | **No**  | Enables Bucket Policy Only access to a bucket. |
| `Cors`   | [list(map)](#cors) | **No**  | Configuration of CORS for bucket with structure as defined in https://registry.terraform.io/providers/hashicorp/google/latest/docs/resources/storage_bucket#cors |
| `default_kms_key_name`   | string | **No**  | A Cloud KMS key that will be used to encrypt objects inserted into this bucket |
| `force_destroy`   | boolean | **No**  | When deleting a bucket, this boolean option will delete all contained objects. If false, Terraform will fail to delete buckets which contain objects. |
| `lifecycle_rules`   | [list(map)](#lifecycle_rules) | **No**  | The bucket's Lifecycle Rules configuration. |
| `log_bucket`   | string | **No**  | The bucket that will receive log objects. |
| `log_object_prefix`   | string | **No**  | The object prefix for log objects. If it's not provided, by default GCS sets this to this bucket's name |
| `public_access_prevention`   | string | **No**  | Prevents public access to a bucket. Acceptable values are inherited or enforced. If inherited, the bucket uses public access prevention, only if the bucket is subject to the public access prevention organization policy constraint. |
| `retention_policy`   | [object](#retention_policy) | **No**  | Configuration of the bucket's data retention policy for how long objects in the bucket should be retained. |
| `website`   | [object](#website) | **No**  | Map of website values. Supported attributes: main_page_suffix, not_found_pag |
| `versioning`   | boolean | **No**  | While set to true, versioning is fully enabled for this bucket. This should be set to false if retention_policy is enabled |

### iam_members

| Property | Type            | Required | Description                                |
|----------|-----------------|----------|--------------------------------------------|
| `role`   | string | **Yes**  | Role to be given for the member associated with the bucket |
| `member`   | string | **Yes**  | The member associated with the bucket |
### cors

The configurations within the cors 

| Name            | Description                                                          | Datatype | Required |
|-----------------|----------------------------------------------------------------------|----------|----------|
| `origin`   | The list of Origins eligible to receive CORS response headers. Note: "*" is permitted in the list of origins, and means "any Origin".                                          | list   | No      |
| `method`    | The list of HTTP methods on which to include CORS response headers, (GET, OPTIONS, POST, etc) Note: "*" is permitted in the list of methods, and means "any method".                              | list   | No      |
| `response_header`    | The list of HTTP headers other than the simple response headers to give permission for the user-agent to share across domains.  | list   | No      |
| `max_age_seconds`    | The value, in seconds, to return in the Access-Control-Max-Age header used in preflight responses.  | number   | No      |

### lifecycle_rules

The bucket's Lifecycle Rules configuration. Multiple blocks of this type are permitted. Structure is documented below.

| Name            | Description                                                          | Datatype | Required |
|-----------------|----------------------------------------------------------------------|----------|----------|
| `action`   |  The Lifecycle Rule's action configuration            | [object](#action)   | Yes      |
| `condition`    |  The Lifecycle Rule's condition configuration                             | [object](#condition)   | Yes      |

#### action

The Lifecycle Rule's action configuration.

| Name            | Description                                                          | Datatype | Required |
|-----------------|----------------------------------------------------------------------|----------|----------|
| `type`   |  The type of the action of this Lifecycle Rule. Supported values include: Delete, SetStorageClass and AbortIncompleteMultipartUpload           | string(#action)   | No      |
| `storage_class`    |  The target Storage Class of objects affected by this Lifecycle Rule. Supported values include: STANDARD, MULTI_REGIONAL, REGIONAL, NEARLINE, COLDLINE, ARCHIVE. configuration                             | string   | Yes (if action type is SetStorageClass)      |

#### condition

The Lifecycle Rule's condition configuration. The condition block supports the following elements, and requires at least one to be defined. If you specify multiple conditions in a rule, an object has to match all of the conditions for the action to be taken:

| Name            | Description                                                          | Datatype | Required |
|-----------------|----------------------------------------------------------------------|----------|----------|
| `age`   |  Minimum age of an object in days to satisfy this condition.  | number   | No      |
| `created_before`    |  A date in the RFC 3339 format YYYY-MM-DD. This condition is satisfied when an object is created before midnight of the specified date in UTC.   | string   | No      |
| `with_state`    |  Match to live and/or archived objects. Unversioned buckets have only live objects. Supported values include: "LIVE", "ARCHIVED", "ANY"   | string   | No      |
| `matches_storage_class`    |   Storage Class of objects to satisfy this condition. Supported values include: STANDARD, MULTI_REGIONAL, REGIONAL, NEARLINE, COLDLINE, ARCHIVE, DURABLE_REDUCED_AVAILABILITY   | string   | No      |
| `matches_prefix`    |  One or more matching name prefixes to satisfy this condition.   | string   | No      |
| `matches_suffix`    |  One or more matching name prefixes to satisfy this condition.   | string   | No      |
| `num_newer_versions`    |  One or more matching name prefixes to satisfy this condition.   | string   | No      |
| `custom_time_before`    |  One or more matching name prefixes to satisfy this condition.   | string   | No      |
| `days_since_custom_time`    |  One or more matching name prefixes to satisfy this condition.   | string   | No      |
| `days_since_noncurrent_time `    |  One or more matching name prefixes to satisfy this condition.   | string   | No      |
| `noncurrent_time_before`    |  Relevant only for versioned objects. The date in RFC 3339 (e.g. 2017-06-13) when the object became nonconcurrent.   | string   | No      |

### retention_period

The retention_policy can be set only if versioning is set to false and the block supports:

| Name            | Description                                                          | Datatype | Required |
|-----------------|----------------------------------------------------------------------|----------|----------|
| `is_locked`   | If set to true, the bucket will be locked and permanently restrict edits to the bucket's retention policy. Caution: Locking a bucket is an irreversible action.  | boolean   | No      |
| `retention_period`   | The period of time, in seconds, that objects in the bucket must be retained and cannot be deleted, overwritten, or archived. The value must be less than 2,147,483,647 seconds.  | number   | Yes      |


### website

The website block supports the following elements, and requires at least one to be defined:

| Name            | Description                                                          | Datatype | Required |
|-----------------|----------------------------------------------------------------------|----------|----------|
| `main_page_suffix`   |  Behaves as the bucket's directory index where missing objects are treated as potential directories.  | number   | No      |
| `not_found_page`   |  The custom object to return when a requested resource is not found.  | No      |


## Out
| Name            | Description                                                          | Datatype | Required |
|-----------------|----------------------------------------------------------------------|----------|----------|
| `name`   | Name of the GCS bucket                                         | string   | Yes      |
| `url`    | URL of the bucket                                                    | string   | Yes      |
