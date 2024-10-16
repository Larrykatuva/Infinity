package com.celica.infinity.common.auth.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetPasswordRequestDto extends CodeRequestDto{
    @NotNull(message = "Password is required")
    @Size(max = 255, message = "Password cannot exceed 255 characters")
    private String password;

    @NotNull(message = "Confirm password is required")
    @Size(max = 255, message = "Confirm password cannot exceed 255 characters")
    private String confirmPassword;
}
