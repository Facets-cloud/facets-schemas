package com.capillary.ops.service;

import com.capillary.ops.bo.Deployment;
import com.capillary.ops.bo.exceptions.DeploymentNotFoundException;
import com.capillary.ops.repository.DeploymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeploymentMongoService {

    @Autowired
    private DeploymentRepository deploymentRepository;

    public Deployment createDeployment(Deployment deployment) {
        deploymentRepository.insert(deployment);
        return deployment;
    }

    public void update(Deployment deployment) {
        deploymentRepository.save(deployment);
    }

    public Deployment getDeployment(String deploymentId) {
        Optional<Deployment> deploymentOptional =
            deploymentRepository.findById(deploymentId);
        if (deploymentOptional.isPresent()) {
            return deploymentOptional.get();
        }
        throw new DeploymentNotFoundException();
    }

    public List<Deployment> getDeploymentOfApp(String applicationId) {
        return deploymentRepository.findByApplicationId(applicationId);
    }
}
