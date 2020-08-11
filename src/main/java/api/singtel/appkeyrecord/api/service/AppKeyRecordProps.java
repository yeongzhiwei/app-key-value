package api.singtel.appkeyrecord.api.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix="api.singtel.appkeyrecord")
@Data
public class AppKeyRecordProps {
    
    private int defaultTtl = 10;

}