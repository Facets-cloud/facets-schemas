package com.capillary.ops.deployer.service;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3DumpService {

    private static final String BUCKET_NAME = "k8s-file-dumps";

    @Autowired
    private S3Client s3Client;

    Logger logger = LoggerFactory.getLogger(S3DumpService.class);

    private String getS3Prefix(String environment, String module, String date) {
        return environment + "/" + module + "/" + date;
    }

    public List<String> listObjects(String environment, String module, String date) {
        String s3Prefix = getS3Prefix(environment, module, date);
        ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder().bucket(BUCKET_NAME).prefix(s3Prefix).build();
        ListObjectsResponse listObjectsResponse = s3Client.listObjects(listObjectsRequest);
        logger.info("fetched objects from s3 with size: {}", listObjectsResponse.contents().size());

        return listObjectsResponse.contents().stream().map(S3Object::key).collect(Collectors.toList());
    }

    public byte[] downloadObject(String path) {
        byte[] bytes;

        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(BUCKET_NAME).key(path).build();
        ResponseInputStream<GetObjectResponse> clientObject = s3Client.getObject(getObjectRequest);
        try {
            bytes = IOUtils.toByteArray(clientObject);
            logger.info("downloaded file from s3: {}", path);
        } catch (IOException e) {
            logger.error("error happened while converting s3 file to byte array", e);
            throw new RuntimeException("unable to parse the file at path: " + path);
        }

        return bytes;
    }
}
