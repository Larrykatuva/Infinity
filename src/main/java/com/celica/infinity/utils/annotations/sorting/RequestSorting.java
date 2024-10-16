package com.celica.infinity.utils.annotations.sorting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestSorting {
    int defaultPage() default 0;
    int defaultSize() default 10;
    String defaultSortBy() default "id";
    String defaultSortDir() default "desc";
}
