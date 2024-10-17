package com.celica.infinity.common.authorization.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@AllArgsConstructor
public class RoleDto {
    private Long id;
    private String name;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
