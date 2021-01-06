package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.Substack;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubstackRepository extends MongoRepository<Substack, String> {
}
