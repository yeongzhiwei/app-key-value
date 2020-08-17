package api.singtel.appkeyrecord.api.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import api.singtel.appkeyrecord.api.model.AppKeyRecord;
import api.singtel.appkeyrecord.api.model.AppKeyRecordNotFoundException;
import api.singtel.appkeyrecord.api.service.AppKeyRecordService;

@WebMvcTest
@WithMockUser(username = "testuser")
class AppKeyRecordControllerTests {
    
    @MockBean AppKeyRecordService service;
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void getAllValidRecordShouldReturnOk() throws Exception {
        when(service.getAll("app1")).thenReturn(Arrays.asList(new AppKeyRecord("app1", "key1", "value1", 10)));
        
        this.mockMvc.perform(get("/apps/app1/keys"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getValidRecordShouldReturnOk() throws Exception {
        when(service.get("app1", "key1")).thenReturn(new AppKeyRecord("app1", "key1", "value1", 10));
        
        this.mockMvc.perform(get("/apps/app1/keys/key1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.value").value("value1"));
    }

    @Test
    void getInvalidRecordShouldReturnNotFound() throws Exception {
        when(service.get("app1", "key1")).thenThrow(new AppKeyRecordNotFoundException("app1", "key1"));

        this.mockMvc.perform(get("/apps/app1/keys/key1"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void postValidRecordShouldReturnCreated() throws Exception {
        when(service.create(eq("app1"), any(AppKeyRecordDTO.class))).thenReturn(new AppKeyRecord("app1", "key1", "value1", 10));

        this.mockMvc.perform(post("/apps/app1/keys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("key", "key1", "value", "value1"))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.value").value("value1"));
    }

    @Test
    void postInvalidAppShouldReturnBadRequest() throws Exception {
        this.mockMvc.perform(post("/apps/app1@/keys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("key", "key1", "value", "value1"))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(containsString("app")));
    }
    
    @Test
    void postInvalidKeyShouldReturnBadRequest() throws Exception {
        this.mockMvc.perform(post("/apps/app1/keys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("key", "key1@", "value", "value1"))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(containsString("key")));
    }
    
    @Test
    void postBlankKeyShouldReturnBadRequest() throws Exception {
        this.mockMvc.perform(post("/apps/app1/keys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("key", "", "value", "value1"))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(containsString("key")));
    }
    
    @Test
    void postMissingKeyShouldReturnBadRequest() throws Exception {
        this.mockMvc.perform(post("/apps/app1/keys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("value", "value1"))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(containsString("key")));
    }
    
    @Test
    void postBlankValueShouldReturnBadRequest() throws Exception {
        this.mockMvc.perform(post("/apps/app1/keys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("key", "key1", "value", ""))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(containsString("value")));
    }

    @Test
    void postMissingValueShouldReturnBadRequest() throws Exception {
        this.mockMvc.perform(post("/apps/app1/keys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("key", "key1"))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(containsString("value")));
    }

    @Test
    void postInvalidJSONShouldReturnBadRequest() throws Exception {
        this.mockMvc.perform(post("/apps/app1/keys")
                .contentType(MediaType.APPLICATION_JSON)
                .content("Not a JSON content"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(containsString("Could not parse JSON")));
    }

    @Test
    void deleteValidAppShouldReturnNoContent() throws Exception {
        this.mockMvc.perform(delete("/apps/app1/keys"))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteInvalidAppShouldReturnBadRequest() throws Exception {
        this.mockMvc.perform(delete("/apps/app1@/keys"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void deleteValidAppAndKeyShouldReturnNoContent() throws Exception {
        this.mockMvc.perform(delete("/apps/app1/keys/key1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteInvalidKeyShouldReturBadRequest() throws Exception {
        this.mockMvc.perform(delete("/apps/app1/keys/key1@"))
            .andExpect(status().isBadRequest());
    }

}