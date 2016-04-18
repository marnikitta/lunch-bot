package ru.compscicenter.projects.lunch.web.exception;

public class NoMenuForDateException extends IllegalStateException {
    public NoMenuForDateException() {
    }

    public NoMenuForDateException(String s) {
        super(s);
    }

    public NoMenuForDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoMenuForDateException(Throwable cause) {
        super(cause);
    }
}
