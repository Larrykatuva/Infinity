package com.celica.infinity.common.settings.dtos.responses;

import com.celica.infinity.common.settings.dtos.requests.CreateCountryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CountryDto {
    private String name;
    private String shortForm;
    private String currency;
    private int code;
    private String timezone;
    private final long id;
    private final boolean isActive;
    private final String logo;
    private final LocalDateTime createdAt;
}
