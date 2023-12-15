---
title: "Blueprint"
slug: "blueprint"
excerpt: "The Single Source of Truth"
hidden: false
createdAt: "2022-12-05T11:41:16.852Z"
updatedAt: "2023-05-31T11:12:13.239Z"
---
## Introduction

A blueprint in Facets is a comprehensive description of the architectural design of a software product, inspired by the blueprints used in the construction industry. Just as a building blueprint provides a detailed plan of the structure, materials, and systems required to construct a building, a blueprint in Facets provides a detailed overview of all the components and resources required to run a software product, including microservices, databases, caches, queues, and configurations. The blueprint serves as the source of truth for the software architecture and provides a clear picture of the overall system design.

## Resource Types/Intents

In Facets, a blueprint is made up of resource types, also referred to as **intents**. Each resource type is meant to represent a specific component or resource within the software architecture, such as a database or cache, and has a [defined schema](https://github.com/Facets-cloud/facets-schemas#supported-services). The schema specifies the properties and attributes of each resource and is used to ensure that each instance of the resource follows the same structure and format.

## Resource

In the blueprint, each resource is defined using an Intent and follows the defined schema for that Intent. The resource types and their associated schemas provide a clear and concise overview of the resources required to run the software, helping to ensure that the system is consistently implemented across all environments. 

>  The blueprint is stored in a Git repository and consists of a collection of JSON files. Each JSON file depicts a resource or a configuration, providing a clear representation of the software architecture.

## Flavors

Each resource in a blueprint can have an implementation, known as a "flavor". The blueprint describes the default flavor for each resource, but the flavor can be changed based on the needs of the environment. This allows for a high degree of customization and flexibility while still maintaining the structure and design of the overall system.

## Fixed and Environment-Specific Architecture

Every software architecture has two components: fixed architecture and environment-specific architecture. The blueprint in Facets represents the fixed architecture and provides sensible defaults for each environment. This means that the blueprint can be used as a starting point for setting up the software in any environment, with the flexibility to make changes as needed to accommodate specific requirements. The blueprint serves as the source of truth for the software architecture and is an essential tool for both development and operations teams.

## FAQs

### What is a blueprint in Facets?

A blueprint in Facets is a comprehensive description of the architectural design of a software product, providing a clear picture of the overall system design.

### What are resource types/intents in Facets?

Resource types, also known as intents, represent specific components or resources within the software architecture such as databases, caches, queues, and configurations. Each resource type has a defined schema to ensure consistent implementation.

### What is a resource in the blueprint?

A resource in the blueprint is defined using an Intent and follows the schema for that Intent, providing a clear representation of the resources required to run the software.

### What is the blueprint stored in?

The blueprint is stored in a Git repository and consists of a collection of JSON files depicting a resource or configuration.

### What are flavors in a blueprint?

Each resource in a blueprint can have an implementation, known as a flavor. The blueprint describes the default flavor for each resource, but it can be changed based on the needs of the environment.

## Related guides

- [Create your first Blueprint](doc:create-your-first-blueprint)