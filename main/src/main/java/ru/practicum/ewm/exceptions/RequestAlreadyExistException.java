package ru.practicum.ewm.exceptions;

public class RequestAlreadyExistException extends RuntimeException {
    public RequestAlreadyExistException(String message) {
        super(message);
    }
}
