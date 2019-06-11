package com.capillary.ops.deployer.service.mocks;

import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.repository.ApplicationRepository;
import com.capillary.ops.deployer.repository.BuildRepository;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Profile("dev")
@Component
public class EcomMockDataBootstrap {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private BuildRepository buildRepository;

    @PostConstruct
    private void init() {
        Application application = createApplication();
        createBuild(application);
    }

    private void createBuild(Application application) {
        Build build = new Build();
        build.setApplicationId(application.getId());
        build.setCodeBuildId(UUID.randomUUID().toString());
        build.setImage(UUID.randomUUID().toString());
        build.setStatus(StatusType.SUCCEEDED);
        build.setTriggeredBy("admin");
        build.setEnvironmentVariables(ImmutableMap.of("key", "value"));
        build.setDescription("test build");
        build.setTag("tag/1.0");
        build.setTimestamp(System.currentTimeMillis());
        buildRepository.save(build);
    }

    private Application createApplication() {
        Application application = new Application();
        application.setApplicationFamily(ApplicationFamily.ECOMMERCE);
        application.setApplicationRootDirectory("v2");
        application.setName("deployer");
        application.setVcsProvider(VCSProvider.BITBUCKET);
        application.setRepositoryUrl("https://bitbucket.org/capillarymartjack/deisdeployer.git");
        application.setBuildType(BuildType.MVN);
        application.setLoadBalancerType(LoadBalancerType.INTERNAL);
        application.setPorts(new ArrayList<>(getPorts()));

        return applicationRepository.save(application);
    }

    private List<Port> getPorts() {
        Port port1 = new Port();
        port1.setName("http");
        port1.setContainerPort(8080L);
        port1.setLbPort(80L);

        Port port2 = new Port();
        port2.setName("http");
        port2.setContainerPort(8080L);
        port2.setLbPort(80L);

        return Lists.newArrayList(port1, port2);
    }
}
