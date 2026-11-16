package hospital.ManagementSystem.Service;

import hospital.ManagementSystem.Config.JwtService;
import hospital.ManagementSystem.Dto.AuthRequest;
import hospital.ManagementSystem.Dto.AuthResponse;
import hospital.ManagementSystem.Entity.Role;
import hospital.ManagementSystem.Entity.User;
import hospital.ManagementSystem.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @InjectMocks private AuthService authService;

    @Test
    void registerUser_savesUserWithEncodedPasswordAndUserRole() {
        AuthRequest request = new AuthRequest(" Akhil ", "AKHIL@example.com", "Password123!");
        when(userRepository.existsByEmail("akhil@example.com")).thenReturn(false);
        when(passwordEncoder.encode("Password123!")).thenReturn("hashed-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthResponse response = authService.registerUser(request);

        assertEquals("User registered successfully", response.message());
        assertEquals("akhil@example.com", response.email());
        assertNull(response.token());
        verify(userRepository).save(argThat(user ->
                user.getName().equals("Akhil") &&
                        user.getEmail().equals("akhil@example.com") &&
                        user.getPassword().equals("hashed-password") &&
                        user.getRole() == Role.USER));
    }

    @Test
    void registerUser_duplicateEmailReturnsConflict() {
        when(userRepository.existsByEmail("akhil@example.com")).thenReturn(true);
        AuthRequest request = new AuthRequest("Akhil", "akhil@example.com", "Password123!");

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> authService.registerUser(request));
        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_shortPasswordReturnsBadRequest() {
        AuthRequest request = new AuthRequest("Akhil", "akhil@example.com", "short");
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> authService.registerUser(request));
        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        verifyNoInteractions(userRepository);
    }

    @Test
    void loginUser_validCredentialsReturnsJwt() {
        User user = new User();
        user.setEmail("akhil@example.com");
        user.setPassword("hashed-password");
        when(userRepository.findByEmail("akhil@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Password123!", "hashed-password")).thenReturn(true);
        when(jwtService.generateToken("akhil@example.com")).thenReturn("example.jwt.token");

        AuthResponse response = authService.loginUser(
                new AuthRequest(null, "AKHIL@example.com", "Password123!"));

        assertEquals("example.jwt.token", response.token());
        assertEquals("Login successful", response.message());
        assertEquals("akhil@example.com", response.email());
    }

    @Test
    void loginUser_wrongPasswordReturnsUnauthorized() {
        User user = new User();
        user.setEmail("akhil@example.com");
        user.setPassword("hashed-password");
        when(userRepository.findByEmail("akhil@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashed-password")).thenReturn(false);

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> authService.loginUser(new AuthRequest(null, "akhil@example.com", "wrong")));
        assertEquals(HttpStatus.UNAUTHORIZED, error.getStatusCode());
        verifyNoInteractions(jwtService);
    }
}
