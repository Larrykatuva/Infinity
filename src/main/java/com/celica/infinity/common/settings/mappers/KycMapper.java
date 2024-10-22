package com.celica.infinity.common.settings.mappers;

import com.celica.infinity.common.settings.dtos.responses.KycDto;
import com.celica.infinity.common.settings.models.Kyc;
import com.celica.infinity.common.settings.models.Requirement;
import com.celica.infinity.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KycMapper {

    private final CountryMapper countryMapper;
    private final BusinessMapper businessMapper;

    public KycMapper(
            CountryMapper countryMapper,
            BusinessMapper businessMapper
    ){
        this.businessMapper = businessMapper;
        this.countryMapper = countryMapper;
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


    public KycDto toKycDto(Kyc kyc){
        return new KycDto(
                kyc.getId(),
                countryMapper.toCountryDto(kyc.getCountry()),
                businessMapper.toBusinessDto(kyc.getBusiness()),
                setRequirements(kyc.getRequirements()),
                kyc.getCreatedAt());
    }

    public List<KycDto> toKycDtoList(Collection<Kyc> kycs){
        return kycs
                .stream()
                .map(this::toKycDto)
                .collect(Collectors.toList());
    }
}
