package ru.gymhub.gymhub.exceptions;

import lombok.AllArgsConstructor;


public class Response {
    private String exception;
    private String message;

    public Response(Exception e){
        exception = e.getClass().getName();
        message = e.getMessage();
    }
}
