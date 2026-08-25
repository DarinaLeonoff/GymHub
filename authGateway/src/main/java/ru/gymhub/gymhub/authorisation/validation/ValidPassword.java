package ru.gymhub.gymhub.authorisation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface ValidPassword {

    String message() default """
            Password must contain:
            - at least 8 characters
            - one uppercase letter
            - one lowercase letter
            - one digit
            - one special character (@$!%*?&)
            """;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
