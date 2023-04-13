package ru.practicum.ewm.exceptions;

public class CategoryIsNotEmptyException extends RuntimeException {
    public CategoryIsNotEmptyException(String message) {
        super(message);
    }
}
