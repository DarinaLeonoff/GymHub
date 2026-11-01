package ru.gymhub.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TelNumber {
  String message() default
      "Некорректный номер телефона. Поддерживаемый формат: +7XXXXXXXXXX или 8XXXXXXXXXX";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
