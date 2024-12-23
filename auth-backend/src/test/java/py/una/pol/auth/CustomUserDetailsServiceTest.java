package py.una.pol.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import py.una.pol.auth.model.Role;
import py.una.pol.auth.model.User;
import py.una.pol.auth.repository.UserRepository;
import py.una.pol.auth.services.CustomUserDetailsService;

import java.util.Set;

class CustomUserDetailsServiceTest {

    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customUserDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    void loadUserByUsername_UserExists() {
        User mockUser = new User(1L, "testuser", "password", Set.of(new Role(1L, "ROLE_USER", null, null)));
        when(userRepository.findByUsername("testuser")).thenReturn(mockUser);

        
        var userDetails = customUserDetailsService.loadUserByUsername("testuser");

        assertEquals("testuser", userDetails.getUsername());
        assertEquals(1, userDetails.getAuthorities().size());
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("unknown"));
    }
}