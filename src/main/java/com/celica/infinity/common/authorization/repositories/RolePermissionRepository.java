package com.celica.infinity.common.authorization.repositories;

import com.celica.infinity.common.authorization.models.Permission;
import com.celica.infinity.common.authorization.models.Role;
import com.celica.infinity.common.authorization.models.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    Optional<RolePermission> findRolePermissionByPermissionAndRole(Permission permission, Role role);
}
