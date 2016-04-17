package ru.compscicenter.projects.lunch.web.service;

public interface TelegramService {
    public String getToken();

    public void sendMessage(long id, String message);

    public void handleUpdate(String json);
}
