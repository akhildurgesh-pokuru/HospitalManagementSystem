package hospital.ManagementSystem.Service;

import hospital.ManagementSystem.Dto.AuthRequest;
import hospital.ManagementSystem.Dto.AuthResponse;
import hospital.ManagementSystem.Entity.Role;
import hospital.ManagementSystem.Entity.User;
import hospital.ManagementSystem.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Locale;
import hospital.ManagementSystem.Config.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse registerUser(AuthRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        }
        if (request.password().length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Password must have at least 8 characters");
        }
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        User user = new User();
        user.setName(request.name().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.password()));
        // Public registration must never grant ADMIN privileges.
        user.setRole(Role.USER);
        userRepository.save(user);
        return new AuthResponse(null, "User registered successfully", user.getEmail());
    }

    public AuthResponse loginUser(AuthRequest request) {
        User user = authenticate(request.email(), request.password());
        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token, "Login successful", user.getEmail());
    }

    public User authenticate(String email, String rawPassword) {
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        return user;
    }
}
