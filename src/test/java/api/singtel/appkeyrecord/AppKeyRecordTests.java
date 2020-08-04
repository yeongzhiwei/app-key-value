package api.singtel.appkeyrecord;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AppKeyRecordTests {

    @Test
    public void createRecordShouldReturnObject() {
        AppKeyRecord record = new AppKeyRecord("app1", "key1", "value1");

        assertEquals("app1", record.getApp());
        assertEquals("key1", record.getKey());
        assertEquals("value1", record.getValue());
    }

    public void setPropertiesShouldUpdateProperties() {
        AppKeyRecord record = new AppKeyRecord("app1", "key1", "value1");

        record.setApp("app2");
        assertEquals("app2", record.getApp());

        record.setKey("key2");
        assertEquals("key2", record.getKey());

        record.setValue("value2");
        assertEquals("value2", record.getValue());
    }

}
