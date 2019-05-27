package com.capillary.ops.deployer.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.capillary.ops.deployer.bo.S3DumpFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//@Service
public class S3DumpService {

    private static final String BUCKET_NAME = "k8s-file-dumps";

    @Autowired
    private AmazonS3 amazonS3;

    Logger logger = LoggerFactory.getLogger(S3DumpService.class);

    private String getS3Prefix(String environment, String module, String date) {
        return environment + "/" + module + "/" + date;
    }

    public List<String> listObjects(String environment, String module, String date) {
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_1).build();
        String s3Prefix = getS3Prefix(environment, module, date);
        ObjectListing objectListing = amazonS3.listObjects(BUCKET_NAME, s3Prefix);
        List<String> keyList = objectListing.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
        logger.info("fetched objects from s3 with size: {}", keyList.size());

        return keyList;
    }

    public S3DumpFile downloadObject(String path) {
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_1).build();
        com.amazonaws.services.s3.model.S3Object s3Object = amazonS3.getObject(BUCKET_NAME, path);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        long contentLength = s3Object.getObjectMetadata().getContentLength();
        logger.info("returning object with content length: {}", contentLength);

        return new S3DumpFile(inputStream, contentLength);
    }
}
