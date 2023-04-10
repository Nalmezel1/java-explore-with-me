package ru.practicum.ewm.exceptions;

public class CommentNotExistException extends RuntimeException {
    public CommentNotExistException(String message) {
        super(message);
    }
}
