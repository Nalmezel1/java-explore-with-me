package ru.practicum.ewm.exceptions;

public class CategoryNotExistException extends RuntimeException {
    public CategoryNotExistException(String message) {
        super(message);
    }
}
