---
title: "Default CI Rule using API"
slug: "default-ci-rule-using-api"
hidden: false
createdAt: "2023-09-06T08:23:52.322Z"
updatedAt: "2023-09-15T13:54:04.471Z"
---
In Facets, you can define default CI Rules for designated blueprints, environments, and release streams, providing a baseline configuration for Continuous Integration.

## Using REST APIs to set default CI Rules

First, let's outline the logic we will employ to establish and configure default CI Rules.

- Facets uses `ruleID` to uniquely identify every CI Rule.
- The `ruleID` is unique for each CI Rule and can be extracted from the URL after selecting the CI Rule.

This guide will help you configure the default CI Rule using API calls. Refer to the [CI Rules documentation](ref:ci-rules) to know more about it.

To achieve this, use the following two APIs:

1. [getArtifactRoutingRule](ref:get-artifact-routing-rule)

2. [updateArtifactRoutingRule](ref:update-artifact-routing-rule)

## Step-by-step instructions

### Step 1: Get the ruleID

1. Navigate to **Artifacts > CI Rules** and select the required CI Rule.
2. Now, locate the `ruleID` from the URL and copy it.  
   **Eg:** _https\://\<control_plane_url>/capc/stack/\<bp_name>/artifacts/ci-rules/edit/<ruleID>_

### Step 2: Get the Artifact Routing Rule

Perform a REST API `GET` call using the `ruleID` as specified in the [Get Artifact Routing Rule](ref:get-artifact-routing-rule) documentation.

**Response:**

You should get a response with multiple fields. Copy the entire response body. 

### Step 3: Update the Response body

In the response body from the previous step, locate the `default: false` parameter.

Modify the key to “`isDefault`” and set the value to `true` or `false`.

Setting it to `true` designates the CI Rule as the default, while setting it to `false` removes the CI rule if is set as default.

### Step 4: Update the Artifact Routing Rule

Perform a REST API `PUT` call as specified in the [Update Artifact Routing Rule](ref:update-artifact-routing-rule) documentation.

**Response:**

You should get a response with the updated values denoting that the request has been successfully sent.

> 📘 
> 
> - Note that each Blueprint and Release Stream / Environment combination can have only one default CI Rule at a time.
> - If you intend to replace an existing default rule with a new one, ensure that you first set the attribute "isDefault" to "false" for the old rule.