---
title: "Self-Hosting Kill Bill application"
slug: "self-host-kill-bill"
excerpt: "Self-host Kill Bill open source application on Facets Control Plane"
hidden: true
createdAt: "2022-04-23T19:02:13.287Z"
updatedAt: "2023-02-14T18:50:39.392Z"
---
In this section, we will explore managed self-hosting of a popular open-source application -[ Kill Bill](https://killbill.io/overview). Kill Bill is an open-source, programmable platform that enables you to build custom billing solutions. 

First, we will study the Kill Bill services and model them in Facets Control Plane as a Blueprint and deploy it. Next, we will explore how deploying Kill Bill through Facets brings in additional manageability of the deployment. 

# Overview of Kill Bill

Kill bill consists of 3 components

1. A Kill Bill server that is a standalone java server. For our purpose, the Kill Bill server runs as a Kubernetes service. Kill Bill customers interact with the Kill Bill server through custom websites or internal tools using REST APIs.
2. kaui, a user interface to interact with Kill Bill. This will run as another Kubernetes service for us.
3. A MySQL relational database. In Facets we will model this to run as Aurora MySQL database on AWS.

## Architecture

The Kill Bill Architecture looks like this:

![](https://files.readme.io/f962853-KillBillImage.png "KillBillImage.png")



# Create Killbill Blueprint

As in previous examples, we first create a Blueprint for Kill Bill in the Facets Control Plane.  
In GitHub we will create the following new directory structure to add the Kill Bill Blueprint details.  As we did before, create a new directory named <code>killbill/</code> with the following directory structure.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/c5f090d-kb-fsstructure.png",
        "kb-fsstructure.png",
        310
      ],
      "align": "center",
      "caption": "Kill Bill Blueprint Directory Structure"
    }
  ]
}
[/block]

The following files remain unchanged from previous examples. So we can copy these files and place them in their respective folders.

1. <code>features.json</code>
2. <code>stack.json</code>
3. <code>sizing.GP.json</code>
4. <code>ingress.json</code>

## Modeling the killbill server

Create <code>killbill.json</code> with the following content. 

```json
{
  "$schema": "https://docs.facets.cloud/schemas/application/instances/application.schema",
  "kind": "facets.modules.common.application",
  "disabled": false,
  "apiVersion": "v2",
  "metadata": {
  },
  "spec": {
    "env": {
      "KILLBILL_DAO_PASSWORD": {
        "type": "credential",
        "permissionName": "killbill-permissions",
        "attribute": "password"
      },
      "KILLBILL_DAO_URL": {
        "type": "static",
        "default": "jdbc:mysql://killbill-db:3306/killbill?usePipelineAuth=false&useBatchMultiSend=false"
      },
      "KILLBILL_DAO_USER": {
        "type": "credential",
        "permissionName": "killbill-permissions",
        "attribute": "userName"
      }
    },
    "loadbalancing": {
      "rules": [
        {
          "ingress": "ingress",
          "path": "/",
          "portName": "port8080",
          "domainPrefix": "killbill"
        }
      ]
    },
    "permission": [
      {
        "name": "killbill-permissions",
        "resourceType": "mysql",
        "resourceName": "killbill-db",
        "permission": "READ_WRITE"
      }
    ],
    "release": {
      "strategy": "RollingUpdate",
      "build": {
        "image": "killbill/killbill:latest"
      }
    },
    "runtime": {
      "size": {
        "value": "small",
        "namespace": "GP"
      },
      "autoscaling": {
        "cpuThreshold": "50",
        "max": 1,
        "min": 1
      },
      "ports": [
        {
          "name": "port8080",
          "port": 8080
        }
      ]
    }
  }
}
```



There are 3 environment variables defined in the above json file, namely, <code>KILLBILL_DAO_PASSWORD</code>,<code>KILLBILL_DAO_URL</code> and <code>KILLBILL_DAO_USER</code>. These are used by the killbill server to access the database. 

> 📘 
> 
> Note that we have added a <code>domainPrefix</code> key in the load balancing rules. This is because both killbill and kaui listen on port <code>8080</code>. In order to expose both of these services using a single ingress controller, we add a domain prefix to each application. In this way, killbill will be reachable on <code>killbill.$ingress-url</code> and kaui will be reachable on <code>kaui.$ingress-url</code>.

