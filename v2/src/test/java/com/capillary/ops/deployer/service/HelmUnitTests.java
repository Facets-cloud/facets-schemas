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
    private BaseChartValueProvider baseChartValueProvider;

    @Injectable
    private SecretService secretService;

    @Injectable
    private IIAMService iamService;

    @Test
    public void getPortDetailsHTTPS(){
        Environment environment = new Environment();

        Application application = new Application();
        application.setName("myapp");
        application.setApplicationFamily(ApplicationFamily.CRM);
        application.setApplicationType(Application.ApplicationType.SERVICE);
        List<Port> ports = new ArrayList<>();
        ports.add(new Port("http",443L,443L,Port.Protocol.HTTPS));
        application.setPorts(ports);

        Map<String,Object> values = baseChartValueProvider.getPortDetails(application);
        Assert.assertEquals("httpsOnly",values.get("protocolGroup"));
    }

    @Test
    public void getPortDetailsHTTP(){
        Environment environment = new Environment();

        Application application = new Application();
        application.setName("myapp");
        application.setApplicationFamily(ApplicationFamily.CRM);
        application.setApplicationType(Application.ApplicationType.SERVICE);
        List<Port> ports = new ArrayList<>();
        ports.add(new Port("http",8080L,8080L,Port.Protocol.HTTP));
        application.setPorts(ports);

        Map<String,Object> values = baseChartValueProvider.getPortDetails(application);
        Assert.assertEquals("httpOnly",values.get("protocolGroup"));
    }

    @Test
    public void getPortDetailsTCP(){
        Environment environment = new Environment();

        Application application = new Application();
        application.setName("myapp");
        application.setApplicationFamily(ApplicationFamily.CRM);
        application.setApplicationType(Application.ApplicationType.SERVICE);
        List<Port> ports = new ArrayList<>();
        ports.add(new Port("http",5454L,8045L,Port.Protocol.TCP));
        application.setPorts(ports);

        Map<String,Object> values = baseChartValueProvider.getPortDetails(application);
        Assert.assertEquals("tcp",values.get("protocolGroup"));
    }


    @Test
    public void getPortDetailsHTTPSAndHTTP(){
        Environment environment = new Environment();

        Application application = new Application();
        application.setName("myapp");
        application.setApplicationFamily(ApplicationFamily.CRM);
        application.setApplicationType(Application.ApplicationType.SERVICE);
        List<Port> ports = new ArrayList<>();
        ports.add(new Port("http",443L,443L,Port.Protocol.HTTPS));
        ports.add(new Port("http",8080L,8080L,Port.Protocol.HTTP));
        application.setPorts(ports);

        Map<String,Object> values = baseChartValueProvider.getPortDetails(application);
        Assert.assertEquals("http&https",values.get("protocolGroup"));
    }


    @Test
    public void getPortDetailsNoProtocol(){
        Environment environment = new Environment();

        Application application = new Application();
        application.setName("myapp");
        application.setApplicationFamily(ApplicationFamily.CRM);
        application.setApplicationType(Application.ApplicationType.SERVICE);
        List<Port> ports = new ArrayList<>();
        ports.add(new Port("http",443L,443L));
        application.setPorts(ports);

        Map<String,Object> values = baseChartValueProvider.getPortDetails(application);
        Assert.assertEquals("tcp",values.get("protocolGroup"));
    }
}
