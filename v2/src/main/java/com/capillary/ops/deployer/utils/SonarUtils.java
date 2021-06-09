package com.capillary.ops.deployer.utils;

import com.capillary.ops.deployer.service.buildspecs.BuildSpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class SonarUtils {

    @Value("${sonar.url}")
    private String sonarUrl;

    @PostConstruct
    public void setSonarUrl() {
        BuildSpec.setSonarUrl(sonarUrl);
    }

}
