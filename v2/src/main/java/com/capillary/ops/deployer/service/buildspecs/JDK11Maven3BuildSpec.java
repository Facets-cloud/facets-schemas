package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDK11Maven3BuildSpec extends MavenBuildSpec {

    public JDK11Maven3BuildSpec(Application application, String sonarUrl) {
        super(application, sonarUrl);
    }

    public JDK11Maven3BuildSpec(Application application, boolean testBuild, List<Registry> registries, String sonarUrl) {
        super(application, testBuild, registries, sonarUrl);
    }

    @Override
    public Map<String, Object> getInstallPhase() {
        List<String> installCommands = new ArrayList<>();
        Map<String, Object> installPhase = new HashMap<>();
        if(configureDockerBuildSteps()) {
            installCommands.add(
                    "nohup dockerd --host=unix:///var/run/docker.sock --host=tcp://0.0.0.0:2375 --storage-driver=overlay&");
            installCommands.add("timeout 25 sh -c \"until docker info; do echo .; sleep 1; done\"");
        }
        installPhase.put("commands", installCommands);
        return installPhase;
    }

    @Override
    public String getBuildEnvironmentImage() {
        return "486456986266.dkr.ecr.us-west-1.amazonaws.com/ops/maven3openjdk11:v1.2";
    }
}
