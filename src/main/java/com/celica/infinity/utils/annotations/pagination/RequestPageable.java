package com.celica.infinity.utils.annotations.pagination;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestPageable {
    int defaultPage() default 0;
    int defaultSize() default 10;
}


