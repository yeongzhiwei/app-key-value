package api.singtel.appkeyrecord.api;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;

import api.singtel.appkeyrecord.redis.RedisProperties;
import redis.embedded.RedisServer;

class BaseRedisIntegrationTests extends BaseIntegrationTests {
    
    @Autowired RedisProperties redisProperties; 
    private RedisServer redisServer;

    @BeforeAll
    void startRedis() throws IOException {
        redisServer = new RedisServer(redisProperties.getPort());
        redisServer.start();
    }

    @AfterAll
    void stopRedis() throws IOException {
        redisServer.stop();
    }

}