## Modeling the kaui application

Create <code>kaui.json</code> with the following content. 

```json
{
  "$schema": "https://docs.facets.cloud/schemas/application/instances/application.schema",
  "kind": "facets.modules.common.application",
  "disabled": false,
  "apiVersion": "v2",
  "metadata": {
  },
  "spec": {
    "env": {
      "KAUI_CONFIG_DAO_PASSWORD": {
        "type": "credential",
        "permissionName": "killbill-permissions",
        "attribute": "password"
      },
      "KAUI_CONFIG_DAO_URL": {
        "type": "static",
        "default": "jdbc:mysql://killbill-db:3306/kaui?usePipelineAuth=false&useBatchMultiSend=false"
      },
      "KAUI_CONFIG_DAO_USER": {
        "type": "credential",
        "permissionName": "killbill-permissions",
        "attribute": "userName"
      },
      "KAUI_KILLBILL_URL": {
          "type": "static",
          "default": "http://killbill:8080"
      }
    },
    "loadbalancing": {
      "rules": [
        {
          "ingress": "ingress",
          "path": "/",
          "portName": "port8080",
          "domainPrefix": "kaui"
        }
      ]
    },
    "permission": [
      {
        "name": "killbill-permissions",
        "resourceType": "mysql",
        "resourceName": "killbill-db",
        "permission": "READ_WRITE"
      }
    ],
    "release": {
      "strategy": "RollingUpdate",
      "build": {
        "image": "killbill/kaui:latest"
      }
    },
    "runtime": {
      "size": {
        "value": "small",
        "namespace": "GP"
      },
      "autoscaling": {
        "cpuThreshold": "50",
        "max": 1,
        "min": 1
      },
      "metrics": [{"portName": "custom-metrics", "path":"/prometheus"}],
      "ports": [
        {
          "name": "port8080",
          "port": 8080
        }
      ]
    }
  }
}
```



Kaui uses 3 environment variables to connect to the database. These are <code>KAUI_CONFIG_DAO_PASSWORD</code>,<code>KAUI_CONFIG_DAO_URL</code>,and <code>KAUI_CONFIG_DAO_USER</code>. In Kaui also, we have used a <code>domainPrefix</code> key. 

## Modelling MySQL

Facets allows provision and configuring of database in a secure, standardized way along with disaster recovery in place.

1. Create an <code>aurora/</code> directory as shown in the directory structure and a file named <code>killbill-db.json</code> with the following content. 

```json
{
    "size": "S",
    "k8s_service_names": [
      "killbill-db"
    ],
    "k8s_slave_service_names": [],
    "name": "killbill-db",
    "schema_dir": "schema/airflow",
    "seed_data_dir": "seed_data/airflow",
    "views_dir" :  "database_views/airflow",
    "use_default_parameters": true,
    "mysqlConfigOverrides": {
      "explicit_defaults_for_timestamp" : "1"
    }
}
```



2. Create a file called <code>sizing.json</code> inside <code>aurora/</code> directory with the following content.

```json
{
    "S": {
      "class": "db.t3.small",
      "vcpu": 2,
      "memory": 2
    },
    "M": {
      "class": "db.t3.medium",
      "vcpu": 2,
      "memory": 4
    }
  }
```



3. Create a file called <code>disaster-recovery.json</code> inside <code>aurora/</code> directory and put the code in the link inside it. This is to take periodic backup of the database. 

```json
{
    "enabled": true,
    "creationStrategy": {
      "triggerSchedule": [
        "0 1 * * ? *"
      ]
    },
    "retentionStrategy": {
      "triggerSchedule": [
        "0 2 * * ? *"
      ],
      "rollingWindow": {
        "hours": 8,
        "days": 15,
        "weeks": 4,
        "months": 6
      },
      "incrementalWindow": {
      }
    }
  }
```



## Seed MySQL with initial schema

Since we are using a standalone instance of MySQL/Aurora, we need to seed the required schema and data into it before applications connect to the database. Facets runs the pre application deployment tasks as a Kubernetes job. In our case, we will create a python script that will connect to the MySQL database and run the SQL scripts provided by Kill bill. This will seed the MySQL database before Kill Bill and Kaui connect to it. Facets supports docker images that are run as jobs. Hence we will also dockerize our seed script and run it as a job. The code of this can be found :point-down: 


