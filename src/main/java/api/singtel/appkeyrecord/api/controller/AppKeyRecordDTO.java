package api.singtel.appkeyrecord.api.controller;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppKeyRecordDTO {
    
    @NotBlank(message = "key is required")
    @AlnumSizePattern(message = "key must be alphanumeric and up to 256 characters")
    private String key;

    @NotBlank(message = "value is required")
    private String value;

    private Integer ttl;

    public AppKeyRecordDTO(String key, String value) {
        this.key = key;
        this.value = value;
    }

}