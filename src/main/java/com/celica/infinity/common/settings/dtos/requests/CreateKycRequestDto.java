package com.celica.infinity.common.settings.dtos.requests;

import com.celica.infinity.common.settings.models.Requirement;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateKycRequestDto {
    @NotNull(message = "Country is required")
    private Long country;

    @NotNull(message = "Business is required")
    private Long business;

    @NotNull(message = "At least one requirements is required")
    private List<Requirement> requirements;
}
