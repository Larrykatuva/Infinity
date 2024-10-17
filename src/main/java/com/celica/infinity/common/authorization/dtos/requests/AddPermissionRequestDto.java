package com.celica.infinity.common.authorization.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddPermissionRequestDto {
    @NotNull(message = "Permission should not be empty")
    private Long permission;
}