[block:embed]
{
  "html": false,
  "url": "https://github.com/Facets-cloud/sample-blueprints/tree/master/examples/killbill-facets/src",
  "title": "sample-blueprints/examples/killbill-facets/src at master · Facets-cloud/sample-blueprints",
  "favicon": "https://github.com/favicon.ico",
  "image": "https://opengraph.githubassets.com/1c2d71713894cb6a012b72bedbf2e1299889900a761236d5e26c5e6afaeb5c77/Facets-cloud/sample-blueprints",
  "provider": "github.com",
  "href": "https://github.com/Facets-cloud/sample-blueprints/tree/master/examples/killbill-facets/src"
}
[/block]




The content is organized as follows

| File Name                                                                                                                                    | Description                                                          |
| :------------------------------------------------------------------------------------------------------------------------------------------- | :------------------------------------------------------------------- |
| [<code>seed.py</code>](https://github.com/Facets-cloud/sample-blueprints/blob/master/examples/killbill-facets/src/seed.py)                   | The python job that will run as a one-time job to populate the MySQL |
| [<code>killbill-ddl.sql</code>](https://github.com/Facets-cloud/sample-blueprints/blob/master/examples/killbill-facets/src/killbill-ddl.sql) | The DDL required by <code>killbill</code> server                     |
| [<code>kaui-ddl.sql</code>](https://github.com/Facets-cloud/sample-blueprints/blob/master/examples/killbill-facets/src/kaui-ddl.sql)         | The DDL required by <code>kaui</code>                                |
| [<code>requirements.txt</code>](https://github.com/Facets-cloud/sample-blueprints/blob/master/examples/killbill-facets/requirements.txt)     | required Python Dependencies                                         |
| [<code>Dockerfile</code>](https://github.com/Facets-cloud/sample-blueprints/blob/master/examples/killbill-facets/Dockerfile)                 | <code>Dockerfile</code> to build the job                             |

Run the following command to create a docker image of the seed job. 

```shell
docker build -t killbill-seed-job:1 .
```



Create the <code>seed.json</code> as described in the blueprint directory structure with the following content.

```json
{
    "$schema": "https://docs.facets.cloud/schemas/jobs/instances/job.schema",
    "apiVersion": "v2",
    "disabled": false,
    "kind": "facets.modules.common.application.job",
    "metadata": {},
    "spec": {
        "env": {
            "MYSQL_PASSWORD": {
                "type": "credential",
                "permissionName": "killbill-permissions",
                "attribute": "password"
              },
              "MYSQL_HOST": {
                "type": "static",
                "default": "killbill-db"
              },
              "MYSQL_USER": {
                "type": "credential",
                "permissionName": "killbill-permissions",
                "attribute": "userName"
              }
        },
        "permission": [
            {
                "name": "killbill-permissions",
                "resourceType": "mysql",
                "resourceName": "killbill-db",
                "permission": "MANAGEMENT"
                
            }
        ],
        "release": {
            "build": {
                "image": "313496281593.dkr.ecr.us-east-1.amazonaws.com/facets/kb-facets/mysql:10"
            },
            "strategy": "RollingUpdate"
        },
        "runtime": {
            "size": {
                "value": "small"            }
        }
    }
}
```



The json consists of a permissions block which creates a user with management level permissions. This user credential is used to run the DDLs against the database.

Create the <code>sizing.json</code> for the seed job with the following content. 

```json
{
    "$schema": "https://docs.facets.cloud/schemas/application/sizing.schema",
  
    "small": {
      "podCPULimit": 2,
      "podMemoryLimit": 4
    }
}
```



# Putting everything together

Just like earlier examples

1. Push the above blueprint to a repository, say, <code>killbill</code> 
2. Register as a new blueprint named, say, <code>kb-facets</code>
3. Create a new environment from the Blueprint, Say <code>QA</code>
4. We fetch the latest images for kaui and killbill from the public docker hub repo and hence no need to push them to Facets console. The only artifact to be pushed additionally is the above mentioned seed job
5. Run the command <code>./facetsctl image push kb-facets mysql QA killbill-seed-job:1 1</code> from your local machine to push the seed job image to facets console.
6. From the Facets -> Artifacts tab, copy image URI for the seed job and modify the seed job json with build image as the copied uri
7. Push the changes to git repo.
8. Now Launch the environment. Once the release succeeds, you will be able to see kaui and killbill applications. 

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/3f77128-kb-apps.png",
        "kb-apps.png",
        512
      ],
      "align": "center",
      "caption": "Killbill apps on Facets"
    }
  ]
}
[/block]

# Access the Killbill application

Click on killbill in the Applications tab and then go to the Live Release tab. Copy the killbill ingress url.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/52b0476-killbill-ingress.png",
        "killbill-ingress.png",
        512
      ],
      "align": "center",
      "caption": "KillBill from Application details view"
    }
  ]
}
[/block]

