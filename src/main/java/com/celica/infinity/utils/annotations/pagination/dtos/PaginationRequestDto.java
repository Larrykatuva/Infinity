package com.celica.infinity.utils.annotations.pagination.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaginationRequestDto {
    private int pageNumber, pageSize;
}
