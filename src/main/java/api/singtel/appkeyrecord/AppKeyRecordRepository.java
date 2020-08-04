package api.singtel.appkeyrecord;

import java.util.Optional;

public interface AppKeyRecordRepository {
    
    public void deleteByAppAndKey(String app, String key);

    public Optional<AppKeyRecord> findByAppAndKey(String app, String key);

    public AppKeyRecord save(AppKeyRecord appkey);
}