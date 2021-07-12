package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.TFDetails;
import com.capillary.ops.cp.repository.TFRepository;
import com.capillary.ops.deployer.exceptions.AlreadyExistsException;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TFService {

    @Autowired
    private TFRepository tfRepository;

    public Optional<TFDetails> getTFDetails(String clusterId) {
        return tfRepository.findOneByClusterId(clusterId);
    }

    public TFDetails createTFDetails(TFDetails tfDetails, String clusterId) {
        tfDetails.setClusterId(clusterId);
        return tfRepository.save(tfDetails);
    }

    public TFDetails updateTFDetails(TFDetails tfDetails, String clusterId) {
        TFDetails tfDetails1 = getTFDetails(clusterId).orElseThrow(() -> new NotFoundException("No TF details found for the cluster id " + clusterId));

        if (tfDetails.getBranchOverride() != null) {
            tfDetails1.setBranchOverride(tfDetails.getBranchOverride());
        }
        if (!tfDetails.getAdditionalEnvVars().isEmpty()){
            tfDetails1.setAdditionalEnvVars(tfDetails.getAdditionalEnvVars());
        }
        return tfRepository.save(tfDetails1);
    }

    public TFDetails deleteTFDetails(String clusterId) {
        TFDetails tfDetails = getTFDetails(clusterId).orElseThrow(() -> new NotFoundException("No TF details found for the cluster id " + clusterId));

        tfRepository.delete(tfDetails);
        return tfDetails;
    }
}
