package com.celica.infinity.common.authorization.dtos.responses;

import com.celica.infinity.common.authorization.enums.PermissionType;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@AllArgsConstructor
public class PermissionDto {
    private Long id;
    private String name;
    private PermissionType type;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
