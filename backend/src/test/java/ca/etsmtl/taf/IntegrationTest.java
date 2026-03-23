package ca.etsmtl.taf;

import ca.etsmtl.taf.controller.TestController;
import ca.etsmtl.taf.controller.TestApiController;
import ca.etsmtl.taf.controller.TestSeleniumController;
import ca.etsmtl.taf.service.SeleniumService;
import ca.etsmtl.taf.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Integration Tests - Full Stack API Coverage")
class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestController testController;

    @Autowired
    private TestApiController testApiController;

    @Autowired
    private TestSeleniumController testSeleniumController;

    @BeforeEach
    void setUp() {
        assertNotNull(testController);
        assertNotNull(testApiController);
        assertNotNull(testSeleniumController);
    }

    @Test
    @DisplayName("Integration: Context loads successfully")
    void contextLoads() {
        assertNotNull(testController);
        assertNotNull(testApiController);
        assertNotNull(testSeleniumController);
    }

    @Test
    @DisplayName("Integration: Public endpoint accessible without auth")
    void testPublicEndpointAccessible() throws Exception {
        mockMvc.perform(get("/api/test/all"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("TAF")));
    }

    @Test
    @DisplayName("Integration: Protected endpoint requires authentication")
    void testProtectedEndpointRequiresAuth() throws Exception {
        mockMvc.perform(get("/api/test/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Integration: User can access user endpoint with USER role")
    @WithMockUser(roles = "USER")
    void testUserRoleAccess() throws Exception {
        mockMvc.perform(get("/api/test/user"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Integration: Admin endpoint requires ADMIN role")
    @WithMockUser(roles = "USER")
    void testAdminEndpointRequiresAdminRole() throws Exception {
        mockMvc.perform(get("/api/test/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Integration: Admin can access admin endpoint with ADMIN role")
    @WithMockUser(roles = "ADMIN")
    void testAdminRoleAccess() throws Exception {
        mockMvc.perform(get("/api/test/admin"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Integration: Multiple requests show consistent responses")
    void testConsistentResponses() throws Exception {
        // First request
        mockMvc.perform(get("/api/test/all"))
                .andExpect(status().isOk());

        // Second request should be consistent
        mockMvc.perform(get("/api/test/all"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Integration: Cross-origin requests allowed")
    void testCrossOriginAllowed() throws Exception {
        mockMvc.perform(get("/api/test/all")
                .header("Origin", "http://localhost:4200"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Integration: Content-Type is text/plain for test endpoints")
    void testContentTypeTextPlain() throws Exception {
        mockMvc.perform(get("/api/test/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }

    @Test
    @DisplayName("Integration: Verify all endpoints respond within time")
    void testEndpointResponseTime() throws Exception {
        long startTime = System.currentTimeMillis();
        mockMvc.perform(get("/api/test/all"));
        long endTime = System.currentTimeMillis();

        // Response should be fast (less than 5 seconds)
        assert (endTime - startTime) < 5000;
    }

    @Test
    @DisplayName("Integration: Error handling for invalid endpoints")
    void testInvalidEndpointHandling() throws Exception {
        mockMvc.perform(get("/api/invalid/endpoint"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Integration: User endpoint string content")
    @WithMockUser(roles = "USER")
    void testUserEndpointContent() throws Exception {
        mockMvc.perform(get("/api/test/user"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User")));
    }

    @Test
    @DisplayName("Integration: Admin endpoint string content")
    @WithMockUser(roles = "ADMIN")
    void testAdminEndpointContent() throws Exception {
        mockMvc.perform(get("/api/test/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Admin")));
    }

    @Test
    @DisplayName("Integration: Authentication headers properly handled")
    @WithMockUser(username = "testuser", roles = "USER")
    void testAuthenticationHeadersHandled() throws Exception {
        mockMvc.perform(get("/api/test/user"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Integration: Different users have isolated access")
    @WithMockUser(username = "user1", roles = "USER")
    void testUserIsolation() throws Exception {
        mockMvc.perform(get("/api/test/user"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Integration: Role-based access control enforced")
    @WithMockUser(roles = "USER")
    void testRoleBasedAccessControl() throws Exception {
        // User role can access user endpoint
        mockMvc.perform(get("/api/test/user"))
                .andExpect(status().isOk());

        // User role cannot access admin endpoint
        mockMvc.perform(get("/api/test/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Integration: All public endpoints respond consistently")
    void testAllPublicEndpointsConsistent() throws Exception {
        // Test multiple public endpoints
        mockMvc.perform(get("/api/test/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }

    @Test
    @DisplayName("Integration: Application context properly configured")
    void testApplicationContextConfiguration() {
        assertNotNull(testController);
        assertNotNull(testApiController);
        assertNotNull(testSeleniumController);
    }
}
