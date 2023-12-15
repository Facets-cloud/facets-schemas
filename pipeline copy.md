---
title: "Delivery Pipeline"
slug: "pipeline"
excerpt: "Change Propagation Across Multiple Environments"
hidden: false
createdAt: "2022-12-16T08:53:15.187Z"
updatedAt: "2023-03-03T10:01:12.070Z"
---
## Introduction

When working with multiple environments, controlling the propagation of changes is crucial for ensuring that systems remain stable and secure. This is where Facets' delivery pipeline feature comes into play.

The delivery pipeline allows you to connect different environments in a pipeline, and define criteria for change propagation. These criteria can include a successful release in the parent environment or a manual sign-off from relevant stakeholders.

The delivery pipeline provides a clear and controlled process for promoting changes from one environment to the next, reducing the risk of errors. This helps organizations keep their systems stable and secure while allowing for a flexible and efficient deployment process.

By using delivery pipelines in Facets, organizations can better manage their environments and ensure that changes are properly validated and approved before they are deployed to production systems. This helps to maintain the stability and security of the organization's systems, while also providing a flexible and efficient deployment process.

>  The delivery pipeline includes all changes except for Docker artifacts, read [Promotion Workflows](./artifacts#promotion-workflows)

## FAQ

### What is a delivery pipeline in Facets?

A delivery pipeline in Facets is a feature that allows you to connect different environments and control the propagation of changes between them. The pipeline allows you to define criteria for change propagation, such as a successful release in the parent environment or manual sign-off from relevant stakeholders.

## Related Guides

- [Setting up the Delivery Pipeline](doc:setting-up-the-delivery-pipeline)