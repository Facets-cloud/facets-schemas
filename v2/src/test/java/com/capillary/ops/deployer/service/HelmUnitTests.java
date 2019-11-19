package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.service.helm.*;
import com.capillary.ops.deployer.service.interfaces.IIAMService;
import mockit.Injectable;
import mockit.*;
import mockit.Mocked;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HelmUnitTests {

    @Tested
    private HelmValueProviderFactory helmValueProviderFactory;

    @Injectable
    private BaseChartValueProvider baseChartValueProvider;

    @Injectable
    private StatefulSetChartValueProvider statefulSetChartValueProvider;

    @Injectable
    private CronJobChartValueProvider cronJobChartValueProvider;

    @Injectable
    private SecretService secretService;

    @Mocked
    private IIAMService iamService;


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
        //Assert.assertTrue(true);
    }

    @Test
    public void getIngressValuesOnlyTCP(){

    }

    @Test
    public void getALBListenPorts() {
        Application application = new Application();
        application.setName("myapp");
        application.setApplicationFamily(ApplicationFamily.CRM);
        application.setApplicationType(Application.ApplicationType.SERVICE);
        Port port1 = new Port("http", 80L, 8888L, Port.Protocol.HTTP);
        Port port2 = new Port("https", 8443L, 4444L, Port.Protocol.HTTPS);
        application.setPorts(Arrays.asList(port1,port2));

        String portList = baseChartValueProvider.getALBListenPorts(application);
        //Assert.assertTrue(portList.contains("8888"));
    }
}
