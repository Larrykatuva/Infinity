package com.celica.infinity.common.settings.mappers;

import com.celica.infinity.common.settings.dtos.requests.CreateCountryDto;
import com.celica.infinity.common.settings.dtos.responses.CountryDto;
import com.celica.infinity.common.settings.models.Country;
import com.celica.infinity.utils.storage.StorageService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryMapper {

    private final StorageService storageService;

    public CountryMapper(StorageService storageService){
        this.storageService = storageService;
    }

    public Country toCountry(CreateCountryDto countryDto, String filename) {
        return Country.builder()
                .name(countryDto.getName())
                .shortForm(countryDto.getShortForm())
                .currency(countryDto.getCurrency())
                .code(countryDto.getCode())
                .timezone(countryDto.getTimezone())
                .logo(filename)
                .build();
    }

    public CountryDto toCountryDto(Country country){
        return new CountryDto(
                country.getName(),
                country.getShortForm(),
                country.getCurrency(),
                country.getCode(),
                country.getTimezone(),
                country.getId(),
                country.isActive(),
                storageService.getFileUrl(country.getLogo()),
                country.getCreatedAt());
    }

    public List<CountryDto> toCountryDtoList(Collection<Country> countries){
        return countries
                .stream()
                .map(this::toCountryDto)
                .collect(Collectors.toList());
    }
}
