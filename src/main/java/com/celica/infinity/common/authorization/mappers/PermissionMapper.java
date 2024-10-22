package com.celica.infinity.common.authorization.mappers;

import com.celica.infinity.common.authorization.dtos.responses.PermissionDto;
import com.celica.infinity.common.authorization.models.Permission;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionMapper {

    public PermissionDto toPermissionDto(Permission permission){
        return new PermissionDto(
                permission.getId(),
                permission.getName(),
                permission.getType(),
                permission.getActive(),
                permission.getCreatedAt(),
                permission.getUpdatedAt());
    }

    public List<PermissionDto> permissionDtoList(Collection<Permission> permissions){
        return permissions
                .stream()
                .map(this::toPermissionDto)
                .collect(Collectors.toList());
    }
}
