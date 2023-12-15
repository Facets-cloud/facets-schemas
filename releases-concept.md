---
title: "Releases"
slug: "releases-concept"
excerpt: "Managing Changes in Cloud Environments"
hidden: false
createdAt: "2022-12-16T08:53:29.777Z"
updatedAt: "2023-02-15T08:43:21.569Z"
---
## Introduction

Facets offers a streamlined way of deploying changes to cloud environments through the use of **releases**. Releases are executed using Terraform apply commands, which perform a three-way comparison of state to determine and deploy the necessary changes to the corresponding cloud environment. This comparison involves checking the current state of the environment, the state defined in generated Terraform configuration files from blueprints, and the desired end state. And only deploying the changes necessary to reach the desired state.

The following are the various types of releases available in Facets:

1. **Full Release:** The default release type, is used for synchronizing all changes in an environment.
2. **Selective Release:** This release type allows for selective deployment of services or changes, commonly used for out-of-turn hotfixes in production environments.
3. **Launch Release:** Used for launching a new environment for the first time.
4. **Destroy Release:** Used to destroy all resources provisioned by Facets.
5. **Plan Release:** Provides a preview of all changes that will be made during the release, allowing for review and approval before applying changes.
6. **Custom Release: **This release type allows power users to issue direct Terraform commands, giving them greater control and flexibility in their deployments.

In addition, Terraform logs are available for power users to view detailed information about the changes made during each release. By using releases in Facets, organizations can have a well-defined and automated process for deploying changes to their cloud environments, which helps reduce the risk of errors and makes it easier to roll back if necessary.

## FAQs

### What is a release in Facets?

Releases in Facets are a streamlined way of deploying changes to cloud environments. They are executed using Terraform apply commands and perform a three-way comparison of state to determine and deploy the necessary changes to the corresponding cloud environment.

### What is a Full Release in Facets?

A Full Release in Facets is the default release type and is used for synchronizing all changes in an environment.

### What is a Selective Release in Facets?

A Selective Release in Facets allows for selective deployment of services or changes, commonly used for out-of-turn hotfixes in production environments.

## Related Guides

- [Scheduling Releases](doc:scheduling-releases)
- [Performing Releases](performing-releases)