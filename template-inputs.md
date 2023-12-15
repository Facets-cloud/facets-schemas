---
title: "Templated Resources"
slug: "template-inputs"
excerpt: "Launch services in your multi-tenant setup"
hidden: true
createdAt: "2022-10-18T05:18:27.257Z"
updatedAt: "2023-02-16T06:05:10.200Z"
---
# Introduction

Often there is a business requirement to have multiple replicas of the same resource whether it be applications or any other infrastructure components. For example one may need to deploy one instance of an application for each tenant housed in an environment. 

This functionality can be realized in Facets by using **Templated Resources**.

This architecture is totally transparent to the end-user, and the underlying infrastructure is usually completely shared, logically isolated, and with fully centralized services. 

Using templated resources, you can create unique silos of users and configurations within a single environment. These silos might represent different customers, business units, subsidiaries, or some other division of business logic. 

# Getting Started with Template Inputs

This guide will help you to define your own templated resources in Facets and assign values per tenant. 

We will cover the definition, setup, instantiation, and release process when using template inputs. They can be broadly categorized as per the following steps: 

1. Define a new Template Input Type in your blueprint definition.
2. Create inputs of this Input Type in the Control Plane for the environment where you want to template.  
3. Define a mustache file in the resource type that needs to be templated in your blueprint definition. 
4. Perform a release to realize multiple replicas of a resource in your multitenant setup with Facets.

## Define a Template Input

Create a JSON file in your blueprint definition to define a Template Input.

The location of this file should be in the `template_input_type/instances` folder.

> 📘 
> 
> The Template Input will appear in the Control Plane based on the filename you have defined. 
> 
> For example, if you create a file named `Customers.json` to define your Template Input, it will be visible as `Customers` in the Template Input screen in your Control Plane.

## Set Up the Template Inputs from Facets Control Plane

Let's take an example scenario to understand how Templated Resources can be realized with Facets. 

Assume you need to manifest a multi-tenant setup for your application, where the resources are exactly the same between tenants but each tenant needs a separate logical identity. 

Following the steps from [Define a Template Input](https://readme.facets.cloud/docs/template-inputs#define-a-template-input), let's create a `tenants.json` file in the `template_input_type/instances` folder in your blueprint definition.

The sample json file will look like this: 

```json
{
  "spec": {
      "fields": ["name", "tenant_id"]
    }
  }
```



Specify the fields that will define your template as per your business logic in this file. 

The name of the Template Input in your Control Plane will be the same as the name of the file defined in the `template_input_type/instances` folder.

Now, 

1. Select the environment where you want to define your **Template Inputs** from the **Blueprint Overview** screen. 
2. Go to **Template Inputs** screen from your sidebar.  
   Template Inputs in your Control Plane will require you to provide a unique ID for each tenant that is defined.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/effa73e-image.png",
        null,
        "Template Inputs screen"
      ],
      "align": "center",
      "sizing": "150px",
      "caption": "Template Inputs screen"
    }
  ]
}
[/block]

3. Click on the Create New Tenants button.  
   Notice the button says "tenants" here. You can modify this by modifying the file name in `template_input_type/instances` folder. 

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/8a12ebb-image.png",
        null,
        ""
      ],
      "align": "center",
      "sizing": "500px"
    }
  ]
}
[/block]



   Template Inputs in your Control Plane will require you to provide a unique ID for each defined input. This can be any string of alphanumeric characters.  
   The other fields are parsed directly from the defined JSON file in your blueprint definition.

4. Input the details for all your tenants as shown in the popup dialog box.

## Define a mustache file for the resource types that need to be templatized

Facets uses Mustache, a logic-less templating system. Using Mustache permits you to use pre-written text files with placeholders that will be replaced at run-time with values particular to a given request. For more information, refer to [Mustache Manual](https://mustache.github.io/mustache.5.html).

Create a `.mustache` file in the `resource_type/instances` directory of the resource where you want to create template inputs.

In your mustache file use the JSON key "templatedOn" to specify which template input to use with the template. For example: "templatedOn": "tenant". This will generate one replica for each tenant defined in your Control Plane.

### Sample Mustache file

As an example, if your application takes `tenant_id` as an input, the below mustache json file creates one instance per defined tenant and passes the respective `tenant_id` field as an argument to the application.

Notice the key `templatedOn` is mapped to the defined Template Input file name.

```json Sample Mustache JSON
{
  "kind": "facets.modules.common.application",
  "apiVersion": "v2",
  "templatedOn": "tenants",
  "metadata": {...},
  "spec": {
    "release": {...}
    },
    "runtime": {
      "command": ["/usr/bin/myapp", "{{current.data.tenant_id}}"],
     ...
    }
  }
}
```



> 📘 NOTE
> 
> To refer to any field in the template input you can use `current.data.<field_name>`.  
> For example,` {{current.data.tenant_name}}`. 
> 
> However, to refer to the unique ID assigned when creating a new tenant in the Control Plane, use `{{current.uid}}`.

## Perform release and override values

You are now all set to create templated resources as defined by you in any environment. 

Perform a Full Release from the Releases screen and all your templated resources should materialize as defined.

## Conclusion

You are now able to define, set up, and manifest resources using Template Inputs.