package api.singtel.appkeyrecord.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix="spring.redis")
@Data
public class RedisProperties {
    
    private int port;
    private String host;

}