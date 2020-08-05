package api.singtel.appkeyrecord;

import java.time.LocalDateTime;

public class AppKeyRecordUtils {

    public static boolean isExpired(AppKeyRecord record) {
        int ttl = record.getTtl();
        LocalDateTime recordedAt = record.getRecordedAt();

        LocalDateTime expireAt = recordedAt.plusSeconds(ttl);
        return expireAt.isBefore(LocalDateTime.now());
    }
    
}