package com.celica.infinity.common.auth.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeRequestDto {
    @NotNull(message = "Code is required")
    @Size(max = 6, min = 6, message = "Code should be 6 characters in length")
    private String code;

    @NotNull(message = "Username is required")
    @Size(max = 255, message = "Username should not exceed 255 characters")
    private String username;
}
