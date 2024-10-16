package com.celica.infinity.utils.annotations.pagination.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PaginatedResponseDto<T> {
    private Long totalItems;
    private int currentPage, totalPages;
    private List<T> results;
}
