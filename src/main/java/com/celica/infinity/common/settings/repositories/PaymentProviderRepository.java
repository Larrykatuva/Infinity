package com.celica.infinity.common.settings.repositories;

import com.celica.infinity.common.settings.models.PaymentProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentProviderRepository extends JpaRepository<PaymentProvider, Long> {
}
