package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.service.interfaces.IIAMService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;

public class IAMServiceUnitTest {

    @Autowired
    private IIAMService iamService;

    @Test
    public void getApplicationIAMRole(){
        String appName = "mockApp";

//        Application testApp = new Application();
//        testApp.setName(appName);
//        testApp.setApplicationFamily(ApplicationFamily.CRM);
//
//        Environment testEnv = new Environment();
//        EnvironmentConfiguration testEnvConf = new EnvironmentConfiguration();
//
//        Assert.assertEquals(iamService.getApplicationRole(testApp,testEnv),appName);
    }

}
