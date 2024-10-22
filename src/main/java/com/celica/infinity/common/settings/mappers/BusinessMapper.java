package com.celica.infinity.common.settings.mappers;

import com.celica.infinity.common.settings.dtos.requests.CreateBusinessDto;
import com.celica.infinity.common.settings.dtos.responses.BusinessDto;
import com.celica.infinity.common.settings.models.Business;
import com.celica.infinity.utils.Utils;
import com.celica.infinity.utils.storage.StorageService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BusinessMapper {

    private final StorageService storageService;

    public BusinessMapper(StorageService storageService){
        this.storageService = storageService;
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

    public Business toBusiness(CreateBusinessDto businessDto, String filename){
       return Business.builder()
               .name(businessDto.getName())
               .description(businessDto.getDescription())
               .logo(filename)
               .metadata(getMetadata(businessDto.getMetadata()))
               .build();
    }

    public BusinessDto toBusinessDto(Business business){
        return new BusinessDto(
                business.getId(),
                business.getName(),
                business.getDescription(),
                storageService.getFileUrl(business.getLogo()),
                setMetadata(business.getMetadata()),
                business.getCreatedAt());
    }

    public List<BusinessDto> toBusinessDtoList(Collection<Business> businesses){
        return businesses
                .stream()
                .map(this::toBusinessDto)
                .collect(Collectors.toList());
    }
}
