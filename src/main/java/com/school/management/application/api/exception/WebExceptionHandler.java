package com.school.management.application.api.exception;

import com.google.protobuf.Message;
import com.school.management.GenericProto;
import com.school.management.application.exceptions.ApplicationException;
import com.school.management.application.exceptions.UnauthorizedException;
import com.school.management.application.validation.validators.PathUUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import jakarta.validation.ConstraintViolationException;

import java.util.Optional;
import java.util.stream.Collectors;


@ControllerAdvice
public class WebExceptionHandler {
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<GenericProto.ErrorResponse> handleApiException(ApplicationException exception) {
        return this.handle(exception);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<GenericProto.ErrorResponse> handleAccessDinedException(AccessDeniedException accessDeniedException) {
        UnauthorizedException exception = UnauthorizedException.ACCESS_DENIED;
        return this.handle(exception);
    };

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<GenericProto.ErrorResponse> handleGenericError(Throwable throwable) {
        ApplicationException exception = new ApplicationException(throwable.getLocalizedMessage());
        return this.handle(exception);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Message> handleConstrainViolationException(ConstraintViolationException exception) {
        if (exception.getConstraintViolations().stream().anyMatch(v -> v.getConstraintDescriptor().getAnnotation().annotationType().equals(PathUUID.class))) {
            return this.handle(new ApplicationException());
        }

        return new ResponseEntity<>(GenericProto.ValidationErrorResponse.newBuilder()
                .setType(exception.getClass().getSimpleName())
                .setCode("422")
                .addAllErrors(
                        exception.getConstraintViolations().stream().map(
                                v-> GenericProto.ValidationError.newBuilder()
                                        .setField(v.getPropertyPath().toString())
                                        .setMessage(v.getMessage())
                                        .setValue(Optional.ofNullable(v.getInvalidValue()).orElse("").toString()).build()).collect(Collectors.toSet())
                ).build(), null, 422);
    }

    private ResponseEntity handle(ApplicationException exception) {
        return new ResponseEntity<>(
                    GenericProto.ErrorResponse.newBuilder()
                            .setCode(exception.getCode())
                            .setType(exception.getClass().getSimpleName())
                            .setMessage(exception.getLocalizedMessage())
                            .putAllParams(exception.getParams())
                            .build(),
                    null,
                    exception.getStatus()
                );
    }

}
