package ca.etsmtl.taf.controller;

import ca.etsmtl.taf.dto.SeleniumCaseDto;
import ca.etsmtl.taf.entity.SeleniumCaseResponse;
import ca.etsmtl.taf.service.SeleniumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("TestSeleniumController - Selenium Test Execution")
class TestSeleniumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SeleniumService seleniumService;

    private List<SeleniumCaseDto> testCases;

    @BeforeEach
    void setUp() {
        testCases = new ArrayList<>();
        SeleniumCaseDto testCase = new SeleniumCaseDto();
        testCase.setTestName("Login Test");
        testCase.setUrl("https://example.com/login");
        testCases.add(testCase);
    }

    @Test
    @DisplayName("POST /api/testselenium - Should execute single selenium test")
    void testRunSingleSeleniumTest() throws Exception {
        SeleniumCaseResponse response = new SeleniumCaseResponse();
        response.setTestName("Login Test");
        response.setStatus("PASSED");
        response.setMessage("Test executed successfully");

        when(seleniumService.sendTestCases(any())).thenReturn(List.of(response));

        mockMvc.perform(post("/api/testselenium")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCases)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].testName").value("Login Test"))
                .andExpect(jsonPath("$[0].status").value("PASSED"));
    }

    @Test
    @DisplayName("POST /api/testselenium - Should execute multiple selenium tests")
    void testRunMultipleSeleniumTests() throws Exception {
        SeleniumCaseDto testCase2 = new SeleniumCaseDto();
        testCase2.setTestName("Logout Test");
        testCase2.setUrl("https://example.com/logout");
        testCases.add(testCase2);

        SeleniumCaseResponse response1 = new SeleniumCaseResponse();
        response1.setTestName("Login Test");
        response1.setStatus("PASSED");

        SeleniumCaseResponse response2 = new SeleniumCaseResponse();
        response2.setTestName("Logout Test");
        response2.setStatus("PASSED");

        when(seleniumService.sendTestCases(any())).thenReturn(List.of(response1, response2));

        mockMvc.perform(post("/api/testselenium")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCases)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("POST /api/testselenium - Should handle failed test")
    void testRunFailedSeleniumTest() throws Exception {
        SeleniumCaseResponse response = new SeleniumCaseResponse();
        response.setTestName("Login Test");
        response.setStatus("FAILED");
        response.setMessage("Element not found: Login button");

        when(seleniumService.sendTestCases(any())).thenReturn(List.of(response));

        mockMvc.perform(post("/api/testselenium")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCases)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("FAILED"));
    }

    @Test
    @DisplayName("POST /api/testselenium - Should handle empty test list")
    void testRunEmptySeleniumTestList() throws Exception {
        List<SeleniumCaseDto> emptyList = new ArrayList<>();

        when(seleniumService.sendTestCases(any())).thenReturn(new ArrayList<>());

        mockMvc.perform(post("/api/testselenium")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("POST /api/testselenium - Should return list response")
    void testRunSeleniumTestReturnsListResponse() throws Exception {
        List<SeleniumCaseResponse> responses = new ArrayList<>();
        SeleniumCaseResponse response = new SeleniumCaseResponse();
        response.setTestName("Form Test");
        response.setStatus("PASSED");
        responses.add(response);

        when(seleniumService.sendTestCases(any())).thenReturn(responses);

        mockMvc.perform(post("/api/testselenium")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCases)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("POST /api/testselenium - Should handle test with execution time")
    void testRunSeleniumTestWithExecutionTime() throws Exception {
        SeleniumCaseResponse response = new SeleniumCaseResponse();
        response.setTestName("Navigation Test");
        response.setStatus("PASSED");
        response.setExecutionTime(1500L);

        when(seleniumService.sendTestCases(any())).thenReturn(List.of(response));

        mockMvc.perform(post("/api/testselenium")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCases)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/testselenium - Should handle test with screenshot path")
    void testRunSeleniumTestWithScreenshot() throws Exception {
        SeleniumCaseResponse response = new SeleniumCaseResponse();
        response.setTestName("Screenshot Test");
        response.setStatus("FAILED");
        response.setScreenshotPath("/screenshots/failure-2024-03-23.png");

        when(seleniumService.sendTestCases(any())).thenReturn(List.of(response));

        mockMvc.perform(post("/api/testselenium")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCases)))
                .andExpect(status().isOk());
    }
}
