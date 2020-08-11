package com.capillary.ops.cp.service.aws.factory;

import com.capillary.ops.cp.service.DRCloudFactory;
import com.capillary.ops.cp.service.DRCloudService;
import com.capillary.ops.cp.service.aws.AwsAuroraDRService;
import com.capillary.ops.cp.service.aws.AwsElasticsearchDRService;
import com.capillary.ops.cp.service.aws.AwsMongoDRService;
import com.capillary.ops.deployer.exceptions.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AwsDRCloudFactory implements DRCloudFactory {

    @Autowired
    private AwsAuroraDRService awsAuroraDRService;

    @Autowired
    private AwsMongoDRService awsMongoDRService;

    @Autowired
    private AwsElasticsearchDRService awsElasticsearchDRService;

    public DRCloudService
    getDRService(String resource) {
        switch (resource) {
            case "aurora":
                return awsAuroraDRService;
            case "mongo":
                return awsMongoDRService;
            case "elasticsearch":
                return awsElasticsearchDRService;
            default:
                throw new NotImplementedException("Resource implementation not found for aws resource: " + resource);
        }
    }
}
