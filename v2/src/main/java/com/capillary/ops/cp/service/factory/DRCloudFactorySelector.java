package com.capillary.ops.cp.service.factory;

import com.capillary.ops.cp.bo.requests.Cloud;
import com.capillary.ops.cp.service.DRCloudFactory;
import com.capillary.ops.cp.service.aws.factory.AwsDRCloudFactory;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DRCloudFactorySelector {

    @Autowired
    private AwsDRCloudFactory awsDRCloudFactory;

    public DRCloudFactory getDRCloudFactory(Cloud cloud) {
        switch (cloud) {
            case AWS:
                return awsDRCloudFactory;
            default:
                throw new NotFoundException("Could not find DR service for cloud: " + cloud.name());
        }
    }
}
