package api.singtel.appkeyrecord.api;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apps/{app}/keys")
@Validated
public class AppKeyRecordController {

    private AppKeyRecordService service;

    public AppKeyRecordController(AppKeyRecordService service) {
        this.service = service;
    }

    @GetMapping(path = "/{key}", produces = "application/json")
    public AppKeyRecord getAppKey(@PathVariable("app") String app, @PathVariable("key") String key) {
        return service.get(app, key);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(code = HttpStatus.CREATED)
    public AppKeyRecord createAppKey(
            @PathVariable("app") @Pattern(regexp = "^[\\p{Alnum}]{1,256}$", message = "app in the URI must be alphanumeric and up to 256 characters") String app, 
            @RequestBody @Valid AppKeyRecordDTO appKeyRecordDTO) {
        return service.create(app, appKeyRecordDTO);
    }

}