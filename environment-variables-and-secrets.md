---
title: "Global Environment Variables and Secrets"
slug: "environment-variables-and-secrets"
hidden: true
createdAt: "2022-03-30T08:18:46.396Z"
updatedAt: "2023-04-17T06:24:28.779Z"
---
Environment variables and secrets are commonly required by applications. In this Section, you will learn how to work with different types of environment variables and secrets. 

In Facets, Blueprints are used to declare all the environment variables that your service requires. These are applied either during run-time or manually by the DevOps teams.

# Static Environment Variables

Static environment variables are application-specific variables defined by the developer for their application. Facets Control Plane is oblivious to their usage so developers have to maintain them globally and per environment. 

> 🚧 
> 
> In order to fine tune variables as needed, developers can override their values per environment.

For example, <code>jettyQueueSize</code> is a typical parameter that might be required by the developer to be changed or overridden per environment. You can add this to the application json in the following manner:

```json
"env": {
    "jettyQueueSize": {
     "type": "static",
     "default": "10"
    }
   }
```



Now say for a particular environment, the developer wants to modify the value to be <code>20</code>, then they can go to the control plane and select **Overrides**, search for their application and add a JSON variable <code> jettyQueueSize: "20"</code>

# Dynamic Environment Variables

Dynamic environment variables are variables that are defined by developers through Facets Intents. Their values will vary per environment and they are managed by Facets. Developers can not override their values per environment.  
In the following example, an application service needs to serve assets which are stored in S3 bucket. The application developer defines this dynamic variable to get the S3 bucket name dynamically per environment

```json
"env": {
    "ASSET_BUCKET": {
     "type": "dynamic",
     "resourceType": "s3",
     "resourceName": "assetfiles",
     "attribute": "bucket_name"
    }
   }
```



<code>assetfiles.json</code>, the corresponding logical resource in the blueprint, defines the desired s3 characteristics. In each environment a new bucket is created and wired to the application in the <code>ASSET_BUCKET</code> environment variable for the application to read and operate.

## Secrets Management

Facets provide secret management capabilities to users for storing their third-party integrations' credentials that can be directly used when deploying resources from the control plane. 

Some common use cases include storing Github authorization credentials, API authentication tokens, or Google Client Secrets. 

### Defining a Secret

A secret needs to be defined in your `stack.json` file under the `clusterVariablesMeta` key with its secret field set to true. For example: 

```json
{
  "clusterVariablesMeta": {
    "MY_SECRET": {
      "secret": true,
      "value": "dummy"
    }
  }
}
```



This secret also needs to explicitly wired in the `application.json` file in the format specified below for it to be available as a secret in your control plane.

```json
"env": {
    "ASSET_BUCKET": {
     "type": "dynamic",
     "resourceType": "cluster",
     "resourceName": "secrets",
     "attribute": "MY_SECRET"
    }
   }
```



> 📘 NOTE
> 
> The `attribute` key needs to be defined in your stack.json file as a secret for it be available in the Facets Control Plane.

### Setting Secrets from Control Plane

After defining a secret, you can view and set a value for them under the **Secrets & Variables** screen in Facets Control Plane. 

> 📌 NOTE
> 
> Ensure to keep a backup of your secret value, since it is not readable after it has been set. 
> 
> You can however set a new value with if you have the appropriate permissions.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/9df07c7-Screenshot_2022-07-12_at_11.44.34_AM.png",
        null,
        "Secrets and Variables screen in Facets UI"
      ],
      "align": "center",
      "sizing": "720[object Object]",
      "caption": "Secrets and Variables screen in Facets UI"
    }
  ]
}
[/block]

# Credentials

Credential management is usually a two step process.  A developer requests access to a certain resources that their application needs. Based on the request, the DevOps team provisions the credentials and passes this to the application as environment variable or through a secret store. 

In Facets, this is handled through Credential environment variables. First a permission block is designed which requests a <code>READ_WRITE</code> permission from a database named <code>master-postgres</code>.

```json
"permission": [
    {
     "name": "creds-master-postgres",
     "resourceType": "postgres",
     "resourceName": "master-postgres",
     "permission": "READ_WRITE",
     "additional_options": {
      "databases": {
       "user_master": {
        "schema": ["public"]
       }
      }
     }
    }
   ]
```



The <code>username</code> and <code>password</code> is then injected through the following application intent. 

```json
"MASTER_PG_USERNAME": {
     "type": "credential",
     "permissionName": "creds-master-postgres",
     "attribute": "userName"
    },
    "MASTER_PG_PASSWORD": {
     "type": "credential",
     "permissionName": "creds-master-postgres",
     "attribute": "password"
    }
```