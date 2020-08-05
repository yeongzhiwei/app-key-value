package api.singtel.appkeyrecord;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class AppKeyRecordControllerTests {
    
    @MockBean
    AppKeyRecordRepository repo;
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getNullRecordShouldReturnNotFound() throws Exception {
        when(repo.findByAppAndKey("app1", "key1")).thenReturn(Optional.ofNullable(null));

        this.mockMvc.perform(get("/apps/app1/keys/key1"))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    public void getValidRecordShouldReturnOk() throws Exception {
        when(repo.findByAppAndKey("app1", "key1")).thenReturn(Optional.ofNullable(new AppKeyRecord("app1", "key1", "value1")));
        
        this.mockMvc.perform(get("/apps/app1/keys/key1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.value").value("value1"));
    }

    @Test
    public void getInvalidRecordShouldReturnNotFound() throws Exception {
        when(repo.findByAppAndKey("app1", "key1")).thenThrow(new AppKeyRecordNotFoundException("app1", "key1"));

        this.mockMvc.perform(get("/apps/app1/keys/key1"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void postValidRecordShouldReturnCreated() throws Exception {
        AppKeyRecord record = new AppKeyRecord("app1", "key1", "value1");

        when(repo.save(any(AppKeyRecord.class))).thenReturn(record);

        Gson gson = new Gson();
        String jsonRecord = gson.toJson(Map.of("key", "key1", "value", "value1"));

        this.mockMvc.perform(post("/apps/app1/keys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRecord))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.value").value("value1"));
    }

}