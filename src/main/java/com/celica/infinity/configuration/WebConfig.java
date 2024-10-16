package com.celica.infinity.configuration;

import com.celica.infinity.utils.annotations.authentication.RequestUserResolver;
import com.celica.infinity.utils.annotations.pagination.RequestPaginationResolver;
import com.celica.infinity.utils.annotations.sorting.RequestSortingResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RequestPaginationResolver requestPaginationResolver;
    private final RequestSortingResolver requestSortingResolver;

    private final RequestUserResolver requestUserResolver;

    public WebConfig(
            RequestPaginationResolver requestPaginationResolver,
            RequestSortingResolver requestSortingResolver,
            RequestUserResolver requestUserResolver
    ) {
        this.requestPaginationResolver = requestPaginationResolver;
        this.requestSortingResolver = requestSortingResolver;
        this.requestUserResolver = requestUserResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(requestPaginationResolver);
        resolvers.add(requestSortingResolver);
        resolvers.add(requestUserResolver);
    }

}
