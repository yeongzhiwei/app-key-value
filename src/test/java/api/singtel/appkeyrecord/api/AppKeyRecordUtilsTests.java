package api.singtel.appkeyrecord.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

public class AppKeyRecordUtilsTests {

    @Test
    public void isExpiredWhenGivenNotExpiredRecordShouldReturnFalse() {
        AppKeyRecord hasNotExpiredRecord = new AppKeyRecord("app", "key", "value", 100, 
            LocalDateTime.now().minus(99, ChronoUnit.SECONDS));
        assertFalse(AppKeyRecordUtils.isExpired(hasNotExpiredRecord));
    }

    @Test
    public void isExpiredWhenGivenExpiredRecordShouldReturnTrue() {
        AppKeyRecord hasExpiredRecord = new AppKeyRecord("app", "key", "value", 100, 
            LocalDateTime.now().minus(101, ChronoUnit.SECONDS));
        assertTrue(AppKeyRecordUtils.isExpired(hasExpiredRecord));
    }

    @Test
    public void convertDTOtoDomainGivenDTOShouldReturnAppKeyRecord() {
        AppKeyRecordDTO dto = new AppKeyRecordDTO("key1", "value1", 10);
        AppKeyRecord appKeyRecord = AppKeyRecordUtils.convertDTOtoAppKeyRecord(dto, "app1", 10);

        assertEquals("app1", appKeyRecord.getApp());
        assertEquals("key1", appKeyRecord.getKey());
        assertEquals("value1", appKeyRecord.getValue());
        assertEquals(10, appKeyRecord.getTtl());
    }
    
    @Test
    public void convertDTOtoDomainGivenDTOWithoutTtlShouldReturnAppKeyRecordWithDefaultTtl() {
        AppKeyRecordDTO dto = new AppKeyRecordDTO("key1", "value1");
        AppKeyRecord appKeyRecord = AppKeyRecordUtils.convertDTOtoAppKeyRecord(dto, "app1", 20);

        assertEquals("app1", appKeyRecord.getApp());
        assertEquals("key1", appKeyRecord.getKey());
        assertEquals("value1", appKeyRecord.getValue());
        assertEquals(20, appKeyRecord.getTtl());
    }
}