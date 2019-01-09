package com.capillary.ops.service;

import com.capillary.ops.bo.AbstractDeploymentResource;
import com.capillary.ops.bo.AbstractInfrastructureResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Service
public class DeploymentServiceClient {

    @Autowired
    private DeploymentServiceSelector deploymentServiceSelector;

    @Autowired
    private DeploymentHandlerSelector deploymentHandlerSelector;

    @Qualifier("DeploymentPool")
    @Autowired
    private ExecutorService executorServicePool;

    public void deploy(AbstractInfrastructureResource infrastructureResource, AbstractDeploymentResource deploymentResource, String deploymentType) {
        executorServicePool.submit(() -> {
            try {
                Object deployment = deploymentServiceSelector.selectService(deploymentType).deploy(deploymentResource);
                deploymentHandlerSelector.selectHandler(deploymentType).handleResponse(infrastructureResource, deploymentResource, deployment);
            } catch (Exception e) {
                System.out.println("error happened while deploying infrastructure resource:" + infrastructureResource);
                e.printStackTrace();

                deploymentHandlerSelector.selectHandler(deploymentType).handleError(infrastructureResource, deploymentResource, e);
            }
        });
    }

    public void update(AbstractInfrastructureResource infrastructureResource, AbstractDeploymentResource deploymentResource, String deploymentType) {
        executorServicePool.submit(() -> {
            try {
                Object deployment = deploymentServiceSelector.selectService(deploymentType).update(deploymentResource);
                deploymentHandlerSelector.selectHandler(deploymentType).handleResponse(infrastructureResource, deploymentResource, deployment);
            } catch (Exception e) {
                System.out.println("error happened while updating infrastructure resource:" + infrastructureResource);
                e.printStackTrace();

                deploymentHandlerSelector.selectHandler(deploymentType).handleError(infrastructureResource, deploymentResource, e);
            }
        });
    }
}
