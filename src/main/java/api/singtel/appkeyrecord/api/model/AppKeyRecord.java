package api.singtel.appkeyrecord.api.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@RedisHash("AppKeyRecord")
@Data
@NoArgsConstructor
public class AppKeyRecord {
    
    @Id
    @ApiModelProperty(notes = "The Redis generated hash")
    private String id;

    @Indexed
    @ApiModelProperty(notes = "The client application name", required = true)
    private String app; 

    @Indexed
    @ApiModelProperty(notes = "The application-specific key", required = true)
    private String key;

    @ApiModelProperty(notes = "The value mapped to app and key", required = true)
    private String value;

    @TimeToLive
    @ApiModelProperty(notes = "The time to live, in seconds")
    private int ttl;
    
    @ApiModelProperty(notes = "The date & time the record is created")
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