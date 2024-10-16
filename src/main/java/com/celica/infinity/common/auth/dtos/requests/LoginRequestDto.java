package com.celica.infinity.common.auth.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    @NotNull(message = "Username is required")
    @Size(max = 255, message = "Username cannot exceed 255 characters")
    private String username;

    @NotNull(message = "Password is required")
    @Size(max = 255, message = "Password should not exceed 255 characters")
    private String password;
}
