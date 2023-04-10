package ru.practicum.ewm.exceptions;

public class WrongTimeException extends RuntimeException {
    public WrongTimeException(String message) {
        super(message);
    }
}
