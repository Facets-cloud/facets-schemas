package com.capillary.ops.deployer.bo.systemcharts;

import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.ISystemChart;
import com.capillary.ops.deployer.bo.K8sLoggingConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FluentdEFSSystemChart implements ISystemChart {

    private static final Logger logger = LoggerFactory.getLogger(FluentdEFSSystemChart.class);

    @Override
    public String getChartPath() {
        return "fluentd-efs";
    }

    @Override
    public Map<String, Object> getValues(ApplicationFamily applicationFamily, Environment environment) {
        Map<String, Object> values = new HashMap<>();
        K8sLoggingConfiguration logConfigs = environment.getEnvironmentConfiguration().getK8sLoggingConfiguration();
        values.put("efsHost",logConfigs.getEfsHost());
        return values;
    }

    @Override
    public String getReleaseName(ApplicationFamily applicationFamily, Environment environment) {
        return "fluentd-efs";
    }
}
