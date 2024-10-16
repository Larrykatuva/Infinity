package com.celica.infinity.common.settings.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCountryDto {
    private String name, shortForm, currency, timeZone;
    private int code;
}
