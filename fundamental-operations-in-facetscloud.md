---
title: "Fundamental Operations in Facets"
slug: "fundamental-operations-in-facetscloud"
excerpt: "This document provides a concise guide to Facets, covering key operations: blueprint creation, resource addition, template creation, environment setup, and opening existing blueprints."
hidden: true
metadata: 
createdAt: "2023-09-18T11:36:40.144Z"
updatedAt: "2023-09-21T04:39:10.537Z"
---
## Create a Blueprint

To create a Blueprint, 

1. Navigate to **Blueprints > List.**  
   This page displays all the configured Blueprints in Facets.
2. Click **New Blueprint.**
3. Choose whether you want to choose from an existing Blueprint Template or create a new Blueprint.
4. If you choose to select from an existing Blueprint Template, search and select the required template. Now, proceed with creating the Blueprint.

> 📘 
> 
> Blueprint Templates encompass all the configured Resources and Services specific to that template. Utilising templates enables users to seamlessly incorporate these resources into their Blueprint, enhancing the creation and import process.

5. If you choose to create a new Blueprint:
   1. Mention a unique **Name **for your Blueprint.
   2. Select **Mark as Template** to save this new Blueprint as a template and enter the **Template Description. **
   3. Now, select the **GitHub Account** and enter the **GitHub Organisation name.**
   4. Click **Create.**

You have successfully created a new Blueprint.  You will now be redirected to the **Blueprint Designer** where you can see the visualization of the resources.

## Access the Existing Blueprint

1. Select the desired Blueprint from the blueprint list page and navigate to **Blueprint > Designer. **
2. Click **Coder** from the top right corner to access your project files.

## Create an Environment

After creating your Blueprint, you can now launch the first manifestation of it, which is the Environment.

1. Navigate to **Blueprint > Overview,** and you will see a prompt to create your first Environment.
2. First, you need to **Name your Environment,** select its **Release Stream, AWS Region** This information will be relevant when wiring builds.
3. You will also need to link the cloud account with the Facets control plane or select a pre-configured **Cloud Account.**
4. Now, under **Advanced Options,** mention the following:
   1. Select the required **Time Zone.**
   2. Choose the **Environment Type** between **Regular** or **Ephemeral.**
   3. Specify the **CIDR Range** for the VPC, making sure not to conflict with existing clusters if you have a use case for peering.
   4. Also mention the **Availability Zone, Instance Type, VPC ID, **and **Request Limit Ratio.**
5. Click **Create.**

A logical environment would be created by clicking the Create button. However, please note that the environment will not be launched immediately and will appear as 'stopped' on the environment page.

## Launch an Environment

To launch your environment, go to the releases page of the respective environment. On this page, you'll see a prompt to launch your environment. Confirm the launch, and the system will run the Terraform.

When you create an environment using Facets, even if you use an empty blueprint, the following resources are created by default:

### Networking:

**Virtual Private Cloud (VPC):** A VPC provides a logically isolated section of the AWS cloud where you can launch AWS resources in a virtual network that you define.

**Subnets and Security Groups:** Facets creates subnets and security groups within the VPC, to segment network traffic and provide access control to resources within the network.

### Kubernetes:

**A Kubernetes cluster:** A Kubernetes cluster is a set of worker nodes and control-plane nodes that run containerized applications.

**Default Node Pool:** Facets creates a default node pool for the Kubernetes cluster, which is used to run containers.

**Prometheus, Grafana, and Alertmanager:** These tools provide a way to monitor and alert on the health and performance of the cluster and its components.

**Kubernetes Dashboard:** The Kubernetes dashboard provides a web-based UI for interacting with the cluster, including viewing logs and monitoring resources.

**ECR Token Refresher:** The ECR token refresher is responsible for refreshing the authentication token for accessing the Amazon Elastic Container Registry (ECR).

**Load Balancer for Tooling:** Facets creates a load balancer for all the tooling components in the cluster, such as Prometheus, Grafana, and the Kubernetes dashboard. This makes it easier to access these components from outside the cluster.

By providing these default resources, Facets makes it easy to get started with your environment and provides a foundation for running and managing your applications.

## Create Resources

1. Select the desired Blueprint and navigate to **Blueprint > Designer. **
2. Click **Add Resource** from the top right corner.
3. In the pop-up window, search for and select the desired type of resource you wish to add.
4. Provide a **Name** for the resource and click **Create.**
5. To create the resource and view its configuration, click **Create & View.**

> 📘 
> 
> **Note:** When creating a resource, it is initially set with default sample values that can be modified after the resource has been created.

You have successfully added a Resource in Facets.

## Edit a Resource

1. Navigate to **Blueprint > Designer** in the left pane.
2. Click the Table mode icon located at the top right corner.
3. This view provides a list of all the resources available in the Blueprint.
4. Locate the resource you want to modify and click on the **Edit** icon adjacent to it.
5. On the page that appears, click **Edit** and make the necessary changes to the configuration settings.
6. Click **Save Changes** to save the updated configuration.