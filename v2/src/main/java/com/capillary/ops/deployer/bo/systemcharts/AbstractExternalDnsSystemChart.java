package com.capillary.ops.deployer.bo.systemcharts;

import com.capillary.ops.deployer.bo.AbstractSystemChart;
import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractExternalDnsSystemChart extends AbstractSystemChart {

    private static final Logger logger = LoggerFactory.getLogger(AbstractExternalDnsSystemChart.class);

    protected abstract String getZoneType();

    @Override
    public String getReleaseName(ApplicationFamily applicationFamily, Environment environment) {
        return environment.getName() + "-" + this.getZoneType() + "-route53-dns";
    }

    @Override
    public String getChartPath() {
        return "route53-dns";
    }

    protected Map<String, Object> getValuesMap(Environment environment, String zoneDns, String zoneId) {
        if (environment.getAwsAccessKeyId() == null || environment.getAwsSecretAccessKey() == null) {
            throw new NotFoundException("cannot read aws secrets, please update the access and secret keys in cluster details");
        }

        Map<String, Object> app = new LinkedHashMap<>();
        app.put("environment", environment.getName());

        Map<String, Object> aws = getAwsValues(environment, zoneDns, zoneId);
        Map<String, Object> valueMap = new LinkedHashMap<>();
        valueMap.put("app", app);
        valueMap.put("aws", aws);

        return valueMap;
    }

    private Map<String, Object> getAwsValues(Environment environment, String zoneDns, String zoneId) {
        Map<String, Object> aws = new LinkedHashMap<>();
        aws.put("zoneId", zoneId);
        aws.put("zoneType", this.getZoneType());
        aws.put("awsAccessKeyId", environment.getAwsAccessKeyId());
        aws.put("awsSecretAccessKey", environment.getAwsSecretAccessKey());
        aws.put("zoneDns", zoneDns);
        return aws;
    }
}
