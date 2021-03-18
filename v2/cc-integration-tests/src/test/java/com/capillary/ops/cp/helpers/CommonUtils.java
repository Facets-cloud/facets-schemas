package com.capillary.ops.cp.helpers;

import com.capillary.ops.cp.exceptions.ClusterDetailsNotFound;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Optional;

@Service
public class CommonUtils {

    private final String DEPLOYMENT_CONTEXT_PATH = "../../capillary-cloud-tf/deploymentcontext.json";

    public JsonObject getDeploymentContext() throws Exception {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(DEPLOYMENT_CONTEXT_PATH));
        return parser.parse(bufferedReader).getAsJsonObject();
    }
}
