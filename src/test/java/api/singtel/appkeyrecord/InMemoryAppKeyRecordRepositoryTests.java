package api.singtel.appkeyrecord;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public class InMemoryAppKeyRecordRepositoryTests {

    @Test
    public void saveGetDeleteShouldUpdateRepository() {
        InMemoryAppKeyRecordRepository repo = new InMemoryAppKeyRecordRepository();
        
        AppKeyRecord savedRecord = repo.save(new AppKeyRecord("app1", "key1", "value1"));
        assertEquals("app1", savedRecord.getApp());
        assertEquals("key1", savedRecord.getKey());
        assertEquals("value1", savedRecord.getValue());

        Optional<AppKeyRecord> validOptionalRecord = repo.findByAppAndKey("app1", "key1");
        assertTrue(validOptionalRecord.isPresent());
        AppKeyRecord validRecord = validOptionalRecord.get();
        assertEquals("value1", validRecord.getValue());

        assertFalse(repo.findByAppAndKey("app2", "key1").isPresent());
        assertFalse(repo.findByAppAndKey("app1", "key2").isPresent());

        repo.deleteByAppAndKey("app1", "key1");
        assertFalse(repo.findByAppAndKey("app1", "key1").isPresent());
    }
}