package com.school.management.application.exceptions;

public class UnauthorizedException extends ApplicationException {
    public static UnauthorizedException INVALID_CORS_REQUEST = new UnauthorizedException("unauthenticated_exception", "1000");
    public static UnauthorizedException ACCESS_DENIED = new UnauthorizedException("access_dined", "403");
    private UnauthorizedException(String messageKey, String code) {
        super(messageKey, code, 403);
    }
}
