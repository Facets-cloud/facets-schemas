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

@Component
public class PublicExternalDnsSystemChart extends AbstractExternalDnsSystemChart {

    private static final Logger logger = LoggerFactory.getLogger(PublicExternalDnsSystemChart.class);

    @Override
    public Map<String, Object> getValues(ApplicationFamily applicationFamily, Environment environment) {
        String publicZoneId = environment.getPublicZoneId();
        if (publicZoneId != null) {
            return getValuesMap(environment, environment.getPublicZoneDns(), publicZoneId);
        }

        logger.error("could not find public or public zone id, returning null");
        return null;
    }

    @Override
    protected String getZoneType() {
        return "public";
    }

    @Override
    public String getChartPath() {
        return "route53-dns";
    }
}
