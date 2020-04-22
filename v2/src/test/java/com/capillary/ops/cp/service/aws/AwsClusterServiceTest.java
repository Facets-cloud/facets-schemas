package com.capillary.ops.cp.service.aws;

import com.amazonaws.regions.Regions;
import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.cp.bo.BuildStrategy;
import com.capillary.ops.cp.bo.requests.AwsClusterRequest;
import com.capillary.ops.cp.bo.requests.Cloud;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Test;

import java.util.ArrayList;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class AwsClusterServiceTest {

    @Tested
    private AwsClusterService awsClusterService;

    @Injectable
    AwsAssumeRoleService awsAssumeRoleService;

    @Test
    public void testSimpleAWSCreate(){
        AwsClusterRequest awsClusterRequest = new AwsClusterRequest();
        awsClusterRequest.setRegion(Regions.US_EAST_1);
        awsClusterRequest.setAzs(new ArrayList<String>(){{
            add("us-east-1a");
        }});
        awsClusterRequest.setExternalId("123");
        awsClusterRequest.setClusterName("clusterName");
        awsClusterRequest.setRoleARN("roleARN");
        awsClusterRequest.setStackName("crm");
        awsClusterRequest.setTz(TimeZone.getDefault());
        awsClusterRequest.setReleaseStream(BuildStrategy.QA);

        new Expectations(){
            {
                awsAssumeRoleService.testRoleAccess(anyString, anyString);
                result = true;
            }

        };
        AwsCluster cluster = awsClusterService.createCluster(awsClusterRequest);

        assert cluster.getAwsRegion().equals(awsClusterRequest.getRegion().getName());
        assert cluster.getExternalId().equals(awsClusterRequest.getExternalId());
        assert cluster.getRoleARN().equals(awsClusterRequest.getRoleARN());
        assert cluster.getVpcCIDR() != null;
        assert cluster.getCloud().equals(Cloud.AWS);
        assert cluster.getStackName().equals(awsClusterRequest.getStackName());
        assert cluster.getName().equals(awsClusterRequest.getClusterName());
        assert cluster.getTz().equals(awsClusterRequest.getTz().getID());
        assert cluster.getReleaseStream().equals(awsClusterRequest.getReleaseStream());
    }

}