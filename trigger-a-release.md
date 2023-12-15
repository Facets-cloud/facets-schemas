---
title: "Trigger a Release"
slug: "trigger-a-release"
excerpt: "This page documents the API endpoint to Trigger a Release"
hidden: false
createdAt: "2022-06-22T06:28:26.797Z"
updatedAt: "2022-06-27T11:03:33.973Z"
---
[block:callout]
{
  "type": "warning",
  "title": "Your Control Plane URL will vary",
  "body": "The Control Plane URL, denoted as <code>YOUR_CP_URL</code> for this API reference document is https://facetsdemo.console.facets.cloud. \nReplace that with the Control Plane URL unique to your account."
}
[/block]
##API Endpoint
This API processes a <code>PUT</code> request to trigger a release on a selected unique cluster.  

[block:code]
{
  "codes": [
    {
      "code": "https://{YOUR_CP_URL}/cc-ui/v1/clusters/{clusterId}/deployments/release",
      "language": "text",
      "name": "Hover on the Text and Click the Notepad icon to Copy"
    }
  ]
}
[/block]
* This API endpoint has two **path parameters**, <code>YOUR_CP_URL</code>, and <code>clusterId</code>.
* There are also two **query parameters**, <code>forceRelease</code>, and <code>withRefresh</code>.

### **Path parameters**
* <code>YOUR_CP_URL</code> of type <code>String</code>, is the Control Plane URL specific to your account.
* <code>clusterId</code> of type <code>String</code>, is the unique ID for the cluster where you want to make a release. You can find this ID in the Environment Overview screen in Facets UI.

### **Query parameters**
There are two required keys for making this API call: 
  * <code>forceRelease</code> of type <code>Boolean</code> with the default value <code>false</code>. 
[block:callout]
{
  "type": "info",
  "title": "",
  "body": "<code>forceRelease</code> can be used to force trigger a release regardless of any changes to the blueprint or application versions."
}
[/block]
  * <code>withRefresh</code>  of type <code>Boolean</code> with the default value <code>true</code>. Note that <code>true</code> will take considerably more time to finish deploying.
[block:callout]
{
  "type": "info",
  "title": "withRefresh is used for Drift detection",
  "body": "Drift detection enables you to detect whether the environment differs, or has drifted, from its blueprint specification. In case the value is set to \"true\", the deployment will try to match the environment configuration as defined in the blueprint if it has drifted from it."
}
[/block]
## **Authenticate your API request**

To learn more about how to generate a basic authentication token for use with Facets API requests, check [Authentication Setup](ref:authentication-setup).
[block:callout]
{
  "type": "info",
  "body": "Facets API uses Basic Authentication."
}
[/block]
## Generate a Sample Request ➡️