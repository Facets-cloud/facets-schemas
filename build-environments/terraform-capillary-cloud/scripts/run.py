import os
import json
import boto3
import yaml
import glob
import socket
import time
import botocore

def executeShell(cmd):
    print("Running: " + cmd)
    if os.system(cmd) != 0:
        raise Exception("command failed: " + cmd)


def main():
    executeShell("mkdir -p /sources")
    executeShell("mkdir -p /scripts")
    gitsources = json.loads(open("/configs/gitsources.json").read())
    s3sources = json.loads(open("/configs/s3sources.json").read())
    for i in gitsources:
        executeShell("git clone --depth=1 " + i["uri"].replace("//", "//" + i["username"] + ":" + i[
            "password"] + "@") + " /sources/" + i["name"].lower())
        executeShell("git --git-dir " + "/sources/" + i["name"].lower() + "/.git " + " fetch origin " + i[
            "version"] + ":workbrnach --depth=1")
        executeShell("git --git-dir " + "/sources/" + i["name"].lower() + "/.git --work-tree=" + "/sources/" + i[
            "name"].lower() + " checkout workbrnach")
        os.environ["CODEBUILD_SRC_DIR_" + i["name"]] = "/sources/" + i["name"].lower()
    s3 = boto3.client('s3')

    for i in s3sources:
        if ("accessKey" in i) and ("secret" in i) and ("region" in i):
            s3_with_key = boto3.client('s3',
                                aws_access_key_id=i["accessKey"],
                                aws_secret_access_key=i["secret"],
                                region_name=i["region"])
            if not os.path.exists("/sources/" + os.path.dirname(i["key"])):
                os.makedirs(os.path.dirname("/sources/" + i["key"]))
            s3_with_key.download_file(i["bucketName"], i["key"], "/sources/" + i["key"])
        else:
            if not os.path.exists("/sources/" + os.path.dirname(i["key"])):
                os.makedirs(os.path.dirname("/sources/" + i["key"]))
            s3.download_file(i["bucketName"], i["key"], "/sources/" + i["key"])
        executeShell("unzip -qq " + "/sources/" + i["key"] + " -d " + "/sources/" + i["name"].lower())
        os.environ["CODEBUILD_SRC_DIR_" + i["name"]] = "/sources/" + i["name"].lower()
    for i in os.environ:
        if i.startswith("KUBERNETES_"):
            print(i)
            os.environ.unsetenv(i)
    buildspec = yaml.load(open("/configs/buildspec.yaml"), Loader=yaml.FullLoader)
    os.chdir("/sources/primary")
    with open("/scripts/build.sh", "w") as f:
        f.writelines(["#!/bin/bash -e\n"])
        f.writelines("%s\n" % l for l in buildspec["phases"]["pre_build"]["commands"])
        f.writelines("%s\n" % l for l in buildspec["phases"]["build"]["commands"])
        f.writelines("%s\n" % l for l in buildspec["phases"]["post_build"]["commands"])
    executeShell("chmod +x /scripts/build.sh")
    try:
        executeShell("/scripts/build.sh")
    except Exception as e:
        raise e
    finally:
        for i in buildspec["artifacts"]["files"]:
            for j in glob.glob(os.path.expandvars("/sources/primary/" + i)):
                print("Uploading " + j)
                artifact_bucket = os.environ["ARTIFACT_BUCKET"]
                s3.upload_file(j, artifact_bucket, socket.gethostname() + "/" + j.replace("/sources/primary/", ""))


if __name__ == '__main__':
    # Retry once for the creds error
    try:
        main()
    except botocore.exceptions.NoCredentialsError as e:
        time.sleep(10)
        main()
