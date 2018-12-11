package com.capillary.ops.repository;

import com.capillary.ops.bo.InstanceType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InstanceTypeRepository extends MongoRepository<InstanceType, String> {

    public List<InstanceType> findByName(String name);

}
