package ru.gymhub.exceptions;

import lombok.Data;

@Data
public class Response {
    private String exception;
    private String message;

    public Response(Exception e){
        exception = e.getClass().getName();
        message = e.getMessage();
    }
}
