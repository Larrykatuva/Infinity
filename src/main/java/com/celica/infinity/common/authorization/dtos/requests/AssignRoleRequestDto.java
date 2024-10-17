package com.celica.infinity.common.authorization.dtos.requests;

import com.celica.infinity.common.authorization.enums.AssignAction;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AssignRoleRequestDto {
    @NotNull(message = "Role should not be null")
    private Long role;

    @NotNull(message = "Action is required")
    private AssignAction action;
}
