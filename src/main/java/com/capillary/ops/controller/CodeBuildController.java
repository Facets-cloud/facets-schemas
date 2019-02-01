package com.capillary.ops.controller;

import com.capillary.ops.bo.codebuild.CodeBuildApplication;
import com.capillary.ops.bo.codebuild.CodeBuildDetails;
import com.capillary.ops.service.CodeBuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CodeBuildController {

    @Autowired
    CodeBuildService codeBuildService;

    @PostMapping("codebuild/applications")
    public ResponseEntity<CodeBuildApplication> createApplication(@RequestBody CodeBuildApplication application) {
        CodeBuildApplication resApplication = codeBuildService.createApplication(application);
        return new ResponseEntity<>(resApplication, HttpStatus.CREATED);
    }

    @PostMapping("codebuild/applications/{applicationId}/build")
    public ResponseEntity<CodeBuildDetails> createBuild(@PathVariable(name = "applicationId") String applicationId,
                                                        @RequestBody CodeBuildDetails details) {
        CodeBuildDetails resDetails = codeBuildService.createBuild(applicationId, details);
        return new ResponseEntity<>(resDetails, HttpStatus.CREATED);
    }

    @GetMapping("codebuild/builds/{buildId}")
    public ResponseEntity<CodeBuildDetails> getBuildDetails(@PathVariable(name = "buildId") String buildId) {
        CodeBuildDetails resDetails = codeBuildService.getBuildDetails(buildId);
        HttpStatus status = (resDetails == null) ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return new ResponseEntity<>(resDetails, status);
    }

    @DeleteMapping("codebuild/builds/{buildId}")
    public ResponseEntity<CodeBuildDetails> stopBuild(@PathVariable(name = "buildId") String buildId) {
        CodeBuildDetails resDetails = codeBuildService.stopBuild(buildId);
        HttpStatus status = (resDetails == null) ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return new ResponseEntity<>(resDetails, status);
    }
}
