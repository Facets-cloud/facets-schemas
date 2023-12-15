---
title: "Rollback an artifact for a specific resource"
slug: "rollback-an-artifact-for-a-specific-resource"
hidden: false
createdAt: "2022-11-15T10:38:28.141Z"
updatedAt: "2023-04-11T16:44:52.316Z"
---
Facets provides users with the ability to access previous build images for any particular application and make a rollback to the desired version. 

Learn how to achieve this from your Control Plane in [Artifact History and Rollback documentation](https://readme.facets.cloud/docs/artifact-history-and-rollback).

## Using REST APIs to rollback

Let's first understand the logic we are going to use to identify and fetch the exact artifact details that we need to perform a rollback.

- Facets uses a `versionId` to uniquely identify every single artifact that is registered for any resource. 
- This list of `versionId` associated with a particular artifact can be fetched for any given environment and resource type using a GET call with a `versioningKey` as a parameter.
- The value for `versioningKey` is unique for any given set of environment, resource name and resource type.

This guide will help you to get the previous “N” artifacts and then choose a particular artifact to rollback to using API calls.  

To achieve this, follow these three steps:

Step 1: [Get the versioning key for the artifact.](https://readme.facets.cloud/reference/get-versioning-key)

Step 2: [Get list of last “N” artifacts.](https://readme.facets.cloud/reference/get-list-of-n-artifacts)

Step 3: [Trigger rollback for an artifact.](https://readme.facets.cloud/reference/trigger-rollback-for-an-artifact)

### Step by step instructions

#### Step 1: Get the versioning key

Perform a REST API `GET` call as specified in the [Get the versioning key for a Resource](https://readme.facets.cloud/reference/get-versioning-key) documentation.

**Response:**  
You should get a response with multiple fields. Copy the value for `versioningKey` field from this response body.

#### Step 2: Get list of “N” artifacts

Perform a REST API `GET` call as specified in the [Get list of last “N” versions](https://readme.facets.cloud/reference/get-list-of-n-artifacts) documentation.

> 📘 NOTE
> 
> Your `versioningKey `should be present in the API response from step 1.

**Response:**  
You should get a response with multiple fields. Copy the value for `id `field from this response body for the artifact for which you want to trigger a rollback.

#### Step 3: Trigger rollback for an artifact

Perform a REST API `GET` call as specified in the [Trigger rollback to a previous version](https://readme.facets.cloud/docs/artifact-history-and-rollback) documentation.

**Response:**  
You should receive a HTTP 200 response if the request has been successfully sent.