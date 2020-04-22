package com.capillary.ops.cp.facade;

import com.amazonaws.regions.Regions;
import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.StackFile;
import com.capillary.ops.cp.repository.CpClusterRepository;
import com.capillary.ops.cp.repository.StackRepository;
import com.capillary.ops.cp.service.ClusterHelper;
import com.capillary.ops.cp.service.factory.ClusterServiceFactory;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

public class ClusterFacadeTest {

    @Tested
    ClusterFacade clusterFacade;

    @Injectable
    private ClusterServiceFactory factory;

    @Injectable
    private CpClusterRepository cpClusterRepository;

    @Injectable
    private ClusterHelper clusterHelper;

    @Injectable
    private StackRepository stackRepository;

    @Test
    public void getCluster() {
       /* AwsClusterRequest request = new AwsClusterRequest();
        String clusterName = "Test";
        String stack = "crm";
        Regions region = Regions.US_EAST_1;

        request.setClusterName(clusterName);
        TimeZone tz = TimeZone.getDefault();
        request.setTz(tz);
        request.setStackName(stack);
        request.setRegion(region);

        AwsCluster cluster = new AwsCluster(clusterName);
        cluster.setStackName(stack);
        cluster.setAwsRegion(region.getName());
        cluster.setTz(tz);

        Map<String, String> map = new HashMap<>();
        map.put("Test1", "Val1");
        Stack crm = new Stack();
        crm.setStackVars(map);

        new Expectations() {

            {
                cpClusterRepository.findById(clusterName);
                result = Optional.of(cluster);
            }

            {
                stackRepository.findById(stack);
                result = Optional.of(crm);
            }
        };
        AbstractCluster cluster1 = clusterFacade.getCluster(clusterName);

        Map<String, String> commonEnvironmentVariables = cluster1.getCommonEnvironmentVariables();
        assert commonEnvironmentVariables.containsKey("TZ") && commonEnvironmentVariables.get("TZ")
            .equals(tz.getID());
        System.out.println(tz.getID());
        assert commonEnvironmentVariables.containsKey("AWS_REGION") && commonEnvironmentVariables.get("AWS_REGION")
            .equals(region.getName());
        assert commonEnvironmentVariables.containsKey("CLUSTER") && commonEnvironmentVariables.get("CLUSTER")
            .equals(clusterName);
        assert commonEnvironmentVariables.containsKey("Test1") && commonEnvironmentVariables.get("Test1")
            .equals("Val1");
        assert commonEnvironmentVariables.containsKey("envVar1") && commonEnvironmentVariables.get("envVar1")
            .equals("envVal");
        assert cluster1.getSecrets().containsKey("secret1") && cluster1.getSecrets().get("secret1")
            .equals("secret1Val1");*/
    }

}