package ru.gymhub.exceptions.exceptions;

public class WrongPasswordException extends RuntimeException{
    public WrongPasswordException(String email) {
        super("Incorrect password for account with email " + email);
    }
}
