package com.capillary.ops.service;

import com.capillary.ops.bo.codebuild.CodeBuildApplication;
import com.capillary.ops.service.impl.CodeBuildServiceMavenImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CodeBuildServiceFactory {

    @Autowired
    CodeBuildServiceMavenImpl codeBuildServiceMavenImpl;

    public CodeBuildService getCodeBuildService(CodeBuildApplication.ApplicationType applicationType) {
        switch (applicationType) {
            case MAVEN_JAVA:
                return codeBuildServiceMavenImpl;
        }
        return null;
    }

    public CodeBuildService getDefaultBuildService() {
        return codeBuildServiceMavenImpl;
    }
}
