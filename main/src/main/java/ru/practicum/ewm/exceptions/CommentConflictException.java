package ru.practicum.ewm.exceptions;

public class CommentConflictException extends RuntimeException {
    public CommentConflictException(String message) {
        super(message);
    }
}
