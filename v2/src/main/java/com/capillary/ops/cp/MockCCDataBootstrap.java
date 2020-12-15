package com.capillary.ops.cp;

import com.amazonaws.regions.Regions;
import com.capillary.ops.cp.bo.*;
import com.capillary.ops.cp.bo.requests.OverrideRequest;
import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.capillary.ops.cp.repository.*;
import com.capillary.ops.deployer.bo.User;
import com.capillary.ops.deployer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.ArrayList;
import java.util.List;

@Profile("dev")
@Component
public class MockCCDataBootstrap {

    @Autowired
    StackRepository stackRepository;

    @Autowired
    CpClusterRepository cpClusterRepository;

    @Autowired
    K8sCredentialsRepository k8sCredentialsRepository;

    @Autowired
    OverrideObjectRepository overrideObjectRepository;

    @Autowired
    DeploymentLogRepository deploymentLogRepository;

    @Autowired
    UserRepository userRepository;

    @PostConstruct
    private void init() {

        Stack stack = new Stack();
        stack.setName("crm");
        stack.setUser("ambar-cap");
        stack.setAppPassword("935d4d4ee673a9531a7d2241f1e9d1ddbbbf0ee1");
        stack.setRelativePath("/");
        stack.setVcs(VCS.BITBUCKET);
        stack.setVcsUrl("tmp");
        StackFile.VariableDetails v1 = new StackFile.VariableDetails(false,"test1");
        StackFile.VariableDetails v2 = new StackFile.VariableDetails(true,"test2");
        StackFile.VariableDetails v3 = new StackFile.VariableDetails(true,"test2");
        HashMap<String, StackFile.VariableDetails> vars = new HashMap<>();
        vars.put("cv1",v1);
        vars.put("cv2",v2);
        vars.put("cv3",v3);
        stack.setClusterVariablesMeta(vars);
        HashMap<String,String> stackVars = new HashMap<>();
        stackVars.put("sv4","v4");
        stackVars.put("sv5","v5");
        stack.setStackVars(stackVars);
        stack.setVcsUrl("https://github.com/Capillary/cc-stack-crm.git");
        stackRepository.save(stack);

        AwsCluster cluster = new AwsCluster("cluster1");
        cluster.setId("cluster1");
        cluster.setTz(TimeZone.getDefault());
        cluster.setAwsRegion(Regions.US_EAST_1.getName());
        cluster.setReleaseStream(BuildStrategy.QA);
        cluster.setStackName(stack.getName());
        cpClusterRepository.save(cluster);

        K8sCredentials x = new K8sCredentials();
        x.setClusterId(cluster.getName());
        x.setKubernetesApiEndpoint("https://BDFAE70546D35DF4713D053669C23B92.gr7.us-east-1.eks.amazonaws.com");
        x.setKubernetesToken(
            "eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZWZhdWx0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6InJlYWRvbmx5LXRva2VuLXRieGhrIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6InJlYWRvbmx5Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQudWlkIjoiYTQ1MzA3NDgtN2VlYi0xMWVhLTljMmYtMTIzOTk5YjQ2YTZiIiwic3ViIjoic3lzdGVtOnNlcnZpY2VhY2NvdW50OmRlZmF1bHQ6cmVhZG9ubHkifQ.5Bg0l_KiLEy8cGl7CCk4fdIitqHBQ0g3-xiUv8VIDu34mh0Bz43us2epbigZGlxZW8GgCeQhyhdbGKvCFplTCZCJDYo9DFLFHAy8nImzlbOH7Wdy2PmPnKopgqjkqEUwt0Z9PzPmE7pOCZ7OB5K8rqcOf_Kwe486YJjzEtsPj3JPzcb2RC6mHg63t9Hvq058mD4QDUX4OtMwbG0MTa2XiMqZIU0teDrcqpL7xU8094Oh7OXsAIjQ8FkR0Q6nkf1y7FlM6a_9YGr3Z_zihazu3TV4_rlzp7CZlKcKHSHMbJ5bEN6YdNwEvjVZ5R-2GYngh9WjBIwLGqkw7Pb-LQ1NNg");
        k8sCredentialsRepository.save(x);

        AwsCluster cluster2 = new AwsCluster("cluster2");

        cluster2.setId("cluster2");
        cluster2.setTz(TimeZone.getDefault());
        cluster2.setAwsRegion(Regions.US_WEST_2.getName());
        cluster2.setReleaseStream(BuildStrategy.PROD);
        cluster2.setStackName(stack.getName());
        //        cluster2.setSchedules(new HashMap<ReleaseType, String>() {{
        //            put(ReleaseType.HOTFIX, "* * * * *");
        //        }});
        cpClusterRepository.save(cluster2);

        OverrideObject request = new OverrideObject();
        request.setResourceType("application");
        request.setClusterId("cluster1");
        request.setResourceName("intouch-api");
        request.setOverrides(new HashMap<String, Object>() {{
            put("size", "XLARGE");
        }});
        overrideObjectRepository.save(request);

        OverrideObject request2 = new OverrideObject();
        request2.setResourceType("application");
        request2.setClusterId("cluster1");
        request2.setResourceName("emf");
        request2.setOverrides(new HashMap<String, Object>() {{
            put("size", "XLARGE");
        }});
        overrideObjectRepository.save(request2);

        DeploymentLog deploymentLog = new DeploymentLog();
        deploymentLog.setCodebuildId("cb007");
        deploymentLog.setClusterId("cluster1");
        deploymentLog.setCreatedOn(new Date());
        deploymentLog.setReleaseType(ReleaseType.HOTFIX);
        deploymentLog.setDescription("Hotfix for CAP-XYZ");
        deploymentLogRepository.save(deploymentLog);

        DeploymentLog deploymentLog2 = new DeploymentLog();
        deploymentLog2.setCodebuildId("cb008");
        deploymentLog2.setClusterId("cluster1");
        deploymentLog2.setCreatedOn(new Date());
        deploymentLog2.setReleaseType(ReleaseType.RELEASE);
        deploymentLog2.setDescription("Release for Sprint X");
        deploymentLogRepository.save(deploymentLog2);

        User user = new User();
        String adminUser = System.getenv("ADMIN_USER");
        user.setUserName(adminUser);
        List<String> roles = new ArrayList<>();
        roles.add("CRM_WRITE");
        user.setRoles(roles);
        userRepository.save(user);
    }

}
