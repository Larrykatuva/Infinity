package com.celica.infinity.common.settings.services;

import com.celica.infinity.common.settings.dtos.requests.CreateCountryDto;
import com.celica.infinity.common.settings.dtos.requests.UpdateCountryDto;
import com.celica.infinity.common.settings.dtos.responses.CountryDto;
import com.celica.infinity.common.settings.models.Country;
import com.celica.infinity.common.settings.repositories.CountryRepository;
import com.celica.infinity.utils.exceptions.BadRequestException;
import com.celica.infinity.utils.annotations.pagination.dtos.PaginatedResponseDto;
import com.celica.infinity.utils.storage.StorageService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class CountryService {

    private final CountryRepository countryRepository;
    private final StorageService storageService;
    private final Logger logger = LoggerFactory.getLogger(CountryService.class);

    public CountryService(
            CountryRepository countryRepository,
            StorageService storageService
    ){
        this.storageService = storageService;
        this.countryRepository = countryRepository;
    }

    public CountryDto registerCountry(CreateCountryDto countryDto) {
        String fileName = storageService.getFilename(countryDto.getLogo());
        var country = Country.builder()
                .name(countryDto.getName())
                .shortForm(countryDto.getShortForm())
                .currency(countryDto.getCurrency())
                .code(countryDto.getCode())
                .timezone(countryDto.getTimezone())
                .logo(fileName)
                .build();
        countryRepository.save(country);
        storageService.saveFile(countryDto.getLogo(), fileName);
        return countryToDto(country);
    }

    public PaginatedResponseDto<CountryDto> getCountries(PageRequest pageRequest) {
        var countries = countryRepository.findAll(pageRequest);
        return new PaginatedResponseDto<CountryDto>(
                        countries.getTotalElements(),
                        countries.getNumber(),
                        countries.getTotalPages(),
                        countryToDto(countries.getContent())
                );
    }

    public CountryDto getCountry(Long id) {
        var country = countryRepository.findById(id).orElseThrow();
        return countryToDto(country);
    }

    public CountryDto updateCountry(Long id, UpdateCountryDto updateCountryDto) {
        var country = countryRepository.findById(id).orElseThrow();
        boolean update = false;
        if (updateCountryDto.getName() != null) {
            country.setName(updateCountryDto.getName());
            update = true;
        }
        if (updateCountryDto.getShortForm() != null) {
            country.setShortForm(updateCountryDto.getShortForm());
            update = true;
        }
        if (updateCountryDto.getCurrency() != null) {
            country.setCurrency(updateCountryDto.getCurrency());
            update = true;
        }
        if (updateCountryDto.getTimeZone() != null) {
            country.setTimezone(updateCountryDto.getTimeZone());
            update = true;
        }
        if (!update) throw new BadRequestException("Nothing to update", "No changes detected");
        countryRepository.save(country);
        return countryToDto(country);
    }

    public CountryDto countryToDto(Country country) {
        return new CountryDto(country.getName(), country.getShortForm(), country.getCurrency(), country.getCode(),
                country.getTimezone(), country.getId(), country.isActive(),
                storageService.getFileUrl(country.getLogo()), country.getCreatedAt());
    }

    public List<CountryDto> countryToDto(Collection<Country> countryList) {
        List<CountryDto> countryDtoList = new ArrayList<>();
        countryList.forEach(country -> countryDtoList.add(countryToDto(country)));
        return countryDtoList;
    }
}

