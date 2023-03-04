package com.school.management.application.api.filter;

import com.school.management.application.api.auth.AuthenticatedUser;
import com.school.management.application.exceptions.UnauthorizedException;
import io.micrometer.common.lang.Nullable;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class CorsFilter extends OncePerRequestFilter {
    @Value("${app.domain.root}")
    private String rootDomain;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionHandlerResolver;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return pathMatcher.match("/actuator/**", request.getRequestURI())
                || pathMatcher.match("/auth/saml2/*/acs", request.getRequestURI());
    }
    public CorsFilter()
    {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "HEAD", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.addAllowedHeader("*");
        configuration.setExposedHeaders(List.of("Content-Disposition"));
        Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
        String requestOrigin = "test.domain.service";
        if(authenticatedUser == null && requestOrigin != null)
        {
            configuration.addAllowedOrigin(requestOrigin);
        }else if (authenticatedUser instanceof AuthenticatedUser){
            //User is authenticated, allow from the users domain in the JWT
            configuration.addAllowedOrigin(((AuthenticatedUser) authenticatedUser).getAllowedOrigin());
        }
        Boolean isValid = processRequest(configuration, request, response);
        if(isValid && !CorsUtils.isPreFlightRequest(request))
        {
            filterChain.doFilter(request, response);
        }
    }
    /**
     * Set the CORS headers for a Request that may not have passed through the filter on its own
     * @param request
     * @param response
     * @throws IOException
     */
    public void applyHeaders(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "HEAD", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.addAllowedHeader("*");
        String requestOrigin = "test.domain.service";
        if(requestOrigin != null)
        {
            configuration.addAllowedOrigin(requestOrigin);
        }
        processRequest(configuration, request, response);
    }
    public boolean processRequest(@Nullable CorsConfiguration config, HttpServletRequest request,
                                  HttpServletResponse response) throws IOException {
        Collection<String> varyHeaders = response.getHeaders(HttpHeaders.VARY);
        if (!varyHeaders.contains(HttpHeaders.ORIGIN)) {
            response.addHeader(HttpHeaders.VARY, HttpHeaders.ORIGIN);
        }
        if (!varyHeaders.contains(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD)) {
            response.addHeader(HttpHeaders.VARY, HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD);
        }
        if (!varyHeaders.contains(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS)) {
            response.addHeader(HttpHeaders.VARY, HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS);
        }
        if (!CorsUtils.isCorsRequest(request)) {
            return true;
        }
        if (response.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN) != null) {
            logger.trace("Skip: response already contains \"Access-Control-Allow-Origin\"");
            return true;
        }
        boolean preFlightRequest = CorsUtils.isPreFlightRequest(request);
        if (config == null) {
            if (preFlightRequest) {
                rejectRequest(new ServletServerHttpRequest(request), new ServletServerHttpResponse(response));
                return false;
            }
            else {
                return true;
            }
        }
        return handleInternal(new ServletServerHttpRequest(request), new ServletServerHttpResponse(response), config, preFlightRequest);
    }
    /**
     * Invoked when one of the CORS checks failed.
     * The default implementation sets the response status to 403 and writes
     * "Invalid CORS request" to the response.
     */
    protected void rejectRequest(ServletServerHttpRequest request, ServletServerHttpResponse response) {
        exceptionHandlerResolver.resolveException(request.getServletRequest(), response.getServletResponse(),null, UnauthorizedException.INVALID_CORS_REQUEST);
    }
    /**
     * Handle the given request.
     */
    protected boolean handleInternal(ServletServerHttpRequest request, ServletServerHttpResponse response,
                                     CorsConfiguration config, boolean preFlightRequest) throws IOException {
        String requestOrigin = request.getHeaders().getOrigin();
        String allowOrigin = checkOrigin(config, requestOrigin);
        HttpHeaders responseHeaders = response.getHeaders();
        if (allowOrigin == null) {
            rejectRequest(request, response);
            return false;
        }
        HttpMethod requestMethod = getMethodToUse(request, preFlightRequest);
        List<HttpMethod> allowMethods = checkMethods(config, requestMethod);
        if (allowMethods == null) {
            rejectRequest(request, response);
            return false;
        }
        List<String> requestHeaders = getHeadersToUse(request, preFlightRequest);
        List<String> allowHeaders = checkHeaders(config, requestHeaders);
        if (preFlightRequest && allowHeaders == null) {
            rejectRequest(request, response);
            return false;
        }
        responseHeaders.setAccessControlAllowOrigin(allowOrigin);
        if (preFlightRequest) {
            responseHeaders.setAccessControlAllowMethods(allowMethods);
        }
        if (preFlightRequest && !allowHeaders.isEmpty()) {
            responseHeaders.setAccessControlAllowHeaders(allowHeaders);
        }
        if (!CollectionUtils.isEmpty(config.getExposedHeaders())) {
            responseHeaders.setAccessControlExposeHeaders(config.getExposedHeaders());
        }
        if (Boolean.TRUE.equals(config.getAllowCredentials())) {
            responseHeaders.setAccessControlAllowCredentials(true);
        }
        if (preFlightRequest && config.getMaxAge() != null) {
            responseHeaders.setAccessControlMaxAge(config.getMaxAge());
        }
        response.flush();
        return true;
    }
    /**
     * Check the origin and determine the origin for the response. The default
     * implementation simply delegates to
     * {@link org.springframework.web.cors.CorsConfiguration#checkOrigin(String)}.
     */
    @Nullable
    protected String checkOrigin(CorsConfiguration config, @Nullable String requestOrigin) {
        return config.checkOrigin(requestOrigin);
    }
    /**
     * Check the HTTP method and determine the methods for the response of a
     * pre-flight request. The default implementation simply delegates to
     * {@link org.springframework.web.cors.CorsConfiguration#checkHttpMethod(HttpMethod)}.
     */
    @Nullable
    protected List<HttpMethod> checkMethods(CorsConfiguration config, @Nullable HttpMethod requestMethod) {
        return config.checkHttpMethod(requestMethod);
    }
    @Nullable
    private HttpMethod getMethodToUse(ServerHttpRequest request, boolean isPreFlight) {
        return (isPreFlight ? request.getHeaders().getAccessControlRequestMethod() : request.getMethod());
    }
    /**
     * Check the headers and determine the headers for the response of a
     * pre-flight request. The default implementation simply delegates to
     * {@link org.springframework.web.cors.CorsConfiguration#checkOrigin(String)}.
     */
    @Nullable
    protected List<String> checkHeaders(CorsConfiguration config, List<String> requestHeaders) {
        return config.checkHeaders(requestHeaders);
    }
    private List<String> getHeadersToUse(ServerHttpRequest request, boolean isPreFlight) {
        HttpHeaders headers = request.getHeaders();
        return (isPreFlight ? headers.getAccessControlRequestHeaders() : new ArrayList<>(headers.keySet()));
    }

}
