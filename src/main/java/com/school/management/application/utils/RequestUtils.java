package com.school.management.application.utils;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RequestUtils {
    private static RequestUtils staticReference = null;

    private HttpServletRequest request;

    public RequestUtils(Optional<HttpServletRequest> request) {
        if (request.isEmpty()) return;

        if (staticReference == null) {
            staticReference = this;
            this.request = request.get();
        } else {
            throw new IllegalStateException("Multiple instances of RequestUtils are not permitted");
        }

    }

    @Nullable
    public static String getRequestPathUrl() {
        if (staticReference == null || staticReference.request == null) {
            return null;
        }
        return staticReference.request.getRequestURL().toString();
    }

    @Nullable
    public static String getHeader(String header, String defaultVal) {
        if (staticReference == null || staticReference.request == null) {
            return null;
        }
        return staticReference.request.getHeader(header).toString();
    }

    @Nullable
    public static String getClientIpAddress() {
        if (staticReference == null || staticReference.request == null) {
            return null;
        }
        return Optional.ofNullable(staticReference.request.getHeader("x-forwarded-for"))
                .orElseGet(() -> staticReference.request.getRemoteAddr());
    }

}
