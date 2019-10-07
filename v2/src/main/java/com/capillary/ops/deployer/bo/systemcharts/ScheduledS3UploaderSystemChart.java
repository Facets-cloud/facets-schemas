package com.capillary.ops.deployer.bo.systemcharts;

import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ScheduledS3UploaderSystemChart implements ISystemChart {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledS3UploaderSystemChart.class);

    @Override
    public String getChartPath() {
        return "scheduled_s3_uploader_chart";
    }

    @Override
    public Map<String, Object> getValues(ApplicationFamily applicationFamily, Environment environment) {

        S3DumpAwsConfig s3DumpAwsConfig = environment.getEnvironmentConfiguration().getS3DumpAwsConfig();
        if (s3DumpAwsConfig == null || s3DumpAwsConfig.getAwsAccessKeyId() == null
                || s3DumpAwsConfig.getAwsSecretAccessKey() == null) {
            logger.error("could not find one of the aws keys");
            throw new NotFoundException("aws access key id or secret access key not found");
        }

        Map<String, String> aws = Maps.newHashMapWithExpectedSize(2);
        aws.put("awsAccessKeyId", s3DumpAwsConfig.getAwsAccessKeyId());
        aws.put("awsSecretAccessKey", s3DumpAwsConfig.getAwsSecretAccessKey());

        logger.info("added aws keys to scheduled_s3_uploader value map");

        Map<String, Object> values = new HashMap<>();
        values.put("aws", aws);

        EnvironmentMetaData environmentMetaData = environment.getEnvironmentMetaData();
        String cluster = environmentMetaData.getApplicationFamily() + "/" + environmentMetaData.getName();
        values.put("cluster", cluster);

        return values;
    }

    @Override
    public String getReleaseName(ApplicationFamily applicationFamily, Environment environment) {
        return "scheduled-s3-uplaoder";
    }
}
