---
title: "Facets CLI"
slug: "command-line-tool-for-facets"
excerpt: "Learn how to use facetsctl Command Line Tool"
hidden: false
createdAt: "2022-08-12T05:29:12.851Z"
updatedAt: "2023-07-11T08:37:55.994Z"
---
This documentation provides a quick reference guide for using the facetsctl CLI tool. It's an easy-to-use alternative to Facets APIs that allows users to push new docker artifacts to the Facets Control Plane and refresh Kubernetes credentials for the user in a specified environment.

## NPM Page

You can view and download the latest `facetsctl` package from its [NPM page](https://www.npmjs.com/package/@facets-cloud/facetsctl/v/1.0.1).

## Commands

`facetsctl` provides the following commands for users.

### `facetsctl help`

This command displays the help for facetsctl. You can use it to get information about a specific command by providing the command name as an argument.

**ARGUMENTS**  
COMMAND: Command to show help for.

**FLAGS**

| Flag                    | Required | What does it do?                                                 |
| :---------------------- | :------- | :--------------------------------------------------------------- |
| `-n, --nested-commands` | No       | Defines if all nested commands should be included in the output. |

***

### `facetsctl login`

This command allows the user to log in to the Facets Control Plane.

```shell
$ facetsctl login -u <value> -t <value> -c <value>
```

**FLAGS**

| Flag                             | Required | What does it do?                                  |
| :------------------------------- | :------- | :------------------------------------------------ |
| `-c, --cp-url` = `<value>`       | Yes      | Defines the URL of the Control plane              |
| `-t, --access-token` = `<value>` | Yes      | Defines the personal token generated for the user |
| ` -u, --username` = `<value>`    | Yes      | Defines the username of the user                  |

***

### `facetsctl push`

The following command enables users to push new docker artifacts to specific release streams or environments in the Facets Control Plane:

```shell
$ facetsctl push -i <value> -a <value> --registration-type CLUSTER|RELEASE_STREAM --registration-value  
    <value> -e <value> [-d <value>] [--artifactory <value>]
```

The following command can be used to push an image to Facets and leverage the benefits of CI Rules for automatic classification:

```Text Shell
$ facetsctl push --ci <ci_integration_name> --image <docker_image_name> --git-ref
  <git reference> -e <external_id_here> -d <your_build_description> --artifactory default
```

**FLAGS**

[block:parameters]
{
  "data": {
    "h-0": "Flag",
    "h-1": "Required",
    "h-2": "What does it do?",
    "0-0": " `-a, --artifact-name` = `<value>`",
    "0-1": "Yes",
    "0-2": "Defines the name of the artifact as mentioned in the Blueprint.",
    "1-0": " `-d, --description` = `<value>`",
    "1-1": "No",
    "1-2": "Defines the description of the build, if any.",
    "2-0": "`-e, --external-id` = `<value>`",
    "2-1": "Yes",
    "2-2": "Defines the external identifier for the artifact.",
    "3-0": "`-i, --docker-image` = `<value>`",
    "3-1": "Yes",
    "3-2": "Defines the image location.  \nUsed as \\<IMAGE:TAG>.",
    "4-0": " `--artifactory `= `<value>`",
    "4-1": "No",
    "4-2": "Defines the artifactory where the image will be pushed.",
    "5-0": "`--registration-type` = `<option>`",
    "5-1": "Yes",
    "5-2": "Defines the mode of registration.  \nUsed as \\<options: CLUSTER|RELEASE_STREAM>.",
    "6-0": "`--registration-value` = `<value>`",
    "6-1": "Yes",
    "6-2": "Defines the value for release stream or cluster id, based on mode of registration."
  },
  "cols": 3,
  "rows": 7,
  "align": [
    "left",
    "left",
    "left"
  ]
}
[/block]

### `facetsctl refresh`

This command will refresh the Kubernetes credentials for the user in the specified environment. 

```shell
facetsctl refresh --cluster-id <value> 
#or 
facetsctl refresh --blueprint-name <value> --cluster-name <value>
```

**FLAGS**

| Flag               | Required? | What does it do?                                              |
| :----------------- | :-------- | :------------------------------------------------------------ |
| `--cluster-id`     | Yes       | Defines the unique id for the environment.                    |
| `--blueprint-name` | Yes       | Defines the unique name of the defined blueprint.             |
| `--cluster-name`   | Yes       | Defines the name of the environment in the defined blueprint. |

### `facetsctl download-kubeconfig`

This command can be used to download kubeconfig files for an Environment. You can either download it using the environment ID or retrieve it by specifying the blueprint name and environment name.

```shell
$ facetsctl download-kubeconfig -e <value>
#or
$ facetsctl download-kubeconfig --environment-name <value>  --blueprint-name ‹value>
```

**FLAGS**

| Flag                   | Required | What does it do?                                                 |
| :--------------------- | :------- | :--------------------------------------------------------------- |
| `-e, --environment-id` | Yes      | Defines the Environment id for downloading kubeconfig.           |
| `--environment-name`   | Yes      | Defines the Environment name for downloading kubeconfig.         |
| `--blueprint-name`     | Yes      | Defines the Blueprint name using which environment was launched. |

### `facetsctl register`

Register docker images from an external repository into the Facets Control Plane.

```Text shell
$ facetsctl register -i <value> -s <value> -e <value> --git-ref <value> [-d <value>]
[--registry <value>] [--registration-type CLUSTER|RELEASE_STREAM] 
[--blueprint-name <value>] [--debug <value>]
```

### FLAGS

[block:parameters]
{
  "data": {
    "h-0": "Flag",
    "h-1": "Required",
    "h-2": "What does it do?",
    "0-0": "-d,  --description=\\<value>",
    "0-1": "No",
    "0-2": "Defines the description of the build if any.",
    "1-0": "-e,  --external-id=\\<value>",
    "1-1": "Yes",
    "1-2": "Defines the external identifier for the artifact.",
    "2-0": "-i, --docker-image=\\<value>",
    "2-1": "Yes",
    "2-2": "Defines the docker image URL in the external registry.",
    "3-0": "-s,  --service=\\<value>",
    "3-1": "Yes",
    "3-2": "Defines the name of the CI Integration for which the artifact needs to be registered.",
    "4-0": "--blueprint-name=\\<value>",
    "4-1": "No",
    "4-2": "Defines the Blueprint name in which the CI Integration will be created if not present already.",
    "5-0": "--git-ref=\\<value>",
    "5-1": "Yes",
    "5-2": "Defines the git ref provided in CI Rule.",
    "6-0": "--registration-type=\\<option>",
    "6-1": "No",
    "6-2": "Defines the mode of registration for CI integration.  \nUsed as \\<options: ENVIRONMENT | RELEASE_STREAM>",
    "7-0": "--registry=\\<value>",
    "7-1": "No",
    "7-2": "Defines the artifactory where the image will be pushed.",
    "8-0": "--debug=\\<value>",
    "8-1": "No",
    "8-2": "Defines if you want to view the original error message thrown.  \n[default: false]"
  },
  "cols": 3,
  "rows": 9,
  "align": [
    "left",
    "left",
    "left"
  ]
}
[/block]

## Facets CLI version v1.0.8

#### For Mac Os

| Type | Architecture | Download link                                                                                                                                                                                     |
| :--- | :----------- | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Gzip | 64-bit       | [facetsctl-darwin-x64.tar.gz](https://facets-cf-templates.s3.amazonaws.com/oclif-tarballs/production/versions/1.0.8-5508370538/30c7d95/facetsctl-v1.0.8-5508370538-30c7d95-darwin-x64.tar.gz)     |
| XZ   | 64-bit       | [facetsctl-darwin-x64.tar.xz](https://facets-cf-templates.s3.amazonaws.com/oclif-tarballs/production/versions/1.0.8-5508370538/30c7d95/facetsctl-v1.0.8-5508370538-30c7d95-darwin-x64.tar.xz)     |
| Gzip | ARM64        | [facetscti-darwin-arm64.tar.gz](https://facets-cf-templates.s3.amazonaws.com/oclif-tarballs/production/versions/1.0.8-5508370538/30c7d95/facetsctl-v1.0.8-5508370538-30c7d95-darwin-arm64.tar.gz) |
| XZ   | ARM64        | [facetsctI-darwin-arm64.tar.xz](https://facets-cf-templates.s3.amazonaws.com/oclif-tarballs/production/versions/1.0.8-5508370538/30c7d95/facetsctl-v1.0.8-5508370538-30c7d95-darwin-arm64.tar.xz) |

#### For Linux

| Type | Architecture | Download link                                                                                                                                                                               |
| :--- | :----------- | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Gzip | 64-bit       | [facetsctl-linux-x64.tar.gz](https://facets-cf-templates.s3.amazonaws.com/oclif-tarballs/production/versions/1.0.8-5508370538/30c7d95/facetsctl-v1.0.8-5508370538-30c7d95-linux-x64.tar.gz) |
| XZ   | 64-bit       | [facetsctI-linux-×64.tar.xz](https://facets-cf-templates.s3.amazonaws.com/oclif-tarballs/production/versions/1.0.8-5508370538/30c7d95/facetsctl-v1.0.8-5508370538-30c7d95-linux-x64.tar.xz) |
| Gzip | ARM          | [facetsctl-linux-arm.tar.gz](https://facets-cf-templates.s3.amazonaws.com/oclif-tarballs/production/versions/1.0.8-5508370538/30c7d95/facetsctl-v1.0.8-5508370538-30c7d95-linux-arm.tar.gz) |
| XZ   | ARM          | [facetscti-linux-arm.tar.xz](https://facets-cf-templates.s3.amazonaws.com/oclif-tarballs/production/versions/1.0.8-5508370538/30c7d95/facetsctl-v1.0.8-5508370538-30c7d95-linux-arm.tar.xz) |

#### For Windows

| Type | Architecture | Download link                                                                                                                                                                               |
| :--- | :----------- | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Gzip | 64-bit       | [facetsctl-win32-x64.tar.gz](https://facets-cf-templates.s3.amazonaws.com/oclif-tarballs/production/versions/1.0.8-5508370538/30c7d95/facetsctl-v1.0.8-5508370538-30c7d95-win32-x64.tar.gz) |
| XZ   | 64-bit       | [facetsctl-win32-x64.tar.xz](https://facets-cf-templates.s3.amazonaws.com/oclif-tarballs/production/versions/1.0.8-5508370538/30c7d95/facetsctl-v1.0.8-5508370538-30c7d95-win32-x64.tar.xz) |
| Gzip | 32-bit       | [facetsctI-win32-x86.tar.gz](https://facets-cf-templates.s3.amazonaws.com/oclif-tarballs/production/versions/1.0.8-5508370538/30c7d95/facetsctl-v1.0.8-5508370538-30c7d95-win32-x86.tar.gz) |
| XZ   | 32-bit       | [facetsctI-win32-x86.tar.xz](https://facets-cf-templates.s3.amazonaws.com/oclif-tarballs/production/versions/1.0.8-5508370538/30c7d95/facetsctl-v1.0.8-5508370538-30c7d95-win32-x86.tar.xz) |