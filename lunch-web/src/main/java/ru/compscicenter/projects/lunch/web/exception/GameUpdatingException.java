package ru.compscicenter.projects.lunch.web.exception;

public class GameUpdatingException extends MyHumbleException {
    public GameUpdatingException() {
    }

    public GameUpdatingException(String s) {
        super(s);
    }

    public GameUpdatingException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameUpdatingException(Throwable cause) {
        super(cause);
    }
}
