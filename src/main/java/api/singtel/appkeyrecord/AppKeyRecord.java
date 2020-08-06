package api.singtel.appkeyrecord;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppKeyRecord {
    
    private String app; 
    private String key;
    private String value;
    private int ttl = 5;
    private LocalDateTime recordedAt = LocalDateTime.now();

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