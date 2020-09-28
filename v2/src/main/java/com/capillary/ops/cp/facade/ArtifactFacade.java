package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.Artifact;
import com.capillary.ops.cp.bo.BuildStrategy;
import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.capillary.ops.cp.repository.ArtifactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ArtifactFacade {
    @Autowired
    private ArtifactRepository artifactRepository;

    public Map<String, Map<String, Artifact>> getAllArtifacts(BuildStrategy releaseStream, ReleaseType releaseType) {
        List<Artifact> artifacts = artifactRepository.findByReleaseStreamAndReleaseType(releaseStream, releaseType);
        return artifacts.stream().collect(Collectors.groupingBy(x -> x.getArtifactory())).entrySet().stream()
                .collect(Collectors.toMap(x -> x.getKey(),
                        x -> x.getValue().stream().collect(
                                Collectors.toMap((Artifact y) -> y.getApplicationName(), (Artifact y) -> y))));
    }

    public void registerArtifact(Artifact artifact) {
        Optional<Artifact> artifactOptional = artifactRepository.findOneByArtifactoryAndApplicationNameAndReleaseStreamAndReleaseType(artifact.getArtifactory(),
                artifact.getApplicationName(), artifact.getReleaseStream(), artifact.getReleaseType());
        if(artifactOptional.isPresent()) {
            Artifact existing = artifactOptional.get();
            existing.setArtifactUri(artifact.getArtifactUri());
            artifactRepository.save(existing);
        } else {
            artifactRepository.save(artifact);
        }
    }
}
