package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.bo.Application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JDK6Maven2BuildSpec extends MavenBuildSpec {

    public JDK6Maven2BuildSpec(Application application) {
        super(application);
    }

    public JDK6Maven2BuildSpec(Application application, boolean testBuild) {
        super(application, testBuild);
    }

    @Override
    protected List<String> getPreBuildCommands() {
        List<String> preBuildCommands = super.getPreBuildCommands();
        preBuildCommands.add("ssh-keyscan mvnrepo.capillary.co.in >> /root/.ssh/known_hosts");
        return preBuildCommands;
    }

    @Override
    public String getBuildEnvironmentImage() {
        return "486456986266.dkr.ecr.us-west-1.amazonaws.com/crm/oraclejdk6maven2:1.0";
    }
}
