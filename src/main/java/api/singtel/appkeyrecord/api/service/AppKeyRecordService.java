package api.singtel.appkeyrecord.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import api.singtel.appkeyrecord.api.controller.AppKeyRecordDTO;
import api.singtel.appkeyrecord.api.model.AppKeyRecord;
import api.singtel.appkeyrecord.api.model.AppKeyRecordNotFoundException;
import api.singtel.appkeyrecord.api.repo.AppKeyRecordRepository;

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
        return repo.findByAppAndKey(app, key).orElseThrow(() -> new AppKeyRecordNotFoundException(app, key));
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