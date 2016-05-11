package ru.compscicenter.projects.lunch.web.exception;

public class MenuUploadingException extends MyHumbleException {
    public MenuUploadingException() {
        super();
    }

    public MenuUploadingException(String message) {
        super(message);
    }

    public MenuUploadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MenuUploadingException(Throwable cause) {
        super(cause);
    }
}

