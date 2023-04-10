package ru.practicum.ewm.exceptions;

public class RequestNotExistException extends RuntimeException {
    public RequestNotExistException(String message) {
        super(message);
    }
}
