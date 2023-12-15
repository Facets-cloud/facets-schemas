---
title: "Part 2 - Connect a MongoDB"
slug: "part-2-connect-a-mongodb"
excerpt: "Deploying a Mongodb and connecting the flask app to mongodb."
hidden: true
createdAt: "2022-03-30T17:21:02.014Z"
updatedAt: "2022-04-04T11:34:06.098Z"
---
In the second part of this demo, we are going to modify our flask app to connect and interact with MongoDB.
 
Through this intent, Facets automatically provisions the MongoDB, provides required accesses to MongoDB from the app as well as automatically creates credentials and makes it available through environment variables to the application.

Below are the steps we will follow to deploy MongoDB and interact with it from within our application.

#Step 1: - Modify the application to connect to MongoDB

We will modify our flask application to interact with MongoDB. We create a new route called <code>/det</code> and pass a query parameter named <code>text</code> which will create a new record every time it is navigated and then displays all the records in a particular MongoDB collection. Each record is a simple json with 2 keys called <code>Random_Number</code> and <code>Your_text</code>. The value of the first key is a random integer between <code>1000</code> and <code>1000000</code>. The value of the second key is the text that you passed as a query parameter.

1. Modify the python code 
[block:code]
{
  "codes": [
    {
      "code": "from http import client\nfrom flask import Flask,request\nimport os\nfrom bson import json_util\nimport json\nimport random\nfrom pymongo import MongoClient\napp = Flask(__name__)\n@app.route('/')\ndef hello_world():\n    return 'Hello World'\n@app.route('/healthz')\ndef healthz():\n    return ''\n@app.route('/det')\ndef det():\n    text = request.args.get('text')\n    client = MongoClient('mongodb://{0}:{1}@{2}'.format(os.getenv('MONGODB_USER'),os.getenv('MONGODB_PASSWORD'),os.getenv('MONGO_HOST')))\n    db = client['facets']\n    cl = db[\"demo\"]\n    rec = {\n        \"Random_Number\": random.randint(1000,1000000),\n        \"Your_text\": text\n    }\n    x = cl.insert_one(rec)\n    cur = cl.find({})\n    return json_util.dumps(cur)\n \nif __name__ == '__main__':\n    app.run(host='0.0.0.0')",
      "language": "python"
    }
  ]
}
[/block]
2. We have used the pymongo python library. Hence, include pymongo in the <code>requirements.txt</code> along with the other dependencies already defined.

3. Rebuild the docker image and push to your control plane with a new tag
[block:code]
{
  "codes": [
    {
      "code": "docker build -t facets-python-demo:2 .\nfacetsctl image push facets-demo1 app QA facets-python-demo:2 1",
      "language": "shell"
    }
  ]
}
[/block]
# Step 2: - Modify blueprint repository to add an instance of MongoDB

1. Create a new directory named <code>mongo/instances</code> inside <code>facets-python-demo/</code> directory.

2. Create a file named <code>facets-demo-mongo.json</code> inside instances/ directory with the following content
[block:code]
{
  "codes": [
    {
      "code": "{\n    \"$schema\": \"https://docs.facets.cloud/schemas/mongo/instances/mongo.schema\",\n    \"name\": \"facets-demo-mongo\",\n    \"k8s_service_names\": [\n        \"facets-demo-mongo\"\n    ],\n    \"size\": \"S\",\n    \"k8s_slave_service_names\": []\n}\n",
      "language": "json"
    }
  ]
}
[/block]
3. Create a file named <code>sizing.json</code> inside <code>mongo/</code> directory with the following content. This file provides sizing options for mongodb.
[block:code]
{
  "codes": [
    {
      "code": "{\n    \"$schema\": \"https://docs.facets.cloud/schemas/mongo/sizing.schema\",\n    \"S\": {\n        \"podCPULimit\": 1,\n        \"podMemoryLimit\": 2,\n        \"volumeSize\": 10\n    }\n}\n",
      "language": "json"
    }
  ]
}
[/block]
4. Create another file named <code>disaster-recovery.json</code> at the same level as <code>sizing.json</code> with the following content. This file defines our disaster recovery configurations for the mongodb. For the purpose of this demo, we have disabled the disaster recovery. 
[block:code]
{
  "codes": [
    {
      "code": "{\n    \"$schema\": \"https://docs.facets.cloud/schemas/mongo/disaster-recovery.schema\",\n    \"creationStrategy\": {\n        \"triggerSchedule\": []\n    },\n    \"enabled\": false,\n    \"retentionStrategy\": {\n        \"incrementalWindow\": {},\n        \"rollingWindow\": {\n            \"days\": 0,\n            \"hours\": 0,\n            \"weeks\": 0\n        },\n        \"triggerSchedule\": []\n    }\n}\n",
      "language": "json"
    }
  ]
}
[/block]
#Step 3: - Modify <code>app.json</code> to include the dependency with MongoDB

