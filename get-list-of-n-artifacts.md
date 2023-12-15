---
title: "Get list of \"N\" versions"
slug: "get-list-of-n-artifacts"
excerpt: "getVersions"
hidden: false
createdAt: "2022-11-15T07:56:56.717Z"
updatedAt: "2022-11-16T09:40:19.994Z"
---
> 🚧 Your Control Plane URL will vary
> 
> The Control Plane URL, denoted as <code>YOUR_CP_URL</code> for this API reference document is <https://facetsdemo.console.facets.cloud>.  
> Replace that with the Control Plane URL unique to your account.

## API Endpoint

This API processes a `GET` request to get a list of specified number of version details for any versioned resource based on the provided `versioningKey`.  

```text Hover on the Text and Click the Notepad icon to Copy
https://{YOUR_CP_URL}/cc-ui/v1/versions/{versioningKey}
```



- This API endpoint has one **path parameter**, `versioningKey`.
- There are also two **query parameters**, `page `and `perPage`.

### **Path parameters**

- `versioningKey `of type `String`, is a unique value for any given set of environment, resource name and resource type.

### **Query parameters**

- `page `of type `Integer `with the default value 0. 
- `perPage `of type `Integer `with the default value 5.

> 📘 
> 
> `page `specifies the number of pages to fetch. Note that this value should be “0” to fetch the latest artifacts.  
> `perPage `specifies the number of artifacts to fetch. Note that the default value is "5".

## **Authenticate your API request**

To learn more about how to generate a basic authentication token for use with Facets API requests, check [Authentication Setup](ref:authentication-setup).

> 📘 
> 
> Facets API uses Basic Authentication.

## Generate a Sample Request ➡️