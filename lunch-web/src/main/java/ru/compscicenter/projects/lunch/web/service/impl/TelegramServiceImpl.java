package ru.compscicenter.projects.lunch.web.service.impl;

import org.json.simple.JSONArray;
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

    @Override
    public String getToken() {
        try {
            Properties prop = new Properties();
            InputStream inputStream = TelegramServiceImpl.class.getClassLoader().getResourceAsStream("token.properties");
            prop.load(inputStream);
            return prop.getProperty("token");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void sendMessage(long id, String message) {
        Map<String, String> map = new HashMap<>();
        map.put("chat_id", id + "");
        map.put("text", message);
        TelegramMethodExecutor.doMethod("sendMessage", map, getToken());
    }


    @Override
    public void handleUpdate(String json) {
        JSONObject jsonObject = (JSONObject) JSONValue.parse(json);
        if (jsonObject.containsKey("ok") && jsonObject.get("ok") == "true") {
            JSONArray updates = (JSONArray) jsonObject.get("response");
            sendMessage(0, json);
        } else {
            logger.severe("NOT OK RESPONSE: " + json);
            throw new IllegalStateException("ok == false");
        }
    }
}
