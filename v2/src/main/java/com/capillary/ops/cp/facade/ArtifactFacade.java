package com.capillary.ops.cp.facade;

import com.amazonaws.regions.Regions;
import com.capillary.ops.cp.bo.Artifact;
import com.capillary.ops.cp.bo.Artifactory;
import com.capillary.ops.cp.bo.BuildStrategy;
import com.capillary.ops.cp.bo.ECRArtifactory;
import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.capillary.ops.cp.exceptions.BadRequestException;
import com.capillary.ops.cp.repository.ArtifactRepository;
import com.capillary.ops.cp.repository.ArtifactoryRepository;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ArtifactFacade {
    @Autowired
    private ArtifactRepository artifactRepository;

    @Autowired
    private ArtifactoryRepository artifactoryRepository;

    public Map<String, Map<String, Artifact>> getAllArtifacts(BuildStrategy releaseStream, ReleaseType releaseType) {
        List<Artifact> artifacts = artifactRepository.findByReleaseStreamAndReleaseType(releaseStream, releaseType);
        return groupByArtifactoryAndMapApplicationName(artifacts);
    }

    public Map<String, Map<String, Artifact>> getAllArtifacts(List<String> artifactories, BuildStrategy releaseStream, ReleaseType releaseType) {
        List<Artifact> artifacts = new ArrayList<>();
        artifactRepository.findByArtifactoryInAndReleaseStreamAndReleaseType(artifactories, releaseStream, releaseType).forEach(artifacts::add);
        return groupByArtifactoryAndMapApplicationName(artifacts);
    }

    private Map<String, Map<String, Artifact>> groupByArtifactoryAndMapApplicationName(List<Artifact> artifacts){
        return artifacts.stream().collect(Collectors.groupingBy(x -> x.getArtifactory())).entrySet().stream()
                .collect(Collectors.toMap(x -> x.getKey(),
                        x -> x.getValue().stream().collect(
                                Collectors.toMap((Artifact y) -> y.getApplicationName(), (Artifact y) -> y))));
    }

    public List<Artifactory> getArtifactoriesByName(List<String> artifactories){
        List<Artifactory> artifactoryList = new ArrayList<>();
        artifactoryRepository.findByNameIn(artifactories).forEach(artifactoryList::add);
        return artifactoryList;
    }

    public void registerArtifact(Artifact artifact) {
        Optional<Artifact> artifactOptional = artifactRepository.findOneByArtifactoryAndApplicationNameAndReleaseStreamAndReleaseType(artifact.getArtifactory(),
                artifact.getApplicationName(), artifact.getReleaseStream(), artifact.getReleaseType());
        if(artifactOptional.isPresent()) {
            Artifact existing = artifactOptional.get();
            existing.setArtifactUri(artifact.getArtifactUri());
            existing.setBuildId(artifact.getBuildId());
            artifactRepository.save(existing);
        } else {
            artifactRepository.save(artifact);
        }
    }

    public ECRArtifactory createECRArtifactory(ECRArtifactory ecrArtifactory){
        try{
            Regions.fromName(ecrArtifactory.getAwsRegion());
        } catch (IllegalArgumentException e){
            throw new BadRequestException("invalid aws region: " + ecrArtifactory.getAwsRegion());
        }
        String uri = createUri(ecrArtifactory);
        ecrArtifactory.setUri(uri);
        return artifactoryRepository.save(ecrArtifactory);
    }

    public ECRArtifactory updateECRArtifactory(ECRArtifactory ecrArtifactory, String artifactoryId){
        try{
            Regions.fromName(ecrArtifactory.getAwsRegion());
        } catch (IllegalArgumentException e){
            throw new BadRequestException("invalid aws region: " + ecrArtifactory.getAwsRegion());
        }
        ECRArtifactory artifactoryExisting = (ECRArtifactory) artifactoryRepository.findById(artifactoryId).orElseThrow(() -> new NotFoundException("could not find artifactory with the given id " + artifactoryId));

        artifactoryExisting.setAwsAccountId(ecrArtifactory.getAwsAccountId());
        artifactoryExisting.setAwsRegion(ecrArtifactory.getAwsRegion());
        artifactoryExisting.setAwsKey(ecrArtifactory.getAwsKey());
        artifactoryExisting.setAwsSecret(ecrArtifactory.getAwsSecret());
        artifactoryExisting.setName(ecrArtifactory.getName());
        String uri = createUri(ecrArtifactory);
        artifactoryExisting.setUri(uri);
        return artifactoryRepository.save(artifactoryExisting);
    }

    public List<Artifactory> getAllArtifactories(){
        return artifactoryRepository.findAll();
    }

    private String createUri(ECRArtifactory ecrArtifactory){
        String chinaSuffix = "";
        if (ecrArtifactory.getAwsRegion().equals("cn-north-1")){
            chinaSuffix = ".cn";
        }
        return String.format("%s.dkr.ecr.%s.amazonaws.com%s", ecrArtifactory.getAwsAccountId(), ecrArtifactory.getAwsRegion(), chinaSuffix);
    }
}
