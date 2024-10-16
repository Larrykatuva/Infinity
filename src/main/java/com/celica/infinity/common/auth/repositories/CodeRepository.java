package com.celica.infinity.common.auth.repositories;

import com.celica.infinity.common.auth.enums.Context;
import com.celica.infinity.common.auth.models.Code;
import com.celica.infinity.common.auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, Long> {
    Optional<Code> findFirstByUserAndCodeAndUsedAndContextOrderByCreatedAtDesc(User user, String code, Boolean used,
                                                                               Context context);
}
