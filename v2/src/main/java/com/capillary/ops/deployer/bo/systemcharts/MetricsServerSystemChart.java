package com.capillary.ops.deployer.bo.systemcharts;

import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.bo.ISystemChart;
import com.capillary.ops.deployer.bo.Kube2IamConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MetricsServerSystemChart implements ISystemChart {

    private static final Logger logger = LoggerFactory.getLogger(MetricsServerSystemChart.class);

    @Override
    public String getChartPath() {
        return "metrics-server";
    }

    @Override
    public Map<String, Object> getValues(ApplicationFamily applicationFamily, Environment environment) {
        if(environment.getEnvironmentConfiguration().isMetricServerEnabled()) {
            return new HashMap<>();
        }
        return null;
    }

    @Override
    public String getReleaseName(ApplicationFamily applicationFamily, Environment environment) {
        return "metrics-server";
    }
}
