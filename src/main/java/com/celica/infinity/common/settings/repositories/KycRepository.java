package com.celica.infinity.common.settings.repositories;

import com.celica.infinity.common.settings.models.Kyc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KycRepository extends JpaRepository<Kyc, Long> {
}
