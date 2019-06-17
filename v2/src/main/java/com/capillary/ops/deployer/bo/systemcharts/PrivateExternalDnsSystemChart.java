package com.capillary.ops.deployer.bo.systemcharts;

import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.bo.ExternalDnsConfiguration;
import com.capillary.ops.deployer.bo.ISystemChart;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class PrivateExternalDnsSystemChart implements ISystemChart {

    private static final Logger logger = LoggerFactory.getLogger(PrivateExternalDnsSystemChart.class);

    @Override
    public String getChartPath() {
        return "route53-dns";
    }

    @Override
    public Map<String, Object> getValues(ApplicationFamily applicationFamily, Environment environment) {
        ExternalDnsConfiguration privateDnsConfiguration =
                environment.getEnvironmentConfiguration().getPrivateDnsConfiguration();
        if(privateDnsConfiguration == null) {
            return null;
        }
        Map<String, Object> publicDnsConfigurationMap = new ObjectMapper()
                .convertValue(privateDnsConfiguration, Map.class);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.putAll(publicDnsConfigurationMap);
        ret.put("environment", environment.getEnvironmentMetaData().getName());
        ret.put("zoneType", "private");
        return ret;
    }

    @Override
    public String getReleaseName(ApplicationFamily applicationFamily, Environment environment) {
        return environment.getEnvironmentMetaData().getName() + "-public-route53-dns";
    }
}
