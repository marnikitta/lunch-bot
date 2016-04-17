package ru.compscicenter.projects.lunch.web.exception;

import java.io.IOException;

public class MenuUploadingException extends IOException {
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

