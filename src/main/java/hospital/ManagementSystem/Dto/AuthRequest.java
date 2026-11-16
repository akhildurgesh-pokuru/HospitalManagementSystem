package hospital.ManagementSystem.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// Registration uses name, email and password. Login uses email and password.
// For login, name can be omitted; validation is done in AuthService.
public record AuthRequest(
        String name,
        @NotBlank(message = "Email is required") @Email(message = "Enter a valid email") String email,
        @NotBlank(message = "Password is required") String password
) {}
