---
title: "CI Integration"
slug: "artifacts"
excerpt: "Streamlining Service Builds"
hidden: false
createdAt: "2022-12-05T11:56:55.398Z"
updatedAt: "2023-06-16T06:01:56.221Z"
---
## Introduction

When designing a software system, it is important to ensure that the correct version of a service is deployed in each environment. Updating the service's Docker image for each environment can become cumbersome, which is why Facets has introduced the concept of "CI Integration".

A CI Integration is an entity in Facets that provides a central repository for storing and managing Docker images for a service. The **CI Integration** can be of two types: [environment ](https://readme.facets.cloud/v1.0/docs/environment#introduction)or [release stream](https://readme.facets.cloud/v1.0/docs/environment#release-stream). In other words, organizations can build images either for a specific environment or for a type of environment.

Once a **CI Integration** is created, it will have a separate bucket for each environment or release stream. The Docker images can be stored in these buckets through two mechanisms: by pushing the image directly via the Facets CLI or through a promotion process. The Facets CLI, known as [facetctl](https://readme.facets.cloud/v1.0/docs/command-line-tool-for-facets), can be integrated with popular CI systems like CodeBuild, Jenkins, CircleCI, or GitHub Actions.

### Promotion Workflows

Organizations often need to promote builds from one environment to another, such as from a QA environment to a Staging environment, and finally to a Production environment. To make this process seamless and controlled, Facets has introduced the concept of promotion workflows.

Promotion workflows in **CI Integration** can be of type environment or release stream and can be attached to similarly-typed **CI Integration**. This allows for clear and concise promotion paths for builds, making it easier for organizations to move their builds from one environment to another.

By using promotion workflows in **CI Integration**, organizations can ensure that builds are only promoted when they are ready and that the promotion process is well-defined and controlled. This helps to reduce the risk of errors and makes it easier for organizations to roll back if needed. Additionally, the promotion workflows in **CI Integration** include auditing information, such as who promoted which build, and when, providing a clear record of the promotion process for accountability and transparency purposes.

## FAQs

### What is CI Integration in Facets?

CI Integration is a resource in Facets that provides a central repository for storing and managing Docker images for a service. It can be of two types: environment or release stream, and provides a way for organizations to build images for a specific environment or for a type of environment.

### How are Docker images pushed to Environments?

Docker images can be pushed to Environments through two mechanisms: by pushing the image directly via the Facets CLI or through a promotion process. 

### What are promotion workflows in CI Integration?

Promotion workflows in CI Integration are used to promote builds from one environment to another, such as from a QA environment to a Staging environment and finally to a Production environment. These workflows help to ensure that builds are only promoted when they are ready and that the promotion process is well-defined and controlled.

### What are the benefits of using promotion workflows in CI Integration?

By using promotion workflows in CI Integration, organizations can reduce the risk of errors and make it easier to roll back if needed. Additionally, the promotion workflows in CI Integration include auditing information, such as who promoted which build and when, providing a clear record of the promotion process for accountability and transparency purposes.

## Related Guides

- [Creating a CI Integration](https://readme.facets.cloud/docs/creating-a-ci-integration)
- [Creating CI Rules](doc:ci-rules)