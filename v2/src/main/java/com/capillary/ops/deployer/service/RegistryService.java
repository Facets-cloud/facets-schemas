package com.capillary.ops.deployer.service;

import com.amazonaws.regions.Regions;
import com.capillary.ops.cp.exceptions.BadRequestException;
import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.ECRRegistry;
import com.capillary.ops.deployer.bo.Registry;
import com.capillary.ops.deployer.repository.RegistryRepository;
import com.jcabi.aspects.Loggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Loggable
public class RegistryService {
    @Autowired
    private RegistryRepository registryRepository;

    private static final Logger logger = LoggerFactory.getLogger(RegistryService.class);

    public ECRRegistry createECRRegistry(ECRRegistry ecrRegistry){
        try{
            Regions.fromName(ecrRegistry.getAwsRegion());
        } catch (IllegalArgumentException e){
            throw new BadRequestException("invalid aws region: " + ecrRegistry.getAwsRegion());
        }
        ECRRegistry registry = createUri(ecrRegistry);
        return registryRepository.save(registry);
    }

    public List<Registry> getAllRegistries(){
        return registryRepository.findAll();
    }

    public List<Registry> getBuildRegistries(Application application, boolean testBuild, boolean tagBuild, boolean addAdditionalRegistries) {
        List<Registry> registries = new ArrayList<Registry>(){{
            ECRRegistry registry = new ECRRegistry();
            registry.setAwsRegion("us-west-1");
            registry.setName("freemium-north-cal-registry");
            registry.setAwsAccountId("486456986266");
            registry.setUri("486456986266.dkr.ecr.us-west-1.amazonaws.com");
            add(registry);
        }};
        if(!testBuild && addAdditionalRegistries){
            if (tagBuild){
                registries.addAll(getTagBuildRegistries(application));
            }else {
                registries.addAll(getBranchBuildRegistries(application));
            }
        }
        return registries;
    }

    public List<Registry> getAdditionalRegistries(Application application){
        List<Registry> registries = new ArrayList<>();

        Set<String> additionalRegistryIds = new HashSet<>();
        if (application.getTagBuildRepositoryIds() != null && !application.getTagBuildRepositoryIds().isEmpty()){
            additionalRegistryIds.addAll(application.getTagBuildRepositoryIds());
        }
        if (application.getBranchBuildRepositoryIds() != null && !application.getBranchBuildRepositoryIds().isEmpty()){
            additionalRegistryIds.addAll(application.getBranchBuildRepositoryIds());
        }
        registryRepository.findAllById(additionalRegistryIds).forEach(registries::add);
        return registries;
    }

    private List<Registry> getTagBuildRegistries(Application application){
        List<Registry> registries = new ArrayList<>();
        if (application.getTagBuildRepositoryIds() != null && !application.getTagBuildRepositoryIds().isEmpty()){
            registryRepository.findAllById(application.getTagBuildRepositoryIds()).forEach(registries::add);
        }
        return registries;
    }

    private List<Registry> getBranchBuildRegistries(Application application){
        List<Registry> registries = new ArrayList<>();
        if (application.getBranchBuildRepositoryIds() != null && !application.getBranchBuildRepositoryIds().isEmpty()){
            registryRepository.findAllById(application.getBranchBuildRepositoryIds()).forEach(registries::add);
        }
        return registries;
    }

    private ECRRegistry createUri(ECRRegistry registry){
        String chinaSuffix = "";
        if (registry.getAwsRegion().equals("cn-north-1")){
            chinaSuffix = ".cn";
        }
        registry.setUri(String.format("%s.dkr.ecr.%s.amazonaws.com%s", registry.getAwsAccountId(), registry.getAwsRegion(), chinaSuffix));

        return registry;
    }
}
