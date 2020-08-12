package api.singtel.appkeyrecord.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import api.singtel.appkeyrecord.api.BaseIntegrationTests;
import api.singtel.appkeyrecord.api.model.AppKeyRecord;
import api.singtel.appkeyrecord.api.service.AppKeyRecordService;

@AutoConfigureMockMvc
public class AppKeyRecordControllerAuthTests extends BaseIntegrationTests {

    @MockBean AppKeyRecordService service;
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @BeforeEach
    public void mockServiceMethods() {
        AppKeyRecord record = new AppKeyRecord("app1", "key1", "value1", 10);
        when(service.get("app1", "key1")).thenReturn(record);
        when(service.create(eq("app1"), any(AppKeyRecordDTO.class))).thenReturn(record);
    }

    @Test
    public void getValidCredentialsShouldReturnOk() throws Exception {
        this.mockMvc.perform(get("/apps/app1/keys/key1")
                .with(httpBasic("testuser", "testpass")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.value").value("value1"));
    }

    @Test
    public void getInvalidUserShouldReturnUnauthorized() throws Exception {
        this.mockMvc.perform(get("/apps/app1/keys/key1")
                .with(httpBasic("wronguser", "testpass")))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void getInvalidPassShouldReturnUnauthorized() throws Exception {
        this.mockMvc.perform(get("/apps/app1/keys/key1")
                .with(httpBasic("testuser", "wrongpass")))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void postValidCredentialsShouldReturnCreated() throws Exception {       
        this.mockMvc.perform(post("/apps/app1/keys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("key", "key1", "value", "value1")))
                .with(httpBasic("testuser", "testpass")))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.value").value("value1"));
    }

    @Test
    public void postInvalidCredentialsShouldReturnUnauthorized() throws Exception {
        this.mockMvc.perform(post("/apps/app1/keys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("key", "key1", "value", "value1")))
                .with(httpBasic("wronguser", "wrongpass")))
            .andExpect(status().isUnauthorized());
    }
    
}