package com.capillary.ops.repository.helm;

import com.capillary.ops.bo.helm.HelmApplication;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IntegrationsHelmApplicationRepository extends MongoRepository<HelmApplication, String> {
}
