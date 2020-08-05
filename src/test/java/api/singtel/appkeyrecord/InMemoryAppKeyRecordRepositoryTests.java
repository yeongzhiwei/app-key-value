package api.singtel.appkeyrecord;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InMemoryAppKeyRecordRepositoryTests {

    private InMemoryAppKeyRecordRepository repo = new InMemoryAppKeyRecordRepository();

    @BeforeEach
    public void seedData() {
        repo.save(new AppKeyRecord("app1", "key1", "value1"));
    }

    @Test
    public void saveGivenRecordShouldReturnRecord() {
        AppKeyRecord record = new AppKeyRecord("app1", "key1", "value1");
        AppKeyRecord savedRecord = repo.save(record);
        assertEquals(record, savedRecord);
    }
    
    @Test
    public void findByAppAndKeyGivenValidArgsShouldReturnRecordOptional() {
        Optional<AppKeyRecord> optional = repo.findByAppAndKey("app1", "key1");
        assertTrue(optional.isPresent());
        AppKeyRecord validRecord = optional.get();
        assertEquals("value1", validRecord.getValue());
    }

    @Test
    public void findByAppAndKeyGivenInvalidAppShouldReturnNullOptional() {
        Optional<AppKeyRecord> optional = repo.findByAppAndKey("app2", "key1");
        assertFalse(optional.isPresent());
    }

    @Test
    public void findByAppAndKeyGivenInvalidKeyShouldReturnNullOptional() {
        Optional<AppKeyRecord> optional = repo.findByAppAndKey("app1", "key2");
        assertFalse(optional.isPresent());
    }

    @Test
    public void deleteByAppAndKeyGivenValidArgsShouldUpdateRepository() {
        repo.deleteByAppAndKey("app1", "key1");
        assertFalse(repo.findByAppAndKey("app1", "key1").isPresent());
    }
}