package com.school.management.application.api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@NoArgsConstructor
public class FilterChainExceptionHandler extends OncePerRequestFilter {
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionHandlerResolver;
    @Autowired
    private CorsFilter corsFilter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            if (!response.containsHeader("Access-Control-Allow-Origin")) {
                corsFilter.applyHeaders(request, response);
            }
            exceptionHandlerResolver.resolveException(request, response, null, e);
        }
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }
}
