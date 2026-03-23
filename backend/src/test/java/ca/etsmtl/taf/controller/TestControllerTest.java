package ca.etsmtl.taf.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("TestController - Public API Endpoints")
class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final TestController controller = new TestController();

    @Test
    @DisplayName("GET /api/test/all - Should return welcome message")
    void testAllAccess() throws Exception {
        mockMvc.perform(get("/api/test/all"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Bienvenue au TAF")));
    }

    @Test
    @DisplayName("GET /api/test/user - Should deny access without authentication")
    void testUserAccessDenied() throws Exception {
        mockMvc.perform(get("/api/test/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/test/user - Should allow USER role access")
    @WithMockUser(roles = "USER")
    void testUserAccessWithUserRole() throws Exception {
        mockMvc.perform(get("/api/test/user"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User Content")));
    }

    @Test
    @DisplayName("GET /api/test/user - Should allow ADMIN role access")
    @WithMockUser(roles = "ADMIN")
    void testUserAccessWithAdminRole() throws Exception {
        mockMvc.perform(get("/api/test/user"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User Content")));
    }

    @Test
    @DisplayName("GET /api/test/admin - Should deny access without ADMIN role")
    @WithMockUser(roles = "USER")
    void testAdminAccessDeniedForUser() throws Exception {
        mockMvc.perform(get("/api/test/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /api/test/admin - Should allow ADMIN role access")
    @WithMockUser(roles = "ADMIN")
    void testAdminAccessWithAdminRole() throws Exception {
        mockMvc.perform(get("/api/test/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Admin Board")));
    }

    @Test
    @DisplayName("GET /api/test/admin - Should deny anonymous access")
    void testAdminAccessDeniedAnonymous() throws Exception {
        mockMvc.perform(get("/api/test/admin"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/test/all - Should return 200 status code")
    void testAllAccessStatusCode() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/test/all"))
                .andExpect(status().isOk())
                .andReturn();
        
        org.junit.jupiter.api.Assertions.assertNotNull(result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("normalizeMessage should handle null and spaces")
    void testNormalizeMessage() {
        assertEquals("", controller.normalizeMessage(null));
        assertEquals("", controller.normalizeMessage("   "));
        assertEquals("hello world", controller.normalizeMessage("  hello   world  "));
    }

    @Test
    @DisplayName("hasAdminAccess should accept ADMIN and ROLE_ADMIN")
    void testHasAdminAccess() {
        assertTrue(controller.hasAdminAccess("ADMIN"));
        assertTrue(controller.hasAdminAccess("role_admin"));
        assertFalse(controller.hasAdminAccess("USER"));
        assertFalse(controller.hasAdminAccess(null));
    }
}
