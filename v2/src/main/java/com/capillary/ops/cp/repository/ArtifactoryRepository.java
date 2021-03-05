package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.Artifactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ArtifactoryRepository extends MongoRepository<Artifactory, String> {

    Iterable<Artifactory> findByNameIn(Iterable<String> names);

    Optional<Artifactory> findByName(String name);
}
