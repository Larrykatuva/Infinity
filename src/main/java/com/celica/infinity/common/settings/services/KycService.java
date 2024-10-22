package com.celica.infinity.common.settings.services;

import com.celica.infinity.common.settings.dtos.requests.CreateKycRequestDto;
import com.celica.infinity.common.settings.dtos.responses.KycDto;
import com.celica.infinity.common.settings.mappers.KycMapper;
import com.celica.infinity.common.settings.models.Kyc;
import com.celica.infinity.common.settings.models.Requirement;
import com.celica.infinity.common.settings.repositories.BusinessRepository;
import com.celica.infinity.common.settings.repositories.CountryRepository;
import com.celica.infinity.common.settings.repositories.KycRepository;
import com.celica.infinity.utils.annotations.pagination.dtos.PaginatedResponseDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KycService {

    private final KycRepository kycRepository;
    private final CountryRepository countryRepository;
    private final BusinessRepository businessRepository;
    private final KycMapper kycMapper;

    public KycService(
            KycRepository kycRepository,
            CountryRepository countryRepository,
            BusinessRepository businessRepository,
            KycMapper kycMapper
    ) {
        this.kycRepository = kycRepository;
        this.countryRepository = countryRepository;
        this.businessRepository = businessRepository;
        this.kycMapper = kycMapper;
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

    public KycDto configureKyc(CreateKycRequestDto kycRequestDto) {
        var country = countryRepository.findById(kycRequestDto.getCountry()).orElseThrow();
        var business = businessRepository.findById(kycRequestDto.getBusiness()).orElseThrow();
        var kyc = Kyc.builder()
                .country(country)
                .business(business)
                .requirements(getRequirements(kycRequestDto.getRequirements()))
                .build();
        kycRepository.save(kyc);
        return kycMapper.toKycDto(kyc);
    }

    public PaginatedResponseDto<KycDto> getKycs(PageRequest pageRequest){
        var kycs = kycRepository.findAll(pageRequest);
        return new PaginatedResponseDto<>(
                kycs.getTotalElements(),
                kycs.getNumber(),
                kycs.getTotalPages(),
                kycMapper.toKycDtoList(kycs.getContent())
        );
    }

    public KycDto getKyc(Long id) {
        var kyc = kycRepository.findById(id).orElseThrow();
        return kycMapper.toKycDto(kyc);
    }

}
