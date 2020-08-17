package api.singtel.appkeyrecord.api.service;

import api.singtel.appkeyrecord.api.controller.AppKeyRecordDTO;
import api.singtel.appkeyrecord.api.model.AppKeyRecord;

public final class AppKeyRecordUtils {

    private AppKeyRecordUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static AppKeyRecord convertDTOtoAppKeyRecord(AppKeyRecordDTO dto, String app, int defaultTtl) {
        Integer ttl = dto.getTtl();
        if (ttl == null) {
            ttl = defaultTtl;
        }

        return new AppKeyRecord(app, dto.getKey(), dto.getValue(), ttl);
    }

}