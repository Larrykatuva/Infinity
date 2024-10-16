package com.celica.infinity.common.settings.services;

import com.celica.infinity.common.settings.dtos.requests.CreateKycRequestDto;
import com.celica.infinity.common.settings.dtos.responses.KycDto;
import com.celica.infinity.common.settings.models.Kyc;
import com.celica.infinity.common.settings.models.Requirement;
import com.celica.infinity.common.settings.repositories.BusinessRepository;
import com.celica.infinity.common.settings.repositories.CountryRepository;
import com.celica.infinity.common.settings.repositories.KycRepository;
import com.celica.infinity.utils.Utils;
import com.celica.infinity.utils.annotations.pagination.dtos.PaginatedResponseDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class KycService {

    private final KycRepository kycRepository;
    private final CountryRepository countryRepository;
    private final CountryService countryService;
    private final BusinessRepository businessRepository;
    private final BusinessService businessService;

    public KycService(
            KycRepository kycRepository,
            CountryRepository countryRepository,
            CountryService countryService,
            BusinessRepository businessRepository,
            BusinessService businessService
    ) {
        this.kycRepository = kycRepository;
        this.countryRepository = countryRepository;
        this.countryService = countryService;
        this.businessRepository = businessRepository;
        this.businessService = businessService;
    }

    private String getRequirements(List<Requirement> requirements) {
        if (requirements.isEmpty()) return null;
        StringBuilder strRequirements = new StringBuilder();
        for (Requirement requirement: requirements) {
            strRequirements.append("{ " + "name:").append(requirement.getName())
                    .append(", ").append("required:").append(requirement.isRequired())
                    .append(", ").append("type:").append(requirement.isRequired())
                    .append("}, ");
        }
        return strRequirements.toString();
    }

    private List<Requirement> setRequirements(String data) {
        List<Requirement> requirements = new ArrayList<>();
        if (Utils.isStringNullOrEmpty(data)) return requirements;
        String[] records = data.split("}, ");
        if (records.length < 1) return requirements;
        for (String record: records) {
            if (record.startsWith("{")) {
                String[] values = record.substring(1).split(",");
                if (values.length == 3) {
                    Requirement requirement = getRequirement(values);
                    requirements.add(requirement);
                }
            }
        }
        return requirements;
    }

    private static Requirement getRequirement(String[] values) {
        Requirement requirement = new Requirement();
        for (String value: values) {
            String[] reqs = value.split(":");
            if (reqs.length == 2) {
                switch (reqs[0]) {
                    case "name":
                        requirement.setName(reqs[1]);
                        break;
                    case  "required":
                        requirement.setRequired(Boolean.parseBoolean(reqs[1]));
                        break;
                    case "type":
                        requirement.setType(reqs[1]);
                        break;
                }
            }
        }
        return requirement;
    }

    public KycDto configureKyc(CreateKycRequestDto kycRequestDto) {
        var country = countryRepository.findById(kycRequestDto.getCountry()).orElseThrow();
        var business = businessRepository.findById(kycRequestDto.getBusiness()).orElseThrow();
        var kyc = Kyc.builder()
                .country(country)
                .business(business)
                .requirements(getRequirements(kycRequestDto.getRequirements()))
                .build();
        kycRepository.save(kyc);
        return kycToDto(kyc);
    }

    public PaginatedResponseDto<KycDto> getKycs(PageRequest pageRequest){
        var kycs = kycRepository.findAll(pageRequest);
        return new PaginatedResponseDto<>(
                kycs.getTotalElements(),
                kycs.getNumber(),
                kycs.getTotalPages(),
                kycToDto(kycs.getContent())
        );
    }

    public KycDto getKyc(Long id) {
        var kyc = kycRepository.findById(id).orElseThrow();
        return kycToDto(kyc);
    }

    private KycDto kycToDto(Kyc kyc) {
        return new KycDto(kyc.getId(), countryService.countryToDto(kyc.getCountry()),
                businessService.businessToDto(kyc.getBusiness()), setRequirements(kyc.getRequirements()),
                kyc.getCreatedAt());
    }

    private List<KycDto> kycToDto(Collection<Kyc> kycList) {
        List<KycDto> kycDtoList = new ArrayList<>();
        kycList.forEach(kyc -> kycDtoList.add(kycToDto(kyc)));
        return kycDtoList;
    }


}
