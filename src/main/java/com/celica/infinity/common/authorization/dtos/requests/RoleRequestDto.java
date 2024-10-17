package com.celica.infinity.common.authorization.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleRequestDto {
    @NotNull(message = "Name is required")
    @Size(max = 255, message = "Name should not exceed 255 characters")
    private String name;

    private boolean active;
}
