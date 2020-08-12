package api.singtel.appkeyrecord.api;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;

import api.singtel.appkeyrecord.redis.RedisProperties;
import redis.embedded.RedisServer;

public class BaseRedisIntegrationTests extends BaseIntegrationTests {
    
    @Autowired RedisProperties redisProperties; 
    private RedisServer redisServer;

    @BeforeAll
    public void startRedis() throws IOException {
        redisServer = new RedisServer(redisProperties.getPort());
        redisServer.start();
    }

    @AfterAll
    public void stopRedis() throws IOException {
        redisServer.stop();
    }

}