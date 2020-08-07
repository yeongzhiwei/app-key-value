package api.singtel.appkeyrecord.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AppKeyRecordServiceTests {

    private AppKeyRecordService service;

    @Mock
    private AppKeyRecordRepository repo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new AppKeyRecordService(repo);

        when(repo.findByAppAndKey("app1", "keyExists")).thenReturn(Optional.of(new AppKeyRecord("app1", "keyExists", "value1", 100)));
        when(repo.findByAppAndKey("app1", "keyDoesNotExist")).thenThrow(new AppKeyRecordNotFoundException("app1", "keyDoesNotExist"));
        when(repo.findByAppAndKey("app1", "keyExpired")).thenReturn(Optional.of(new AppKeyRecord("app1", "keyExpired", "value1", 10, LocalDateTime.now().minusSeconds(20))));
        doNothing().when(repo).delete(any(AppKeyRecord.class));
        when(repo.save(any(AppKeyRecord.class))).thenAnswer(i -> i.getArguments()[0]);
    }

    @Test
    public void getGivenValidAppAndKeyShouldReturnRecord() throws Exception {
        AppKeyRecord actualRecord = service.get("app1", "keyExists");
        assertEquals("app1", actualRecord.getApp());
        assertEquals("keyExists", actualRecord.getKey());
        assertEquals("value1", actualRecord.getValue());
    }

    @Test
    public void getGivenInvalidAppAndKeyShouldThrowAppKeyRecordNotFoundException() throws Exception {
        assertThrows(AppKeyRecordNotFoundException.class, () -> {
            service.get("app1", "keyDoesNotExist");
        });
    }

    @Test
    public void getGivenExpiredAppAndKeyShouldThrowAppKeyRecordNotFoundException() throws Exception {
        assertThrows(AppKeyRecordNotFoundException.class, () -> {
            service.get("app1", "keyExpired");
        });
    }

    @Test
    public void createGivenValidAppAndDtoShouldReturnRecord() throws Exception {
        AppKeyRecordDTO dto = new AppKeyRecordDTO("key1", "value1", 100);
        AppKeyRecord actualRecord = service.create("app1", dto);
        assertEquals("app1", actualRecord.getApp());
        assertEquals("key1", actualRecord.getKey());
        assertEquals("value1", actualRecord.getValue());
        assertEquals(100, actualRecord.getTtl());
    }

    @Test
    public void deleteGivenAnyRecordShouldReturnNothing() throws Exception {
        service.delete(new AppKeyRecord("app1", "keyExists", "value1"));
    }
}