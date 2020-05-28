package com.capillary.ops.deployer.bo.systemcharts;

import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.bo.ISystemChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FilebeatSystemChart implements ISystemChart {

    private static final Logger logger = LoggerFactory.getLogger(FilebeatSystemChart.class);

    @Override
    public String getChartPath() {
        return "filebeat";
    }

    @Override
    public Map<String, Object> getValues(ApplicationFamily applicationFamily, Environment environment) {
        if(environment.getEnvironmentConfiguration().isFilebeatEnabled()) {
            return new HashMap<>();
        }
        return null;
    }

    @Override
    public String getReleaseName(ApplicationFamily applicationFamily, Environment environment) {
        return "filebeat";
    }
}
