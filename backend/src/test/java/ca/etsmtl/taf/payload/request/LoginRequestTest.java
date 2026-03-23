package ca.etsmtl.taf.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("LoginRequest - User Authentication Request")
class LoginRequestTest {

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
    }

    @Test
    @DisplayName("Should set and get username")
    void testSetGetUsername() {
        loginRequest.setUsername("testuser");
        assertEquals("testuser", loginRequest.getUsername());
    }

    @Test
    @DisplayName("Should set and get password")
    void testSetGetPassword() {
        loginRequest.setPassword("password123");
        assertEquals("password123", loginRequest.getPassword());
    }

    @Test
    @DisplayName("Should handle null username")
    void testNullUsername() {
        loginRequest.setUsername(null);
        assertNull(loginRequest.getUsername());
    }

    @Test
    @DisplayName("Should handle null password")
    void testNullPassword() {
        loginRequest.setPassword(null);
        assertNull(loginRequest.getPassword());
    }

    @Test
    @DisplayName("Should handle empty username")
    void testEmptyUsername() {
        loginRequest.setUsername("");
        assertEquals("", loginRequest.getUsername());
    }

    @Test
    @DisplayName("Should handle special characters in password")
    void testSpecialCharactersInPassword() {
        String specialPassword = "p@ssw0rd!#$%^&*()";
        loginRequest.setPassword(specialPassword);
        assertEquals(specialPassword, loginRequest.getPassword());
    }

    @Test
    @DisplayName("Should handle long username")
    void testLongUsername() {
        String longUsername = "a".repeat(100);
        loginRequest.setUsername(longUsername);
        assertEquals(longUsername, loginRequest.getUsername());
    }

    @Test
    @DisplayName("Should handle unicode characters")
    void testUnicodeCharacters() {
        loginRequest.setUsername("用户名");
        assertEquals("用户名", loginRequest.getUsername());
    }
}
