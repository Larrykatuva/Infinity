package com.celica.infinity.common.authorization.services;

import com.celica.infinity.common.auth.dtos.response.UserResponseDto;
import com.celica.infinity.common.auth.models.User;
import com.celica.infinity.common.auth.repositories.UserRepository;
import com.celica.infinity.common.auth.services.UserService;
import com.celica.infinity.common.authorization.dtos.requests.AssignRoleRequestDto;
import com.celica.infinity.common.authorization.dtos.responses.RoleDto;
import com.celica.infinity.common.authorization.models.Role;
import com.celica.infinity.common.authorization.models.UserRole;
import com.celica.infinity.common.authorization.repositories.RoleRepository;
import com.celica.infinity.common.authorization.repositories.UserRoleRepository;
import com.celica.infinity.utils.dtos.MessageResponseDto;
import com.celica.infinity.utils.exceptions.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleService roleService;
    private final UserService userService;

    public UserRoleService(
            UserRoleRepository userRoleRepository,
            UserRepository userRepository,
            RoleRepository roleRepository,
            RoleService roleService,
            UserService userService
    ){
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
        this.userService = userService;
    }

    public MessageResponseDto assignRole(AssignRoleRequestDto assignDto, Long userId, User actionUser) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BadRequestException("User not found", "User matching the given id not found")
        );
        Role role = roleRepository.findById(assignDto.getRole()).orElseThrow(
                () -> new BadRequestException("Role not found", "Role matching the given id not found")
        );
        Optional<UserRole> userRole = userRoleRepository.findUserRoleByRoleAndUser(role, user);
        String message = null;
        switch (assignDto.getAction()){
            case ASSIGN -> {
                if (userRole.isPresent())
                    throw new BadRequestException("Role exists", "User already assigned to this role");
                var newUserRole = UserRole.builder()
                        .role(role)
                        .user(user)
                        .assignedBy(actionUser)
                        .build();
                userRoleRepository.save(newUserRole);
                message = "Role assigned successfully";
            }
            case REVOKE -> {
                if (userRole.isEmpty())
                    throw new BadRequestException("Role not found", "User is not assigned to this role");
                UserRole existingRole = userRole.orElseThrow();
                if (!existingRole.getActive())
                    throw new BadRequestException("Role already revoked", "User role is already revoked");
                existingRole.setRevokedBy(actionUser);
                existingRole.setActive(false);
                userRoleRepository.save(existingRole);
                message = "Role revoked successfully";
            }
            default -> throw new BadRequestException("Invalid action", "Action type could not be processed");
        }
        return new MessageResponseDto(message);
    }

    private List<RoleDto> roleToDto(Collection<UserRole> userRoles) {
        List<RoleDto> roleDtoList = new ArrayList<>();
        userRoles.forEach(userRole -> roleService.roleToDto(userRole.getRole()));
        return roleDtoList;
    }

    private List<UserResponseDto> userToDto(Collection<UserRole> userRoles) {
        List<UserResponseDto> userResponseDtoList = new ArrayList<>();
        userRoles.forEach(userRole -> userService.userToDto(userRole.getUser()));
        return userResponseDtoList;
    }
}
