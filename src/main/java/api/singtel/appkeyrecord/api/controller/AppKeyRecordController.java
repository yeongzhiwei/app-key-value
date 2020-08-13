package api.singtel.appkeyrecord.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import api.singtel.appkeyrecord.api.model.AppKeyRecord;
import api.singtel.appkeyrecord.api.service.AppKeyRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/apps/{app}/keys")
@Validated
@Api(description = "Read, create, and delete app:key-value records")
public class AppKeyRecordController {

    private AppKeyRecordService service;

    public AppKeyRecordController(AppKeyRecordService service) {
        this.service = service;
    }

    @GetMapping(produces = "application/json")
    @ApiOperation(value = "Get a list of records by app")
    public List<AppKeyRecord> getAll(@PathVariable("app") String app) {
        return service.getAll(app);
    }

    @GetMapping(path = "/{key}", produces = "application/json")
    @ApiOperation(value = "Get a record by app and key")
    public AppKeyRecord getOne(@PathVariable("app") String app, @PathVariable("key") String key) {
        return service.get(app, key);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiOperation(value = "Create a record by app")
    public AppKeyRecord createOne(
            @PathVariable("app") @AlnumSizePattern(message = "app must be alphanumeric and up to 256 characters") String app, 
            @RequestBody @Valid AppKeyRecordDTO dto) {
        return service.create(app, dto);
    }

    @DeleteMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete all records by app")
    public void deleteAll(
            @PathVariable("app") @AlnumSizePattern(message = "app must be alphanumeric and up to 256 characters") String app) {
        service.delete(app);
    }

    @DeleteMapping(path = "/{key}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a record by app and key")
    public void deleteOne(
            @PathVariable("app") @AlnumSizePattern(message = "app must be alphanumeric and up to 256 characters") String app,
            @PathVariable("key") @AlnumSizePattern(message = "key must be alphanumeric and up to 256 characters") String key) {
        service.delete(app, key);
    }
    
}