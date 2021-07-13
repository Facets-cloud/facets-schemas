package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.TFRunConfigurations;
import com.capillary.ops.cp.repository.TFRunConfigurationsRepository;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TFRunConfigurationsService {

    @Autowired
    private TFRunConfigurationsRepository tfRunConfigurationsRepository;

    public Optional<TFRunConfigurations> getTFDetails(String clusterId) {
        return tfRunConfigurationsRepository.findOneByClusterId(clusterId);
    }

    public TFRunConfigurations createTFDetails(TFRunConfigurations tfRunConfigurations, String clusterId) {
        tfRunConfigurations.setClusterId(clusterId);
        return tfRunConfigurationsRepository.save(tfRunConfigurations);
    }

    public TFRunConfigurations updateTFDetails(TFRunConfigurations tfRunConfigurations, String clusterId) {
        TFRunConfigurations tfRunConfigurations1 = getTFDetails(clusterId).orElseThrow(() -> new NotFoundException("No TF details found for the cluster id " + clusterId));

        if (tfRunConfigurations.getBranchOverride() != null) {
            tfRunConfigurations1.setBranchOverride(tfRunConfigurations.getBranchOverride());
        }
        if (!tfRunConfigurations.getAdditionalEnvVars().isEmpty()){
            tfRunConfigurations1.setAdditionalEnvVars(tfRunConfigurations.getAdditionalEnvVars());
        }
        return tfRunConfigurationsRepository.save(tfRunConfigurations1);
    }

    public TFRunConfigurations deleteTFDetails(String clusterId) {
        TFRunConfigurations tfRunConfigurations = getTFDetails(clusterId).orElseThrow(() -> new NotFoundException("No TF details found for the cluster id " + clusterId));

        tfRunConfigurationsRepository.deleteByClusterId(clusterId);
        return tfRunConfigurations;
    }
}
