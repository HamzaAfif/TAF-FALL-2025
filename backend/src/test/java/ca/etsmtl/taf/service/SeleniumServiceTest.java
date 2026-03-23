package ca.etsmtl.taf.service;

import ca.etsmtl.taf.dto.SeleniumCaseDto;
import ca.etsmtl.taf.entity.SeleniumCaseResponse;
import ca.etsmtl.taf.apiCommunication.SeleniumServiceRequester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("SeleniumService - Test Case Processing")
class SeleniumServiceTest {

    @Autowired
    private SeleniumService seleniumService;

    @MockBean
    private SeleniumServiceRequester seleniumServiceRequester;

    private List<SeleniumCaseDto> testCases;

    @BeforeEach
    void setUp() {
        testCases = new ArrayList<>();
        SeleniumCaseDto testCase = new SeleniumCaseDto();
        testCase.setTestName("Login Test");
        testCase.setUrl("https://example.com");
        testCases.add(testCase);
    }

    @Test
    @DisplayName("Should process single test case successfully")
    void testSendSingleTestCase() {
        SeleniumCaseResponse response = new SeleniumCaseResponse();
        response.setTestName("Login Test");
        response.setStatus("PASSED");

        when(seleniumServiceRequester.sendTestCase(any())).thenReturn(Mono.just(response));

        List<SeleniumCaseResponse> results = seleniumService.sendTestCases(testCases);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("PASSED", results.get(0).getStatus());
    }

    @Test
    @DisplayName("Should process multiple test cases")
    void testSendMultipleTestCases() {
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

        when(seleniumServiceRequester.sendTestCase(any())).thenReturn(Mono.just(response1), Mono.just(response2));

        List<SeleniumCaseResponse> results = seleniumService.sendTestCases(testCases);

        assertNotNull(results);
        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("Should handle empty test case list")
    void testSendEmptyTestCaseList() {
        List<SeleniumCaseResponse> results = seleniumService.sendTestCases(new ArrayList<>());

        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    @DisplayName("Should handle test case with failure")
    void testSendTestCaseWithFailure() {
        SeleniumCaseResponse response = new SeleniumCaseResponse();
        response.setTestName("Login Test");
        response.setStatus("FAILED");
        response.setMessage("Assertion failed");

        when(seleniumServiceRequester.sendTestCase(any())).thenReturn(Mono.just(response));

        List<SeleniumCaseResponse> results = seleniumService.sendTestCases(testCases);

        assertNotNull(results);
        assertEquals("FAILED", results.get(0).getStatus());
    }

    @Test
    @DisplayName("Should process test case with complex data")
    void testSendTestCaseWithComplexData() {
        SeleniumCaseDto complexCase = new SeleniumCaseDto();
        complexCase.setTestName("Complex Navigation");
        complexCase.setUrl("https://example.com/dashboard");
        complexCase.setExpectedResult("User dashboard loaded");
        testCases.add(complexCase);

        SeleniumCaseResponse response = new SeleniumCaseResponse();
        response.setTestName("Complex Navigation");
        response.setStatus("PASSED");

        when(seleniumServiceRequester.sendTestCase(any())).thenReturn(Mono.just(response));

        List<SeleniumCaseResponse> results = seleniumService.sendTestCases(testCases);

        assertNotNull(results);
        assertTrue(results.size() > 0);
    }
}
