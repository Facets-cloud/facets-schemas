package com.capillary.ops.deployer.bo.systemcharts;

import com.capillary.ops.deployer.bo.AbstractSystemChart;
import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ExternalDnsSystemChart extends AbstractSystemChart {

    private static final Logger logger = LoggerFactory.getLogger(ExternalDnsSystemChart.class);

    @Override
    public Map<String, Object> getValues(ApplicationFamily applicationFamily, Environment environment) {
        String zoneType = (String) this.getConfig().getOrDefault("zoneType", "");
        if (StringUtils.isEmpty(zoneType)) {
            logger.error("zoneType not found, cannot return value map");
            throw new NotFoundException("zoneType not found in the config map");
        }

        if (zoneType.equals("private") && environment.getPrivateZoneId() != null) {
            return getValuesMap(environment, environment.getPrivateZoneDns(), zoneType);
        }

        if (zoneType.equals("public") && environment.getPublicZoneDns() != null) {
            return getValuesMap(environment, environment.getPublicZoneDns(), zoneType);
        }

        logger.error("could not find public or private zone id, returning null");
        return null;
    }

    @Override
    public String getReleaseName(ApplicationFamily applicationFamily, Environment environment) {
        String zoneType = (String) this.getConfig().getOrDefault("zoneType", "");
        if (StringUtils.isEmpty(zoneType)) {
            logger.error("zoneType not found, cannot return value map");
            throw new NotFoundException("zoneType not found in the config map");
        }

        return environment.getName() + "-" + zoneType + "-route53-dns";
    }

    @Override
    public String getName() {
        return "route53-dns";
    }

    @Override
    public String getChartPath() {
        return "route53-dns";
    }

    private Map<String, Object> getValuesMap(Environment environment, String zoneDns, String zoneType) {
        if (environment.getAwsAccessKeyId() == null || environment.getAwsSecretAccessKey() == null) {
            throw new NotFoundException("cannot read aws secrets, please update the access and secret keys in cluster details");
        }

        Map<String, Object> app = new LinkedHashMap<>();
        app.put("environment", environment.getName());

        Map<String, Object> aws = getAwsValues(environment, zoneDns, zoneType);
        Map<String, Object> valueMap = new LinkedHashMap<>();
        valueMap.put("app", app);
        valueMap.put("aws", aws);

        return valueMap;
    }

    private Map<String, Object> getAwsValues(Environment environment, String zoneDns, String zoneType) {
        Map<String, Object> aws = new LinkedHashMap<>();
        aws.put("zoneId", environment.getPrivateZoneId());
        aws.put("zoneType", zoneType);
        aws.put("awsAccessKeyId", environment.getAwsAccessKeyId());
        aws.put("awsSecretAccessKey", environment.getAwsSecretAccessKey());
        aws.put("zoneDns", zoneDns);
        return aws;
    }
}
