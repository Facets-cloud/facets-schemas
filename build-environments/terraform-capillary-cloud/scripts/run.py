import os
import json
import boto3
import yaml
import glob
import socket
import time
import botocore

USE_MINIO = os.getenv("USE_MINIO", False)
MINIO_ENDPOINT = os.getenv("MINIO_ENDPOINT")
MINIO_ACCESS_KEY = os.getenv("MINIO_ACCESS_KEY")
MINIO_SECRET_KEY = os.getenv("MINIO_SECRET_KEY")

class FacetsRun:
    def __init__(self):
        self.s3 = boto3.client('s3') if not USE_MINIO else boto3.client('s3', 
        endpoint_url=MINIO_ENDPOINT, aws_access_key_id=MINIO_ACCESS_KEY, aws_secret_access_key=MINIO_SECRET_KEY)
        self.gitsources = json.loads(open("/configs/gitsources.json").read())
        self.s3sources = json.loads(open("/configs/s3sources.json").read())
        self.buildspec = yaml.load(open("/configs/buildspec.yaml"), Loader=yaml.FullLoader)

    def execute_shell(self, cmd):
        print("Running: " + cmd)
        if os.system(cmd) != 0:
            raise Exception("Command failed: " + cmd)
        return cmd
        
    def get_s3_client(self, s3source):
        if ("accessKey" in s3source) and ("secret" in s3source) and ("region" in s3source):
            return boto3.client('s3',
                                aws_access_key_id=s3source["accessKey"],
                                aws_secret_access_key=s3source["secret"],
                                region_name=s3source["region"])
        else:
            if USE_MINIO:
                return boto3.client('s3',
                                    endpoint_url = MINIO_ENDPOINT,
                                    aws_access_key_id=MINIO_ACCESS_KEY,
                                    aws_secret_access_key=MINIO_SECRET_KEY)
            else:
                return boto3.client('s3')

    def setup_git_sources(self):
        for i in self.gitsources:
            self.execute_shell("git clone --depth=1 " + i["uri"].replace("//", "//" + i["username"] + ":" + i["password"] + "@") + " /sources/" + i["name"].lower())
            self.execute_shell("git --git-dir " + "/sources/" + i["name"].lower() + "/.git " + " fetch origin " + i["version"] + ":workbrnach --depth=1")
            self.execute_shell("git --git-dir " + "/sources/" + i["name"].lower() + "/.git --work-tree=" + "/sources/" + i["name"].lower() + " checkout workbrnach")
            os.environ["CODEBUILD_SRC_DIR_" + i["name"]] = "/sources/" + i["name"].lower()

    def download_s3_sources(self):
        for i in self.s3sources:
            s3_with_key = self.get_s3_client(i)
            if not os.path.exists("/sources/" + os.path.dirname(i["key"])):
                os.makedirs(os.path.dirname("/sources/" + i["key"]))
            s3_with_key.download_file(i["bucketName"], i["key"], "/sources/" + i["key"])
            self.execute_shell("unzip -qq " + "/sources/" + i["key"] + " -d " + "/sources/" + i["name"].lower())
            os.environ["CODEBUILD_SRC_DIR_" + i["name"]] = "/sources/" + i["name"].lower()

    def configure_kubernetes_environment(self):
        for i in os.environ:
            if i.startswith("KUBERNETES_"):
                print(i)
                os.environ.pop(i)

    def create_build_script(self, buildspec):
        os.chdir("/sources/primary")
        with open("/scripts/build.sh", "w") as f:
            f.writelines(["#!/bin/bash -e\n"])
            f.writelines("%s\n" % l for l in buildspec["phases"]["pre_build"]["commands"])
            f.writelines("%s\n" % l for l in buildspec["phases"]["build"]["commands"])
            f.writelines("%s\n" % l for l in buildspec["phases"]["post_build"]["commands"])
        self.execute_shell("chmod +x /scripts/build.sh")

    def run_build_script(self):
        try:
            self.execute_shell("/scripts/build.sh")
        except Exception as e:
            raise e
        finally:
            self.upload_build_artifacts(self.buildspec)

    def upload_build_artifacts(self, buildspec):
        for i in buildspec["artifacts"]["files"]:
            for j in glob.glob(os.path.expandvars("/sources/primary/" + i)):
                print("Uploading " + j)
                artifact_bucket = os.environ["ARTIFACT_BUCKET"]
                self.s3.upload_file(j, artifact_bucket, socket.gethostname() + "/" + j.replace("/sources/primary/", ""))

    def run(self):
        try:
            self.execute_shell("mkdir -p /sources")
            self.execute_shell("mkdir -p /scripts")

            self.setup_git_sources()
            self.download_s3_sources()
            self.configure_kubernetes_environment()

            
            self.create_build_script(self.buildspec)
            self.run_build_script()
            self.upload_build_artifacts(self.buildspec)

        except botocore.exceptions.NoCredentialsError as e:
            time.sleep(10)
            self.run()


if __name__ == '__main__':
    code_build_executor = FacetsRun()
    code_build_executor.run()