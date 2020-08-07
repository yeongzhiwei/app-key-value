package api.singtel.appkeyrecord.api;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RedisHash("AppKeyRecord")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppKeyRecord {
    
    @Id
    private String id;

    @Indexed
    private String app; 

    @Indexed
    private String key;

    private String value;

    private int ttl = 5;
    
    private LocalDateTime recordedAt = LocalDateTime.now();

    public AppKeyRecord(String app, String key, String value, int ttl, LocalDateTime recordedAt) {
        this(app, key, value, ttl);
        this.recordedAt = recordedAt;
    }

    public AppKeyRecord(String app, String key, String value, int ttl) {
        this(app, key, value);
        this.ttl = ttl;
    }

    public AppKeyRecord(String app, String key, String value) {
        this.app = app;
        this.key = key;
        this.value = value;
    }

}