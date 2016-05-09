package ru.compscicenter.projects.lunch.estimator;

public class DeciderException extends RuntimeException {
    public DeciderException() {
        super();
    }

    public DeciderException(String message) {
        super(message);
    }

    public DeciderException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeciderException(Throwable cause) {
        super(cause);
    }

    protected DeciderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
