---
title: "Configuration Management for Services"
slug: "configuration-management-for-services"
excerpt: "How to do configuration management for services?"
hidden: false
createdAt: "2023-02-14T06:03:26.999Z"
updatedAt: "2023-08-07T08:40:16.331Z"
---
## Introduction

In software development, managing configurations and secrets is a critical aspect that requires great care. Configurations refer to the parameters that determine the behavior of an application, while secrets refer to sensitive information that should not be publicly exposed, such as passwords and API keys.

The two main types of configurations in a product are **global configurations and local configurations.** Global configurations are shared across applications and include environment types, external service endpoints, and keys for tools such as New Relic. On the other hand, local configurations are specific to an application and include service discovery, credential management, and feature flags.

**Service discovery** is used to locate other microservices, databases, or caches, and their URLs change per environment. **Credential management** is responsible for tracking usernames and passwords of databases, while feature flags allow changing the behavior of an application based on the environment.

Managing configurations and secrets can be challenging because their values change based on the environment they are deployed in. This means that the same application may have different configurations and secrets in each environment, making it difficult to manage them consistently and securely. Read more [here](https://www.facets.cloud/blog/simplifying-config-management-part-1)

In this concept document, we will explore how Facets simplifies configuration management for applications.

### Environment Level Secrets and Variables:

Facets allows you to manage global configurations through environment-level secrets and variables. These secrets and variables can be filled manually or through automation at the time of launching a new environment. You can also edit these secrets and variables in a running environment.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/a6babc0-image.png",
        null,
        null
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Click on the image to expand"
    }
  ]
}
[/block]

### Service Level Environment Variables:

Facets also enables you to manage local configurations through service-level environment variables. Service discovery-related concerns for services within the Facets blueprint can be solved as it allows you to refer to services by their logical or abstract names and refer to them in your application as environment variables. Facets takes care of wiring the changing values per environment and provides a seamless solution for managing credential management. In case a service needs to access a cloud resource, Facets automatically provisions IAM roles, making the process of configuration management even more convenient.

## FAQs

### Can I edit the environment-level secrets and variables in a running environment in Facets?

Yes, you can edit the environment-level secrets and variables in a running environment in Facets.

### How does Facets solve service discovery concerns in configuration management?

Facets allows you to refer to services by their logical or abstract names and refer to them in your application as environment variables. Facets takes care of wiring the changing values per environment and provides a seamless solution for managing service discovery.

### How does Facets handle IAM management for cloud resources?

Facets provides a seamless solution for managing credential management by automatically provisioning IAM roles in case a service needs to access a cloud resource.

### Does Facets simplify feature flag management in configuration management?

Yes, Facets simplifies feature flag management by allowing you to manage local configurations through service-level environment variables.