package api.singtel.appkeyrecord;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppKeyRecordRepositoryConfig {
    
    @Bean
    public AppKeyRecordRepository appKeyRepository() {
        return new InMemoryAppKeyRecordRepository();
    }
    
}