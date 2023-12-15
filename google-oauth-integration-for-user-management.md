---
title: "Integrating with Google Oauth"
slug: "google-oauth-integration-for-user-management"
hidden: false
createdAt: "2022-12-16T06:59:53.487Z"
updatedAt: "2023-05-05T10:58:34.417Z"
---
## Step 1: Create an OAuth Client ID in your Google Console

1. Go to the Google Console and follow the steps in the Setting up OAuth 2.0 guide.
2. Set up an Authorized Redirect URI using the following format: `<YOUR_CP_URL>/login/oauth2/code/<unique-registrationID>`.
3. Make note of the unique Registration ID provided by Google.
   1. Replace `<YOUR_CP_URL>` in the table above with the unique URL for your organization's Control Plane.
   2. We recommend using a single word, all in lower-case, as the `<unique-registrationID>`. This will be used later in the Facets UI when adding the OAuth integration to your Control Plane.

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
      "sizing": "300px",
      "border": true
    }
  ]
}
[/block]



## Step 2: Add Integration for Google OAuth in Control Plane

1. Log in to your Control Plane and go to the "Settings" section in the navigation pane.
2. Click on the "Oauth Integrations" tab.
3. Click on the "Add Google Integration" button.
4. Fill in the following information:
   1. Registration Id: Use the <unique-registrationID> from the Authorized Redirect URI in Step 1.
   2. Client Id: Use the Client Id from your Google Console.
   3. Client Secret: Use the Client Secret from your Google Console.
   4. Login Button Text: Choose the text that will appear in the Control Plane Login screen. For example, you can use your organization's name.

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
      "sizing": "400px",
      "border": true
    }
  ]
}
[/block]



That's it! You have successfully set up Google OAuth integration in Facets.