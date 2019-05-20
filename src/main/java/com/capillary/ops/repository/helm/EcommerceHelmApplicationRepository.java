package com.capillary.ops.repository.helm;

import com.capillary.ops.bo.helm.HelmApplication;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EcommerceHelmApplicationRepository
    extends MongoRepository<HelmApplication, String> {
  public List<HelmApplication> findByName(String name);
}
