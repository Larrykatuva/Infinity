package com.celica.infinity.common.authorization.services;

import com.celica.infinity.common.authorization.dtos.responses.PermissionDto;
import com.celica.infinity.common.authorization.mappers.PermissionMapper;
import com.celica.infinity.common.authorization.models.Permission;
import com.celica.infinity.common.authorization.repositories.PermissionRepository;
import com.celica.infinity.utils.annotations.pagination.dtos.PaginatedResponseDto;
import com.celica.infinity.utils.exceptions.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionService(
            PermissionRepository permissionRepository,
            PermissionMapper permissionMapper
    ){
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    public PaginatedResponseDto<PermissionDto> getPermissions(PageRequest pageRequest) {
        var permissions = permissionRepository.findAll(pageRequest);
        return new PaginatedResponseDto<PermissionDto>(
                permissions.getTotalElements(),
                permissions.getNumber(),
                permissions.getTotalPages(),
                permissionMapper.permissionDtoList(permissions.getContent())
        );
    }

    public PermissionDto getPermission(Long id) {
        Permission permission = permissionRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Permission not found", "Permission matching the id not found")
        );
        return permissionMapper.toPermissionDto(permission);
    }

}
