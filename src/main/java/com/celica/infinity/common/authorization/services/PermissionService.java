package com.celica.infinity.common.authorization.services;

import com.celica.infinity.common.authorization.dtos.responses.PermissionDto;
import com.celica.infinity.common.authorization.models.Permission;
import com.celica.infinity.common.authorization.repositories.PermissionRepository;
import com.celica.infinity.utils.annotations.pagination.dtos.PaginatedResponseDto;
import com.celica.infinity.utils.exceptions.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository){
        this.permissionRepository = permissionRepository;
    }

    public PaginatedResponseDto<PermissionDto> getPermissions(PageRequest pageRequest) {
        var permissions = permissionRepository.findAll(pageRequest);
        return new PaginatedResponseDto<PermissionDto>(
                permissions.getTotalElements(),
                permissions.getNumber(),
                permissions.getTotalPages(),
                permissionToDto(permissions.getContent())
        );
    }

    public PermissionDto getPermission(Long id) {
        Permission permission = permissionRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Permission not found", "Permission matching the id not found")
        );
        return permissionToDto(permission);
    }

    public PermissionDto permissionToDto(Permission permission) {
        return new PermissionDto(permission.getId(),permission.getName(), permission.getType(), permission.getActive(),
                permission.getCreatedAt(), permission.getUpdatedAt());
    }

    private List<PermissionDto> permissionToDto(Collection<Permission> permissions){
        List<PermissionDto> permissionDtoList = new ArrayList<>();
        permissions.forEach(permission -> permissionDtoList.add(permissionToDto(permission)));
        return permissionDtoList;
    }
}
