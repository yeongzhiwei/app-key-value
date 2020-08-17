package api.singtel.appkeyrecord.api.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import api.singtel.appkeyrecord.api.controller.AppKeyRecordDTO;
import api.singtel.appkeyrecord.api.model.AppKeyRecord;

class AppKeyRecordUtilsTests {

    @Test
    void convertDTOtoDomainGivenDTOShouldReturnAppKeyRecord() {
        AppKeyRecordDTO dto = new AppKeyRecordDTO("key1", "value1", 10);
        AppKeyRecord appKeyRecord = AppKeyRecordUtils.convertDTOtoAppKeyRecord(dto, "app1", 10);

        assertAll("record",
            () -> assertEquals("app1", appKeyRecord.getApp()),
            () -> assertEquals("key1", appKeyRecord.getKey()),
            () -> assertEquals("value1", appKeyRecord.getValue()),
            () -> assertEquals(10, appKeyRecord.getTtl())
        );
    }
    
    @Test
    void convertDTOtoDomainGivenDTOWithoutTtlShouldReturnAppKeyRecordWithDefaultTtl() {
        AppKeyRecordDTO dto = new AppKeyRecordDTO("key1", "value1");
        AppKeyRecord appKeyRecord = AppKeyRecordUtils.convertDTOtoAppKeyRecord(dto, "app1", 20);

        assertAll("record",
            () -> assertEquals("app1", appKeyRecord.getApp()),
            () -> assertEquals("key1", appKeyRecord.getKey()),
            () -> assertEquals("value1", appKeyRecord.getValue()),
            () -> assertEquals(20, appKeyRecord.getTtl())
        );
    }
}