package ru.compscicenter.projects.lunch.web.exception;

public class MenuDuplicateException extends Exception {
    public MenuDuplicateException(String msg) {
        super(msg);
    }

    public MenuDuplicateException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
