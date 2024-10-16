package com.celica.infinity.common.settings.repositories;

import com.celica.infinity.common.settings.models.Business;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Long> {
    Optional<Business> findByName(String name);
}
