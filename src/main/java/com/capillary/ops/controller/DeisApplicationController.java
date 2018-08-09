package com.capillary.ops.controller;

import com.capillary.ops.App;
import com.capillary.ops.bo.Application;
import com.capillary.ops.bo.Deployment;
import com.capillary.ops.bo.Environments;
import com.capillary.ops.bo.SSHKeyPair;
import com.capillary.ops.bo.exceptions.ApplicationAlreadyExists;
import com.capillary.ops.bo.exceptions.ApplicationDoesNotExist;
import com.capillary.ops.service.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DeisApplicationController {

    @Autowired
    ApplicationMongoService applicationMongoService;

    @Autowired
    private DeisApiService deisApiService;

    @Autowired
    private DeploymentMongoService deploymentMongoService;

    @Autowired
    private SSHKeyPairGenerator sshKeyPairGenerator;

    @Autowired
    private GitService gitService;

    @PostMapping("/applications")
    public ResponseEntity<Application> createApplication(@RequestBody Application application) throws ApplicationAlreadyExists {
        SSHKeyPair keyPair = sshKeyPairGenerator.generate();
        application.setPrivateKey(keyPair.getPrivateKey());
        application.setPublicKey(keyPair.getPublicKey());
        application = applicationMongoService.createApplication(application);

        for (Environments environment : Environments.values()) {
            deisApiService.createApplication(environment, application);
        }
        return new ResponseEntity<>(application, HttpStatus.OK);
    }

    @PostMapping("/applications/{applicationName}/deployment")
    public ResponseEntity<Deployment> deploy(@PathVariable String applicationName,
                                                  @RequestBody Deployment deployment) throws ApplicationDoesNotExist {
        Application application = applicationMongoService.getApplication(applicationName);
        if(application == null) {
            throw new ApplicationDoesNotExist();
        }
        deployment.setApplicationId(application.getId());
        deploymentMongoService.createDeployment(deployment);
        gitService.pushToDeis(application, deployment);
        return new ResponseEntity<>(deployment, HttpStatus.OK);
    }

    @GetMapping("/deployments/{deploymentId}")
    public ResponseEntity<Deployment> getDeployment(@PathVariable String deploymentId) {
        Deployment deployment = deploymentMongoService.getDeployment(deploymentId);
        return new ResponseEntity<>(deployment, HttpStatus.OK);
    }

    @GetMapping("/applications")
    public ResponseEntity<List<Application>> getApps() {
        List<Application> apps = applicationMongoService.getAll();
        return new ResponseEntity<>(apps, HttpStatus.OK);
    }

}
