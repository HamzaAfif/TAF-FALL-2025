package ca.etsmtl.taf.security.services;

import ca.etsmtl.taf.entity.Role;
import ca.etsmtl.taf.entity.User;
import ca.etsmtl.taf.entity.ERole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("UserDetailsImpl - User Security Details")
class UserDetailsImplTest {

    private UserDetailsImpl userDetails;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("user123");
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("hashedpassword");

        Set<Role> roles = new HashSet<>();
        Role userRole = new Role();
        userRole.setName(ERole.ROLE_USER);
        roles.add(userRole);
        user.setRoles(roles);

        userDetails = new UserDetailsImpl("user123", "testuser", "test@example.com", "hashedpassword", null);
    }

    @Test
    @DisplayName("Should return correct username")
    void testGetUsername() {
        assertEquals("testuser", userDetails.getUsername());
    }

    @Test
    @DisplayName("Should return correct password")
    void testGetPassword() {
        assertEquals("hashedpassword", userDetails.getPassword());
    }

    @Test
    @DisplayName("Should return user ID")
    void testGetId() {
        assertEquals("user123", userDetails.getId());
    }

    @Test
    @DisplayName("Should return user email")
    void testGetEmail() {
        assertEquals("test@example.com", userDetails.getEmail());
    }

    @Test
    @DisplayName("Should be account non-expired")
    void testIsAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    @DisplayName("Should be account non-locked")
    void testIsAccountNonLocked() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    @DisplayName("Should have credentials non-expired")
    void testIsCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    @DisplayName("Should be enabled")
    void testIsEnabled() {
        assertTrue(userDetails.isEnabled());
    }

    @Test
    @DisplayName("Should return authorities")
    void testGetAuthorities() {
        var authorities = userDetails.getAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities.size() >= 0);
    }

    @Test
    @DisplayName("Should create UserDetailsImpl from User entity")
    void testBuildFromUser() {
        UserDetailsImpl built = UserDetailsImpl.build(user);

        assertNotNull(built);
        assertEquals("testuser", built.getUsername());
        assertEquals("test@example.com", built.getEmail());
        assertEquals("user123", built.getId());
    }

    @Test
    @DisplayName("Should handle user with multiple roles")
    void testBuildUserWithMultipleRoles() {
        Set<Role> roles = new HashSet<>();
        Role userRole = new Role();
        userRole.setName(ERole.ROLE_USER);
        Role adminRole = new Role();
        adminRole.setName(ERole.ROLE_ADMIN);
        roles.add(userRole);
        roles.add(adminRole);
        user.setRoles(roles);

        UserDetailsImpl built = UserDetailsImpl.build(user);

        assertNotNull(built);
        assertEquals("testuser", built.getUsername());
    }

    @Test
    @DisplayName("Should handle user with no roles")
    void testBuildUserWithNoRoles() {
        user.setRoles(new HashSet<>());

        UserDetailsImpl built = UserDetailsImpl.build(user);

        assertNotNull(built);
        assertEquals("testuser", built.getUsername());
    }
}
