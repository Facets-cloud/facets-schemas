package com.capillary.ops.deployer.bo.systemcharts;

import com.capillary.ops.deployer.bo.AbstractSystemChart;
import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ScheduledS3UploaderSystemChart extends AbstractSystemChart {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledS3UploaderSystemChart.class);

    @Override
    public String getName() {
        return "scheduled-s3-uploader";
    }

    @Override
    public String getChartPath() {
        return "scheduled_s3_uploader_chart";
    }

    @Override
    public Map<String, Object> getValues(ApplicationFamily applicationFamily, Environment environment) {
        String awsAccessKeyId = environment.getAwsAccessKeyId();
        String awsSecretAccessKey = environment.getAwsSecretAccessKey();

        if (awsAccessKeyId == null || awsSecretAccessKey == null) {
            logger.error("could not find one of the aws keys");
            throw new NotFoundException("aws access key id or secret access key not found");
        }

        Map<String, String> aws = Maps.newHashMapWithExpectedSize(2);
        aws.put("awsAccessKeyId", awsAccessKeyId);
        aws.put("awsSecretAccessId", awsSecretAccessKey);

        logger.info("added aws keys to scheduled_s3_uploader value map");

        Map<String, Object> values = new HashMap<>();
        values.put("aws", aws);

        return values;
    }

    @Override
    public String getReleaseName(ApplicationFamily applicationFamily, Environment environment) {
        return "scheduled-s3-uplaoder";
    }
}