In order for our flask application to connect to the MongoDB database, we need database credentials. Facets Control Plane automatically creates credentials and sets them as environment variables. This is done using the <code>permission</code> block in the <code>app.json</code>. Application can then simply request credentials with the set of permissions defined in the permission block. 

1. The permission block below instructs facets to create a user with read-write permission to the facets-demo-mongo database.
[block:code]
{
  "codes": [
    {
      "code": "\"permission\": [\n      {\n        \"name\": \"facets-demo-mongo\",\n        \"resourceType\": \"mongo\",\n        \"resourceName\": \"facets-demo-mongo\",\n        \"permission\": \"READ_WRITE\"\n      }\n    ]",
      "language": "json"
    }
  ]
}
[/block]
2. We now define environment variables in our app.json file. For this application, we use 3 environment variables namely <code>MONGODB_USER</code>, <code>MONGODB_PASSWORD</code> and <code>MONGO_HOST</code>. The below block describes defining environment variables in our <code>app.json</code>.
[block:code]
{
  "codes": [
    {
      "code": "\n\"env\": {\n      \"MONGO_HOST\": {\n        \"type\": \"static\",\n        \"default\": \"facets-demo-mongo.default\"\n      },\n      \"MONGODB_USER\": {\n        \"type\": \"credential\",\n        \"permissionName\": \"facets-demo-mongo\",\n        \"attribute\": \"userName\"\n      },\n      \"MONGODB_PASSWORD\": {\n        \"type\": \"credential\",\n        \"permissionName\": \"facets-demo-mongo\",\n        \"attribute\": \"password\"\n      }\n    },\n\n",
      "language": "json"
    }
  ]
}
[/block]
3. Copy the below modified code in your app.json for permissions and environment variables blocks. Note - Make sure to change the image URI as well. Copy the image URI from the Artifacts tab in the control plane for the latest pushed image and replace the image URI in the code below with your URI.
[block:code]
{
  "codes": [
    {
      "code": "{\n  \"$schema\": \"https://docs.facets.cloud/schemas/application/instances/application.schema\",\n  \"kind\": \"facets.modules.common.application\",\n  \"disabled\": false,\n  \"apiVersion\": \"v2\",\n  \"metadata\": {\n  },\n  \"spec\": {\n    \"env\": {\n      \"MONGO_HOST\": {\n        \"type\": \"static\",\n        \"default\": \"facets-demo-mongo.default\"\n      },\n      \"MONGODB_USER\": {\n        \"type\": \"credential\",\n        \"permissionName\": \"facets-demo-mongo\",\n        \"attribute\": \"userName\"\n      },\n      \"MONGODB_PASSWORD\": {\n        \"type\": \"credential\",\n        \"permissionName\": \"facets-demo-mongo\",\n        \"attribute\": \"password\"\n      }\n    },\n    \"loadbalancing\": {\n      \"rules\": [\n        {\n          \"ingress\": \"ingress\",\n          \"path\": \"/\",\n          \"portName\": \"port5000\"\n        }\n      ]\n    },\n    \"permission\": [\n      {\n        \"name\": \"facets-demo-mongo\",\n        \"resourceType\": \"mongo\",\n        \"resourceName\": \"facets-demo-mongo\",\n        \"permission\": \"READ_WRITE\"\n      }\n    ],\n    \"release\": {\n      \"strategy\": \"RollingUpdate\",\n      \"build\": {\n        \"image\": \"313496281593.dkr.ecr.us-east-1.amazonaws.com/facets/facets-demo1/app:facets8\"\n      }\n    },\n    \"runtime\": {\n      \"size\": {\n        \"value\": \"small\",\n        \"namespace\": \"GP\"\n      },\n      \"autoscaling\": {\n        \"cpuThreshold\": \"50\",\n        \"max\": 1,\n        \"min\": 1\n      },\n      \"ports\": [\n        {\n          \"name\": \"port5000\",\n          \"port\": 5000\n        }\n      ]\n    }\n  }\n}\n",
      "language": "json"
    }
  ]
}
[/block]
4. Push all changes to your git repo

#Step 4: - Release and test the changes

1. Go to the Release tab in Facets control Plane and click Release. This will perform a complete release of all the changes in your blueprint. There is also a button called Selective which selectively releases your changes. For example, if you have multiple applications and you selectively want to release changes to only a few of them, use this option.

2. The logs for this release can be seen in Actions -> Terraform Logs.

3. Once the release is a success, navigate to the Application tab, find the application and the ingress details

4. Go to your browser and paste the ingress url. Append <code>/det?text=Hello</code> to the url and access the application. Every time you refresh the page, you should see a new row added and displayed.

:clap: If you are able to see an increase in the number of rows every time you refresh the page, it means the application is successfully deployed and able to communicate with the MongoDB.