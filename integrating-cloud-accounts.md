---
title: "Integrating Cloud Accounts"
slug: "integrating-cloud-accounts"
hidden: false
createdAt: "2023-03-02T09:41:24.267Z"
updatedAt: "2023-10-10T13:24:57.832Z"
---
This document provides a step-by-step guide for integrating cloud accounts with Facets. Amazon, Azure, and Google Cloud Platform (GCP) are the three types of cloud accounts that can be integrated with Facets.

## Adding a Cloud Account

Navigate to **Settings > Account Management.** All the accounts in Facets will be displayed here. Here, you will also be able to add new VCS and Cloud accounts.

### Azure

1. From the **Account Management** UI, click **Azure.**
2. Mention the **Account Name, Subscription ID, Tenant ID, Client ID, **and **Client Secret.**  
   Refer to the following documents for more details:

   1. [Subscription ID, Tenant ID](https://learn.microsoft.com/en-us/azure/azure-portal/get-subscription-tenant-id)
   2. [Client ID, Client Secret](https://learn.microsoft.com/en-us/answers/questions/834401/hi-i-want-my-client-id-and-client-secret-key)
3. Now, click **Add Account. **

You have successfully added a new Azure account to the Facets control plane.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/7cb8756-Azure_C_A.gif",
        null,
        "Click on the image to expand"
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Click on the image to expand"
    }
  ]
}
[/block]


### Google Cloud Platform

1. From the **Account Management** UI, click **GCP.**
2. Mention the **Account Name, Service account key**, and **Project ID.**  
   Refer to the following documents for more details:

   1. [Service account key](https://cloud.google.com/iam/docs/keys-list-get)
   2. [Project ID](https://support.google.com/googleapi/answer/7014113?hl=en)
3. Click **Add Account. **

You have successfully added a new Google Cloud Platform account to the Facets control plane.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/9b543ef-GCP_CA.gif",
        null,
        "Click on the image to expand"
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Click on the image to expand"
    }
  ]
}
[/block]


### AWS

1. From the **Account Management** UI, click **AWS.**
2. Mention the **Account Name.**
3. The **External ID** will be automatically generated and will be pre-filled.
4. To get the **IAM Role,** click the **CF template** link from the modal. 
   1. Log in to the AWS console, mention the required details, and click **Create Stack.**
   2. In the **Cloud Formation** window that appears, select the **Output** tab and copy the **Role ARN Value.**
5. Paste the **Role ARN Value** as the **IAM Role **and click **Add Account. **

You have successfully added a new AWS account to the Facets control plane.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/a94af72-AWS_CA.gif",
        null,
        "Click on the image to expand"
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Click on the image to expand"
    }
  ]
}
[/block]


### Kubernetes

1. From the **Account Management** UI, click **Kubernetes.**  
   NOTE: Please ensure you download the kubeconfig file from your kubernetes cluster before proceeding.
2. Mention the **Account Name, Host, Certificate Authority **and** Token.**
   1. **Account Name:** The name of the account that has to be displayed in Facets.
   2. **Host:** The URL or endpoint of the Kubernetes API server, which is the entry point for all cluster interactions.
   3. **Certificate Authority:** The path to the certificate authority (CA) file used to verify the authenticity of the Kubernetes API server. 
   4. **Token:** An authentication bearer token.
3. Click **Add Account.**

You have successfully added a new Kubernetes account to the Facets control plane.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/32f2f12-cloud_acc_kubernetes.gif",
        null,
        "Click on the image to expand"
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Click on the image to expand"
    }
  ]
}
[/block]