package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.providedresources.ProvidedResources;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProvidedResourcesRepository extends MongoRepository<ProvidedResources, String> {
  public Optional<ProvidedResources> findOneByClusterId(String clusterId);
}
