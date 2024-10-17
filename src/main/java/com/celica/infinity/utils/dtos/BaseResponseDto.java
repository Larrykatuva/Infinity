package com.celica.infinity.utils.dtos;

import lombok.AllArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@AllArgsConstructor
public class BaseResponseDto {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
