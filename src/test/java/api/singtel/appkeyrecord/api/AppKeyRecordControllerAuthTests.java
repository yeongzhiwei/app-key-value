package api.singtel.appkeyrecord.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
public class AppKeyRecordControllerAuthTests {

    @MockBean
    AppKeyRecordRepository repo;
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getValidCredentialsShouldReturnOk() throws Exception {
        when(repo.findByAppAndKey("app1", "key1")).thenReturn(Optional.ofNullable(new AppKeyRecord("app1", "key1", "value1", 10)));
        
        this.mockMvc.perform(get("/apps/app1/keys/key1")
                .with(httpBasic("user", "pass")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.value").value("value1"));
    }

    @Test
    public void getInvalidUserShouldReturnUnauthorized() throws Exception {
        when(repo.findByAppAndKey("app1", "key1")).thenReturn(Optional.ofNullable(new AppKeyRecord("app1", "key1", "value1", 10)));
        
        this.mockMvc.perform(get("/apps/app1/keys/key1")
                .with(httpBasic("wrongUser", "pass")))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void getInvalidPassShouldReturnUnauthorized() throws Exception {
        when(repo.findByAppAndKey("app1", "key1")).thenReturn(Optional.ofNullable(new AppKeyRecord("app1", "key1", "value1", 10)));
        
        this.mockMvc.perform(get("/apps/app1/keys/key1")
                .with(httpBasic("user", "wrongPass")))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void postValidCredentialsShouldReturnCreated() throws Exception {
        AppKeyRecord record = new AppKeyRecord("app1", "key1", "value1", 10);

        when(repo.save(any(AppKeyRecord.class))).thenReturn(record);

        Gson gson = new Gson();
        String jsonRecord = gson.toJson(Map.of("key", "key1", "value", "value1"));

        this.mockMvc.perform(post("/apps/app1/keys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRecord)
                .with(httpBasic("user", "pass")))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.value").value("value1"));
    }

    @Test
    public void postInvalidCredentialsShouldReturnUnauthorized() throws Exception {
        AppKeyRecord record = new AppKeyRecord("app1", "key1", "value1", 10);

        when(repo.save(any(AppKeyRecord.class))).thenReturn(record);

        Gson gson = new Gson();
        String jsonRecord = gson.toJson(Map.of("key", "key1", "value", "value1"));

        this.mockMvc.perform(post("/apps/app1/keys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRecord)
                .with(httpBasic("wrongUser", "wrongPass")))
            .andExpect(status().isUnauthorized());
    }
    
}