package ru.gymhub.exceptions;

import org.springframework.validation.Errors;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
