package com.capillary.ops.cp.service;

import com.amazonaws.regions.Regions;
import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.cp.bo.requests.Cloud;
import com.capillary.ops.deployer.exceptions.InvalidCloudProviderException;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

/**
 * Select Terraform provider to be used to manage the cluster
 */
@Service
public class CloudTFImplementationSelector {

    private static final String TF_PROVIDER_DEFAULT = "tfaws";
    private static final String TF_PROVIDER_AWS_DEFAULT = "tfaws";
    private static final String TF_PROVIDER_AWS_CN = "tfawscn";

    private String selectAwsTfProvider(AwsCluster awsCluster) {
        switch (Regions.fromName(awsCluster.getAwsRegion())) {
            case CN_NORTH_1:
            case CN_NORTHWEST_1:
                return TF_PROVIDER_AWS_CN;
            default:
                return TF_PROVIDER_AWS_DEFAULT;
        }
    }

    /**
     * Select Terraform provider to be used to manage the cluster
     * @param cluster Cluster object corresponding to the actual cluster that needs to be managed
     * @return tfProvider
     */
    public String selectTFProvider(AbstractCluster cluster) {
        if (cluster.getCloud() == null) {
            throw new InvalidCloudProviderException("no cloud provider present");
        }

        switch (cluster.getCloud()) {
            case AWS:
                return selectAwsTfProvider((AwsCluster) cluster);
            default:
                throw new InvalidCloudProviderException(String.valueOf(cluster.getCloud()));
        }
    }
}
