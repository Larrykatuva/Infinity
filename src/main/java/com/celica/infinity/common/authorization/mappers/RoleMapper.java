package com.celica.infinity.common.authorization.mappers;

import com.celica.infinity.common.authorization.dtos.responses.RoleDto;
import com.celica.infinity.common.authorization.models.Role;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleMapper {


    public RoleDto toRoleDto(Role role) {
        return new RoleDto(
                role.getId(),
                role.getName(),
                role.getActive(),
                role.getCreatedAt(),
                role.getUpdatedAt());
    }

    public List<RoleDto> toRoleDtoList(Collection<Role> roles) {
        return roles
                .stream()
                .map(this::toRoleDto)
                .collect(Collectors.toList());
    }
}
