package api.singtel.appkeyrecord;

public class AppKeyRecord {

    private String app;
    private String key;
    private String value;

    public AppKeyRecord(String app, String key, String value) {
        this.app = app;
        this.key = key;
        this.value = value;
    }

    public String getApp() {
        return app;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

}