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
//            String appName = "k8s-demo";
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
//            newRelicService.createAlerts(application,environment);
//            String newRelicServiceAlertsURL2 = newRelicService.getAlertsURL(application, environment);
//
//            newRelicService.disableAlerts(application,environment);
//
//            String newRelicServiceAlertsURL = newRelicService.getAlertsURL(application, environment);
//            System.out.println(newRelicServiceAlertsURL);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
    }

}
