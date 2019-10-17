package com.capillary.ops.deployer.bo.systemcharts;

import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.bo.ISystemChart;
import com.capillary.ops.deployer.bo.K8sLoggingConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CapTransferLogsSystemChart implements ISystemChart {

    private static final Logger logger = LoggerFactory.getLogger(CapTransferLogsSystemChart.class);

    @Override
    public String getChartPath() {
        return "cap-transfer-logs";
    }

    @Override
    public Map<String, Object> getValues(ApplicationFamily applicationFamily, Environment environment) {
        Map<String, Object> values = new HashMap<>();
        K8sLoggingConfiguration logConfigs = environment.getEnvironmentConfiguration().getK8sLoggingConfiguration();
        if(logConfigs.getLogsS3Bucket() == null) {
            return null;
        }
        values.put("awsAccessKeyID",logConfigs.getAwsAccessKeyID());
        values.put("awsSecretAccessKey",logConfigs.getAwsSecretAccessKey());
        values.put("awsRegion",logConfigs.getAwsRegion());
        values.put("logsS3Bucket",logConfigs.getLogsS3Bucket());
        return values;
    }

    @Override
    public String getReleaseName(ApplicationFamily applicationFamily, Environment environment) {
        return "cap-transfer-logs";
    }
}
