package com.celica.infinity.utils.annotations.pagination;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@ControllerAdvice
public class RequestPaginationResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(PageRequest.class) &&
                parameter.hasParameterAnnotation(RequestPageable.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {

        RequestPageable pageable = parameter.getParameterAnnotation(RequestPageable.class);
        String pageParam = webRequest.getParameter("page");
        String sizeParam = webRequest.getParameter("size");

        int page = pageParam != null ? Integer.parseInt(pageParam) : pageable.defaultPage();
        int size = sizeParam != null ? Integer.parseInt(sizeParam) : pageable.defaultSize();

        return PageRequest.of(page, size);
    }
}
