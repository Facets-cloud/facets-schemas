---
title: "Trigger rollback to a previous version"
slug: "trigger-rollback-for-an-artifact"
excerpt: "restore"
hidden: false
createdAt: "2022-11-15T10:31:58.275Z"
updatedAt: "2022-11-16T09:40:58.565Z"
---
> 🚧 Your Control Plane URL will vary
> 
> The Control Plane URL, denoted as <code>YOUR_CP_URL</code> for this API reference document is <https://facetsdemo.console.facets.cloud>.  
> Replace that with the Control Plane URL unique to your account.

## API Endpoint

This API processes a `POST` request to trigger rollback for a given version ID of any versioned resource. 

```text Hover on the Text and Click the Notepad icon to Copy
https://{YOUR_CP_URL}/cc-ui/v1/versions/{versionId}/restore
```



- This API endpoint has one **path parameter**, `versionId`.

### **Path parameters**

- `versionId` of type `String` is the unique ID of the resource version for which you want to trigger a rollback.

## **Authenticate your API request**

To learn more about how to generate a basic authentication token for use with Facets API requests, check [Authentication Setup](ref:authentication-setup).

> 📘 
> 
> Facets API uses Basic Authentication.

## Generate a Sample Request ➡️