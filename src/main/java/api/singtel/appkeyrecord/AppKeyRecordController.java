package api.singtel.appkeyrecord;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apps/{app}/keys")
public class AppKeyRecordController {

    private AppKeyRecordRepository repo;

    public AppKeyRecordController(AppKeyRecordRepository repo) {
        this.repo = repo;
    }

    @GetMapping(path = "/{key}", produces = "application/json")
    public AppKeyRecord getAppKey(@PathVariable("app") String app, @PathVariable("key") String key) {
        return repo.findByAppAndKey(app, key).orElseThrow(() -> new AppKeyRecordNotFoundException(app, key));
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(code = HttpStatus.CREATED)
    public AppKeyRecord createAppKey(@PathVariable("app") String app, @RequestBody AppKeyRecord appKeyRecord) {
        appKeyRecord.setApp(app);
        return repo.save(appKeyRecord);
    }

}