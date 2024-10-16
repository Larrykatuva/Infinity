package com.celica.infinity.common.settings.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class BusinessDto {
    private Long id;
    private String name;
    private String description;
    private String logo;
    private HashMap<String, String> metadata;
    private LocalDateTime createdAt;
}
