package ca.etsmtl.taf.security.jwt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("AuthEntryPointJwt - Unit")
class AuthEntryPointJwtTest {

    private final AuthEntryPointJwt authEntryPointJwt = new AuthEntryPointJwt();

    @Test
    @DisplayName("commence should return a 401 JSON payload")
    void commenceShouldWriteUnauthorizedResponse() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/private");

        MockHttpServletResponse response = new MockHttpServletResponse();
        BadCredentialsException exception = new BadCredentialsException("No token provided");

        authEntryPointJwt.commence(request, response, exception);

        assertEquals(401, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        Map<String, Object> body = new ObjectMapper().readValue(
            response.getContentAsByteArray(),
            new TypeReference<Map<String, Object>>() {}
        );

        assertEquals(401, body.get("status"));
        assertEquals("Unauthorized", body.get("error"));
        assertEquals("No token provided", body.get("message"));
        assertEquals("/api/private", body.get("path"));
    }
}
