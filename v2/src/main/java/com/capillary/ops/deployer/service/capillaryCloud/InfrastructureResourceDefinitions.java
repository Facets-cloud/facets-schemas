package com.capillary.ops.deployer.service.capillaryCloud;

import com.capillary.ops.deployer.bo.capillaryCloud.InfrastructureResource;
import com.capillary.ops.deployer.bo.capillaryCloud.InfrastructureResourceType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class InfrastructureResourceDefinitions {

    public List<InfrastructureResource> getAll() {
        return Arrays.asList(
                new InfrastructureResource("crm-vpc", InfrastructureResourceType.VPC),
                new InfrastructureResource("crm-k8scluster", InfrastructureResourceType.K8S_CLUSTER)
        );
    }

    public InfrastructureResource findByName(String name) {
        return getAll().stream().filter(x -> x.getName().equals(name)).findFirst().get();
    }
}
