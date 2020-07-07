package com.capillary.ops.deployer.controller;

import com.capillary.ops.deployer.bo.webhook.sonar.CallbackBody;
import com.capillary.ops.deployer.service.facade.ApplicationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;

@RestController
@RequestMapping("callback")
public class CallbackController {

    @Autowired
    ApplicationFacade applicationFacade;

    @PermitAll
    @PostMapping("/sonar")
    public boolean sonarCallBack(@RequestBody CallbackBody body){

        return applicationFacade.processSonarCallback(body);
    }

}
