package com.capillary.ops.deployer.service.mocks;

import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.repository.*;
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
public class MockDataBootstrap {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private BuildRepository buildRepository;

    @Autowired
    private EnvironmentRepository environmentRepository;

    @Autowired
    ApplicationSecretsRepository applicationSecretsRepository;

    @Autowired
    ApplicationSecretRequestsRepository applicationSecretRequestsRepository;

    @PostConstruct
    private void init() {
        createEnvironment("nightly", EnvironmentType.QA, ApplicationFamily.ECOMMERCE);
        createEnvironment("stage", EnvironmentType.QA, ApplicationFamily.ECOMMERCE);
        createEnvironment("sg", EnvironmentType.PRODUCTION, ApplicationFamily.ECOMMERCE);
        createEnvironment("eu", EnvironmentType.PRODUCTION, ApplicationFamily.ECOMMERCE);

        createEnvironment("nightly", EnvironmentType.QA, ApplicationFamily.CRM);

        createApplication("intouch-api", ApplicationFamily.CRM);
        createApplication("api-rate-limiter", ApplicationFamily.CRM);
        createApplication("emf", ApplicationFamily.CRM);

        createApplication("catalog-service", ApplicationFamily.ECOMMERCE);
        createApplication("catalog", ApplicationFamily.ECOMMERCE);
        createApplication("navigation-panel-app", ApplicationFamily.ECOMMERCE);
        createApplication("fileservice", ApplicationFamily.ECOMMERCE);

        for(int i=0; i < 100; i++) {
            createApplication("some-integration-app" + String.valueOf(i+1), ApplicationFamily.INTEGRATIONS);
        }
        createApplication("some-integration-api", ApplicationFamily.INTEGRATIONS);
        createApplication("some-integration-app", ApplicationFamily.INTEGRATIONS);
        createApplication("some-integration-india", ApplicationFamily.INTEGRATIONS);

        createApplication("deployer", ApplicationFamily.OPS);

    }

    private void createEnvironment(String name, EnvironmentType type, ApplicationFamily applicationFamily) {
        Environment nightly = new Environment();
        EnvironmentMetaData nightlyMetaData = new EnvironmentMetaData();
        nightlyMetaData.setApplicationFamily(applicationFamily);
        nightlyMetaData.setName(name);
        nightlyMetaData.setEnvironmentType(type);
        nightly.setEnvironmentMetaData(nightlyMetaData);
        environmentRepository.save(nightly);
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

    private Application createApplication(String name, ApplicationFamily applicationFamily) {
        Application application = new Application();
        application.setApplicationRootDirectory("v2");
        application.setApplicationFamily(applicationFamily);
        application.setName(name);
        application.setVcsProvider(VCSProvider.GITHUB);
        application.setRepositoryUrl("https://github.com/Capillary/api.git");
        application.setBuildType(BuildType.MVN);
        application.setLoadBalancerType(LoadBalancerType.INTERNAL);
        application.setPorts(new ArrayList<>(getPorts()));
        Application result = applicationRepository.save(application);
        createBuild(result);
        ApplicationSecretRequest request = new ApplicationSecretRequest();
        request.setSecretName("DB_PASSWORD");
        request.setDescription("DB PASSWORD");
        request.setApplicationFamily(applicationFamily);
        request.setApplicationId(application.getId());
        applicationSecretRequestsRepository.save(request);
        applicationSecretsRepository.save(new ApplicationSecret("nightly", applicationFamily, application.getId(),
                request.getSecretName(), "somevalue", ApplicationSecret.SecretStatus.FULFILLED));
        return result;
    }

    private List<Port> getPorts() {
        Port port1 = new Port();
        port1.setName("http");
        port1.setContainerPort(1900L);
        port1.setLbPort(80L);

        Port port2 = new Port();
        port2.setName("thrift");
        port2.setContainerPort(9999L);
        port2.setLbPort(9999L);

        return Lists.newArrayList(port1, port2);
    }
}
