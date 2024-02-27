## Properties

| Property   | Type                | Required | Description                                                                                                                                                         |
|------------|---------------------|----------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `flavor`   | string              | **Yes**  | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc. Possible values are: `default`.                          |
| `kind`     | string              | **Yes**  | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances Possible values are: `externaldns`. |
| `spec`     | [object](#spec)     | **Yes**  |                                                                                                                                                                     |
| `version`  | string              | **Yes**  | This field can be used to pin to a particular version Possible values are: `0.1`, `latest`.                                                                         |
| `advanced` | [object](#advanced) | No       | Advanced externaldns Schema                                                                                                                                         |

## advanced

Advanced externaldns Schema

### Properties

| Property      | Type                   | Required | Description                            |
|---------------|------------------------|----------|----------------------------------------|
| `externaldns` | [object](#externaldns) | No       | Advanced values for externaldns module |

### externaldns

Advanced values for externaldns module

#### Properties

| Property          | Type              | Required | Description                                                                                                                      |
|-------------------|-------------------|----------|----------------------------------------------------------------------------------------------------------------------------------|
| `cleanup_on_fail` | boolean           | No       | if the chart fails for some reason, do u want to clean up all the ones that are installed                                        |
| `namespace`       | string            | No       | The namespace where you want to install the dns chart                                                                            |
| `timeout`         | integer           | No       | The version of the chart that needs to be used                                                                                   |
| `values`          | [object](#values) | No       | The chart values that can be passed based on this `https://github.com/bitnami/charts/blob/main/bitnami/external-dns/values.yaml` |
| `version`         | string            | No       | The version of the chart that needs to be used                                                                                   |
| `wait`            | boolean           | No       | Do you want terraform to wait until the chart is fully installed                                                                 |

#### values

The chart values that can be passed based on this `https://github.com/bitnami/charts/blob/main/bitnami/external-dns/values.yaml`

| Property | Type | Required | Description |
|----------|------|----------|-------------|

## spec

### Properties

| Property       | Type                    | Required | Description                                                                                                                                                                                                                                                                                |
|----------------|-------------------------|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `domains`      | array                   | **Yes**  | The list of domains that you want to manage via externaldns                                                                                                                                                                                                                                |
| `provider`     | string                  | **Yes**  | DNS provider where the DNS records will be created Possible values are: `akamai`, `scaleway`, `ns1`, `ovh`, `pdns`, `civo`, `alibabacloud`, `aws`, `azure`, `cloudflare`, `designate`, `digitalocean`, `google`, `hetzner`, `infoblox`, `linode`, `rfc2136`, `transip`, `oci`, `exoscale`. |
| `akamai`       | [object](#akamai)       | No       | The akamai configurations that needs to be set                                                                                                                                                                                                                                             |
| `aws`          | [object](#aws)          | No       | The AWS configurations that needs to be set                                                                                                                                                                                                                                                |
| `azure`        | [object](#azure)        | No       | The AZURE configurations that needs to be set                                                                                                                                                                                                                                              |
| `civo`         | [object](#civo)         | No       | The civo configurations that needs to be set                                                                                                                                                                                                                                               |
| `cloudflare`   | [object](#cloudflare)   | No       | The cloudflare configurations that needs to be set                                                                                                                                                                                                                                         |
| `digitalocean` | [object](#digitalocean) | No       | The digitalocean configurations that needs to be set                                                                                                                                                                                                                                       |
| `exoscale`     | [object](#exoscale)     | No       | The exoscale configurations that needs to be set                                                                                                                                                                                                                                           |
| `google`       | [object](#google)       | No       | The GOOGLE configurations that needs to be set                                                                                                                                                                                                                                             |
| `hetzner`      | [object](#hetzner)      | No       | The hetzner configurations that needs to be set                                                                                                                                                                                                                                            |
| `linode`       | [object](#linode)       | No       | The linode configurations that needs to be set                                                                                                                                                                                                                                             |
| `ns1`          | [object](#ns1)          | No       | The linode configurations that needs to be set                                                                                                                                                                                                                                             |
| `scaleway`     | [object](#scaleway)     | No       | The scaleway configurations that needs to be set                                                                                                                                                                                                                                           |
| `transip`      | [object](#transip)      | No       | The transip configurations that needs to be set                                                                                                                                                                                                                                            |
| `vinyldns`     | [object](#vinyldns)     | No       | The vinyldns configurations that needs to be set                                                                                                                                                                                                                                           |

### akamai

The akamai configurations that needs to be set

#### Properties

| Property        | Type   | Required | Description                                                   |
|-----------------|--------|----------|---------------------------------------------------------------|
| `access_token`  | string | No       | Access Token to use for EdgeGrid auth                         |
| `client_secret` | string | No       | When using the Akamai provider, `AKAMAI_CLIENT_SECRET` to set |
| `client_token`  | string | No       | Client Token to use for EdgeGrid auth                         |
| `host`          | string | No       | Hostname to use for EdgeGrid auth                             |
| `secret_name`   | string | No       | Use an existing secret with key `akamai_api_seret` defined.   |

### aws

The AWS configurations that needs to be set

#### Properties

| Property                 | Type                   | Required | Description                                                                                              |
|--------------------------|------------------------|----------|----------------------------------------------------------------------------------------------------------|
| `api_retries`            | integer                | No       | Maximum number of retries for AWS API calls before giving up                                             |
| `assume_role_arn`        | string                 | No       | Assume role by specifying --aws-assume-role to the external-dns daemon                                   |
| `batch_change_size`      | integer                | No       | set the maximum number of changes that will be applied in each batch                                     |
| `credentials`            | [object](#credentials) | No       | The aws credentials block where you define for externaldns                                               |
| `dynamodb_region`        | string                 | No       | sets the DynamoDB table region to use for dynamodb registry                                              |
| `dynamodb_table`         | string                 | No       | sets the DynamoDB table name to use for dynamodb registry                                                |
| `evaluate_target_health` | boolean                | No       | sets the evaluate target health flag (options: true, false)                                              |
| `prefer_cname`           | boolean                | No       | replaces Alias records with CNAME (options: true, false)                                                 |
| `region`                 | string                 | No       | AWS_DEFAULT_REGION to set in the environment                                                             |
| `role_arn`               | string                 | No       | Specify role ARN to the external-dns daemon                                                              |
| `zone_tags`              | array                  | No       | Filter for zones with these tags                                                                         |
| `zone_type`              | string                 | No       | Filter for zones of this type Possible values are: `public`, `private`.                                  |
| `zones_cache_duration`   | integer                | No       | If the list of Route53 zones managed by ExternalDNS doesn't change frequently, cache it by setting a TTL |

#### credentials

The aws credentials block where you define for externaldns

##### Properties

| Property                       | Type                                    | Required | Description                                                                     |
|--------------------------------|-----------------------------------------|----------|---------------------------------------------------------------------------------|
| `access_key_id_secret_ref`     | [object](#access_key_id_secret_ref)     | No       | Reference to where access key id is stored in the secret                        |
| `mount_path`                   | string                                  | No       | When using the AWS provider, rootpath to check the aws creds                    |
| `secret_access_key_id`         | string                                  | No       | When using the AWS provider, set `aws_access_key_id` in the AWS credentials     |
| `secret_access_key_secret_ref` | [object](#secret_access_key_secret_ref) | No       | Reference to where access key id is stored in the secret                        |
| `secret_access_key`            | string                                  | No       | When using the AWS provider, set `aws_secret_access_key` in the AWS credentials |
| `secret_name`                  | string                                  | No       | Use an existing secret with key credentials defined.                            |

##### access_key_id_secret_ref

Reference to where access key id is stored in the secret

###### Properties

| Property | Type   | Required | Description                                                  |
|----------|--------|----------|--------------------------------------------------------------|
| `key`    | string | **Yes**  | Define the key of the secret that stores aws_access_key_id.  |
| `name`   | string | **Yes**  | Define the name of the secret that stores aws_access_key_id. |

##### secret_access_key_secret_ref

Reference to where access key id is stored in the secret

###### Properties

| Property | Type   | Required | Description                                                      |
|----------|--------|----------|------------------------------------------------------------------|
| `key`    | string | **Yes**  | Define the key of the secret that stores aws_secret_access_key.  |
| `name`   | string | **Yes**  | Define the name of the secret that stores aws_secret_access_key. |

### azure

The AZURE configurations that needs to be set

#### Properties

| Property                          | Type    | Required | Description                                                                                                          |
|-----------------------------------|---------|----------|----------------------------------------------------------------------------------------------------------------------|
| `aad_client_id`                   | string  | No       | set the Azure AAD Client ID                                                                                          |
| `aad_client_secret`               | string  | No       | set the Azure AAD Client Secret                                                                                      |
| `cloud`                           | string  | No       | set the Azure Cloud                                                                                                  |
| `resource_group`                  | string  | No       | set the Azure Resource Group                                                                                         |
| `secret_name`                     | string  | No       | Use an existing secret with key credentials defined, set the secret containing the `azure.json` file                 |
| `subscription_id`                 | string  | No       | set the Azure Subscription ID                                                                                        |
| `tenant_id`                       | string  | No       | set the Azure Tenant ID                                                                                              |
| `use_managed_identity_extension`  | boolean | No       | set if you use Azure MSI                                                                                             |
| `use_workload_identity_extension` | boolean | No       | set if you use Workload Identity extension.                                                                          |
| `user_assigned_identity_id`       | string  | No       | set Client ID of Azure user-assigned managed identity (optional, otherwise system-assigned managed identity is used) |

### civo

The civo configurations that needs to be set

#### Properties

| Property      | Type   | Required | Description                                         |
|---------------|--------|----------|-----------------------------------------------------|
| `api_token`   | string | No       | `CIVO_TOKEN` to set                                 |
| `secret_name` | string | No       | Use an existing secret with key 'apiToken' defined. |

### cloudflare

The cloudflare configurations that needs to be set

#### Properties

| Property      | Type    | Required | Description                                                                        |
|---------------|---------|----------|------------------------------------------------------------------------------------|
| `api_key`     | string  | No       | `CF_API_KEY` to set                                                                |
| `api_token`   | string  | No       | `CF_API_TOKEN` to set                                                              |
| `email`       | string  | No       | `CF_API_EMAIL` to set (optional). Needed when using CF_API_KEY                     |
| `proxied`     | boolean | No       | enable the proxy feature (DDOS protection, CDN...)                                 |
| `secret_name` | string  | No       | it's the name of the secret containing cloudflare_api_token or cloudflare_api_key. |

### digitalocean

The digitalocean configurations that needs to be set

#### Properties

| Property      | Type   | Required | Description                                                                              |
|---------------|--------|----------|------------------------------------------------------------------------------------------|
| `api_token`   | string | No       | `DO_TOKEN` to set                                                                        |
| `secret_name` | string | No       | Use an existing secret with key `digitalocean_api_token` defined. This ignores api_token |

### exoscale

The exoscale configurations that needs to be set

#### Properties

| Property      | Type   | Required | Description                                                                                                                              |
|---------------|--------|----------|------------------------------------------------------------------------------------------------------------------------------------------|
| `api_key`     | string | No       | `EXTERNAL_DNS_EXOSCALE_APISECRET` to set                                                                                                 |
| `api_token`   | string | No       | `EXTERNAL_DNS_EXOSCALE_APIKEY` to set                                                                                                    |
| `secret_name` | string | No       | Use an existing secret with keys `exoscale_api_key` and `exoscale_api_token` defined. This ignores exoscale.apiKey and exoscale.apiToken |

### google

The GOOGLE configurations that needs to be set

#### Properties

| Property                     | Type    | Required | Description                                                                                                       |
|------------------------------|---------|----------|-------------------------------------------------------------------------------------------------------------------|
| `project`                    | string  | **Yes**  | specify the Google project (required when provider=google)                                                        |
| `batch_change_size`          | integer | No       | set the maximum number of changes that will be applied in each batch                                              |
| `service_account_key`        | string  | No       | specify the service account key JSON file. In this case a new secret will be created holding this service account |
| `service_account_secret_key` | string  | No       | When using the Google provider with an existing secret, specify the key name                                      |
| `service_account_secret`     | string  | No       | specify the existing secret which contains credentials.json                                                       |
| `zone_visibility`            | string  | No       | fiter for zones of a specific visibility (private or public) Possible values are: `public`, `private`.            |

### hetzner

The hetzner configurations that needs to be set

#### Properties

| Property      | Type   | Required | Description                                                                                                                                                                                  |
|---------------|--------|----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `secret_key`  | string | No       | When using the Hetzner provider with an existing secret, specify the key name                                                                                                                |
| `secret_name` | string | No       | When using the Hetzner provider, specify the existing secret which contains your token. Disables the usage of `hetzner.token`                                                                |
| `token`       | string | No       | `specify your token here. (required when `hetzner.secret_name` is not provided. In this case a new secret will be created holding the token.) Mutually exclusive with `hetzner.secret_name`. |

### linode

The linode configurations that needs to be set

#### Properties

| Property      | Type   | Required | Description                                                                               |
|---------------|--------|----------|-------------------------------------------------------------------------------------------|
| `api_token`   | string | No       | `LINODE_TOKEN` to set                                                                     |
| `secret_name` | string | No       | Use an existing secret with key `linode_api_token` defined. This ignores linode.api_token |

### ns1

The linode configurations that needs to be set

#### Properties

| Property      | Type   | Required | Description                                                                       |
|---------------|--------|----------|-----------------------------------------------------------------------------------|
| `api_key`     | string | No       | specify the API key to use                                                        |
| `min_ttl`     | string | No       | specify minimal TTL, as an integer, for records                                   |
| `secret_name` | string | No       | Use an existing secret with key `ns1-api-key` defined. This ignores ns1.api_token |

### scaleway

The scaleway configurations that needs to be set

#### Properties

| Property         | Type   | Required | Description                                                       |
|------------------|--------|----------|-------------------------------------------------------------------|
| `scw_access_key` | string | **Yes**  | specify an existing access key. (required when provider=scaleway) |
| `scw_secret_key` | string | **Yes**  | specify an existing secret key. (required when provider=scaleway) |

### transip

The transip configurations that needs to be set

#### Properties

| Property  | Type   | Required | Description                 |
|-----------|--------|----------|-----------------------------|
| `account` | string | **Yes**  | specify the account name.   |
| `api_key` | string | **Yes**  | specify the API key to use. |

### vinyldns

The vinyldns configurations that needs to be set

#### Properties

| Property     | Type   | Required | Description                    |
|--------------|--------|----------|--------------------------------|
| `access_key` | string | **Yes**  | specify the Access Key to use. |
| `host`       | string | **Yes**  | specify the VinylDNS API host. |
| `secret_key` | string | **Yes**  | specify the Secret key to use. |

