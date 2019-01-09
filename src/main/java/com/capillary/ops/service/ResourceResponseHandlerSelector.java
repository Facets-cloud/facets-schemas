package com.capillary.ops.service;

import com.capillary.ops.service.impl.MongoResourceResponseHandler;
import com.capillary.ops.service.impl.RedisResourceResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceResponseHandlerSelector {

  @Autowired private MongoResourceResponseHandler mongoResourceResponseHandler;

  @Autowired private RedisResourceResponseHandler redisResourceResponseHandler;

  public AbstractResourceResponseHandler selectHandler(String resourceType) {
    switch (resourceType) {
      case "mongodb":
        return mongoResourceResponseHandler;
      case "redis":
        return redisResourceResponseHandler;
      default:
        throw new RuntimeException(
            "cannot select unknown handler for resource type: " + resourceType);
    }
  }
}
