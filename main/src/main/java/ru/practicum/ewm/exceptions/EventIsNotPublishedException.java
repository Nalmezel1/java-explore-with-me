package ru.practicum.ewm.exceptions;

public class EventIsNotPublishedException extends RuntimeException {
    public EventIsNotPublishedException(String message) {
        super(message);
    }
}
