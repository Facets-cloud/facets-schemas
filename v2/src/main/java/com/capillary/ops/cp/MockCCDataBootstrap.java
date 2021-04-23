package com.capillary.ops.cp;

import com.amazonaws.regions.Regions;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.*;
import com.capillary.ops.cp.bo.components.ComponentType;
import com.capillary.ops.cp.bo.notifications.ChannelType;
import com.capillary.ops.cp.bo.notifications.NotificationType;
import com.capillary.ops.cp.bo.notifications.Subscription;
import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.capillary.ops.cp.bo.user.Role;
import com.capillary.ops.cp.repository.*;
import com.capillary.ops.cp.service.ClusterHelper;
import com.capillary.ops.deployer.bo.User;
import com.capillary.ops.deployer.repository.UserRepository;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import com.capillary.ops.cp.bo.AutoCompleteObject;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import javax.annotation.PostConstruct;
import java.util.*;

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

    @Autowired
    AutoCompleteObjectRepository autoCompleteObjectRepository;

    @Autowired
    ClusterResourceDetailsRepository clusterResourceDetailsRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    TeamMembershipRepository teamMembershipRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    ArtifactoryRepository artifactoryRepository;

    @PostConstruct
    private void init() {
        Stack stack = new Stack();
        stack.setName("crm");
        stack.setUser("ambar-cap");
        stack.setAppPassword("935d4d4ee673a9531a7d2241f1e9d1ddbbbf0ee1");
        stack.setRelativePath("/");
        stack.setVcs(VCS.GITHUB);
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
        Map<ComponentType, String> stackComponentVersion = new HashMap<>();
        stackComponentVersion.put(ComponentType.KUBERNETES, "1.17");
        stack.setComponentVersions(stackComponentVersion);
        stackRepository.save(stack);

        Stack stackTesting = new Stack();
        stackTesting.setName("cc-stack-cctesting");
        stackTesting.setUser("ambar-cap");
        stackTesting.setAppPassword("935d4d4ee673a9531a7d2241f1e9d1ddbbbf0ee1");
        stackTesting.setRelativePath("/");
        stackTesting.setVcs(VCS.GITHUB);
        stackTesting.setVcsUrl("tmp");
        StackFile.VariableDetails v1Testing = new StackFile.VariableDetails(false,"test1");
        StackFile.VariableDetails v2Testing = new StackFile.VariableDetails(true,"test2");
        StackFile.VariableDetails v3Testing = new StackFile.VariableDetails(true,"test2");
        HashMap<String, StackFile.VariableDetails> varsTesting = new HashMap<>();
        varsTesting.put("cv1",v1Testing);
        varsTesting.put("cv2",v2Testing);
        varsTesting.put("cv3",v3Testing);
        stackTesting.setClusterVariablesMeta(varsTesting);
        HashMap<String,String> stackVarsTesting = new HashMap<>();
        stackVarsTesting.put("sv4","v4");
        stackVarsTesting.put("sv5","v5");
        stackTesting.setStackVars(stackVarsTesting);
        stackTesting.setVcsUrl("https://github.com/Capillary/cc-stack-cctesting.git");
        stackTesting.setComponentVersions(stackComponentVersion);
        stackRepository.save(stackTesting);

        AutoCompleteObject apps = new AutoCompleteObject("crm","application");
        AutoCompleteObject crons = new AutoCompleteObject("crm","cronjob");
        AutoCompleteObject statefulsets = new AutoCompleteObject("crm","statefulsets");
        apps.setResourceNames(new HashSet<>(Arrays.asList("intouch-api", "emf")));
        crons.setResourceNames(new HashSet<>(Arrays.asList("crondemo-one", "crondemo-two")));
        statefulsets.setResourceNames(new HashSet<>(Arrays.asList("sts-demo", "sts-demo-two")));
        List<AutoCompleteObject> autoCompleteObjects = new ArrayList<>();
        autoCompleteObjects.add(apps);
        autoCompleteObjects.add(crons);
        autoCompleteObjects.add(statefulsets);
        autoCompleteObjectRepository.saveAll(autoCompleteObjects);

        AutoCompleteObject appsTesting = new AutoCompleteObject("cc-stack-cctesting","application");
        AutoCompleteObject cronsTesting = new AutoCompleteObject("cc-stack-cctesting","cronjob");
        AutoCompleteObject statefulsetsTesting = new AutoCompleteObject("cc-stack-cctesting","statefulsets");
        appsTesting.setResourceNames(new HashSet<>(Arrays.asList("intouch-api", "emf")));
        cronsTesting.setResourceNames(new HashSet<>(Arrays.asList("crondemo-one", "crondemo-two")));
        statefulsetsTesting.setResourceNames(new HashSet<>(Arrays.asList("sts-demo", "sts-demo-two")));
        List<AutoCompleteObject> autoCompleteObjectsTesting = new ArrayList<>();
        autoCompleteObjectsTesting.add(appsTesting);
        autoCompleteObjectsTesting.add(cronsTesting);
        autoCompleteObjectsTesting.add(statefulsetsTesting);
        autoCompleteObjectRepository.saveAll(autoCompleteObjectsTesting);

        ClusterResourceDetails cd = new ClusterResourceDetails();
        cd.setClusterId("cluster1");
        cd.setStatus(StatusType.SUCCEEDED);
        ResourceDetails rd = new ResourceDetails();
        rd.setName(ClusterHelper.TOOLS_NAME_IN_RESOURCES);
        rd.setResourceType("ingress");
        rd.setResourceName(ClusterHelper.TOOLS_PASS_KEY_IN_RESOURCES);
        rd.setKey(ClusterHelper.TOOLS_PASS_KEY_IN_RESOURCES);
        rd.setValue("NfWaKLiPZt");
        cd.setResourceDetails(Collections.singletonList(rd));
        clusterResourceDetailsRepository.save(cd);

        AwsCluster cluster = new AwsCluster("crm-staging-new");
        cluster.setId("cluster1");
        cluster.setTz(TimeZone.getDefault());
        cluster.setAwsRegion(Regions.US_EAST_1.getName());
        cluster.setReleaseStream(BuildStrategy.QA);
        cluster.setStackName(stack.getName());
        Map<ComponentType, String> componentVersion = new HashMap<>();
        componentVersion.put(ComponentType.KUBERNETES, "1.15");
        cluster.setComponentVersions(componentVersion);
        cpClusterRepository.save(cluster);

        AwsCluster clusterEksUpgrade = new AwsCluster("eks-upgrade");
        clusterEksUpgrade.setId("eks-upgrade");
        clusterEksUpgrade.setTz(TimeZone.getDefault());
        clusterEksUpgrade.setAwsRegion(Regions.US_EAST_1.getName());
        clusterEksUpgrade.setReleaseStream(BuildStrategy.QA);
        clusterEksUpgrade.setStackName(stackTesting.getName());
        clusterEksUpgrade.setComponentVersions(componentVersion);
        cpClusterRepository.save(clusterEksUpgrade);

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
        cluster2.setComponentVersions(componentVersion);
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
        roles.add("CC-ADMIN");
        roles.add(Role.K8S_READER.getId());
        user.setRoles(roles);
        userRepository.save(user);

        Team team = new Team();
        team.setName("foundation");
        team.setResources(new HashSet(Arrays.asList(new TeamResource("crm", "application", "emf"))));
        team.setNotificationChannels(ImmutableMap.of(ChannelType.FLOCK, "https://dummy"));
        teamRepository.save(team);
        teamMembershipRepository.save(new TeamMembership(team.getId(), adminUser));

        Subscription subscription = new Subscription();
        subscription.setNotificationType(NotificationType.ALERT);
        subscription.setStackName("crm");
        subscription.setChannelType(ChannelType.FLOCK);
        subscription.setChannelAddress("https://api.flock.com/hooks/sendMessage/9f986e4b-8e7f-462a-9479-f9f1b716cfb0");
        subscription.setNotificationSubject(Subscription.ALL);
        subscriptionRepository.save(subscription);


        ECRArtifactory repo = new ECRArtifactory();
        repo.setAwsAccountId("accountId");
        repo.setAwsKey("key");
        repo.setAwsRegion("region");
        repo.setAwsSecret("secret");
        repo.setId("id1");
        repo.setName("name");
        repo.setUri("uri");
        artifactoryRepository.save(repo);
    }

}
