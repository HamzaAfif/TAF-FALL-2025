package ca.etsmtl.taf.security.services;

import ca.etsmtl.taf.entity.ERole;
import ca.etsmtl.taf.entity.Role;
import ca.etsmtl.taf.entity.User;
import ca.etsmtl.taf.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserDetailsServiceImpl - Unit")
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User buildUser(String username) {
        User user = new User("Test User", username, username + "@example.com", "password");
        user.setRoles(Set.of(new Role(ERole.ROLE_USER)));
        return user;
    }

    @Test
    @DisplayName("loadUserByUsername should return user details when found")
    void loadUserByUsernameShouldReturnUserDetails() {
        User user = buildUser("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDetailsImpl details = (UserDetailsImpl) userDetailsService.loadUserByUsername("testuser");

        assertEquals("testuser", details.getUsername());
        assertEquals("testuser@example.com", details.getEmail());
        assertFalse(details.getAuthorities().isEmpty());
    }

    @Test
    @DisplayName("loadUserByUsername should throw when user is missing")
    void loadUserByUsernameShouldThrowWhenMissing() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
            UsernameNotFoundException.class,
            () -> userDetailsService.loadUserByUsername("missing")
        );

        assertEquals("User Not Found with username: missing", exception.getMessage());
    }
}
