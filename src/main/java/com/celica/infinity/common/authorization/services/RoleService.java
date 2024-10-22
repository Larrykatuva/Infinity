package com.celica.infinity.common.authorization.services;

import com.celica.infinity.common.auth.models.User;
import com.celica.infinity.common.authorization.dtos.requests.RoleRequestDto;
import com.celica.infinity.common.authorization.dtos.responses.RoleDto;
import com.celica.infinity.common.authorization.mappers.RoleMapper;
import com.celica.infinity.common.authorization.models.Role;
import com.celica.infinity.common.authorization.repositories.RoleRepository;
import com.celica.infinity.utils.annotations.pagination.dtos.PaginatedResponseDto;
import com.celica.infinity.utils.dtos.MessageResponseDto;
import com.celica.infinity.utils.exceptions.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleService(
            RoleRepository roleRepository,
            RoleMapper roleMapper
    ) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    public MessageResponseDto createRole(RoleRequestDto roleDto, User user) {
        Optional<Role> role = roleRepository.findRoleByName(roleDto.getName());
        if (role.isPresent())
            throw new BadRequestException("Role exists", "Role with the given name already exists");
        var newRole = Role.builder()
                .name(roleDto.getName())
                .addedBy(user)
                .build();
        roleRepository.save(newRole);
        return new MessageResponseDto("Role added successfully");
    }

    public PaginatedResponseDto<RoleDto> getRoles(PageRequest pageRequest){
        var roles = roleRepository.findAll(pageRequest);
        return new PaginatedResponseDto<RoleDto>(
                roles.getTotalElements(),
                roles.getNumber(),
                roles.getTotalPages(),
                roleMapper.toRoleDtoList(roles.getContent())
        );
    }

    public MessageResponseDto updateRole(RoleRequestDto roleDto, Long id, User user) {
        Role role = roleRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Role not found", "Role matching the id not found")
        );
        boolean update = false;
        if (roleDto.getName() != null) {
            role.setName(role.getName());
            update = true;
        }
        if (!update)
            throw new BadRequestException("Nothing to update", "No changes detected");
        role.setUpdatedBy(user);
        roleRepository.save(role);
        return new MessageResponseDto("Role updated successfully");
    }

    public RoleDto getRole(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Role not found", "Role matching the id not found")
        );
        return roleMapper.toRoleDto(role);
    }

}
