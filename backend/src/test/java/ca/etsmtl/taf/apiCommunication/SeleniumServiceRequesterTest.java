package ca.etsmtl.taf.apiCommunication;

import ca.etsmtl.taf.dto.SeleniumCaseDto;
import ca.etsmtl.taf.entity.SeleniumCaseResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SeleniumServiceRequester - Unit")
class SeleniumServiceRequesterTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Test
    @DisplayName("sendTestCase should build the POST request and return the response")
    void sendTestCaseShouldCallWebClientChain() throws Exception {
        SeleniumServiceRequester requester = new SeleniumServiceRequester(webClient);
        SeleniumCaseDto testCase = buildTestCase(7, "Login flow");

        SeleniumCaseResponse response = new SeleniumCaseResponse();
        response.setOutput("ok");
        response.setSuccess(true);

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/microservice/selenium/test")).thenReturn(requestBodySpec);
        when(requestBodySpec.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Mono.class), eq(SeleniumCaseDto.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(SeleniumCaseResponse.class)).thenReturn(Mono.just(response));

        SeleniumCaseResponse actual = requester.sendTestCase(testCase).block();

        assertEquals("ok", actual.getOutput());
        verify(webClient).post();
        verify(requestBodyUriSpec).uri("/microservice/selenium/test");
        verify(requestBodySpec).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    private SeleniumCaseDto buildTestCase(int caseId, String caseName) throws ReflectiveOperationException {
        SeleniumCaseDto dto = new SeleniumCaseDto();

        Field caseIdField = SeleniumCaseDto.class.getDeclaredField("case_id");
        caseIdField.setAccessible(true);
        caseIdField.set(dto, caseId);

        Field caseNameField = SeleniumCaseDto.class.getDeclaredField("caseName");
        caseNameField.setAccessible(true);
        caseNameField.set(dto, caseName);

        return dto;
    }
}
