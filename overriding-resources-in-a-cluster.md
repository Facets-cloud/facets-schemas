---
title: "Overriding Resources in an Environment"
slug: "overriding-resources-in-a-cluster"
excerpt: "Learn how to override different types of resources defined in your blueprint"
hidden: true
createdAt: "2022-07-19T05:43:59.297Z"
updatedAt: "2023-02-14T20:08:36.926Z"
---
Facets provides the capability to override the configuration for any resources in an environment from the Control Plane.

## Prerequisites

- The environment you are using should have at least one defined resource in it to be able to override it. 

## Override a resource from the Control Plane

> 📘 
> 
> For a given blueprint and environment, you can find all the defined resources under the Resources tab in the sidebar.

1. Login to Facets Control Plane. 
2. Select the Environment in your defined Blueprint.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/e847578-env_page.png",
        null,
        "Select the environment (Click on the image to expand)"
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Select the environment (Click on the image to expand)"
    }
  ]
}
[/block]

3. Navigate to the Resources tab from the sidebar and select the resource that you want to override.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/981f29d-appingress.png",
        null,
        "Select the Resource (Click on the image to expand)"
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Select the Resource (Click on the image to expand)"
    }
  ]
}
[/block]

4. You will now be able to view the Configuration screen where you can find currently defined values for that resource.

> 📘 NOTE
> 
> All defined resources except for type `application `will directly open the **Configuration** screen for that resource.
> 
> Resources that are of type `application `will direct the user to the **Live Release** tab. The **Configuration** tab can be accessed next to the Live Release tab. 
> 
> See [Override resources of type `application `](https://readme.facets.cloud/docs/overriding-resources-in-a-cluster#override-resources-of-type-application)for more information.

5. Click on the Override button, this will open a new page.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/afae04d-override_appingress.png",
        null,
        "Override Button (Click on the image to expand)"
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Override Button (Click on the image to expand)"
    }
  ]
}
[/block]

6. You can set new values or update any of the predefined values in this page. You can also view the original configuration as defined in the blueprint.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/3370d3b-blueprint_config.png",
        null,
        "View current configuration (Click on the image to expand)"
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "View current configuration (Click on the image to expand)"
    }
  ]
}
[/block]

7. Click the Submit button to submit your override. 

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/00dcf81-submit_override.png",
        null,
        "Submit the override (Click on the image to expand)"
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Submit the override (Click on the image to expand)"
    }
  ]
}
[/block]

8. You will now be able to view the Review Changes screen.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/9a68d2a-review_changes.png",
        null,
        "Review Changes (Click on the image to expand)"
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Review Changes (Click on the image to expand)"
    }
  ]
}
[/block]

9. Click Save Changes button and you will see a popup toaster confirming the change with a message that this change will be applied in the next release.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/e5fb656-success_toaster.png",
        null,
        "Success Toaster (Click on the image to expand)"
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Success Toaster (Click on the image to expand)"
    }
  ]
}
[/block]

### Override resources of type `application `

> 📘 
> 
> The override screen for resources that are of type `application` has two modes:
> 
> - **Form Mode**
> - **JSON Mode**

You can override defined parameters for **Container Configuration** and **User-defined Variables** in **Form Mode**. 

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/955960a-override_backend.png",
        null,
        "Override `application` (Click on the image to expand)"
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Override `application` (Click on the image to expand)"
    }
  ]
}
[/block]

You can also toggle to **JSON Mode** to get an override page matching all the other resources where you can input values from your keyboard. 

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/259c897-json_mode.png",
        null,
        "Toggle JSON Mode (Click on the image to expand)"
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Toggle JSON Mode (Click on the image to expand)"
    }
  ]
}
[/block]

## Constraints

The overridden configuration as changed by a user will only be enforced on the next release of the environment.