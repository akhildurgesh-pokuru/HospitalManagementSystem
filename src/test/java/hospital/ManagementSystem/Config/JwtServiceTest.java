package hospital.ManagementSystem.Config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        // Only a test key: never use this value for a deployed application.
        ReflectionTestUtils.setField(jwtService, "secret",
                "0123456789abcdef0123456789abcdef");
        ReflectionTestUtils.setField(jwtService, "expirationMillis", 60000L);
        ReflectionTestUtils.invokeMethod(jwtService, "initialize");
    }

    @Test
    void generateToken_canExtractSubject() {
        String token = jwtService.generateToken("akhil@example.com");
        assertEquals("akhil@example.com", jwtService.extractUsername(token));
    }

    @Test
    void isTokenValid_correctUserReturnsTrue() {
        String token = jwtService.generateToken("akhil@example.com");
        assertTrue(jwtService.isTokenValid(token, "akhil@example.com"));
    }

    @Test
    void isTokenValid_differentUserReturnsFalse() {
        String token = jwtService.generateToken("akhil@example.com");
        assertFalse(jwtService.isTokenValid(token, "someone@example.com"));
    }

    @Test
    void isTokenValid_modifiedTokenReturnsFalse() {
        String token = jwtService.generateToken("akhil@example.com");
        // Change one character in the signature.
        String[] parts = token.split("\\.");
        char first = parts[2].charAt(0);
        parts[2] = (first == 'A' ? 'B' : 'A') + parts[2].substring(1);
        assertFalse(jwtService.isTokenValid(String.join(".", parts), "akhil@example.com"));
    }
}

