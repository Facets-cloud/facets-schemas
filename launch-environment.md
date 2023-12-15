---
title: "Launch the Environment"
slug: "launch-environment"
hidden: false
metadata: 
  title: "Onboard a Service - Facets DevOps Platform"
  description: "Deploy a service on the environment in the Facets DevOps Platform"
createdAt: "2022-03-09T19:54:57.333Z"
updatedAt: "2023-02-15T12:23:07.519Z"
---
To launch your environment, go to the releases page for the respective environment. On this page, you'll see a prompt to launch your environment. Confirm the launch, and the system will run the Terraform.

When you create an environment using Facets, even if you use an empty blueprint, the following resources are created by default:

## Networking:

**Virtual Private Cloud (VPC):** A VPC provides a logically isolated section of the AWS cloud where you can launch AWS resources in a virtual network that you define.

**Subnets and Security Groups: **Facets creates subnets and security groups within the VPC, to segment network traffic and provide access control to resources within the network.

## Kubernetes:

**A Kubernetes cluster:** A Kubernetes cluster is a set of worker nodes and control-plane nodes that run containerized applications.

**Default Node Pool: **Facets creates a default node pool for the Kubernetes cluster, which is used to run containers.

**Prometheus, Grafana, and Alertmanager:** These tools provide a way to monitor and alert on the health and performance of the cluster and its components.

**Kubernetes Dashboard:** The Kubernetes dashboard provides a web-based UI for interacting with the cluster, including viewing logs and monitoring resources.

**ECR Token Refresher:** The ECR token refresher is responsible for refreshing the authentication token for accessing the Amazon Elastic Container Registry (ECR).

**Load Balancer for Tooling:** Facets creates a load balancer for all the tooling components in the cluster, such as Prometheus, Grafana, and the Kubernetes dashboard. This makes it easier to access these components from outside the cluster.

By providing these default resources, Facets makes it easy to get started with your environment and provides a foundation for running and managing your applications.