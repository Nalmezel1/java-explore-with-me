package ru.practicum.ewm.exceptions;

public class EventNotExistException extends RuntimeException {
    public EventNotExistException(String message) {
        super(message);
    }
}
