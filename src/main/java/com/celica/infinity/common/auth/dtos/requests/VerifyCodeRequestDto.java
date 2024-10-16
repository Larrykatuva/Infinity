package com.celica.infinity.common.auth.dtos.requests;

import com.celica.infinity.common.auth.enums.Context;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VerifyCodeRequestDto {
    @NotNull(message = "Code is required")
    @Size(max = 6, min = 6, message = "Code should be 6 characters in length")
    private String code;

    @NotNull(message = "Username is required")
    @Size(max = 255, message = "Username should not exceed 255 characters")
    private String username;

    @NotNull(message = "Context is required")
    private Context context;
}
