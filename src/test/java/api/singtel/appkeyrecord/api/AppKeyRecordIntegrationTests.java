package api.singtel.appkeyrecord.api;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import api.singtel.appkeyrecord.api.model.AppKeyRecord;
import api.singtel.appkeyrecord.api.repo.AppKeyRecordRepository;

@AutoConfigureMockMvc
@WithMockUser(username = "testuser")
class AppKeyRecordIntegrationTests extends BaseRedisIntegrationTests {
    
    @Autowired private MockMvc mockMvc;
    @Autowired private AppKeyRecordRepository repository;
    @Autowired private ObjectMapper objectMapper;

    @BeforeEach
    void seedData() {
        repository.save(new AppKeyRecord("app1", "key1", "value1", 1));
        repository.save(new AppKeyRecord("app1", "key2", "value2", 10));
        repository.save(new AppKeyRecord("app2", "key1", "value3", 10));
    }

    @AfterEach
    void removeData() {
        repository.deleteAll();
    }
    
    @Test
    void getAllValidRecordShouldReturnOkWithData() throws Exception {
        String json = this.mockMvc.perform(get("/apps/app1/keys"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        List<AppKeyRecord> actual = objectMapper.readValue(json, new TypeReference<List<AppKeyRecord>>() { });
     
        assertTrue(actual.size() > 0);
        assertThat(actual, hasItem(hasProperty("app", equalTo("app1"))));
    }

    @Test
    void getAllNoRecordShouldReturnOkWithEmptyList() throws Exception {
        this.mockMvc.perform(get("/apps/noSuchApp/keys"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getOneValidRecordShouldReturnOkWithData() throws Exception {
        this.mockMvc.perform(get("/apps/app1/keys/key1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.app").value("app1"))
            .andExpect(jsonPath("$.key").value("key1"))
            .andExpect(jsonPath("$.value").value("value1"))
            .andExpect(jsonPath("$.ttl").value(1));
    }
    
    @Test
    void getOneInvalidRecordShouldReturnNotFound() throws Exception {
        this.mockMvc.perform(get("/apps/app1/keys/noSuchKey"))
            .andExpect(status().isNotFound());
    }

    @Test
    void postRecordShouldReturnOkWithData() throws Exception {
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
    void postRecordNoTtlShouldReturnOkWithDataDefaultTtl() throws Exception {
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
    
    @Test
    void deleteAllValidAppShouldReturnOkWithNoContent() throws Exception {
        this.mockMvc.perform(delete("/apps/app1/keys"))
            .andExpect(status().isNoContent());

        assertTrue(repository.findAllByApp("app1").size() == 0);
    }

    @Test
    void deleteAllNoRecordShouldReturnOkWithNoContent() throws Exception {
        this.mockMvc.perform(delete("/apps/noSuchApp/keys"))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteOneValidAppAndKeyShouldReturnOkWithNoContent() throws Exception {
        this.mockMvc.perform(delete("/apps/app1/keys/key1"))
            .andExpect(status().isNoContent());

        assertTrue(repository.findAllByAppAndKey("app1", "key1").size() == 0);
    }

    @Test
    void deleteOneNoRecordShouldReturnOkWithNoContent() throws Exception {
        this.mockMvc.perform(delete("/apps/app1/keys/noSuchKey"))
            .andExpect(status().isNoContent());
    }
    
}