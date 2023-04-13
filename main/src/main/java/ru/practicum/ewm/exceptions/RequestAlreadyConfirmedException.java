package ru.practicum.ewm.exceptions;

public class RequestAlreadyConfirmedException extends RuntimeException {
    public RequestAlreadyConfirmedException(String message) {
        super(message);
    }
}
