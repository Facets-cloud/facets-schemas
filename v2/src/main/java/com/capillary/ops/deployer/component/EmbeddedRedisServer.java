package com.capillary.ops.deployer.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Profile("dev")
@Component
public class EmbeddedRedisServer {

    private RedisServer redisServer;

    public EmbeddedRedisServer() {
        this.redisServer = new RedisServer();
    }

    @PostConstruct
    public void postConstruct() {
        redisServer.start();
    }

    @PreDestroy
    public void preDestroy() {
        redisServer.stop();
    }
}
