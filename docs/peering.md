# Peering

## Schema Properties

|                       | Type                                     | Description                                                                                                                                                                    | Required   |
|:----------------------|:-----------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                  | string                                   | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances                                                | Yes        |
| flavor                | string                                   | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc.                                                                     | Yes        |
| version               | string                                   | This field can be used to pin to a particular version                                                                                                                          | Yes        |
| lifecycle             | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                              | No         |
| disabled              | boolean                                  | Flag to disable the resource                                                                                                                                                   | No         |
| conditional_on_intent | string                                   | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| provided              | boolean                                  | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| depends_on            | array                                    | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| metadata.name         | string                                   | Name of the resource                                                                                                                                                           | No         |
|                       |                                          |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                  | object                                   | Specification as per resource types schema                                                                                                                                     | Yes        |
| advanced              |                                          |                                                                                                                                                                                | No         |
| spec.account_id       | string                                   | The account id of the cloud that you want to peer vpc with                                                                                                                     | No         |
| spec.vpc_id           | string                                   | The accepter vpc id of the account id                                                                                                                                          | No         |
| spec.cidr             | string                                   | The CIDR range of the vpc that you want to peered                                                                                                                              | No         |
| spec.region           | string                                   | The region where the vpc exists in your account                                                                                                                                | No         |

## Outputs

|                                  | Type   | Description                                       | Required   | Referencing                                               |
|:---------------------------------|:-------|:--------------------------------------------------|:-----------|:----------------------------------------------------------|
| out                              | object | Output given by the resource for others to refer. | No         | ${peering.RESOURCE_NAME.out}                              |
| out.interfaces.username          | string | Username to connect (if any)                      | No         | ${peering.RESOURCE_NAME.out.interfaces.username}          |
| out.interfaces.password          | string | Password to connect (if any)                      | No         | ${peering.RESOURCE_NAME.out.interfaces.password}          |
| out.interfaces.host              | string | Host for service discovery                        | No         | ${peering.RESOURCE_NAME.out.interfaces.host}              |
| out.interfaces.port              | string | Port for service discovery                        | No         | ${peering.RESOURCE_NAME.out.interfaces.port}              |
| out.interfaces.name              | string | Name of interface, same as key                    | No         | ${peering.RESOURCE_NAME.out.interfaces.name}              |
| out.interfaces.connection_string | string | Connection string to connect                      | No         | ${peering.RESOURCE_NAME.out.interfaces.connection_string} |

