package ru.gymhub.exceptions;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.gymhub.exceptions.exceptions.WrongPasswordException;
import ru.gymhub.exceptions.exceptions.EmailAlreadyRegisteredException;
import ru.gymhub.exceptions.exceptions.GymNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(GymNotFoundException.class)
  public ResponseEntity<String> handleGymNotFound(GymNotFoundException ex) {

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationFail(
      MethodArgumentNotValidException ex) {

    String message =
        ex.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(FieldError::getDefaultMessage)
            .orElse("Ошибка валидации");

    return ResponseEntity.badRequest().body(Map.of("message", message));
  }

  @ExceptionHandler(WrongPasswordException.class)
  public ResponseEntity<String> handleAuthFail(
          WrongPasswordException ex) {

    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(EmailAlreadyRegisteredException.class)
  public ResponseEntity<String> handleEmailRegistrationFail(
          EmailAlreadyRegisteredException ex) {

    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
  }

}
