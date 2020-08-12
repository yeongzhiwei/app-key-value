package api.singtel.appkeyrecord.api.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import api.singtel.appkeyrecord.api.controller.AppKeyRecordDTO;
import api.singtel.appkeyrecord.api.model.AppKeyRecord;
import api.singtel.appkeyrecord.api.model.AppKeyRecordNotFoundException;
import api.singtel.appkeyrecord.api.repo.AppKeyRecordRepository;

public class AppKeyRecordServiceTests {

    private AppKeyRecordService service;

    @Mock private AppKeyRecordRepository repo;
    @Mock private AppKeyRecordProps props;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new AppKeyRecordService(repo, props);

        AppKeyRecord validRecord = new AppKeyRecord("app1", "keyExists", "value1", 100);
        AppKeyRecord expiredRecord = new AppKeyRecord("app1", "keyExpired", "value1", 10, LocalDateTime.now().minusSeconds(20));

        when(props.getDefaultTtl()).thenReturn(10);
        when(repo.findAllByApp("app1")).thenReturn(Arrays.asList(validRecord, expiredRecord));
        when(repo.findByAppAndKey("app1", "keyExists")).thenReturn(Optional.of(validRecord));
        when(repo.findByAppAndKey("app1", "keyDoesNotExist")).thenThrow(new AppKeyRecordNotFoundException("app1", "keyDoesNotExist"));
        when(repo.findByAppAndKey("app1", "keyExpired")).thenReturn(Optional.empty());
        doNothing().when(repo).delete(any(AppKeyRecord.class));
        when(repo.save(any(AppKeyRecord.class))).thenAnswer(i -> i.getArguments()[0]);
    }

    @Test
    public void getAllGivenValidAppShouldReturnRecords() throws Exception {
        List<AppKeyRecord> actualRecords = service.getAll("app1");
        assertEquals(2, actualRecords.size());
        assertThat(actualRecords, hasItem(hasProperty("app", is("app1"))));
    }

    @Test
    public void getAllGivenInvalidAppShouldReturnEmptyList() throws Exception {
        List<AppKeyRecord> actualRecords = service.getAll("appDoesNotExist");
        assertEquals(0, actualRecords.size());
    }

    @Test
    public void getGivenValidAppAndKeyShouldReturnRecord() throws Exception {
        AppKeyRecord actualRecord = service.get("app1", "keyExists");
        assertAll("record",
            () -> assertEquals("app1", actualRecord.getApp()),
            () -> assertEquals("keyExists", actualRecord.getKey()),
            () -> assertEquals("value1", actualRecord.getValue())
        );
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
        assertAll("record",
            () -> assertEquals("app1", actualRecord.getApp()),
            () -> assertEquals("key1", actualRecord.getKey()),
            () -> assertEquals("value1", actualRecord.getValue()),
            () -> assertEquals(100, actualRecord.getTtl())
        );
    }

    @Test
    public void deleteGivenAnyRecordShouldReturnNothing() throws Exception {
        service.delete(new AppKeyRecord("app1", "keyExists", "value1", 10));
    }
}