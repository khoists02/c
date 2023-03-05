package com.school.management.application.api.filter;

import com.school.management.application.Application;
import com.school.management.application.exceptions.ApplicationException;
import com.school.management.application.exceptions.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;

@Component
@NoArgsConstructor
public class FilterChainExceptionHandler extends OncePerRequestFilter {
    private Logger logger = LoggerFactory.getLogger(FilterChainExceptionHandler.class);
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionHandlerResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            this.logger.info("Do FilterChainExceptionRequest");
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            this.logger.error("Exception FilterChainExceptionHandler %s", e.getLocalizedMessage());
            exceptionHandlerResolver.resolveException(request, response, null, e);
        }
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }
}
