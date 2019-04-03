package com.capillary.ops.repository.helm;

import com.capillary.ops.bo.helm.CrmHelmApplication;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CrmHelmApplicationRepository extends MongoRepository<CrmHelmApplication, String> {}
