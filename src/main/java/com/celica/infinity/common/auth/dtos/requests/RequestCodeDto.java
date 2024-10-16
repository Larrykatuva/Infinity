package com.celica.infinity.common.auth.dtos.requests;

import com.celica.infinity.common.auth.enums.Context;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestCodeDto {
    @NotNull(message = "Context is required")
    private Context context;

    @NotNull(message = "Username is required")
    @Size(max = 255, message = "Username should not exceed 255 characters")
    private String username;
}
