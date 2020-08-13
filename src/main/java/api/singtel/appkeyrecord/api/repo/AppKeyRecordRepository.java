package api.singtel.appkeyrecord.api.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import api.singtel.appkeyrecord.api.model.AppKeyRecord;

public interface AppKeyRecordRepository extends CrudRepository<AppKeyRecord, String> {
    
    public List<AppKeyRecord> findAllByApp(String app);

    public List<AppKeyRecord> findAllByAppAndKey(String app, String key);

    public Optional<AppKeyRecord> findByAppAndKey(String app, String key);


}