package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.bo.EnvironmentConfiguration;
import com.capillary.ops.deployer.bo.EnvironmentMetaData;
import com.capillary.ops.deployer.service.newrelic.NewRelicService;
import mockit.Tested;
import org.junit.Test;

public class NewRelicServiceUnitTests {

    @Tested
    private NewRelicService newRelicService;


    @Test
    public void CreateAlertPolicy(){
//        try {
//            String appName = "intouch-api-v3";
//            Application application = new Application();
//            application.setName(appName);
//            application.setNewRelicAlertRecipients("madan.gopal@capillarytech.com");
//
//            Environment environment = new Environment();
//            EnvironmentMetaData metaData = new EnvironmentMetaData();
//            metaData.setName("nightly-crm");
//            EnvironmentConfiguration environmentConfiguration = new EnvironmentConfiguration();
//            environmentConfiguration.setNewRelicClusterName("nightlyk8s.capillary.in");
//            environment.setEnvironmentMetaData(metaData);
//            environment.setEnvironmentConfiguration(environmentConfiguration);
//            newRelicService.disableAlerts(application,environment);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
    }

}
