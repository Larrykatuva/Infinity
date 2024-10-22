package com.celica.infinity.common.settings.services;

import com.celica.infinity.common.settings.dtos.requests.BusinessCountryDto;
import com.celica.infinity.common.settings.dtos.requests.CreateBusinessDto;
import com.celica.infinity.common.settings.dtos.responses.BusinessDto;
import com.celica.infinity.common.settings.dtos.responses.CountryDto;
import com.celica.infinity.common.settings.enums.Action;
import com.celica.infinity.common.settings.mappers.BusinessMapper;
import com.celica.infinity.common.settings.mappers.CountryMapper;
import com.celica.infinity.common.settings.models.Business;
import com.celica.infinity.common.settings.models.Country;
import com.celica.infinity.common.settings.repositories.BusinessRepository;
import com.celica.infinity.common.settings.repositories.CountryRepository;
import com.celica.infinity.utils.annotations.pagination.dtos.PaginatedResponseDto;
import com.celica.infinity.utils.dtos.MessageResponseDto;
import com.celica.infinity.utils.exceptions.BadRequestException;
import com.celica.infinity.utils.storage.StorageService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final StorageService storageService;
    private final CountryRepository countryRepository;
    private final BusinessMapper businessMapper;
    private final CountryMapper countryMapper;

    public BusinessService(
            BusinessRepository businessRepository,
            StorageService storageService,
            CountryRepository countryRepository,
            BusinessMapper businessMapper,
            CountryMapper countryMapper
    ) {
        this.businessRepository = businessRepository;
        this.storageService = storageService;
        this.countryRepository = countryRepository;
        this.businessMapper = businessMapper;
        this.countryMapper = countryMapper;
    }

    public BusinessDto createBusiness(CreateBusinessDto businessDto) {
        Optional<Business> businessExists = businessRepository.findByName(businessDto.getName());
        if (businessExists.isPresent())
            throw new BadRequestException("Business exists", "Business with the given name already exists");
        String fileName = storageService.getFilename(businessDto.getLogo());
        var business = businessMapper.toBusiness(businessDto, fileName);
        businessRepository.save(business);
        storageService.saveFile(businessDto.getLogo(), fileName);
        return businessMapper.toBusinessDto(business);
    }

    public PaginatedResponseDto<BusinessDto> getBusinesses(PageRequest pageRequest) {
        var businesses = businessRepository.findAll(pageRequest);
        return new PaginatedResponseDto<>(
                businesses.getTotalElements(),
                businesses.getNumber(),
                businesses.getTotalPages(),
                businessMapper.toBusinessDtoList(businesses.getContent())
        );
    }

    public BusinessDto getBusiness(Long id) {
        var business = businessRepository.findById(id).orElseThrow();
        return businessMapper.toBusinessDto(business);
    }

    public MessageResponseDto configureCountry(BusinessCountryDto businessCountryDto, Long id) {
        var business = businessRepository.findById(id).orElseThrow();
        var country = countryRepository.findById(businessCountryDto.getCountry()).orElseThrow();
        Set<Country> countries = business.getCountries();
        String message = null;
        if (businessCountryDto.getAction() == Action.ADD) {
            if (countries.contains(country))
                throw new BadRequestException("Country exists", "This country is already linked to this business");
            business.getCountries().add(country);
            message = "Country configured successfully";
        } else {
            if (!countries.contains(country))
                throw new BadRequestException("Country does not exists", "This country is not linked to this business");
            business.getCountries().remove(country);
            message = "Country unconfirmed successfully";
        }
        businessRepository.save(business);
        return new MessageResponseDto(message);
    }

    public List<CountryDto> configuredCountries(Long id) {
        var business = businessRepository.findById(id).orElseThrow();
        var countries = business.getCountries().stream().toList();
        return countryMapper.toCountryDtoList(countries);
    }

}
