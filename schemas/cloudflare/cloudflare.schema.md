## Introduction

A Cloudflare intent which can be used to create Cloudflare Ruleset resource in Cloudflare Account

## Properties

| Property   | Type                | Required | Description                           |
|------------|---------------------|----------|---------------------------------------|
| `flavor`   | string              | **Yes**  | Possible values are: `default`.       |
| `kind`     | string              | **Yes**  | Possible values are: `cloudflare`.    |
| `spec`     | [object](#spec)     | **Yes**  |                                       |
| `version`  | string              | **Yes**  | Possible values are: `0.1`, `latest`. |
| `advanced` | [object](#advanced) | No       |                                       |

## spec

### Properties

| Property                 | Type                              | Required | Description                                                                                                                                                                                                                                                               |
|--------------------------|-----------------------------------|----------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `domain`                | [object](#domain)                | **Yes**  | Domain/Website managed by this cloudflare. Follow this url <https://developers.cloudflare.com/fundamentals/setup/manage-domains/add-site/> to understand the requirements for adding a website to cloudflare. |

### domain

 Domain/Website managed by this cloudflare. Follow this url <https://developers.cloudflare.com/fundamentals/setup/manage-domains/add-site/> to understand the requirements for adding a website to cloudflare.

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `<domain>` | string | **Yes** | Domain/Website managed by this cloudflare |

## advanced

### Properties

| Property     | Type                  | Required | Description                                                                                                                                                                                      |
|--------------|-----------------------|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `cloudflare` | [object](#cloudflare) | No       | The advanced section of Cloudflare module. |

### cloudflare

The advanced section of Cloudflare module.

#### Properties

| Property                  | Type                               | Required | Description                                                                                                                                                                            |
|---------------------------|------------------------------------|----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `managed_rules`  | [array](#managed_rules)  | No       | List of managed rulesets to be added.                                                                                                                                            |
| `ruleset` | [object](#ruleset) | No       | One or more Rulesets to be added to cloudflare. The Cloudflare Ruleset Engine allows you to create and deploy rules and rulesets. Please check this url for supported attributes - <https://registry.terraform.io/providers/cloudflare/cloudflare/latest/docs/resources/ruleset#schema>. |

### Flavors

* `default`
