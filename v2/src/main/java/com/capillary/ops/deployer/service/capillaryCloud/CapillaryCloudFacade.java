package com.capillary.ops.deployer.service.capillaryCloud;

import com.capillary.ops.deployer.bo.capillaryCloud.*;
import com.capillary.ops.deployer.repository.capillaryCloud.ClusterRepository;
import com.capillary.ops.deployer.repository.capillaryCloud.InfrastructureResourceRepository;
import com.capillary.ops.deployer.repository.capillaryCloud.infraResources.K8sClusterRepository;
import com.capillary.ops.deployer.repository.capillaryCloud.infraResources.VPCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CapillaryCloudFacade {

    @Autowired
    private ClusterRepository clusterRepository;

    @Autowired
    private TerraformService terraformService;

    @Autowired
    private InfrastructureResourceRepository infrastructureResourceRepository;

    @Autowired
    private VPCRepository vpcRepository;

    @Autowired
    private K8sClusterRepository k8sClusterRepository;

    @Autowired
    private ApplicationContext applicationContext;

    public Cluster defineCluster(Cluster cluster) {
        clusterRepository.save(cluster);
        return cluster;
    }

    public ProcessExecutionResult planCluster(String clusterId) {
        Cluster cluster = clusterRepository.findById(clusterId).get();
        return terraformService.plan(cluster);
    }

    public Cluster createCluster(String clusterId) {
        return clusterRepository.findById(clusterId).get();
    }

    public ProcessExecutionResult syncCluster(String clusterId) {
        Cluster cluster = clusterRepository.findById(clusterId).get();
        return terraformService.apply(cluster);
    }

    public InfrastructureResource createInfrastructureResource(InfrastructureResource infrastructureResource) {
        infrastructureResourceRepository.save(infrastructureResource);
        return infrastructureResource;
    }

    public K8sCluster registerK8sCluster(K8sCluster k8sCluster) {
        k8sClusterRepository.save(k8sCluster);
        return k8sCluster;
    }

    public VPC registerVPC(VPC vpc) {
        vpcRepository.save(vpc);
        return vpc;
    }

    public ProcessExecutionResult destroyCluster(String clusterId) {
        Cluster cluster = clusterRepository.findById(clusterId).get();
        return terraformService.destroy(cluster);
    }
}
