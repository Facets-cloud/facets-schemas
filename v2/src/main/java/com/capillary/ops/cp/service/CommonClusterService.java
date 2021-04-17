package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.repository.CpClusterRepository;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonClusterService {

    @Autowired
    private StackService stackService;

    @Autowired
    private CpClusterRepository clusterRepository;

    public List<AbstractCluster> getClustersByStackName(String stackName) {
        if (!stackService.getStackById(stackName).isPresent()) {
            throw new NotFoundException("Stack Not Found: " + stackName);
        }
        return clusterRepository.findAllByStackName(stackName);
    }
}
