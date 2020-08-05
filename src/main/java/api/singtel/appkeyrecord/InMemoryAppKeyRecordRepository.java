package api.singtel.appkeyrecord;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryAppKeyRecordRepository implements AppKeyRecordRepository {

    Map<String, AppKeyRecord> records = new HashMap<>();

    @Override
    public void deleteByAppAndKey(String app, String key) {
        String mapKey = getMapKey(app, key);
        records.remove(mapKey);
    }

    @Override
    public Optional<AppKeyRecord> findByAppAndKey(String app, String key) {
        String mapKey = getMapKey(app, key);
        AppKeyRecord record = records.get(mapKey);
        if (record != null && AppKeyRecordUtils.isExpired(record)) {
            delete(record);
            return Optional.empty();
        }
        return Optional.ofNullable(record);
    }

    @Override
    public AppKeyRecord save(AppKeyRecord appKey) {
        String mapKey = getMapKey(appKey.getApp(), appKey.getKey());
        records.put(mapKey, appKey);
        return appKey;
    }
    
    private String getMapKey(String app, String key) {
        return app + ":" + key;
    }

    private void delete(AppKeyRecord record) {
        deleteByAppAndKey(record.getApp(), record.getKey());
    }

}