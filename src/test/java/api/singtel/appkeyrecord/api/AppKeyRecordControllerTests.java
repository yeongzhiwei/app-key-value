package api.singtel.appkeyrecord.api;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Map;

import com.google.gson.Gson;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AppKeyRecordControllerTests {
    
    @MockBean
    AppKeyRecordService service;
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllValidRecordShouldReturnOk() throws Exception {
        when(service.getAll("app1")).thenReturn(Arrays.asList(new AppKeyRecord("app1", "key1", "value1", 10)));
        
        this.mockMvc.perform(get("/apps/app1/keys"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void getValidRecordShouldReturnOk() throws Exception {
        when(service.get("app1", "key1")).thenReturn(new AppKeyRecord("app1", "key1", "value1", 10));
        
        this.mockMvc.perform(get("/apps/app1/keys/key1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.value").value("value1"));
    }

    @Test
    public void getInvalidRecordShouldReturnNotFound() throws Exception {
        when(service.get("app1", "key1")).thenThrow(new AppKeyRecordNotFoundException("app1", "key1"));

        this.mockMvc.perform(get("/apps/app1/keys/key1"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void postValidRecordShouldReturnCreated() throws Exception {
        AppKeyRecord record = new AppKeyRecord("app1", "key1", "value1", 10);
        when(service.create(eq("app1"), any(AppKeyRecordDTO.class))).thenReturn(record);

        Gson gson = new Gson();
        String jsonRecord = gson.toJson(Map.of("key", "key1", "value", "value1"));

        this.mockMvc.perform(post("/apps/app1/keys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRecord))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.value").value("value1"));
    }

    @Test
    public void postInvalidAppShouldReturnBadRequest() throws Exception {
        Gson gson = new Gson();
        String jsonRecord = gson.toJson(Map.of("key", "key1", "value", "value1"));

        this.mockMvc.perform(post("/apps/app1@/keys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRecord))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(containsString("app")));
    }
    
    @Test
    public void postInvalidKeyShouldReturnBadRequest() throws Exception {
        Gson gson = new Gson();
        String jsonRecord = gson.toJson(Map.of("key", "key1@", "value", "value1"));

        this.mockMvc.perform(post("/apps/app1/keys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRecord))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(containsString("key")));
    }
    
    @Test
    public void postBlankKeyShouldReturnBadRequest() throws Exception {
        Gson gson = new Gson();
        String jsonRecord = gson.toJson(Map.of("key", "", "value", "value1"));

        this.mockMvc.perform(post("/apps/app1@/keys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRecord))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(containsString("key")));
    }
    
    @Test
    public void postMissingKeyShouldReturnBadRequest() throws Exception {
        Gson gson = new Gson();
        String jsonRecord = gson.toJson(Map.of("value", "value1"));

        this.mockMvc.perform(post("/apps/app1@/keys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRecord))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(containsString("key")));
    }
    
    @Test
    public void postBlankValueShouldReturnBadRequest() throws Exception {
        Gson gson = new Gson();
        String jsonRecord = gson.toJson(Map.of("key", "key1", "value", ""));

        this.mockMvc.perform(post("/apps/app1@/keys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRecord))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(containsString("value")));
    }

    @Test
    public void postMissingValueShouldReturnBadRequest() throws Exception {
        Gson gson = new Gson();
        String jsonRecord = gson.toJson(Map.of("key", "key1"));

        this.mockMvc.perform(post("/apps/app1@/keys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRecord))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(containsString("value")));
    }

}