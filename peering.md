---
title: "Peering with Facets"
slug: "peering"
excerpt: "Learn how to establish bi-directional communication between services"
hidden: true
createdAt: "2023-02-01T12:06:29.626Z"
updatedAt: "2023-02-02T06:22:40.213Z"
---
Peering with Facets Control Plane enables you to set up communication between multiple services, irrespective of their environments. 

This guide will walk you through the steps to peer your existing environment with Facets managed Environment. 

## Peering from your Control Plane

1. In your Blueprint, create a folder in path `peering/instances`.
2. Create a file named `<resource_name>.json` and commit the changes.  
   Example: `existing_cluster.json.`
3. Now, open Facets Control Plane and navigate to **Environment >> Resources.** 

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/7c8a899-image.png",
        null,
        "Resources tab in left Navigation menu"
      ],
      "align": "center",
      "sizing": "350px",
      "border": true,
      "caption": "Resources tab in left Navigation menu"
    }
  ]
}
[/block]

> 📘 Note
> 
> After updating the Blueprint, it may take up to 5 minutes for changes to appear on the Resources page.  
> Click the **Sync with Git** button on the top right corner of the Resources screen to sync the resources with the blueprint.  
> Please reload the page if you are still unable to view the newly defined resources.

4. Under resource type **Peering**, select the resource **existing_cluster.**

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/88fa328-image.png",
        null,
        "Resource Center"
      ],
      "align": "center",
      "sizing": "700px",
      "border": true,
      "caption": "Resource Center"
    }
  ]
}
[/block]

5. In the **Resource Details** page, click **Override.**

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/557a724-image.png",
        null,
        "Resource Details"
      ],
      "align": "center",
      "sizing": "700px",
      "border": true,
      "caption": "Resource Details"
    }
  ]
}
[/block]

6. This will open up the JSON file that we just committed.  
   Copy and paste the below override command in the right column and provide the existing VPC details of the environment it should be peered with.

```json
{
    "flavor": "default",
    "kind": "peering",
    "version": "0.1",
    "disabled": false,
    "metadata": {
        "name": ""
    },
    "spec": {
        "account_id": "",
        "vpc_id": "",
        "cidr": "",
        "region": ""
    },
    "advanced": {
    }
}
```



7. Click **Save Changes**.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/6923d46-image.png",
        null,
        "Override Configuration"
      ],
      "align": "center",
      "sizing": "700px",
      "border": true,
      "caption": "Override Configuration"
    }
  ]
}
[/block]

8. After performing the next release, you can approve the peering connection from your cloud console.

You have successfully established bi-directional communication between your services.