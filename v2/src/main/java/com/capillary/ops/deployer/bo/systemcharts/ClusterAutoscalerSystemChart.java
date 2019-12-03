package com.capillary.ops.deployer.bo.systemcharts;

import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.ClusterAutoscalerConfiguration;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.bo.ISystemChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ClusterAutoscalerSystemChart implements ISystemChart {

    private static final Logger logger = LoggerFactory.getLogger(ClusterAutoscalerSystemChart.class);

    @Override
    public String getChartPath() {
        return "cluster-autoscaler";
    }

    @Override
    public Map<String, Object> getValues(ApplicationFamily applicationFamily, Environment environment) {
        Map<String, Object> values = new HashMap<>();
        ClusterAutoscalerConfiguration configuration = environment.getEnvironmentConfiguration().getClusterAutoscalerConfiguration();
        if(configuration == null) {
            return null;
        }
        Map<String, Object> asg = new HashMap<>();
        asg.put("name",configuration.getAutoscalingGroup());
        asg.put("minSize",configuration.getMinSize());
        asg.put("maxSize",configuration.getMaxSize());
        values.put("autoscalingGroups",asg);
        return values;
    }

    @Override
    public String getReleaseName(ApplicationFamily applicationFamily, Environment environment) {
        return "cluster-autoscaler";
    }
}
