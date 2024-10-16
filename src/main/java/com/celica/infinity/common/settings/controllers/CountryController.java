package com.celica.infinity.common.settings.controllers;

import com.celica.infinity.common.settings.dtos.requests.CreateCountryDto;
import com.celica.infinity.common.settings.dtos.requests.UpdateCountryDto;
import com.celica.infinity.common.settings.dtos.responses.CountryDto;
import com.celica.infinity.common.settings.services.CountryService;
import com.celica.infinity.utils.annotations.pagination.dtos.PaginatedResponseDto;
import com.celica.infinity.utils.annotations.sorting.RequestSorting;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/v1/settings/country")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping(value = "", consumes = "multipart/form-data")
    public ResponseEntity<CountryDto> createCountry(@ModelAttribute @Valid CreateCountryDto countryDto) {
        var country = countryService.registerCountry(countryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(country);
    }

    @GetMapping()
    public ResponseEntity<PaginatedResponseDto<CountryDto>> getCountries(
            @RequestSorting PageRequest pageRequest
    ) {
        var countries = countryService.getCountries(pageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(countries);
    }

    @GetMapping("{id}")
    public ResponseEntity<CountryDto> getCountry(@PathVariable Long id) {
        var country = countryService.getCountry(id);
        return ResponseEntity.status(HttpStatus.OK).body(country);
    }

    @PatchMapping("{id}")
    public ResponseEntity<CountryDto> updateCountry(
            @RequestBody @Valid UpdateCountryDto updateCountryDto,
            @PathVariable Long id
    ) {
        var country = countryService.updateCountry(id, updateCountryDto);
        return ResponseEntity.status(HttpStatus.OK).body(country);
    }


}
