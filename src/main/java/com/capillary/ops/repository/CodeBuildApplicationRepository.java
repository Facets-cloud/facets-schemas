package com.capillary.ops.repository;

import com.capillary.ops.bo.codebuild.CodeBuildApplication;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CodeBuildApplicationRepository
    extends MongoRepository<CodeBuildApplication, String> {

  CodeBuildApplication findByName(String name);
}
