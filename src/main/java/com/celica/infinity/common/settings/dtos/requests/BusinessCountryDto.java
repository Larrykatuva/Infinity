package com.celica.infinity.common.settings.dtos.requests;

import com.celica.infinity.common.settings.enums.Action;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessCountryDto {
    @NotNull(message = "Country is required")
    private Long country;

    @NotNull(message = "Action is required")
    @Size(max = 256, message = "Action should not exceed 255 characters")
    private Action action;
}
