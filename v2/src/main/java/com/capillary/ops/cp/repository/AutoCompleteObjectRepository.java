package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.Artifact;
import com.capillary.ops.cp.bo.AutoCompleteObject;
import com.capillary.ops.cp.bo.BuildStrategy;
import com.capillary.ops.cp.bo.requests.ReleaseType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutoCompleteObjectRepository extends MongoRepository<AutoCompleteObject, String> {

    List<AutoCompleteObject> findAllByStackName(String stackName);

    List<AutoCompleteObject> findAllByStackNameAndResourceType(String stackName, String resourceType);
}
