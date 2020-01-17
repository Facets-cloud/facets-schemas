package com.capillary.ops.deployer.service.capillaryCloud;

import com.capillary.ops.deployer.bo.capillaryCloud.Cluster;
import com.capillary.ops.deployer.bo.capillaryCloud.ProcessExecutionResult;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class TerraformService {

    public ProcessExecutionResult plan(Cluster cluster) {
        try(TerraformEnvironment terraformEnvironment = new TerraformEnvironment(cluster)) {
            return terraformEnvironment.plan();
        }
    }

    public ProcessExecutionResult apply(Cluster cluster) {
        try(TerraformEnvironment terraformEnvironment = new TerraformEnvironment(cluster)) {
            return terraformEnvironment.apply();
        }
    }

    public ProcessExecutionResult destroy(Cluster cluster) {
        try(TerraformEnvironment terraformEnvironment = new TerraformEnvironment(cluster)) {
            return terraformEnvironment.destroy();
        }
    }
}
