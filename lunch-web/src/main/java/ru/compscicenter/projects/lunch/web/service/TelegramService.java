package ru.compscicenter.projects.lunch.web.service;

public interface TelegramService {
    public String getToken();

    public void sendMessage(final long id, final String message);

    public void handleUpdate(final String json);
}
