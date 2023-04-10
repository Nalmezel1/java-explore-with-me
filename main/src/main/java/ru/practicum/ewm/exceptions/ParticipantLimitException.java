package ru.practicum.ewm.exceptions;

public class ParticipantLimitException extends RuntimeException {
    public ParticipantLimitException(String message) {
        super(message);
    }
}
