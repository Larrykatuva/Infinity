package com.celica.infinity.common.authorization.repositories;

import com.celica.infinity.common.authorization.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
