package ca.etsmtl.taf.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("DevCorsConfiguration - Unit")
class DevCorsConfigurationTest {

    @Test
    @DisplayName("addCorsMappings should register the API path and exposed headers")
    void addCorsMappingsShouldConfigureApiCors() {
        DevCorsConfiguration configuration = new DevCorsConfiguration();
        CorsRegistry registry = mock(CorsRegistry.class);
        CorsRegistration registration = mock(CorsRegistration.class);

        when(registry.addMapping("/api/**")).thenReturn(registration);
        when(registration.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")).thenReturn(registration);
        when(registration.exposedHeaders("Authorization")).thenReturn(registration);

        configuration.addCorsMappings(registry);

        verify(registry).addMapping("/api/**");
        verify(registration).allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
        verify(registration).exposedHeaders("Authorization");
    }
}
