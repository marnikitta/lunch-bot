package ru.compscicenter.projects.lunch.web.service.impl;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.compscicenter.projects.lunch.web.service.TelegramService;
import ru.compscicenter.projects.lunch.web.util.TelegramMethodExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TelegramServiceImpl implements TelegramService {

    private static Logger logger = LoggerFactory.getLogger(TelegramServiceImpl.class);

    private static String token = null;

    @Override
    public String getToken() {
        if (token == null) {
            try {
                Properties prop = new Properties();
                InputStream inputStream = TelegramServiceImpl.class.getClassLoader().getResourceAsStream("token.properties");
                prop.load(inputStream);
                token = prop.getProperty("token");
            } catch (IOException e) {
                logger.error("Error loading token", e);
            }
        }
        return token;
    }

    @Override
    public void sendMessage(final long id, final String message) {
        Map<String, String> map = new HashMap<>();
        map.put("chat_id", id + "");
        map.put("text", message);
        try {
            TelegramMethodExecutor.doMethod("sendMessage", map, getToken());
        } catch (IOException e) {
            logger.error("Error during method execution", e);
        }
    }

    @Override
    public void handleUpdate(final String json) {
        JSONObject update = (JSONObject) JSONValue.parse(json);
        JSONObject message = (JSONObject) update.get("message");

        long id = (Long) ((JSONObject) message.get("from")).get("id");
        String text = (String) message.get("text");
        sendMessage(id, text);
    }
}
