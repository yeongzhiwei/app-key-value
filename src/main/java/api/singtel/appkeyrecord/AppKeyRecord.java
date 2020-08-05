package api.singtel.appkeyrecord;

import lombok.Data;

@Data
public class AppKeyRecord {

    private String app;
    private String key;
    private String value;

    public AppKeyRecord(String app, String key, String value) {
        this.app = app;
        this.key = key;
        this.value = value;
    }

}