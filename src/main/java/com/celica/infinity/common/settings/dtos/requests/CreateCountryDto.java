package com.celica.infinity.common.settings.dtos.requests;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateCountryDto {
    @NotNull(message = "Country name is required")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;

    @NotNull(message = "Country short form is required")
    @Size(max = 255, message = "Country short form cannot exceed 255 characters")
    private String shortForm;

    @NotNull(message = "Currency is required")
    @Size(max = 255, message = "Currency cannot exceed 255 characters")
    private String currency;

    @NotNull(message = "Country code is required")
    private int code;

    @NotNull(message = "Timezone is required")
    @Size(max = 255, message = "Timezone cannot exceed 255 characters")
    private String timezone;

    private MultipartFile logo;
}
