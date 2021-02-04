package com.capillary.ops.deployer.service.facade;

import com.amazonaws.regions.Regions;
import com.capillary.ops.cp.exceptions.BadRequestException;
import com.capillary.ops.deployer.bo.Registry;
import com.capillary.ops.deployer.bo.ECRRegistry;
import com.capillary.ops.deployer.repository.RegistryRepository;
import com.capillary.ops.deployer.service.CodeBuildService;
import com.capillary.ops.deployer.service.RegistryService;
import com.jcabi.aspects.Loggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Loggable
public class RegistryFacade {
    @Autowired
    private RegistryService registryService;

    private static final Logger logger = LoggerFactory.getLogger(RegistryFacade.class);

    public ECRRegistry createECRRegistry(ECRRegistry ecrRegistry){
        return registryService.createECRRegistry(ecrRegistry);
    }

    public List<Registry> getAllRegistries(){
        return registryService.getAllRegistries();
    }
}
