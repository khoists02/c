package com.school.management.application.api.auth;
import org.springframework.security.core.AuthenticationException;

public class AuthenticationExceptionWrapper extends AuthenticationException {
    AuthenticationExceptionWrapper(Throwable cause){ super("", cause); }
}
