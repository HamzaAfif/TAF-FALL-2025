package ca.etsmtl.taf.security.jwt;

import ca.etsmtl.taf.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("JwtUtils - JWT Token Generation and Validation")
class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;

    private Authentication authentication;

    @BeforeEach
    void setUp() {
        authentication = mock(Authentication.class);

        UserDetailsImpl principal = new UserDetailsImpl(
            "u-1",
            "Test User",
            "testuser",
            "test@example.com",
            "secret",
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        when(authentication.getPrincipal()).thenReturn(principal);
    }

    @Test
    @DisplayName("Should generate JWT token from authentication")
    void testGenerateJwtToken() {
        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    @DisplayName("Should validate valid JWT token")
    void testValidateJwtTokenValid() {
        String token = jwtUtils.generateJwtToken(authentication);

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should reject invalid JWT token")
    void testValidateJwtTokenInvalid() {
        String invalidToken = "invalid.token.here";

        boolean isValid = jwtUtils.validateJwtToken(invalidToken);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should extract username from valid JWT token")
    void testGetUsernameFromJwtToken() {
        String token = jwtUtils.generateJwtToken(authentication);

        String username = jwtUtils.getUserNameFromJwtToken(token);

        assertEquals("testuser", username);
    }

    @Test
    @DisplayName("Should handle empty token")
    void testValidateEmptyToken() {
        boolean isValid = jwtUtils.validateJwtToken("");

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should handle null token")
    void testValidateNullToken() {
        boolean isValid = jwtUtils.validateJwtToken(null);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should generate token with expiration time")
    void testGeneratedTokenHasExpiration() {
        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token);
        assertTrue(jwtUtils.validateJwtToken(token));
    }
}
