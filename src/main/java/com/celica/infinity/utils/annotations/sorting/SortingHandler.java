package com.celica.infinity.utils.annotations.sorting;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class SortingHandler {

    public static Sort getRequestSorting(String sortBy) {
        List<String> sortProperties = List.of(sortBy.split("&"));
        List<Sort.Order> orders = sortProperties.stream()
                .map(prop -> {
                    String[] sortAndDirection = prop.split("=");
                    if (sortAndDirection.length == 1) {
                        return Sort.Order.asc(prop);
                    } else {
                        String direction = sortAndDirection[1];
                        if (direction.equals("desc") || direction.equals("descending")) {
                            return Sort.Order.desc(prop);
                        } else {
                            return Sort.Order.asc(prop);
                        }
                    }
                })
                .collect(Collectors.toList());
        return Sort.by(orders);
    }
}
