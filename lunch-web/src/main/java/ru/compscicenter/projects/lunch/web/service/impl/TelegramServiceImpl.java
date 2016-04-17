package ru.compscicenter.projects.lunch.web.service.impl;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import ru.compscicenter.projects.lunch.web.service.TelegramService;
import ru.compscicenter.projects.lunch.web.util.TelegramMethodExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

public class TelegramServiceImpl implements TelegramService {

    private static Logger logger = Logger.getLogger(TelegramService.class.getName());
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
                e.printStackTrace();
            }
        }
        return token;
    }

    @Override
    public void sendMessage(long id, String message) {
        Map<String, String> map = new HashMap<>();
        map.put("chat_id", id + "");
        map.put("text", message);
        TelegramMethodExecutor.doMethod("sendMessage", map, getToken());
    }


    //TODO: remove shitty code!
    @Override
    public void handleUpdate(String json) {
        logger.info(json);
        JSONObject update = (JSONObject) JSONValue.parse(json);
        JSONObject message = (JSONObject) update.get("message");
        long id = (Long) ((JSONObject) message.get("from")).get("id");
        String text = (String) message.get("text");
        sendMessage(id, text);
    }

    @Override
    public void update() {
        Map<String, String> map = new HashMap<>();
        map.put("limit", "1");
        map.put("offset", "-1");
        String response = TelegramMethodExecutor.doMethod("getUpdates", map, getToken());
        handleUpdate(response);
    }
}
