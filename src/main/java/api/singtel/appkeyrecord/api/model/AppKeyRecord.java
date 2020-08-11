package api.singtel.appkeyrecord.api.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.Data;
import lombok.NoArgsConstructor;

@RedisHash("AppKeyRecord")
@Data
@NoArgsConstructor
public class AppKeyRecord {
    
    @Id
    private String id;

    @Indexed
    private String app; 

    @Indexed
    private String key;

    private String value;

    private int ttl;
    
    private LocalDateTime recordedAt = LocalDateTime.now();

    public AppKeyRecord(String app, String key, String value, int ttl, LocalDateTime recordedAt) {
        this(app, key, value, ttl);
        this.recordedAt = recordedAt;
    }

    public AppKeyRecord(String app, String key, String value, int ttl) {
        this.app = app;
        this.key = key;
        this.value = value;
        this.ttl = ttl;
    }

}