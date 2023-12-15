---
title: "Update Artifact Routing Rule"
slug: "update-artifact-routing-rule"
hidden: false
createdAt: "2023-09-11T09:55:22.039Z"
updatedAt: "2023-09-15T13:54:59.844Z"
---
> 🚧 Your Control Plane URL will vary
> 
> The Control Plane URL, denoted as <code>YOUR_CP_URL</code> for this API reference document is <https://facetsdemo.console.facets.cloud.>  
> Replace that with the Control Plane URL unique to your account.

## API Endpoint

This API processes a <code>PUT</code> request to override the current configuration for CI Rules.

```text Hover on the Text and Click the Notepad icon to Copy
https://<CP_URL>/cc-ui/v1/artifact-routing-rule \
```

### **Body parameters**

`artifactRoutingRuleRequest `is an object that will take `key:value` pairs of type String in code.

```json Example response body
{
  "artifactRoutingRuleRequest": {
    "criteria": [
      {
        "metadataKey": "string",
        "operator": "EQUALS",
        "priority": 0,
        "routeTo": "string",
        "value": "string"
      }
    ],
    "id": "string",
    "isDefault": true,
    "registrationType": "ENVIRONMENT",
    "ruleName": "string",
    "stackName": "string"
  }
}
```

## **Authenticate your API request**

To learn more about how to generate a basic authentication token for use with Facets API requests, check [Authentication Setup](ref:authentication-setup).

> 📘 
> 
> Facets API uses Basic Authentication.

## Generate a Sample Request ➡️

Fill in the mandatory parameter below using the updated response body to generate a sample request. To know more about updating the body parameter, refer to the [Update Response Body](ref:default-ci-rule-using-api#step-3-update-the-response-body) section.