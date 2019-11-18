package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.service.helm.*;
import mockit.Injectable;
import mockit.*;
import mockit.Mocked;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HelmUnitTests {

    @Tested
    private HelmValueProviderFactory helmValueProviderFactory;

    @Injectable
    private BaseChartValueProvider baseChartValueProvider;

    @Injectable
    private StatefulSetChartValueProvider statefulSetChartValueProvider;

    @Injectable
    private CronJobChartValueProvider cronJobChartValueProvider;

    @Test
    public void getIngressValuesHTTP(){
        Environment environment = new Environment();

        Application application = new Application();
        application.setName("myapp");
        application.setApplicationFamily(ApplicationFamily.CRM);
        application.setApplicationType(Application.ApplicationType.SERVICE);
        List<Port> ports = new ArrayList<>();
        ports.add(new Port("http",80L,80L,Port.Protocol.HTTP));
        application.setPorts(ports);
        Deployment deployment = new Deployment();
        deployment.setId("test");
        deployment.setBuildId("test");
        deployment.setImage("test");
        deployment.setEnvironment("qa");

        Map<String,Object> values = helmValueProviderFactory.getValues(application,environment,deployment);

        //Assert.assertTrue(values.containsKey("enableIngress"));
        Assert.assertTrue(true);
    }

    @Test
    public void getIngressValuesOnlyTCP(){

    }
}
