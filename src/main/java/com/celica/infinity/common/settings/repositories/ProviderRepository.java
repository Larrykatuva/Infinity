package com.celica.infinity.common.settings.repositories;

import com.celica.infinity.common.settings.models.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepository extends JpaRepository<Provider, Long> {
}
