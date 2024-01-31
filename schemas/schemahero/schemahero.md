## Introduction
SchemaHero is a Kubernetes Operator for Declarative Schema Management for various databases.


## Spec


| Property           | Type                                                 | Required | Description                                              |
|--------------------|------------------------------------------------------|----------|----------------------------------------------------------|
| `size`             | [object](../../traits/kubernetes-sizing.schema.json) | **Yes**  | Sizing attribute for postgres writer and reader instance |


## Advanced
| Name                      | Description                                                                                  | Datatype | Required |
|---------------------------|----------------------------------------------------------------------------------------------|----------|----------|
| default.schemahero.values | Helm values as per the helm chart https://artifacthub.io/packages/helm/schemahero/schemahero | map      | no       |
