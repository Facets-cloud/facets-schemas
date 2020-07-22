package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.bo.Application;

import java.util.*;

public class MavenIonicBuildSpec extends MavenBuildSpec {

    public MavenIonicBuildSpec(Application application) {
        super(application);
    }

    public MavenIonicBuildSpec(Application application, boolean testBuild) {
        super(application, testBuild);
    }

    @Override
    public String getBuildEnvironmentImage() {
        return "486456986266.dkr.ecr.us-west-1.amazonaws.com/ops/mavenionicbuildenv:7fd9327-1";
    }

    @Override
    protected Map<String, Object> getInstallPhaseTest() {
        List<String> installCommands = new ArrayList<>();
        Map<String, Object> installPhase = new HashMap<>();
        installCommands.add("apt-get install docker-ce=17.03.0~ce-0~ubuntu-xenial -y --allow-downgrades");
        installCommands.add(
                "nohup dockerd --host=unix:///var/run/docker.sock --host=tcp://0.0.0.0:2375 --storage-driver=overlay&");
        installCommands.add("timeout 15 sh -c \"until docker info; do echo .; sleep 1; done\"");
        installPhase.put("commands", installCommands);
        return installPhase;
    }

}
