package com.capillary.ops.repository;

import com.capillary.ops.bo.HelmInfrastructureResource;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HelmInfrastructureRepository
    extends MongoRepository<HelmInfrastructureResource, String> {
  //    public List<HelmInfrastructureResource> findByNameAndChartVersion(String name, String
  // chartVersion);
}
