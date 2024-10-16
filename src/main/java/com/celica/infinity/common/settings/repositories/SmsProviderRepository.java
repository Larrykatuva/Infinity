package com.celica.infinity.common.settings.repositories;

import com.celica.infinity.common.settings.models.SmsProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsProviderRepository extends JpaRepository<SmsProvider, Long> {
}
