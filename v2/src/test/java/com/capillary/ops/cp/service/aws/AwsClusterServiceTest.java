package com.capillary.ops.cp.service.aws;

import com.amazonaws.regions.Regions;
import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.cp.bo.BuildStrategy;
import com.capillary.ops.cp.bo.requests.AwsClusterRequest;
import com.capillary.ops.cp.bo.requests.Cloud;
import com.capillary.ops.cp.service.ComponentVersionService;
import mockit.Expectations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AwsClusterServiceTest {

    @InjectMocks
    private AwsClusterService awsClusterService;

    @Mock
    AwsAssumeRoleService awsAssumeRoleService;

    @Mock
    ComponentVersionService componentVersionService;

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
        when(componentVersionService.getClusterComponentVersions(anyString(), any())).thenReturn(new HashMap<>());
        when(awsAssumeRoleService.testRoleAccess(anyString(), anyString())).thenReturn(true);
        AwsCluster cluster = awsClusterService.createCluster(awsClusterRequest);

        assert cluster.getAwsRegion().equals(awsClusterRequest.getRegion().getName());
        assert cluster.getExternalId().equals(awsClusterRequest.getExternalId());
        assert cluster.getRoleARN().equals(awsClusterRequest.getRoleARN());
        assert cluster.getCloud().equals(Cloud.AWS);
        assert cluster.getStackName().equals(awsClusterRequest.getStackName());
        assert cluster.getName().equals(awsClusterRequest.getClusterName());
        assert cluster.getTz().equals(awsClusterRequest.getTz().getID());
        assert cluster.getReleaseStream().equals(awsClusterRequest.getReleaseStream());
    }

}