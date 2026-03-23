package ca.etsmtl.taf.controller;

import ca.etsmtl.taf.payload.request.TestApiRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("TestApiController - External API Integration")
class TestApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private TestApiRequest testApiRequest;

    @BeforeEach
    void setUp() {
        testApiRequest = new TestApiRequest();
        testApiRequest.setMethod("GET");
        testApiRequest.setEndpoint("/test");
        testApiRequest.setHeaders("Content-Type:application/json");
    }

    @Test
    @DisplayName("POST /api/testapi/checkApi - Should accept valid request")
    void testCheckApiWithValidRequest() throws Exception {
        mockMvc.perform(post("/api/testapi/checkApi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testApiRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/testapi/checkApi - Should return 400 for null request body")
    void testCheckApiWithNullBody() throws Exception {
        mockMvc.perform(post("/api/testapi/checkApi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/testapi/checkApi - Should accept request with all fields")
    void testCheckApiWithFullRequest() throws Exception {
        testApiRequest.setMethod("POST");
        testApiRequest.setEndpoint("/api/users");
        testApiRequest.setHeaders("Content-Type:application/json,Authorization:Bearer token123");
        testApiRequest.setBody("{\"name\":\"test\"}");

        mockMvc.perform(post("/api/testapi/checkApi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testApiRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/testapi/checkApi - Should handle GET requests")
    void testCheckApiWithGetMethod() throws Exception {
        testApiRequest.setMethod("GET");
        testApiRequest.setEndpoint("/api/status");

        mockMvc.perform(post("/api/testapi/checkApi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testApiRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/testapi/checkApi - Should handle PUT requests")
    void testCheckApiWithPutMethod() throws Exception {
        testApiRequest.setMethod("PUT");
        testApiRequest.setEndpoint("/api/users/123");
        testApiRequest.setBody("{\"name\":\"updated\"}");

        mockMvc.perform(post("/api/testapi/checkApi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testApiRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/testapi/checkApi - Should handle DELETE requests")
    void testCheckApiWithDeleteMethod() throws Exception {
        testApiRequest.setMethod("DELETE");
        testApiRequest.setEndpoint("/api/users/456");

        mockMvc.perform(post("/api/testapi/checkApi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testApiRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/testapi/checkApi - Should handle complex endpoints")
    void testCheckApiWithComplexEndpoint() throws Exception {
        testApiRequest.setEndpoint("/api/v1/users/123/posts/456/comments?sort=desc&limit=10");

        mockMvc.perform(post("/api/testapi/checkApi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testApiRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/testapi/checkApi - Should handle multiple headers")
    void testCheckApiWithMultipleHeaders() throws Exception {
        testApiRequest.setHeaders("Content-Type:application/json,Authorization:Bearer xyz,X-Custom:value");

        mockMvc.perform(post("/api/testapi/checkApi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testApiRequest)))
                .andExpect(status().isOk());
    }
}
