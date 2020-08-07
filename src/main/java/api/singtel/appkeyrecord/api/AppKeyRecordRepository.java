package api.singtel.appkeyrecord.api;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface AppKeyRecordRepository extends CrudRepository<AppKeyRecord, String> {
    
    public void deleteAllByAppAndKey(String app, String key);

    public Optional<AppKeyRecord> findByAppAndKey(String app, String key);

    public List<AppKeyRecord> findAllByApp(String app);

}