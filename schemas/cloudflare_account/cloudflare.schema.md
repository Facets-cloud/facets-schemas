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
| `api_token_secret_name`                | [string](#api_token_secret_name)                | **Yes**  | Name of the Secret in which the Cloudflare API Token is saved. |

### Flavors

* `default`
