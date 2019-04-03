package com.capillary.ops.repository.helm;

import com.capillary.ops.bo.helm.HelmApplication;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EcommerceHelmApplicationRepository extends MongoRepository<HelmApplication, String> {
    public List<HelmApplication> findByName(String name);
}
