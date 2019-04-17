# Kubernetes Service Status

Service
-------
 - Labels
 - namespace
 - Internal endpoints
 - Ports
 - Protocol
 - creation-time
 
Deployment
----------
 - Labels
 - Namespace
 - number of replicas
 - creation-time
  

Pods
----

 - Labels
 - Stauts
 - Namespace
 - image
 - imageID
 - creation-time
 
Status API
----------

Request Url: [/{applicationFamily}/{environment}/applications/applicationId/stauts?namespace=default](Status)

Response:
```json
{
  "service": {
    "type": "ClusterIP",
    "internalEndpoints": {
      "ports": [
        "mongodb://lumpy-gopher-mongodb.default:27017"
      ],
      "nodePorts": [
        "mongodb://lumpy-gopher-mongodb.default:32197"
      ]
    },
    "labels": {
      "app": "metrics-server",
      "chart": "metrics-server-2.6.0",
      "heritage": "Tiller",
      "release": "winsome-molly"
    },
    "selectors": {
      "app": "metrics-server",
      "release": "winsome-molly"
    }
  },
  "deployment": {
    "name": "lumpy-gopher-mongodb",
    "replicas": {
      "ready": 1,
      "unavailable": 0
    },
    "labels": {
      "app": "mongodb",
      "chart": "mongodb-4.9.0",
      "release": "lumpy-gopher"
    },
    "creationTimestamp": "2019-04-16T09:06:28Z"
  },
  "pods": [
    "lumpy-gopher-mongodb-6dc4648d97-plc28": {
      "labels": {
        "app": "mongodb",
        "chart": "mongodb-4.9.0",
        "pod-template-hash": "2870204853",
        "release": "lumpy-gopher"
      },
      "namespace": "default",
      "kind": "replicaset",
      "status": "Running",
      "image": "bitnami/mongodb:4.0.3",
      "imageID": "docker-pullable://bitnami/mongodb@sha256:f06a3c8adf3cf4ea2a4205763262b4584a13abca6bd230fb3b0d2a1de4a015fe",
      "creationTimestamp": "2019-04-16T09:06:28Z"
    }
  ]
}
```
