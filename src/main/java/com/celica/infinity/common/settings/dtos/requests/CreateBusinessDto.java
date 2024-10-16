package com.celica.infinity.common.settings.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class CreateBusinessDto {
    @NotNull(message = "Business name is required")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;

    @NotNull(message = "Business description is required")
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    private MultipartFile logo;

    private HashMap<String, String> metadata;
}
