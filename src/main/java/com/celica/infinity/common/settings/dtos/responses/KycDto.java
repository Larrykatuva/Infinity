package com.celica.infinity.common.settings.dtos.responses;

import com.celica.infinity.common.settings.models.Requirement;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class KycDto {
    private Long id;
    private CountryDto country;
    private BusinessDto business;
    private List<Requirement> requirements;
    private LocalDateTime createdAt;
}
