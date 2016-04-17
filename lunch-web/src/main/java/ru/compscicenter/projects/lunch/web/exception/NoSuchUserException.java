package ru.compscicenter.projects.lunch.web.exception;

public class NoSuchUserException extends IllegalStateException{
    public NoSuchUserException() {
    }

    public NoSuchUserException(String s) {
        super(s);
    }

    public NoSuchUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchUserException(Throwable cause) {
        super(cause);
    }
}
