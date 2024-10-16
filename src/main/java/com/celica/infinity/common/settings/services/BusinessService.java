package com.celica.infinity.common.settings.services;

import com.celica.infinity.common.settings.dtos.requests.BusinessCountryDto;
import com.celica.infinity.common.settings.dtos.requests.CreateBusinessDto;
import com.celica.infinity.common.settings.dtos.responses.BusinessDto;
import com.celica.infinity.common.settings.dtos.responses.CountryDto;
import com.celica.infinity.common.settings.enums.Action;
import com.celica.infinity.common.settings.models.Business;
import com.celica.infinity.common.settings.models.Country;
import com.celica.infinity.common.settings.repositories.BusinessRepository;
import com.celica.infinity.common.settings.repositories.CountryRepository;
import com.celica.infinity.utils.Utils;
import com.celica.infinity.utils.dtos.MessageResponseDto;
import com.celica.infinity.utils.exceptions.BadRequestException;
import com.celica.infinity.utils.annotations.pagination.dtos.PaginatedResponseDto;
import com.celica.infinity.utils.storage.StorageService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final StorageService storageService;
    private final CountryRepository countryRepository;
    private final CountryService countryService;

    public BusinessService(
            BusinessRepository businessRepository,
            StorageService storageService,
            CountryRepository countryRepository,
            CountryService countryService
    ) {
        this.businessRepository = businessRepository;
        this.storageService = storageService;
        this.countryRepository = countryRepository;
        this.countryService = countryService;
    }

    private String getMetadata(HashMap<String, String> metadata) {
        StringBuilder stringMetadata = new StringBuilder();
        if (metadata.isEmpty()) return null;
        for (Map.Entry<String, String> entry: metadata.entrySet()) {
            stringMetadata.append("{").append(entry.getKey()).append("=").append(entry.getValue()).append("}, ");
        }
        return stringMetadata.toString();
    }

    private HashMap<String, String> setMetadata(String metadata) {
        HashMap<String, String > map = new HashMap<>();
        if (Utils.isStringNullOrEmpty(metadata)) return map;
        String[] records = metadata.split("}, ");
        for (String record: records) {
            if (!Utils.isStringNullOrEmpty(record)) {
                if (record.startsWith("{")) {
                    String[] data = record.substring(1).split("=");
                    if (data.length == 2) {
                        map.put(data[0], data[1]);
                    }
                }
            }
        }
        return map;
    }

    public BusinessDto createBusiness(CreateBusinessDto businessDto) {
        Optional<Business> businessExists = businessRepository.findByName(businessDto.getName());
        if (businessExists.isPresent())
            throw new BadRequestException("Business exists", "Business with the given name already exists");
        String fileName = storageService.getFilename(businessDto.getLogo());
        var business = Business.builder()
                .name(businessDto.getName())
                .description(businessDto.getDescription())
                .logo(fileName)
                .metadata(getMetadata(businessDto.getMetadata()))
                .build();
        businessRepository.save(business);
        storageService.saveFile(businessDto.getLogo(), fileName);
        return businessToDto(business);
    }

    public PaginatedResponseDto<BusinessDto> getBusinesses(PageRequest pageRequest) {
        var businesses = businessRepository.findAll(pageRequest);
        return new PaginatedResponseDto<>(
                businesses.getTotalElements(),
                businesses.getNumber(),
                businesses.getTotalPages(),
                businessToDto(businesses.getContent())
        );
    }

    public BusinessDto getBusiness(Long id) {
        var business = businessRepository.findById(id).orElseThrow();
        return businessToDto(business);
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
        return countryService.countryToDto(countries);
    }

    public BusinessDto businessToDto(Business business) {
        return new BusinessDto(business.getId(), business.getName(), business.getDescription(),
                storageService.getFileUrl(business.getLogo()), setMetadata(business.getMetadata()),
                business.getCreatedAt());
    }

    private List<BusinessDto> businessToDto(Collection<Business> businessesList) {
        List<BusinessDto> businessDtoList = new ArrayList<>();
        businessesList.forEach(business -> businessDtoList.add(businessToDto(business)));
        return businessDtoList;
    }
}
