package com.school.management.application.api.auth;

import com.school.management.application.services.AuthenticationService;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class MethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
    private final AuthenticationService authenticationService;
    public MethodSecurityExpressionHandler(AuthenticationService authenticationService)
    {
        this.authenticationService = authenticationService;
    }
    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        MethodSecurityExpressionRoot root = new MethodSecurityExpressionRoot(authentication, authenticationService);
        root.setPermissionEvaluator(this.getPermissionEvaluator());
        root.setTrustResolver(this.getTrustResolver());
        root.setRoleHierarchy(this.getRoleHierarchy());
        root.setDefaultRolePrefix(this.getDefaultRolePrefix());
        return root;
    }
}