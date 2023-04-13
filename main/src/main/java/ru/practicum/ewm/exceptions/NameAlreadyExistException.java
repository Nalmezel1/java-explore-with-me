package ru.practicum.ewm.exceptions;

public class NameAlreadyExistException extends RuntimeException {
    public NameAlreadyExistException(String message) {
        super(message);
    }
}
