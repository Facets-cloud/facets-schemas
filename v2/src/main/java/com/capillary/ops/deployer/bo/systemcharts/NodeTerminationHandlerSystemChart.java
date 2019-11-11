package com.capillary.ops.deployer.bo.systemcharts;


import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.bo.ISystemChart;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NodeTerminationHandlerSystemChart implements ISystemChart {

    @Override
    public String getChartPath() {
        return "aws-node-termination-handler";
    }

    @Override
    public Map<String, Object> getValues(ApplicationFamily applicationFamily, Environment environment) {
        if(environment.getEnvironmentConfiguration().isSpotTerminationHandlingEnabled()) {
            return new HashMap<>();
        }
        return null;
    }

    @Override
    public String getReleaseName(ApplicationFamily applicationFamily, Environment environment) {
        return "aws-node-termination-handler";
    }
}
