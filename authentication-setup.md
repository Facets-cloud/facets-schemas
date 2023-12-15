---
title: "Authentication Setup"
slug: "authentication-setup"
excerpt: "This page will help you generate a basic authentication token for use with Facets API requests."
hidden: false
createdAt: "2022-06-22T06:57:50.142Z"
updatedAt: "2022-11-09T07:30:56.712Z"
---
## How to Authenticate your API requests

Facets API uses Basic Authentication Setup, meaning users require a username and a password to make API calls along with any parameters that may be specific to the endpoint.

The username is your registered email ID with which you access the Facets Control Plane, and the password is the unique token that you can generate from within Facets CP. For detailed instructions on how to generate a token, check out the following section.

### Generate Authentication Token (Password for Basic Auth)

1. Login to Facets Control Plane and click on your user profile picture to display Account Settings menu. 
2. Click on Generate Token to see a popup window.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/c2763bc-image.png",
        null,
        null
      ],
      "sizing": "666px",
      "caption": "Generate Token button"
    }
  ]
}
[/block]

3. Add a note detailing the purpose for generating this token.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/fb10ca6-generate_token2.png",
        "generate token2.png",
        2158
      ],
      "sizing": "80",
      "caption": "Token Note (Click on the image to expand)"
    }
  ]
}
[/block]

4. You will see another popup window with a string of characters. This is your personal token and password for the basic authentication to make API calls. 

> 📘 Save this token!
> 
> Copy this generated token and note it down somewhere since it will be visible only in this window!

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/10b2fb5-image.png",
        null,
        null
      ],
      "sizing": "666px",
      "caption": "Copy your Token from this popup window"
    }
  ]
}
[/block]

You can now use this username and password combination to make API calls to Facets Endpoints.