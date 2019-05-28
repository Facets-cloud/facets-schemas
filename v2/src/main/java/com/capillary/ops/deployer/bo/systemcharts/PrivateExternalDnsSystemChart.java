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
public class PrivateExternalDnsSystemChart extends AbstractExternalDnsSystemChart {

    private static final Logger logger = LoggerFactory.getLogger(PrivateExternalDnsSystemChart.class);

    @Override
    public Map<String, Object> getValues(ApplicationFamily applicationFamily, Environment environment) {
        String privateZoneId = environment.getPrivateZoneId();
        if (!StringUtils.isEmpty(privateZoneId)) {
            return getValuesMap(environment, environment.getPrivateZoneDns(), privateZoneId);
        }

        logger.error("could not find public or private zone id, returning null");
        return null;
    }

    @Override
    protected String getZoneType() {
        return "private";
    }

    @Override
    public String getChartPath() {
        return "route53-dns";
    }
}
