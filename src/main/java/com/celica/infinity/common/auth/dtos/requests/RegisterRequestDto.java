package com.celica.infinity.common.auth.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {
    @NotNull(message = "Full names are required")
    @Size(max = 255, message = "Full name cannot exceed 255 characters")
    private String fullName;

    @NotNull(message = "Password is required")
    @Size(max = 255, message = "Password cannot exceed 255 characters")
    private String password;

    @NotNull(message = "Confirm password is required")
    @Size(max = 255, message = "Confirm password cannot exceed 255 characters")
    private String confirmPassword;

    @Size(max = 255, message = "Name cannot exceed 255 characters")
    @Email
    private String email;

    @Size(max = 12, message = "Phone number cannot exceed 12 characters")
    @Size(min = 9, message = "Phone number cannot be below 9 characters")
    private String phoneNumber;
}
