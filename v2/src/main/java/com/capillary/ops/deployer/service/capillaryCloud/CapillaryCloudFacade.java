package com.capillary.ops.deployer.service.capillaryCloud;

import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.bo.EnvironmentConfiguration;
import com.capillary.ops.deployer.bo.EnvironmentMetaData;
import com.capillary.ops.deployer.bo.capillaryCloud.*;
import com.capillary.ops.deployer.exceptions.NoSuchInfrastructureResourceException;
import com.capillary.ops.deployer.repository.EnvironmentRepository;
import com.capillary.ops.deployer.repository.capillaryCloud.ClusterRepository;
import com.capillary.ops.deployer.repository.capillaryCloud.InfrastructureResourceInstanceRepository;
import com.capillary.ops.deployer.service.facade.ApplicationFacade;
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

    @Autowired
    private EnvironmentRepository environmentRepository;

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

    public void upsertDeployerEnvironment(String clusterName, String infrastructureResourceName, K8sCluster k8sCluster) {
        Cluster cluster = clusterRepository.findOneByName(clusterName).get();
        String environmentName = infrastructureResourceName + "-" + clusterName;

        Optional<Environment> existingEnvOptional =
                environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(cluster.getApplicationFamily(), environmentName);
        if(existingEnvOptional.isPresent()) {
            Environment existingEnv = existingEnvOptional.get();
            existingEnv.getEnvironmentConfiguration().setKubernetesToken(k8sCluster.getToken());
            existingEnv.getEnvironmentConfiguration().setKubernetesApiEndpoint(k8sCluster.getApiEndpoint());
            environmentRepository.save(existingEnv);
        } else {
            EnvironmentMetaData environmentMetaData =
                    new EnvironmentMetaData(cluster.getName(), cluster.getEnvironmentType(), environmentName,
                            cluster.getApplicationFamily());
            EnvironmentConfiguration environmentConfiguration = new EnvironmentConfiguration();
            environmentConfiguration.setKubernetesApiEndpoint(k8sCluster.getApiEndpoint());
            environmentConfiguration.setKubernetesToken(k8sCluster.getToken());
            Environment newEnvironment = new Environment(environmentMetaData, environmentConfiguration);
            environmentRepository.save(newEnvironment);
        }
    }

    public void deleteDeployerEnvironment(String clusterName, String infrastructureResourceName) {
        String environmentName = infrastructureResourceName + "-" + clusterName;
        Cluster cluster = clusterRepository.findOneByName(clusterName).get();
        Optional<Environment> existingEnvOptional =
                environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(cluster.getApplicationFamily(), environmentName);
        environmentRepository.delete(existingEnvOptional.get());
    }
}
