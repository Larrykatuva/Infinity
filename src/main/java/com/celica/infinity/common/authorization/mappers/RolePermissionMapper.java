package com.celica.infinity.common.authorization.mappers;

import com.celica.infinity.common.authorization.dtos.responses.PermissionDto;
import com.celica.infinity.common.authorization.models.RolePermission;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolePermissionMapper {

    private final PermissionMapper permissionMapper;

    public RolePermissionMapper(
            PermissionMapper permissionMapper
    ) {
        this.permissionMapper = permissionMapper;
    }


    public List<PermissionDto> toPermissionDtoList(Collection<RolePermission> rolePermissions) {
        return rolePermissions
                .stream()
                .map(
                        rolePermission -> permissionMapper.toPermissionDto(rolePermission.getPermission())
                )
                .collect(Collectors.toList());

    }

}
