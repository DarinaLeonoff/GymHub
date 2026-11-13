package ru.gymhub.exceptions.exceptions;

public class EmailAlreadyRegisteredException extends RuntimeException{
    public EmailAlreadyRegisteredException() {
        super("Аккаунт с такой почтой уже существует");
    }
}
