package com.capillary.ops.deployer.repository;

import com.capillary.ops.deployer.bo.PullRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PullRequestRepository extends MongoRepository<PullRequest, String> {
    public List<PullRequest> findAllByBuildStatusAndUpdatedAtAfter(StatusType buildStatus, Date updatedAt);
    public List<PullRequest> findAllByApplicationIdAndSha(String applicationId, String sha);
    public Optional<PullRequest> findById(String pullRequestId);
}
