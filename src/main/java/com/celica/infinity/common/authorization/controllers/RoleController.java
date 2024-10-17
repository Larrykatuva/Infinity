package com.celica.infinity.common.authorization.controllers;

import com.celica.infinity.common.authorization.services.RoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/authorization/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(
            RoleService roleService
    ){
        this.roleService = roleService;
    }

}
