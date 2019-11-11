package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.service.interfaces.IIAMService;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.IamClientBuilder;
import software.amazon.awssdk.services.iam.model.ListRolesRequest;
import software.amazon.awssdk.services.iam.model.ListRolesResponse;
import software.amazon.awssdk.services.iam.model.Role;

import java.util.ArrayList;
import java.util.List;

public class IAMServiceUnitTest {

    @Tested
    private IAMService iamService;

    @Mocked
    private IamClient iamClient;

    @Mocked
    private IamClientBuilder iamClientBuilder;

    @Test
    public void getApplicationIAMRole(){

        List<ListRolesRequest> requests = new ArrayList<>();
        new Expectations() {
            {
                iamClientBuilder.build();
                result = iamClient;
            }
            {
                iamClient.listRoles((ListRolesRequest) withCapture(requests));
                result = ListRolesResponse.builder().roles(Role.builder().roleName("myapp-qa").arn("somearn").build()).build();
            }
        };

        Environment environment = new Environment();
        EnvironmentMetaData environmentMetaData = new EnvironmentMetaData();
        environmentMetaData.setName("qa");
        environment.setEnvironmentMetaData(environmentMetaData);
        EnvironmentConfiguration environmentConfiguration = new EnvironmentConfiguration();
        environmentConfiguration.setKube2IamConfiguration(new Kube2IamConfiguration(true, "", ""));
        environment.setEnvironmentConfiguration(environmentConfiguration);
        Application application = new Application();
        application.setName("myapp");
        application.setApplicationFamily(ApplicationFamily.CRM);
        String applicationRole = iamService.getApplicationRole(application, environment);
        Assert.assertEquals("somearn", applicationRole);
        Assert.assertEquals("/deployer/crm", requests.get(0).pathPrefix());
    }

    @Test
    public void getApplicationIAMRoleFail(){

        List<ListRolesRequest> requests = new ArrayList<>();
        new Expectations() {
            {
                iamClientBuilder.build();
                result = iamClient;
            }
            {
                iamClient.listRoles((ListRolesRequest) withCapture(requests));
                result = ListRolesResponse.builder().roles(Role.builder().roleName("myapp-qa2").arn("somearn").build()).build();
            }
        };

        Environment environment = new Environment();
        EnvironmentMetaData environmentMetaData = new EnvironmentMetaData();
        environmentMetaData.setName("qa");
        environment.setEnvironmentMetaData(environmentMetaData);
        EnvironmentConfiguration environmentConfiguration = new EnvironmentConfiguration();
        environmentConfiguration.setKube2IamConfiguration(new Kube2IamConfiguration(true, "", ""));
        environment.setEnvironmentConfiguration(environmentConfiguration);
        Application application = new Application();
        application.setName("myapp");
        application.setApplicationFamily(ApplicationFamily.CRM);
        String applicationRole = iamService.getApplicationRole(application, environment);
        Assert.assertEquals(null, applicationRole);
        Assert.assertEquals("/deployer/crm", requests.get(0).pathPrefix());
    }

    @Test
    public void getApplicationIAMRoleFailWithException(){

        List<ListRolesRequest> requests = new ArrayList<>();
        new Expectations() {
            {
                iamClientBuilder.build();
                result = iamClient;
            }
            {
                iamClient.listRoles((ListRolesRequest) withCapture(requests));
                result = new RuntimeException();
            }
        };

        Environment environment = new Environment();
        EnvironmentMetaData environmentMetaData = new EnvironmentMetaData();
        environmentMetaData.setName("qa");
        environment.setEnvironmentMetaData(environmentMetaData);
        EnvironmentConfiguration environmentConfiguration = new EnvironmentConfiguration();
        environmentConfiguration.setKube2IamConfiguration(new Kube2IamConfiguration(true, "", ""));
        environment.setEnvironmentConfiguration(environmentConfiguration);
        Application application = new Application();
        application.setName("myapp");
        application.setApplicationFamily(ApplicationFamily.CRM);
        String applicationRole = iamService.getApplicationRole(application, environment);
        Assert.assertEquals(null, applicationRole);
        Assert.assertEquals("/deployer/crm", requests.get(0).pathPrefix());
    }

    @Test
    public void getApplicationIAMRoleFailWithNoConfig(){

        Environment environment = new Environment();
        EnvironmentMetaData environmentMetaData = new EnvironmentMetaData();
        environmentMetaData.setName("qa");
        environment.setEnvironmentMetaData(environmentMetaData);
        Application application = new Application();
        application.setName("myapp");
        application.setApplicationFamily(ApplicationFamily.CRM);
        String applicationRole = iamService.getApplicationRole(application, environment);
        Assert.assertEquals(null, applicationRole);
    }

}
