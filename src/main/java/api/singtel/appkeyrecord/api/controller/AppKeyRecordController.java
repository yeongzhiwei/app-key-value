package api.singtel.appkeyrecord.api.controller;

import java.util.List;

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

import api.singtel.appkeyrecord.api.model.AppKeyRecord;
import api.singtel.appkeyrecord.api.service.AppKeyRecordService;

@RestController
@RequestMapping("/apps/{app}/keys")
@Validated
public class AppKeyRecordController {

    private AppKeyRecordService service;

    public AppKeyRecordController(AppKeyRecordService service) {
        this.service = service;
    }

    @GetMapping(produces = "application/json")
    public List<AppKeyRecord> getAll(@PathVariable("app") String app) {
        return service.getAll(app);
    }

    @GetMapping(path = "/{key}", produces = "application/json")
    public AppKeyRecord getOne(@PathVariable("app") String app, @PathVariable("key") String key) {
        return service.get(app, key);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(code = HttpStatus.CREATED)
    public AppKeyRecord createOne(
            @PathVariable("app") @Pattern(regexp = "^[\\p{Alnum}]{1,256}$", message = "app in the URI must be alphanumeric and up to 256 characters") String app, 
            @RequestBody @Valid AppKeyRecordDTO appKeyRecordDTO) {
        return service.create(app, appKeyRecordDTO);
    }

}