package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.Stack;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StackRepository extends MongoRepository<Stack, String> {


}
