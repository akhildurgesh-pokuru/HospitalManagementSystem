package hospital.ManagementSystem.Controller;

import hospital.ManagementSystem.Dto.AuthRequest;
import hospital.ManagementSystem.Dto.AuthResponse;
import hospital.ManagementSystem.Service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {
    private final AuthService service = mock(AuthService.class);
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new AuthController(service)).build();
    }

    @Test
    void register_returnsCreated() throws Exception {
        when(service.registerUser(any(AuthRequest.class)))
                .thenReturn(new AuthResponse(null, "User registered successfully", "akhil@example.com"));
        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Akhil\",\"email\":\"akhil@example.com\",\"password\":\"Password123!\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    void login_returnsToken() throws Exception {
        when(service.loginUser(any(AuthRequest.class)))
                .thenReturn(new AuthResponse("sample.jwt.token", "Login successful", "akhil@example.com"));
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"akhil@example.com\",\"password\":\"Password123!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("sample.jwt.token"));
    }
}
