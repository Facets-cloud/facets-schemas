package com.capillary.ops.constants;

public class CodeBuildConstants {

    //TODO get the constants from mongo

    public static final String IMAGE_PATH = "486456986266.dkr.ecr.us-east-1.amazonaws.com/mavendocker:v1.4";
    public static final String CACHE_BUCKET = "capbuildcache";
    public static final String SERVICE_ROLE = "cap-codebuild-role";
    public static final Integer TIMEOUT = 180;
    public static final String CLOUDWATCH_GROUP = "codebuild-test";
}
