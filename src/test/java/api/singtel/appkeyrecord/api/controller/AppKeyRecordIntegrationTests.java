package api.singtel.appkeyrecord.api.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import api.singtel.appkeyrecord.api.BaseRedisIntegrationTests;
import api.singtel.appkeyrecord.api.model.AppKeyRecord;
import api.singtel.appkeyrecord.api.repo.AppKeyRecordRepository;
import api.singtel.appkeyrecord.api.service.AppKeyRecordService;

@AutoConfigureMockMvc
@WithMockUser(username = "testuser")
public class AppKeyRecordIntegrationTests extends BaseRedisIntegrationTests {
    
    @Autowired private MockMvc mockMvc;
    @Autowired private AppKeyRecordRepository repository;
    @Autowired private AppKeyRecordService service;
    @Autowired private ObjectMapper objectMapper;

    @BeforeEach
    public void seedData() {
        service.create("app1", new AppKeyRecordDTO("key1", "value1", 1));
        service.create("app1", new AppKeyRecordDTO("key2", "value2"));
        service.create("app2", new AppKeyRecordDTO("key1", "value3", 10));
    }
    
    @Test
    public void getAllValidRecordShouldReturnOkWithData() throws Exception {
        String json = this.mockMvc.perform(get("/apps/app1/keys"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        List<AppKeyRecord> actual = objectMapper.readValue(json, new TypeReference<List<AppKeyRecord>>() { });
     
        assertThat(actual, hasItem(allOf(hasProperty("app", equalTo("app1")))));
    }

    @Test
    public void getAllNoRecordShouldReturnOkWithEmptyList() throws Exception {
        this.mockMvc.perform(get("/apps/nosuchapp/keys"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void getOneValidRecordShouldReturnOkWithData() throws Exception {
        this.mockMvc.perform(get("/apps/app1/keys/key1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.app").value("app1"))
            .andExpect(jsonPath("$.key").value("key1"))
            .andExpect(jsonPath("$.value").value("value1"))
            .andExpect(jsonPath("$.ttl").value(1));
    }
    
    @Test
    public void getOneInvalidRecordShouldReturnNotFound() throws Exception {
        this.mockMvc.perform(get("/apps/app1/keys/nosuchkey"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void postRecordShouldReturnOkWithData() throws Exception {
        this.mockMvc.perform(post("/apps/app1/keys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("key", "key3", "value", "value3", "ttl", 3))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.app").value("app1"))
            .andExpect(jsonPath("$.key").value("key3"))
            .andExpect(jsonPath("$.value").value("value3"))
            .andExpect(jsonPath("$.ttl").value(3));

        assertTrue(repository.findByAppAndKey("app1", "key3").isPresent());
    }
    
    @Test
    public void postRecordNoTtlShouldReturnOkWithDataDefaultTtl() throws Exception {
        this.mockMvc.perform(post("/apps/app1/keys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("key", "key4", "value", "value4"))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.app").value("app1"))
            .andExpect(jsonPath("$.key").value("key4"))
            .andExpect(jsonPath("$.value").value("value4"))
            .andExpect(jsonPath("$.ttl").value(888));

        assertTrue(repository.findByAppAndKey("app1", "key4").isPresent());
    }
    
}