---
title: "Override Resource Definition in an Environment"
slug: "override-resource-defintion-in-an-environment"
hidden: false
createdAt: "2022-07-28T07:17:46.421Z"
updatedAt: "2023-09-11T10:32:41.855Z"
---
> 🚧 Your Control Plane URL will vary
> 
> The Control Plane URL, denoted as <code>YOUR_CP_URL</code> for this API reference document is <https://facetsdemo.console.facets.cloud>.  
> Replace that with the Control Plane URL unique to your account.

## API Endpoint

This API processes a <code>POST</code> request to override current configuration for resources across an environment.

```text Hover on the Text and Click the Notepad icon to Copy
https://{YOUR_CP_URL}/cc-ui/v1/clusters/{clusterId}/resourceType/{resourceType}/resourceName/{resourceName}/overrides
```

- This API endpoint has four **path parameters**: `YOUR_CP_URL`, `clusterId`, `resourceType`, and `resourceName`.
- There is also a **body parameter**, `overrides`.

### **Path parameters**

- `YOUR_CP_URL `of type `String`.  
  This is the Control Plane URL specific to your account.
- `clusterId `of type `String`.  
  This is the unique ID for the environment where you want to override values in a defined resource. You can find this ID in the Environment Overview screen in Facets UI.
- `resourceType`of type `String`.  
  This is the type of resource you want to override.
- `resourceName`of type `String`.  
  This is the name of the resource you want to override. 

> ⚠️ Validate your API request
> 
> Ensure that the blueprint contains a defined `resourceName.json` file for the resource in the path `resourceType/instances/resourceName` where you want to override values.

### **Body parameters**

`overrides` is an object that will take `key:value` pairs of type `String`.

> 📘 
> 
> - The data in the `overrides `object will be merged with the existing definition in the `resourceName.json` file defined in the blueprint.
> - Ensure that the`key:value` pairs match the keys and values in the `resourceName.json` file defined in the blueprint.

```json overrides
{
"overrides": {
   "key1": "value1",
   "key2": "value2"
}
}
```
```json Example of overrides
# To override the values for cpuThreshold to 60 
# In https://github.com/Facets-cloud/sample-springboot-app-stack/blob/master/application/instances/backend.json

{
"overrides": {
   "cpuThreshold": "60"
}
}
```

## **Authenticate your API request**

To learn more about how to generate a basic authentication token for use with Facets API requests, check [Authentication Setup](ref:authentication-setup).

> 📘 
> 
> Facets API uses Basic Authentication.

## Generate a Sample Request ➡️

Fill in the mandatory parameters below to generate a sample request.