package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.Artifact;
import com.capillary.ops.cp.bo.BuildStrategy;
import com.capillary.ops.cp.bo.requests.ReleaseType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtifactRepository extends MongoRepository<Artifact, String> {
    Optional<Artifact> findOneByArtifactoryAndApplicationNameAndReleaseStreamAndReleaseType(String artifactory, String applicationName, BuildStrategy releaseStream, ReleaseType releaseType);
    List<Artifact> findByReleaseStreamAndReleaseType(BuildStrategy releaseStream, ReleaseType releaseType);

    Iterable<Artifact> findByArtifactoryInAndReleaseStreamAndReleaseType(Iterable<String> artifactories, BuildStrategy releaseStream, ReleaseType releaseType);
}
