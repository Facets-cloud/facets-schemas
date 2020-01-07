package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.service.newrelic.NewRelicService;
import mockit.Tested;
import org.junit.Test;

public class NewRelicServiceUnitTests {

    @Tested
    private NewRelicService newRelicService;


    @Test
    public void CreateAlertPolicy(){
        Application application = new Application();
        application.setName("unittest-02");
        Environment environment = new Environment();
        String returnId = newRelicService.createAlerts(application,environment);
    }

}
