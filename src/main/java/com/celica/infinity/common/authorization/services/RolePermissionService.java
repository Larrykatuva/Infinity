package com.celica.infinity.common.authorization.services;

import com.celica.infinity.common.auth.models.User;
import com.celica.infinity.common.authorization.dtos.responses.PermissionDto;
import com.celica.infinity.common.authorization.models.Permission;
import com.celica.infinity.common.authorization.models.Role;
import com.celica.infinity.common.authorization.models.RolePermission;
import com.celica.infinity.common.authorization.repositories.RolePermissionRepository;
import com.celica.infinity.utils.annotations.pagination.dtos.PaginatedResponseDto;
import com.celica.infinity.utils.dtos.MessageResponseDto;
import com.celica.infinity.utils.exceptions.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionService permissionService;

    public RolePermissionService(
            RolePermissionRepository rolePermissionRepository,
            PermissionService permissionService
    ){
        this.rolePermissionRepository = rolePermissionRepository;
        this.permissionService = permissionService;
    }

    public MessageResponseDto addPermissionToRole(Permission permission, Role role, User user) {
        Optional<RolePermission> rolePermission = rolePermissionRepository.
                findRolePermissionByPermissionAndRole(permission, role);
        if (rolePermission.isPresent()){
            RolePermission existing = rolePermission.orElseThrow();
            if (existing.getActive())
                throw new BadRequestException("Permission exists", "Permission already linked to this role");
        }
        var newRolePermission = RolePermission.builder()
                .permission(permission)
                .role(role)
                .addedBy(user)
                .build();
        rolePermissionRepository.save(newRolePermission);
        return new MessageResponseDto("Permission added successfully");
    }

    public MessageResponseDto revokePermission(Permission permission, Role role, User user){
        RolePermission rolePermission = rolePermissionRepository.findRolePermissionByPermissionAndRole(
                permission, role).orElseThrow(
                () -> new BadRequestException("No permission found", "Role is not linked to this permission")
        );
        if (!rolePermission.getActive())
            throw new BadRequestException("No permission found", "Role is not linked to this permission");
        rolePermission.setActive(false);
        rolePermission.setRevokedBy(user);
        rolePermissionRepository.save(rolePermission);
        return new MessageResponseDto("Permission revoked successfully");
    }

    public PaginatedResponseDto<PermissionDto> getRolePermissions(PageRequest pageRequest){
        var rolePermissions = rolePermissionRepository.findAll(pageRequest);
        return new PaginatedResponseDto<PermissionDto>(
                rolePermissions.getTotalElements(),
                rolePermissions.getNumber(),
                rolePermissions.getTotalPages(),
                permissionDtoList(rolePermissions.getContent())
        );
    }

    private List<PermissionDto> permissionDtoList(Collection<RolePermission> rolePermissions) {
        List<PermissionDto> permissionDtoList = new ArrayList<>();
        rolePermissions.forEach(rolePermission -> permissionDtoList.add(
                permissionService.permissionToDto(rolePermission.getPermission())
        ));
        return permissionDtoList;
    }
}
