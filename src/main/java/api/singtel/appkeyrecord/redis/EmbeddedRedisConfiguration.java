package api.singtel.appkeyrecord.redis;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.embedded.RedisServer;

@Component
public class EmbeddedRedisConfiguration {

    @Value("${api.singtel.embedded-redis-server.enabled:false}") private boolean enabled;
    private int redisPort;
    private RedisServer redisServer;

    public EmbeddedRedisConfiguration(RedisProperties redisProperties) {
        this.redisPort = redisProperties.getPort();
    }

    @PostConstruct
    public void startRedis() {
        if (enabled) {
            redisServer = new RedisServer(redisPort);
            redisServer.start();
        }
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

}