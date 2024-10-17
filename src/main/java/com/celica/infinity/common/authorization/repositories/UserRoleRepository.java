package com.celica.infinity.common.authorization.repositories;

import com.celica.infinity.common.auth.models.User;
import com.celica.infinity.common.authorization.models.Role;
import com.celica.infinity.common.authorization.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findUserRoleByRoleAndUser(Role role, User user);
}
