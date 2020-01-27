package com.capillary.ops.deployer.bo.systemcharts;

import com.capillary.ops.deployer.bo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Kube2IAMSystemChart implements ISystemChart {

    private static final Logger logger = LoggerFactory.getLogger(Kube2IAMSystemChart.class);

    @Override
    public String getChartPath() {
        return "kube2iam";
    }

    @Override
    public Map<String, Object> getValues(ApplicationFamily applicationFamily, Environment environment) {
        Map<String, Object> values = new HashMap<>();
        Kube2IamConfiguration kube2IamConfiguration = environment.getEnvironmentConfiguration().getKube2IamConfiguration();
        if(kube2IamConfiguration == null || !kube2IamConfiguration.isEnabled()) {
            return null;
        }
        Map<String, Object> awsCreds = new HashMap<>();
        awsCreds.put("access_key",kube2IamConfiguration.getAwsAccessKeyID());
        awsCreds.put("secret_key",kube2IamConfiguration.getAwsSecretAccessKey());
        values.put("aws",awsCreds);

        Map<String, Object> networkInterface = new HashMap<>();
        awsCreds.put("interface",kube2IamConfiguration.getHostNetworkInterface());
        values.put("host",networkInterface);

        return values;
    }

    @Override
    public String getReleaseName(ApplicationFamily applicationFamily, Environment environment) {
        return "kube2iam";
    }
}
