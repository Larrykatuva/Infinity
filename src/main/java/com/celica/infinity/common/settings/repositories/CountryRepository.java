package com.celica.infinity.common.settings.repositories;

import com.celica.infinity.common.settings.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
