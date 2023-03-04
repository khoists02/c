package com.school.management.application.exceptions;

public class UnauthorizedException extends ApplicationException {
    public static UnauthorizedException INVALID_CORS_REQUEST = new UnauthorizedException("unauthenticated_exception", "1000");
    private UnauthorizedException(String messageKey, String code) {
        super(messageKey, code, 403);
    }
}
