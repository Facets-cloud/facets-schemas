---
title: "Get your Control Plane"
slug: "setup-your-control-plane"
excerpt: "Your first step towards effortless deployments with Facets!"
hidden: false
metadata: 
  title: "Facets Control Plane - DevOps Platform"
  description: "Pre-requisites and getting started with the Facets DevOps Platform"
createdAt: "2022-03-09T13:39:58.881Z"
updatedAt: "2023-10-08T14:34:09.070Z"
---
Welcome to Facets! In this getting started guide, we will walk you through the process of getting your control plane. The first step is to get a demo and have a short discussion with the Facets team.

1. **Get a Demo **: To get started, simply request a [demo by contacting the Facets team](https://www.facets.cloud/demo). Our team will understand the requirements and will help you get started. 
2. **Launch the Cloudformation Template**: You will receive an email with a personalized link to launch a Cloudformation template. This template will create an IAM role in the AWS account of your choice. Please note that the CloudFormation template must be executed in the Mumbai region. However, this does not determine the region of your control plane.
   > Facets never requires you to provide any IAM User credentials (root or otherwise). The IAM role created by the CloudFormation template will have a limited set of IAM policies attached. The role will have a trust policy set. You can optionally limit the role permissions further after the initial control plane set up by updating the CloudFormation stack with it's option to limit permissions.
3. **The Facets team launches the Control Plane**: After you have created the IAM role in the AWS account, the Facets team will launch the control plane in the closest region to your developers. The control plane will be set up and ready to use in 60 minutes.
4. **Welcome Email with Control Plane URL**: You will receive a welcome email with your personal control plane URL, along with a username and password reset link. This will allow you to log into the control plane and start using Facets.

## Prerequisites

Setting up a Facets control plane in AWS requires an AWS account with sufficient service quotas to accommodate the resources listed in the ["resources deployed" section](https://readme.facets.cloud/docs/setup-your-control-plane#resources-deployed).

## Resources Deployed

To run your control plane in AWS, Facets deploys the following AWS resources: 

1. **VPC**: A dedicated Virtual Private Cloud for the Facets control plane.
2. **Subnets**:
   1. Private Subnets: One in each of two Availability Zones (AZs) with a corresponding NAT Gateway for each.
   2. Public Subnets: One in each of two AZs, connected to a single Internet Gateway (IGW).
3. **EKS**:
   1. Cluster: An Amazon Elastic Kubernetes Service cluster with Secret encryption with KMS
   2. Nodes: EKS nodes configured with 8 vCPUs, 32GB RAM, and a 100GB root volume.
4. **Load Balancer**: Two Internet facing Elastic Load Balancers (ELB).
5. **Certificates**: Two ACM (AWS Certificate Manager) certificates.
6. **Storage**:
   1. **S3 Buckets**: Three AES encrypted S3 buckets for:
      1. Logging (No public access)
      2. Internal Artifacts Storage (No public access)
      3. Infrastructure as Code (IAC) State storage (No public access)
   2. **EBS**: KMS encrypted EBS volumes for metadata, metrics and hot tier logs
7. **DynamoDB**: A table for Infrastructure as Code (IAC) state locking.
8. **Secret Management**: Any sensitive data submitted to your control plane is stored in an AWS secret manager secret.

### Deployment Options

You may choose to run the control plane in any AWS Region and Availability Zones of your choice. You can communicate your preferences with the Facets team before deploying the control plane. Any resource sizing changes may be requested over an email to [support@facets.cloud].

### Backups

The control plane data is backed up in the following manner:

1. The S3 bucket storing IAC state is versioned. Refer [this guide](https://docs.aws.amazon.com/AmazonS3/latest/userguide/RestoringPreviousVersions.html) for restoring previous versions.
2. Any changes to the metadata (Overrides, Environment Configurations etc) are versioned in the control plane and can be recovered with the click of a button! In addition, the metadata EBS volumes are backed up in EBS Snapshots at a daily frequency. Daily snapshots are maintained for 15 days and weekly snapshots for 4 weeks.
3. Data in AWS Secret Manager is also versioned. Refer to [this guide](https://docs.aws.amazon.com/secretsmanager/latest/userguide/manage_restore-secret.html) for restoring previous versions.

> Facets is soon going to be launched publicly, and it will be easy to launch a private or hosted control plane at your convenience. 

For any support, please send an email to [support@facets.cloud](mailto:support@facets.cloud).

And that's it! You now have access to your control plane and are ready to start using Facets. Next, you can create your first blueprint and start exploring the different concepts and features of Facets.