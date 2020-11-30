package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.AutoCompleteObject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutoCompleteObjectRepository extends MongoRepository<AutoCompleteObject, String> {

    List<AutoCompleteObject> findAllByStackNameIn(List<String> stackName);

    Optional<AutoCompleteObject> findOneByStackNameAndResourceType(String stackName, String resourceType);

    List<AutoCompleteObject> findAllByStackNameInAndResourceType(List<String> stackName, String resourceType);
}
