---
title: "A Flask App with MongoDB"
slug: "deploy-a-simple-flask-application"
hidden: true
metadata: 
  title: "How to Deploy a Flask App in Facets DevOps Platform"
  description: "This is a complete step by step guide to deploy a basic flask based python application on facets.cloud DevOps Platform"
createdAt: "2022-03-30T15:11:09.151Z"
updatedAt: "2023-02-14T18:50:19.644Z"
---
This is a complete step by step guide to deploy a basic flask based python application on facets.cloud. After the successful deployment, you should be able to access the application from the outside world.

# Step 1: Create a simple flask application

We have a very simple flask application written in python. The application currently has two routes configured.

1. <code>/</code> returns a message <code>Hello World</code> and 
2. <code>/healthz</code> returns an empty string. This route is configured to implement liveness checks for the application. :point-down: 

```python
from flask import Flask
app = Flask(__name__)
@app.route('/')
def hello_world():
    return 'Hello World'
@app.route('/healthz')
def healthz():
    return ''
# main driver function
if __name__ == '__main__':
    app.run(host='0.0.0.0')
```



1. Create a directory named <code>src/</code> and inside this directory create a file named <code>app.py</code>. Paste the above code it in <code>app.py</code>. 

2. Create another file named <code>requirements.txt</code> at the same level as <code>src/</code> directory and put all the required application dependencies in it. For this application we only require flask.

# Step 2: Dockerize the application

In order to deploy applications on Facets, the applications need to be containerized. For this we need to create images for various components of our application. For our demo application, we only have one component and hence we will create one docker image for it.

Create a file named <code>Dockerfile</code> at the same level as <code>src/</code> directory.  
Place the following content in the <code>Dockerfile</code>.

```dockerfile
# Stage 1 - Install build dependencies
FROM python:3.7-alpine AS builder
WORKDIR /app
RUN python -m venv .venv && .venv/bin/pip install --no-cache-dir -U pip setuptools
COPY requirements.txt .
RUN .venv/bin/pip install --no-cache-dir -r requirements.txt && find /app/.venv \( -type d -a -name test -o -name tests \) -o \( -type f -a -name '*.pyc' -o -name '*.pyo' \) -exec rm -rf '{}' \+
# Stage 2 - Copy only necessary files to the runner stage
FROM python:3.7-alpine
RUN apk add --update --no-cache bind-tools
WORKDIR /app
COPY --from=builder /app /app
COPY src/ .
ENV PATH="/app/.venv/bin:$PATH"
CMD ["python", "app.py"]
```



Build  and tag the docker image by running the following command in the same directory where you created <code>Dockerfile</code>.

```shell
docker build -t facets-python-demo:1 .
```