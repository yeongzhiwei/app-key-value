package api.singtel.appkeyrecord.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppKeyRecordDTO {
    
    @NotBlank(message = "key is required")
    @Size(max = 256, message = "key can contain up to 256 characters only")
    @Pattern(regexp = "^[\\p{Alnum}]*$", message = "key must be alphanumeric only")
    private String key;

    @NotBlank(message = "value is required")
    private String value;

    private Integer ttl;

    public AppKeyRecordDTO(String key, String value) {
        this.key = key;
        this.value = value;
    }

}