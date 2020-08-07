package api.singtel.appkeyrecord.redis;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import redis.embedded.RedisServer;

@Profile("dev")
@Component
public class EmbeddedRedisConfiguration {

    private int redisPort;
    private RedisServer redisServer;

    public EmbeddedRedisConfiguration(RedisProperties redisProperties) {
        this.redisPort = redisProperties.getPort();
    }

    @PostConstruct
    public void startRedis() throws IOException {
        redisServer = new RedisServer(redisPort);
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }

}