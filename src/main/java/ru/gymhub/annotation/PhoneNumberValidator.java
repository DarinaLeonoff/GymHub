package ru.gymhub.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<TelNumber, String> {

  // Регулярное выражение (разрешает +7, 7, 8 и далее 10 цифр; также пропускает null, т.к. за null
  // отвечает @NotNull/@NotBlank)
  private static final String PHONE_REGEX = "^(\\+7|7|8)[0-9]{10}$";
  private static final Pattern PATTERN = Pattern.compile(PHONE_REGEX);

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isBlank()) {
      return true; // Валидацию пустых значений лучше доверять аннотациям @NotNull / @NotBlank
    }
    return PATTERN.matcher(value.trim()).matches();
  }
}
