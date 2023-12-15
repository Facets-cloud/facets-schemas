---
title: "Facets Control Plane"
slug: "control-plane"
hidden: false
createdAt: "2022-12-05T11:41:49.552Z"
updatedAt: "2023-02-22T11:18:58.480Z"
---
## Introduction

Control Plane is a self-serve DevOps platform that allows organizations to manage all aspects of their software development lifecycle in one place. It enables organizations to streamline their Software Delivery Life Cycle (SDLC), manage multiple environments, and collaborate more effectively, all while maintaining a high level of security and compliance.

> Control Plane is a comprehensive DevOps platform that provides a centralized control center for managing infrastructure and applications.

## Architecture

The Control Plane is designed as a microservices architecture and operates within a customer's cloud environment, offering full ownership. It requires a Kubernetes cluster to function and is currently only deployable on AWS. However, it has the capability to manage resources on all three major cloud platforms, including AWS, Azure, and GCP. The following diagram provides a simple illustration of its architecture. Additionally, it is [self-deployable](https://readme.facets.cloud/v1.0/docs/setup-your-control-plane) and can be easily set up on the customer's AWS account with a single click, through a fully automated process.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/d599bf3-image.png",
        null,
        null
      ],
      "align": "center",
      "sizing": "700px",
      "border": true
    }
  ]
}
[/block]

## FAQs

### What is Control Plane?

Control Plane is a self-serve DevOps platform that enables organizations to manage all aspects of their software development lifecycle in one centralized location.

### How is Control Plane deployed?

Control Plane is self-deployable and can be easily set up on the customer's AWS account with a single click, through a fully automated process.

### Can Control Plane manage resources on other cloud platforms?

Yes, Control Plane has the capability to manage resources on all major cloud platforms, including AWS, Azure, and GCP.

### Can Control Plane be deployed in Azure or GCP?

Currently, Control Plane is only deployable on AWS

## Related Guides

- [Get your control plane](https://readme.facets.cloud/docs/setup-your-control-plane)