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

    public Optional<TFRunConfigurations> getTFRunConfigurations(String clusterId) {
        return tfRunConfigurationsRepository.findOneByClusterId(clusterId);
    }

    public TFRunConfigurations createTFRunConfigurations(TFRunConfigurations tfRunConfigurations, String clusterId) {
        tfRunConfigurations.setClusterId(clusterId);
        return tfRunConfigurationsRepository.save(tfRunConfigurations);
    }

    public TFRunConfigurations updateTFRunConfigurations(TFRunConfigurations tfRunConfigurations, String clusterId) {
        TFRunConfigurations tfRunConfigurations1 = getTFRunConfigurations(clusterId).orElseThrow(() -> new NotFoundException("No TF details found for the cluster id " + clusterId));

        if (tfRunConfigurations.getBranchOverride() != null) {
            tfRunConfigurations1.setBranchOverride(tfRunConfigurations.getBranchOverride());
        }
        if (!tfRunConfigurations.getAdditionalEnvVars().isEmpty()){
            tfRunConfigurations1.setAdditionalEnvVars(tfRunConfigurations.getAdditionalEnvVars());
        }
        return tfRunConfigurationsRepository.save(tfRunConfigurations1);
    }

    public TFRunConfigurations deleteTFRunConfigurations(String clusterId) {
        TFRunConfigurations tfRunConfigurations = getTFRunConfigurations(clusterId).orElseThrow(() -> new NotFoundException("No TF details found for the cluster id " + clusterId));

        tfRunConfigurationsRepository.deleteByClusterId(clusterId);
        return tfRunConfigurations;
    }
}
