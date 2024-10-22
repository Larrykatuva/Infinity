package com.celica.infinity.common.settings.services;

import com.celica.infinity.common.settings.dtos.requests.CreateCountryDto;
import com.celica.infinity.common.settings.dtos.requests.UpdateCountryDto;
import com.celica.infinity.common.settings.dtos.responses.CountryDto;
import com.celica.infinity.common.settings.mappers.CountryMapper;
import com.celica.infinity.common.settings.repositories.CountryRepository;
import com.celica.infinity.utils.annotations.pagination.dtos.PaginatedResponseDto;
import com.celica.infinity.utils.exceptions.BadRequestException;
import com.celica.infinity.utils.storage.StorageService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CountryService {

    private final CountryRepository countryRepository;
    private final StorageService storageService;
    private final CountryMapper countryMapper;

    public CountryService(
            CountryRepository countryRepository,
            StorageService storageService,
            CountryMapper countryMapper
    ){
        this.storageService = storageService;
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }

    public CountryDto registerCountry(CreateCountryDto countryDto) {
        String fileName = storageService.getFilename(countryDto.getLogo());
        var country = countryMapper.toCountry(countryDto, fileName);
        countryRepository.save(country);
        storageService.saveFile(countryDto.getLogo(), fileName);
        return countryMapper.toCountryDto(country);
    }

    public PaginatedResponseDto<CountryDto> getCountries(PageRequest pageRequest) {
        var countries = countryRepository.findAll(pageRequest);
        return new PaginatedResponseDto<CountryDto>(
                        countries.getTotalElements(),
                        countries.getNumber(),
                        countries.getTotalPages(),
                        countryMapper.toCountryDtoList(countries.getContent())
                );
    }

    public CountryDto getCountry(Long id) {
        var country = countryRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Country not found", "Country matching the id not found")
        );
        return countryMapper.toCountryDto(country);
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
        return countryMapper.toCountryDto(country);
    }

}

