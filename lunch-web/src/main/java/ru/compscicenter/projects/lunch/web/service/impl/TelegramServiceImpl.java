package ru.compscicenter.projects.lunch.web.service.impl;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.web.service.MenuService;
import ru.compscicenter.projects.lunch.web.service.TelegramService;
import ru.compscicenter.projects.lunch.web.service.UserService;
import ru.compscicenter.projects.lunch.web.util.TelegramMethodExecutor;
import ru.compscicenter.projects.lunch.web.util.ToStrings;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TelegramServiceImpl implements TelegramService {

    private static Logger logger = LoggerFactory.getLogger(TelegramServiceImpl.class);

    private UserService userService;
    private MenuService menuService;

    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private static String token = null;

    private static final String HELP = "/help - show help" + "\n" +
            "/list [-r] [-n \"<MEAL_NAME>|<REGEX>\"] [-p <MAX_PRICE>|<EXACT_PRICE>]";

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
    public void sendMessage(final long id, final String message, final Map<String, String> params) {
        Map<String, String> map = new HashMap<>();

        if (params != null) {
            map.putAll(params);
        }

        map.put("chat_id", id + "");
        map.put("text", message);
        try {
            String response = TelegramMethodExecutor.doMethod("sendMessage", map, getToken());
        } catch (IOException e) {
            logger.error("Error during method execution", e);
        }
    }


    private void registerUser(long id) {
        if (!userService.exists(id)) {
            userService.createUser(id);
        }
    }

    @Override
    public void handleUpdate(final String json) {
        try {
            JSONObject update = (JSONObject) JSONValue.parse(json);
            JSONObject message = (JSONObject) update.get("message");

            long from = (Long) ((JSONObject) message.get("from")).get("id");
            long chat = (Long) ((JSONObject) message.get("chat")).get("id");

            registerUser(from);

            if (message.containsKey("text")) {
                String text = (String) message.get("text");
                text = text.replaceAll("@.*", "");

                handleTextQuery(from, chat, text);
            }

        } catch (Exception e) {
            sendMessage(169022871, "Exception: " + e.toString(), null);
        }
    }

    public void handleTextQuery(long from, long chat, String text) {
        String list = "\\/list(\\s+(?<regex>-r))?(\\s+-n\\s+\\\"(?<name>.*)\\\")?(\\s+-p\\s+(?<price>[\\d]+))?\\s*";
        String start = "\\/start";
        String help = "\\/help";

        if (text.matches(list)) {
            handleList(chat, text);
        } else if (text.matches(help)) {
            sendHelp(chat);
        } else if (text.matches(start)) {
            handleStart(chat);
        } else {
            sendMessage(chat, "No such command " + text, null);
            sendHelp(chat);
        }
    }

    public void handleList(long chat, String text) {
        String list = "\\/list(\\s+(?<regex>-r))?(\\s+-n\\s+\\\"(?<name>.*)\\\")?(\\s+-p\\s+(?<price>[\\d]+))?\\s*";
        Pattern pattern = Pattern.compile(list);
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            String name = ".*";
            if (matcher.group("name") != null) {
                name = matcher.group("name");
            }
            double price = 1000;
            if (matcher.group("price") != null) {
                price = Double.parseDouble(matcher.group("price"));
            }

            if (matcher.group("regex") != null || text.equals("/list")) {
                List<MenuItem> items = menuService.getForNameAndPriceRegex(name, 0, price);
                sendMessage(chat, ToStrings.menuItems(items), null);
            } else {
                MenuItem menuItem = menuService.getForNameAndPrice(name, price);
                if (menuItem != null) {
                    sendMessage(chat, ToStrings.menuItem(menuItem), null);
                } else {
                    sendMessage(chat, "No such item", null);
                }
            }
        }
    }

    @Override
    public void sendHelp(long id) {
        sendMessage(id, HELP, null);
    }

    public void handleStart(long chat) {
        sendHelp(chat);
    }

    /*
        Надо:
            1. Если сообщение из конфы, к каждому ответу прикреплять упоминание человека, который написал
            2. Обрабатывать инлайн запросы
            3. Хендлить ok:false в запросах
            4. Организовать по-человечески
        Команды:
            /list [-r] [-n <MEAL_NAME>] [-p <PRICE> | <LOWER_PRICE>, <UPPER_PRICE>]
            /rand
            /test
                /love <regex>
                /hate <regex>
                /love -f <regex>
                /hate -f <regex>
                /cancel
            /month <MONTH number>
            /period <START_DATE> <END_DATE>
            /today
            /day <DATE>
            /help
            /reset
                /reset -f
                /cancel
            /list <love|hate>
            /menu <DATE>
            /help
            /about
     */

}
