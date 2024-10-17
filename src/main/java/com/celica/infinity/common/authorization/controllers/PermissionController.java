package com.celica.infinity.common.authorization.controllers;

import com.celica.infinity.common.authorization.dtos.responses.PermissionDto;
import com.celica.infinity.common.authorization.services.PermissionService;
import com.celica.infinity.utils.annotations.pagination.dtos.PaginatedResponseDto;
import com.celica.infinity.utils.annotations.sorting.RequestSorting;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/authorization/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @RequestMapping("")
    public ResponseEntity<PaginatedResponseDto<PermissionDto>> getPermissions(
            @RequestSorting PageRequest pageRequest
    ){
        var permissions = permissionService.getPermissions(pageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(permissions);
    }

    @RequestMapping("{id}")
    public ResponseEntity<PermissionDto> getPermission(@PathVariable Long id){
        var permission = permissionService.getPermission(id);
        return ResponseEntity.status(HttpStatus.OK).body(permission);
    }
}
