package com.capillary.ops.deployer.service.capillaryCloud;

import com.capillary.ops.deployer.bo.capillaryCloud.*;
import com.capillary.ops.deployer.exceptions.NoSuchInfrastructureResourceException;
import com.capillary.ops.deployer.repository.capillaryCloud.ClusterRepository;
import com.capillary.ops.deployer.repository.capillaryCloud.InfrastructureResourceInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CapillaryCloudFacade {

    @Autowired
    private ClusterRepository clusterRepository;

    @Autowired
    private TerraformService terraformService;

    @Autowired
    private InfrastructureResourceDefinitions infrastructureResourceDefinitions;

    @Autowired
    private InfrastructureResourceInstanceRepository infrastructureResourceInstanceRepository;

    @Autowired
    private ApplicationContext applicationContext;

    public Cluster defineCluster(Cluster cluster) {
        clusterRepository.save(cluster);
        return cluster;
    }

    public ProcessExecutionResult planCluster(String clusterName) {
        Cluster cluster = clusterRepository.findOneByName(clusterName).get();
        return terraformService.plan(cluster);
    }

    public Cluster createCluster(String clusterName) {
        return clusterRepository.findOneByName(clusterName).get();
    }

    public ProcessExecutionResult syncCluster(String clusterName) {
        Cluster cluster = clusterRepository.findOneByName(clusterName).get();
        return terraformService.apply(cluster);
    }

    public <T extends InfrastructureResourceInstance> T registerInstance(String infrastructureResourceName, String clusterName,
                                                                         T instance) {
        instance.setClusterName(clusterName);
        instance.setInfrastructureResourceName(
                infrastructureResourceDefinitions.findByName(infrastructureResourceName).getName());
        infrastructureResourceInstanceRepository.save(instance);
        return instance;
    }

    public ProcessExecutionResult destroyCluster(String clusterName) {
        Cluster cluster = clusterRepository.findOneByName(clusterName).get();
        return terraformService.destroy(cluster);
    }

    public Collection<InfrastructureResource> getInfrastructureResources() {
        return infrastructureResourceDefinitions.getAll();
    }

    public InfrastructureResourceInstances getInfrastructureResourceInstances(String clusterName) {
        List<InfrastructureResource> infrastructureResources =
                infrastructureResourceDefinitions.getAll();

        Map<String, VPC> vpcMap =
                getInstanceMap(infrastructureResources, InfrastructureResourceType.VPC, clusterName);

        Map<String, K8sCluster> k8sClustersMap =
                getInstanceMap(infrastructureResources, InfrastructureResourceType.K8S_CLUSTER, clusterName);

        InfrastructureResourceInstances result = new InfrastructureResourceInstances();
        result.setK8sClusters(k8sClustersMap);
        result.setVpcs(vpcMap);
        return result;
    }

    private <T extends InfrastructureResourceInstance> Map<String, T>
      getInstanceMap(List<InfrastructureResource> infrastructureResources,
          InfrastructureResourceType type, String clusterName) {
        Map<String, T> instances =
                infrastructureResourceInstanceRepository.findByClusterName(clusterName).stream()
                        .collect(Collectors.toMap(x -> x.getInfrastructureResourceName(), x->(T) x));
        Map<String, T> instancesMap = infrastructureResources.stream()
                .filter(x -> x.getType().equals(type))
                .map(x -> new HashMap.SimpleImmutableEntry<>(x.getName(), instances.get(x.getName())))
                .filter(x -> x.getValue() != null)
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
        return instancesMap;
    }

    public <T extends InfrastructureResourceInstance> T getInstance(String infrastructureResourceName, String clusterName) {
        InfrastructureResource infraResource = infrastructureResourceDefinitions.findByName(infrastructureResourceName);
        Optional<InfrastructureResourceInstance> resultOptional = infrastructureResourceInstanceRepository.findOneByInfrastructureResourceNameAndClusterName(infraResource.getName(), clusterName);
        if(resultOptional.isPresent()) {
            return (T) resultOptional.get();
        }
        throw new NoSuchInfrastructureResourceException();
    }

    public <T extends InfrastructureResourceInstance> T deleteInstance(String infrastructureResourceName, String clusterName) {
        InfrastructureResourceInstance instance = getInstance(infrastructureResourceName, clusterName);
        infrastructureResourceInstanceRepository.delete(instance);
        return (T) instance;
    }
}
