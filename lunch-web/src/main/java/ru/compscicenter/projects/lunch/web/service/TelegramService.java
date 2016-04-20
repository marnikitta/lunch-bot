package ru.compscicenter.projects.lunch.web.service;

import java.util.Map;

public interface TelegramService {
    public String getToken();

    public void sendMessage(long id, String message, Map<String, String> params);

    public void sendHelp(long id);

    public void handleUpdate(final String json);
}
