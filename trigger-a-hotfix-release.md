---
title: "Trigger a Selective release"
slug: "trigger-a-hotfix-release"
hidden: false
createdAt: "2022-06-30T04:58:17.541Z"
updatedAt: "2022-10-07T04:32:15.704Z"
---
> 🚧 Your Control Plane URL will vary
> 
> The Control Plane URL, denoted as <code>YOUR_CP_URL</code> for this API reference document is <https://facetsdemo.console.facets.cloud>.  
> Replace that with the Control Plane URL unique to your account.

## API Endpoint

This API processes a <code>POST</code> request to trigger a selective release on a selected unique cluster.

```text Hover on the Text and Click the Notepad icon to Copy
https://{YOUR_CP_URL}/cc-ui/v1/clusters/{clusterId}/deployments/release/deployment/hotfix
```



- This API endpoint has two **path parameters**, <code>YOUR_CP_URL</code>, and <code>clusterId</code>.
- There is also a **body parameter**, <code>resourceList</code>.

### **Path parameters**

- <code>YOUR_CP_URL</code> of type <code>String</code>, is the Control Plane URL specific to your account.
- <code>clusterId</code> of type <code>String</code>, is the unique ID for the environment where you want to make a release. You can find this ID in the Environment Overview screen in Facets UI.

### **Body parameters**

<code>resourceList</code> is an Array of Objects, defined as:

- <code>resourceName</code> of type <code>string</code>. 

> 📘 NOTE:
> 
> <code>resourceName</code> should be the name of the application as defined under the <code>application/instance</code> path.

- <code>resourceType</code> of type <code>string</code>,  with the default value <code>application</code>. 

```text Example of resourceList
{
  "resourceList": [
    {
      "resourceName": "name",
      "resourceType": "application"
    }
  ]
}
```



## **Authenticate your API request**

To learn more about how to generate a basic authentication token for use with Facets API requests, check [Authentication Setup](ref:authentication-setup).

> 📘 
> 
> Facets API uses Basic Authentication.

## Generate a Sample Request ➡️