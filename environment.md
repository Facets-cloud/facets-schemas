---
title: "Environment"
slug: "environment"
excerpt: "The Manifestation"
hidden: false
createdAt: "2022-12-05T11:42:20.206Z"
updatedAt: "2023-02-15T04:55:19.207Z"
---
## Introduction

A [blueprint](./blueprint) in Facets provides a comprehensive description of the architectural design of a software product. The blueprint serves as a source of truth for the software architecture, but it's just the definition. To run the software in a specific cloud environment, a manifestation of the blueprint must be created in Facets. This manifestation is called an environment.

### Creating an Environment

An environment can be created in Facets by linking a cloud account for AWS, GCP, or Azure. Once the cloud account is linked, the Facets runtime translates the blueprint into the respective cloud environment. The environment can be customized to accommodate specific requirements, such as changing the flavor of a resource type or adding additional resources.

## Release Stream

Environments can be classified into different types, such as QA, Dev, Staging, and Production. This classification helps in collectively managing the behavior and configurations of environments. This classification is known as the Release Stream in Facets.

## FAQs

### How do I create an environment in Facets?

To create an environment in Facets, link a cloud account for AWS, GCP, or Azure. The Facets runtime will then translate the blueprint into the respective cloud environment.

### Can I customize an environment in Facets?

Yes, an environment in Facets can be customized to accommodate specific requirements, such as changing the flavor of a resource type or adding additional resources.

### What is the Release Stream in Facets?

The Release Stream in Facets is a classification of environments into different types, such as QA, Dev, Staging, and Production, to collectively manage the behavior and configurations of environments.

## Related Guides

- [Create an Environment](doc:create-an-environment)