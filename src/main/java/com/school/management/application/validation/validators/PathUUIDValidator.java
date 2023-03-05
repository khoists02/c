package com.school.management.application.validation.validators;

import jakarta.validation.ConstraintValidator;
import java.util.regex.Pattern;

public class PathUUIDValidator implements ConstraintValidator<PathUUID, String> {
    Pattern pattern = Pattern.compile("^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$", 2);

    public PathUUIDValidator() {

    }

    @Override
    public boolean isValid(String uuid, jakarta.validation.ConstraintValidatorContext constraintValidatorContext) {
        return uuid == null || this.pattern.matcher(uuid).matches();
    }
}
