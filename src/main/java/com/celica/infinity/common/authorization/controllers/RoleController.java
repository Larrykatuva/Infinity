package com.celica.infinity.common.authorization.controllers;

import com.celica.infinity.common.auth.models.User;
import com.celica.infinity.common.authorization.dtos.requests.RoleRequestDto;
import com.celica.infinity.common.authorization.dtos.responses.RoleDto;
import com.celica.infinity.common.authorization.services.RoleService;
import com.celica.infinity.utils.annotations.authentication.RequestUser;
import com.celica.infinity.utils.annotations.pagination.dtos.PaginatedResponseDto;
import com.celica.infinity.utils.annotations.sorting.RequestSorting;
import com.celica.infinity.utils.dtos.MessageResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/authorization/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(
            RoleService roleService
    ){
        this.roleService = roleService;
    }

    @PostMapping("")
    public ResponseEntity<MessageResponseDto> createRole(
            @RequestBody @Valid RoleRequestDto roleRequestDto,
            @RequestUser User user
    ){
        var response = roleService.createRole(roleRequestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RequestMapping("")
    public ResponseEntity<PaginatedResponseDto<RoleDto>> getRoles(
            @RequestSorting PageRequest pageRequest
    ) {
        var roles = roleService.getRoles(pageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }

    @RequestMapping("{id}")
    public ResponseEntity<RoleDto> getRole(@PathVariable Long id) {
        var role = roleService.getRole(id);
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }

    @PatchMapping("{id}")
    public ResponseEntity<MessageResponseDto> updateRole(
         @PathVariable Long id,
         @RequestBody @Valid RoleRequestDto updateDto,
         @RequestUser User user
    ) {
        var response = roleService.updateRole(updateDto, id, user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
