package com.celica.infinity.utils.annotations.sorting;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@ControllerAdvice
public class RequestSortingResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(PageRequest.class) &&
                parameter.hasParameterAnnotation(RequestSorting.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        RequestSorting pageableSort = parameter.getParameterAnnotation(RequestSorting.class);
        int page = getIntParameter(webRequest, "page", pageableSort.defaultPage());
        int size = getIntParameter(webRequest, "size", pageableSort.defaultSize());
        String sortBy = getStringParameter(webRequest, "sortBy", pageableSort.defaultSortBy());

        Sort sort = SortingHandler.getRequestSorting(sortBy);

        return PageRequest.of(page, size, sort);
    }

    private int getIntParameter(NativeWebRequest request, String param, int defaultValue) {
        String value = request.getParameter(param);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }

    private String getStringParameter(NativeWebRequest request, String param, String defaultValue) {
        String value = request.getParameter(param);
        return value != null ? value : defaultValue;
    }
}
