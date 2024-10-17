package com.celica.infinity.common.authorization.repositories;

import com.celica.infinity.common.authorization.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.lang.model.element.Name;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByName(String name);

}
