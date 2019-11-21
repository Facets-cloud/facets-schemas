package com.capillary.ops.deployer.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.S3DumpFile;
import com.amazonaws.services.s3.model.S3Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Service
public class S3DumpService {

    @Value("${aws.s3bucket.dumps.name}")
    private String defaultS3Bucket;

    @Value("${aws.s3bucket.dumps.region}")
    private String defaultS3BucketRegion;

    Logger logger = LoggerFactory.getLogger(S3DumpService.class);

    private String getS3Prefix(ApplicationFamily applicationFamily, String environment, String module, String date) {
        return new StringJoiner("/")
                .add(applicationFamily.name())
                .add(environment)
                .add(module)
                .add(date)
                .toString();
    }

    public List<String> listObjects(ApplicationFamily applicationFamily, String environment, String module, String date) {
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withRegion(Regions.valueOf(defaultS3BucketRegion)).build();
        String s3Prefix = getS3Prefix(applicationFamily, environment, module, date);
        ObjectListing objectListing = amazonS3.listObjects(defaultS3Bucket, s3Prefix);
        List<String> keyList = objectListing.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .filter(x -> !x.endsWith("/"))
                .collect(Collectors.toList());
        logger.info("fetched objects from s3 with size: {}", keyList.size());

        return keyList;
    }

    public S3DumpFile downloadObject(String path) {
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withRegion(Regions.valueOf(defaultS3BucketRegion)).build();
        S3Object s3Object = amazonS3.getObject(defaultS3Bucket, path);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        long contentLength = s3Object.getObjectMetadata().getContentLength();
        logger.info("returning object with content length: {}", contentLength);

        return new S3DumpFile(inputStream, contentLength);
    }

    public S3DumpFile downloadObject(String bucketName, String path, Regions region) {
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withRegion(region).build();
        S3Object s3Object = amazonS3.getObject(bucketName, path);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        long contentLength = s3Object.getObjectMetadata().getContentLength();
        logger.info("returning object with content length: {}", contentLength);

        return new S3DumpFile(inputStream, contentLength);
    }
}
