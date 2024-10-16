package com.celica.infinity.common.settings.controllers;

import com.celica.infinity.common.settings.dtos.requests.BusinessCountryDto;
import com.celica.infinity.common.settings.dtos.requests.CreateBusinessDto;
import com.celica.infinity.common.settings.dtos.responses.BusinessDto;
import com.celica.infinity.common.settings.dtos.responses.CountryDto;
import com.celica.infinity.common.settings.services.BusinessService;
import com.celica.infinity.utils.dtos.MessageResponseDto;
import com.celica.infinity.utils.annotations.pagination.dtos.PaginatedResponseDto;
import com.celica.infinity.utils.annotations.sorting.RequestSorting;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/settings/business")
public class BusinessController {
    public final BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @PostMapping(value = "", consumes = "multipart/form-data")
    public ResponseEntity<BusinessDto> createBusiness(@ModelAttribute @Valid CreateBusinessDto businessDto) {
        var business = businessService.createBusiness(businessDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(business);
    }

    @GetMapping()
    public ResponseEntity<PaginatedResponseDto<BusinessDto>> getBusinesses(
            @RequestSorting PageRequest pageRequest
    ) {
        var businesses = businessService.getBusinesses(pageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(businesses);
    }

    @GetMapping("{id}")
    public ResponseEntity<BusinessDto> getBusiness(@PathVariable Long id) {
        var business = businessService.getBusiness(id);
        return ResponseEntity.status(HttpStatus.OK).body(business);
    }

    @PostMapping("{id}/country")
    public ResponseEntity<MessageResponseDto> configureCountry(
            @RequestBody @Valid BusinessCountryDto businessCountryDto,
            @PathVariable Long id)
    {
        var response = businessService.configureCountry(businessCountryDto, id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("{id}/country")
    public ResponseEntity<List<CountryDto>> configuredCountries(@PathVariable Long id) {
        var countries = businessService.configuredCountries(id);
        return ResponseEntity.status(HttpStatus.OK).body(countries);
    }

}
