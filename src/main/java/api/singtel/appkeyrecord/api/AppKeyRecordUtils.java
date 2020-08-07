package api.singtel.appkeyrecord.api;

import java.time.LocalDateTime;

public final class AppKeyRecordUtils {

    public static boolean isExpired(AppKeyRecord record) {
        int ttl = record.getTtl();
        LocalDateTime recordedAt = record.getRecordedAt();

        LocalDateTime expireAt = recordedAt.plusSeconds(ttl);
        return expireAt.isBefore(LocalDateTime.now());
    }

    public static AppKeyRecord convertDTOtoAppKeyRecord(AppKeyRecordDTO dto, String app, int defaultTtl) {
        Integer ttl = dto.getTtl();
        if (ttl == null) {
            ttl = defaultTtl;
        }

        return new AppKeyRecord(app, dto.getKey(), dto.getValue(), ttl);
    }

}