## Introduction

Apache zookeeper implementation

## Spec

| Property              | Type                                    | Required | Description                        |
|-----------------------|-----------------------------------------|----------|------------------------------------|
| `zookeeper_version`   | string                                  | **Yes**  | Version of redis e.g. 6.3          |
| `size`                | [object](../../traits/size.md)          | **Yes**  |                                    |

## Outputs

| Property     | Type                                                      | Required | Description |
|--------------|-----------------------------------------------------------|----------|-------------|
| `interfaces` | [object](../../traits/interface.schema.md)                | No       |             |


## Flavors

- k8s

## Alerts

| Alert Name                    | Impact                         | Mitigation                                           |
|-------------------------------|--------------------------------|------------------------------------------------------|
