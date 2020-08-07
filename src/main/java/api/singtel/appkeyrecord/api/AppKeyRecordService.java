package api.singtel.appkeyrecord.api;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class AppKeyRecordService {

    private AppKeyRecordRepository repo;
    private int defaultTtl;

    public AppKeyRecordService(AppKeyRecordRepository repo, AppKeyRecordProps props) {
        this.repo = repo;
        this.defaultTtl = props.getDefaultTtl();
    }

    public List<AppKeyRecord> getAll(String app) {
        return repo.findAllByApp(app);
    }

    public AppKeyRecord get(String app, String key) {
        AppKeyRecord record = repo.findByAppAndKey(app, key).orElseThrow(() -> new AppKeyRecordNotFoundException(app, key));
        if (AppKeyRecordUtils.isExpired(record)) {
            delete(record);
            throw new AppKeyRecordNotFoundException(app, key); 
        }
        return record;
    }

    public AppKeyRecord create(String app, AppKeyRecordDTO dto) {
        repo.findByAppAndKey(app, dto.getKey()).ifPresent(repo::delete);

        AppKeyRecord record = AppKeyRecordUtils.convertDTOtoAppKeyRecord(dto, app, defaultTtl);
        return repo.save(record);
    }

    public void delete(AppKeyRecord record) {
        repo.delete(record);
    }

}