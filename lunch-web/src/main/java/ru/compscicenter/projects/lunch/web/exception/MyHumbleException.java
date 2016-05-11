package ru.compscicenter.projects.lunch.web.exception;

public class MyHumbleException extends Exception {
    public MyHumbleException() {
        super();
    }

    public MyHumbleException(String message) {
        super(message);
    }

    public MyHumbleException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyHumbleException(Throwable cause) {
        super(cause);
    }

    protected MyHumbleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
