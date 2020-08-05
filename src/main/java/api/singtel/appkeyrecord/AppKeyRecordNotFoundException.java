package api.singtel.appkeyrecord;

public class AppKeyRecordNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    AppKeyRecordNotFoundException(String app, String key) {
        super(String.format("Could not find record for app (%s) and key (%s). It either does not exist or has expired.", app, key));
    }

}