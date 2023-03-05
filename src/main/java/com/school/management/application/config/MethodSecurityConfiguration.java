package com.school.management.application.config;

import com.school.management.application.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {
    @Autowired
    private AuthenticationService authenticationService;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new com.school.management.application.api.auth.MethodSecurityExpressionHandler(authenticationService);
    }
}
