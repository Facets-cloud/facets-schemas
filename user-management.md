---
title: "Managing Users"
slug: "user-management"
excerpt: "How to add users and assign permissions to your users"
hidden: true
createdAt: "2022-03-18T12:40:32.793Z"
updatedAt: "2023-02-14T20:44:28.354Z"
---
During the set up of Facets Control, an admin role would have been assigned to you. In this Section, you will learn how to add your developers to Facets Control Plane.

# Adding Users Manually

You can add users manually if you have Admin Credentials. 

Click on the Settings Icon and click on Users.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/b21a031-Users_button_in_UI.PNG",
        "Users button in UI.PNG",
        "Users Button in Settings (Click on the image to expand)"
      ],
      "align": "center",
      "sizing": "80",
      "caption": "Users Button in Settings (Click on the image to expand)"
    }
  ]
}
[/block]

Click on Create User and fill in the details as required.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/2e01539-Create_User_popup.PNG",
        "Create User popup.PNG",
        "Creating a user manually (Click on the image to expand)"
      ],
      "align": "center",
      "sizing": "80",
      "caption": "Creating a user manually (Click on the image to expand)"
    }
  ]
}
[/block]

# RBAC

Role Based Access Control helps you in assigning roles to users. 

## Base Roles

> 📘 Permissions associated with each role is now listed as a tooltip in the Create User screen.
> 
> You can also refer to [Granular Permissions](https://readme.facets.cloud/docs/user-management#granular-permissions-for-rbac-roles) section for a description of all permissions.

[block:parameters]
{
  "data": {
    "h-0": "Base Role",
    "h-1": "Roles",
    "0-0": "ADMIN",
    "0-1": "All pages, buttons and actions are accessible to the ADMIN role, including those in the DEVELOPER and VIEWER roles.",
    "1-0": "DEVELOPER",
    "1-1": "The following actions are available for DEVELOPER role in the listed screens:  \n• **Environments **screen: Pause/Resume Releases  \n• **Secrets and Variables** screen: Edit an Entry and Submit Changes.  \n• **Releases** screen: Configure Release and Selective Release, Schedule Release and Unlock State.  \n• **Overrides **screen: Add and Delete Override.  \n• **Backup **screen: Create Snapshots.  \n• **Provided Resources** screen: Update Resources.  \n• **Template Inputs** screen: Create New Tenants and Edit Tenants.  \n• **Alerts Centre** screen: Silence Alert  \n• All the access privileges of VIEWER role.",
    "2-0": "VIEWER",
    "2-1": "• **Environments **screen:  Sync with Git  \n• **Environment Overview** screen: Renew and Download K8s credentials and Sync with Git  \n• **Backup **screen: List Snapshots",
    "3-0": "GUEST",
    "3-1": "Usually gets assigned for a new user by default. Useful for Admin to filter and assign a role later.",
    "4-0": "CLUSTER_ADMIN",
    "4-1": "Perform Releases"
  },
  "cols": 2,
  "rows": 5,
  "align": [
    "left",
    "left"
  ]
}
[/block]

## Additional Roles

> 🚧 
> 
> Any Base Role can be assigned Additional Roles that give access to specific actions in Facets.

| Additional Roles  | Roles                                                       |
| :---------------- | :---------------------------------------------------------- |
| K8S_READER        | Can download K8s credentials and do readonly operations     |
| K8S_DEBUGGER      | Can download K8s credentials and do write operations on K8s |
| CLI_ARTIFACT_PUSH | Can push artifacts using CLI                                |

## Granular Permissions for RBAC roles

RBAC roles defined in Facets have a list of associated permissions that grant the user privileges to perform certain actions as listed in [Base Roles table](https://readme.facets.cloud/docs/user-management#base-roles). You can find a comprehensive list of permissions listed below.

| Permission                  | Description                                                         |
| :-------------------------- | :------------------------------------------------------------------ |
| ALERTS_CONFIGURE            | Grants permission to configure alerts.                              |
| APPLICATION_ROLLING_RESTART | Grants permission to initiate a rolling restart for an application. |
| ARTIFACTORY_WRITE           | Grants permissions to create and edit artifactories.                |
| ARTIFACTS_DELETE            | Grants permissions to delete artifacts.                             |
| ARTIFACTS_WRITE             | Grants Write permissions for actions related to artifacts.          |
| CHANNEL_WRITE               | Grants permission to create and edit channels.                      |
| ENVIRONMENT_CONFIGURE       | Grants permission to configure environments.                        |
| ENVIRONMENT_DELETE          | Grants permission to delete environments.                           |
| ENVIRONMENT_DESTROY         | Grants permission to destroy environments.                          |
| ENVIRONMENT_LAUNCH          | Grants permission to launch environments.                           |
| ENVIRONMENT_WRITE           | Grants permission to create, edit and configure environments.       |
| OAUTH_INTEGRATION_DELETE    | Grants permission to delete OAuth integration.                      |
| OAUTH_INTEGRATION_WRITE     | Grants permission to add and edit OAuth integration.                |
| STACK_CONFIGURE             | Grants permission to pause and unpause releases in an environment.  |
| STACK_WRITE                 | Grants permission to add and edit blueprints.                       |
| SUBSCRIPTION_DELETE         | Grants permission to delete subscriptions.                          |
| SUBSCRIPTION_WRITE          | Grants permission to create and edit subscriptions.                 |
| TEMPLATE_WRITE              | Grants permission to create and edit tenants.                       |
| USER_WRITE                  | Grants permission to create and edit user and also edit passwords.  |

# Google OAuth Integration

If you are using google workspace for your team logins, Facets can use the same login mechanism to give your users an instant access. 

## ☝️ Create an Oauth Client ID in your google console

Please refer to the google docs below 


[block:embed]
{
  "html": false,
  "url": "https://support.google.com/cloud/answer/6158849?hl=en",
  "title": "Setting up OAuth 2.0",
  "favicon": "https://support.google.com/favicon.ico",
  "provider": "support.google.com",
  "href": "https://support.google.com/cloud/answer/6158849?hl=en"
}
[/block]




[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/700bc57-google-oauth.png",
        "google-oauth.png",
        ""
      ],
      "align": "center",
      "sizing": "80"
    }
  ]
}
[/block]



| Variable Name            | Value                                                |
| :----------------------- | :--------------------------------------------------- |
| Application Type         | Web Application                                      |
| Name                     | Any name indicating the name of the Oauth 2.0 client |
| Authorized Redirect URIs | <<CP_URL>>login/oauth2/code/<unique-registration ID> |

> 📘 
> 
> This unique registration ID will be used later in the Facets UI to onboard the integration. We suggest it to be a single word all lower case.

## ✌️ Add the integration in Facets

![](https://files.readme.io/d151f83-oauth-add.png "oauth-add.png")

Click on the "Oauth Integrations" and choose "Add new Google Integration". 

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/ebca59d-add_new_google.png",
        "add new google.png",
        ""
      ],
      "align": "center",
      "sizing": "80"
    }
  ]
}
[/block]



Fill in the values as below

| Variable Name     | Value                                                            |
| :---------------- | :--------------------------------------------------------------- |
| Registration Id   | <unique-registration ID> given in redirect URI                   |
| Client Id         | From Google Console                                              |
| Client Secret     | From Google Console                                              |
| Login Button Text | Text that will appear in the CP Login screen. For e.g. <OrgName> |

> 📘 
> 
> Logout to see this new button on the login screen. Any new user by default will receive a GUEST role until an ADMIN assign her to a desired [Role](doc:user-management#rbac)