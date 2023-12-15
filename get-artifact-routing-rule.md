---
title: "Get Artifact Routing Rule"
slug: "get-artifact-routing-rule"
excerpt: "getArtifactRoutingRuleUsingGET"
hidden: false
createdAt: "2023-09-06T08:33:57.437Z"
updatedAt: "2023-09-15T13:54:26.253Z"
---
> 🚧 Your Control Plane URL will vary
> 
> The Control Plane URL, denoted as <code>CP_URL</code> for this API reference document is <https://facetsdemo.console.facets.cloud>.  
> Replace that with the Control Plane URL unique to your account.

## API Endpoint

All CI Rules will have a  `ruleID` that is uniquely associated with it.

This API processes a `GET` request to fetch the details for the given CI Rule.

```text Hover on the Text and Click the Notepad icon to Copy
https://<CP_URL>.console.facets.cloud/cc-ui/v1/artifact-routing-rule/{ruleId}
```

### **Path parameters**

- `ruleID` of type `String` is the unique ID for the CI Rule that you want to make default. You can find this ID in the URL of the selected CI Rule.

## **Authenticate your API request**

To learn more about how to generate a basic authentication token for use with Facets API requests, check [Authentication Setup](ref:authentication-setup).

> 📘 
> 
> Facets API uses Basic Authentication.

## Generate a Sample Request ➡️

Fill in the mandatory parameters below to generate a sample request.