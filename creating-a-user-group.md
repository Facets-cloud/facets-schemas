---
title: "Creating a User Group"
slug: "creating-a-user-group"
hidden: false
createdAt: "2023-01-10T11:27:18.642Z"
updatedAt: "2023-07-26T06:21:03.516Z"
---
This guide will walk you through the process of creating and managing a User Group.

1. Navigate to **Settings > User Management.**
2. Select the **User Groups** tab. Here, on this page, you will find all the User Groups available in Facets.
3. Click **Create User Group.**
4. In the page that appears, mention the **Group Name **(mandatory).
5. Select the **Default Role**(mandatory) and** Accessible Environments.**

> 📘 
> 
> If no **Accessible Environments** are selected, the users in this group will have access to all the environments.

6. Under **Env-specific permissions,** select the required environment and select the role you wish to assign to that environment and click **Add Permissions.**

> 📘 
> 
> - **Env-specific permissions** will take precedence over the user group's default role. 
> - In **Env-specific permissions,** you will only be able to select the environments added under **Accessible Environments**  
>   If no **Accessible Environments** are selected, you will be able to select from all the available environments.

7. Click the toggle to enable **Resource Group Based Access Control** and select the **Accessible Resource Groups.**

> 📘 
> 
> Selecting **Accessible Resource groups** ensures that the users within this group can only access the  resorce in those specified resource groups.

7. Click **Create.**

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/d592c7a-user_groups.gif",
        null,
        null
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Click on the image to expand"
    }
  ]
}
[/block]

You have successfully created a user group.

## Related Guide

- [Creating a User](doc:creating-a-user)
- [Creating Custom Roles](doc:custom-roles)