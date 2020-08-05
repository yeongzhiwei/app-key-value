package api.singtel.appkeyrecord;

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

}