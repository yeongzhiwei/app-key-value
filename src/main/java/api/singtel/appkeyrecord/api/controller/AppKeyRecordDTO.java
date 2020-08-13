package api.singtel.appkeyrecord.api.controller;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AppKeyRecordDTO {
    
    @NotBlank(message = "key is required")
    @AlnumSizePattern(message = "key must be alphanumeric and up to 256 characters")
    @ApiModelProperty(notes = "The application-specific key", required = true)
    private String key;

    @NotBlank(message = "value is required")
    @ApiModelProperty(notes = "The value mapped to app and key", required = true)
    private String value;

    @ApiModelProperty(notes = "The time to live, in seconds")
    private Integer ttl;

    public AppKeyRecordDTO(String key, String value) {
        this.key = key;
        this.value = value;
    }

}