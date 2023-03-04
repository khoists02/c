package com.school.management.application.services;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ConfigurationService {
    @Value("${app.auth.token.refreshTokenValidMinutes}")
    private Integer refreshTokenValidMinutes;

    @Value("${app.auth.token.accessTokenValidMinutes}")
    private Integer accessTokenValidMinutes;

    @Value("${app.auth.cookie.secure:true}")
    private Boolean cookieSecure;

    @Value("${app.auth.cookie.domain}")
    private String cookieDomain;

    @Value("${app.auth.token.issuer}")
    private String tokenIssuer;
}