Append <code>index.html</code> to the copied ingress url and access it using your browser. If you see a page like below, it means killbill is successfully deployed on Facets.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/cc94cf2-killbill-welcome.png",
        "killbill-welcome.png",
        512
      ],
      "align": "center",
      "caption": "Killbill running confirmation page"
    }
  ]
}
[/block]

Similarly, go back to the Applications tab in Facets Control Plane and now click on kaui. Copy the ingress url. Then, go to your browser and access the url. If you see the below Login page, it means Killbill is installed and accessible using kaui.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/7a8abce-killbill-login_page.png",
        "killbill-login page.png",
        512
      ],
      "align": "center",
      "caption": "KillBill Login Page"
    }
  ]
}
[/block]

The default username is <code>admin</code> and default password is <code>password</code>. Upon login, you should be able to see a page like this.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/e80d9f3-killbill-app_page.png",
        "killbill-app page.png",
        512
      ],
      "align": "center",
      "caption": "Kill Bill App running page"
    }
  ]
}
[/block]

This confirms that kaui is up and running and now explore the rest of the application as per your business requirements.

# Value-adds in Facets

Apart from a hosting platform, Facets also provides some out-of-the-box capabilities which help in better application logging, monitoring and implementing disaster recovery.

## Managed Backups

Facets provides the capability to configure backups for your database. This is achieved by creating and configuring a disaster-recovery.json inside the <code>aurora/</code> directory. This file dictates how the backups will be taken as well as the schedule for the backups. We have already included <code>disaster-recovery.json</code> file in our blueprint.

In order to see the backups, go to Backups from Facets console and in resource type choose aurora and in resource name choose your db service name. You should be able to see all the backups done for the killbill database

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/079e046-kb-aurora-backups.png",
        "kb-aurora-backups.png",
        512
      ],
      "align": "center",
      "caption": "KillBill Aurora backup details"
    }
  ]
}
[/block]

## Aurora mysql metrics

Alike applications, facets also projects aurora metrics to default inbuilt Grafana dashboards. These metrics are aurora specific and can be used to monitor the databases. In order to see the metrics, go to Tools from the facets console and choose the Mysql overview dashboard from built-in Grafana dashboards.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/c4a196f-kb-mysql-dashaboards.png",
        "kb-mysql-dashaboards.png",
        512
      ],
      "align": "center",
      "caption": "Kill Bill MySQL Dashboards"
    }
  ]
}
[/block]

## Log collection

Facets provides additional log collection capabilities which are accessible through a web-console logs for applications. Simply enable <code>logging_nfs</code> plugin in the top level <code>stack.json</code>

```json
{
    "$schema": "https://docs.facets.cloud/schemas/stack.schema",
    "componentVersions": {
        "KUBERNETES": "1.19"
    },
    "composition": {
        "plugins": [
            {
            "name": "logging_nfs"
            }
        ]
    },
    "clusterVariablesMeta": {}
}
```



After this, perform a release. Go to Tools section on Facets console and click on Logs tab as shown.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/789c054-kb-logs-tab.png",
        "kb-logs-tab.png",
        512
      ],
      "align": "center",
      "caption": "Logs Tab for the Kill Bill Blueprint"
    }
  ]
}
[/block]

Enter username and password as shown. You should be able to get a web console (similar to the image below) that has application / container logs in respective folders. 

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/5fbd00f-kb-wetty-logs.png",
        "kb-wetty-logs.png",
        512
      ],
      "align": "center",
      "caption": "Kill Bill Application Logs"
    }
  ]
}
[/block]