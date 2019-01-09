package com.capillary.ops.repository.redis;

import com.capillary.ops.bo.Environments;
import com.capillary.ops.bo.redis.RedisResource;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RedisInfraRepository extends MongoRepository<RedisResource, String> {

  public List<RedisResource> findByResourceNameAndEnvironment(
      String resourceName, Environments environment);
}
