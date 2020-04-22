package com.capillary.ops.cp.service.factory;

import com.capillary.ops.cp.bo.requests.Cloud;
import com.capillary.ops.cp.service.ClusterService;
import com.capillary.ops.cp.service.aws.AwsClusterService;
import com.capillary.ops.deployer.exceptions.NotImplementedException;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Loggable
public class ClusterServiceFactory implements ServiceFactory<ClusterService> {

    @Autowired
    AwsClusterService awsClusterService;

    @Override
    public ClusterService getService(Cloud cloud) {

        switch (cloud) {
            case AWS:
                return awsClusterService;
            default:
                throw new NotImplementedException("This cloud is not yet implemented");
        }
    }
}
