package hospital.ManagementSystem.Dto;

import jakarta.validation.constraints.*;

public record PatientDto(
        Long id,
        @NotBlank(message = "Name is required") String name,
        @NotNull(message = "Age is required") @Min(0) @Max(130) Integer age,
        @NotBlank(message = "Gender is required") String gender,
        @NotBlank(message = "Disease is required") String disease,
        String address,
        @Pattern(regexp = "^$|^[0-9+() -]{7,20}$", message = "Enter a valid phone number") String phoneNumber
) {}
