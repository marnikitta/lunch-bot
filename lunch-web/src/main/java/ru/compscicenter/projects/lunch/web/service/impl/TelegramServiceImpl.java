package ru.compscicenter.projects.lunch.web.service.impl;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.web.service.DeciderService;
import ru.compscicenter.projects.lunch.web.service.MenuService;
import ru.compscicenter.projects.lunch.web.service.TelegramService;
import ru.compscicenter.projects.lunch.web.service.UserService;
import ru.compscicenter.projects.lunch.web.util.TelegramMethodExecutor;
import ru.compscicenter.projects.lunch.web.util.ToStrings;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TelegramServiceImpl implements TelegramService {

    private static Logger logger = LoggerFactory.getLogger(TelegramServiceImpl.class);

    private UserService userService;
    private MenuService menuService;
    private DeciderService deciderService;

    public void setDeciderService(DeciderService deciderService) {
        this.deciderService = deciderService;
    }

    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private static String token = null;

    private static final String HELP = "/help - show help" + "\n" +
            "/list [-r] [<MEAL_NAME>|<REGEX>]";

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
        logger.info(json);

        try {
            JSONObject update = (JSONObject) JSONValue.parse(json);
            JSONObject message = (JSONObject) update.get("message");

            long from = (Long) ((JSONObject) message.get("from")).get("id");
            long chat = (Long) ((JSONObject) message.get("chat")).get("id");

            try {
                registerUser(from);

                if (message.containsKey("text")) {
                    String text = (String) message.get("text");
                    text = text.replaceAll("@.*", "");

                    handleTextQuery(from, chat, text);
                }
            } catch (Exception e) {
                logger.error("smth went wrong", e);
                sendMessage(chat, "Ooops... Something went wrong", null);
                sendMessage(169022871, "Exception: " + e.toString(), null);
            }

        } catch (Exception e) {
            logger.error("Error during update parsing", e);
            sendMessage(169022871, "Exception: " + e.toString(), null);
        }
    }

    public void handleTextQuery(long from, long chat, String text) {
        String list = "\\/list(\\s+(?<regex>-r))?\\s+(?<name>.*?)?\\s*";
        String menu = "\\/menu?(\\s+(?<date>\\d+\\.\\d+.\\d+))?\\s*";
        String day = "\\/day?(\\s+(?<date>\\d+\\.\\d+.\\d+))?\\s*";
        String sum = "\\/sum?(\\s+(?<date>\\d+.\\d+))?\\s*";

        String start = "\\/start";
        String help = "\\/help";

        if (text.matches(list)) {
            handleList(chat, text);
        } else if (text.matches(menu)) {
            handleMenu(chat, text);
        } else if (text.matches(day)) {
            handleDecisionForDay(from, chat, text);
        } else if (text.matches(sum)) {
            handleSum(from, chat, text);
        } else if (text.matches(help)) {
            sendHelp(chat);
        } else if (text.matches(start)) {
            handleStart(chat);
        } else {
            sendMessage(chat, "No such command " + text, null);
            sendHelp(chat);
        }
    }

    private void handleSum(long from, long chat, String text) {
        String sum = "\\/sum?(\\s+(?<date>\\d+.\\d+))?\\s*";

        Pattern pattern = Pattern.compile(sum);
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            Calendar end = new GregorianCalendar();
            Calendar start = new GregorianCalendar();
            start.set(Calendar.DAY_OF_MONTH, 1);

            if (matcher.group("date") != null) {
                String[] splitted = matcher.group("date").split("\\.");
                try {
                    start = new GregorianCalendar(Integer.parseInt(splitted[1]), Integer.parseInt(splitted[0]) - 1, 1);
                    end = new GregorianCalendar(Integer.parseInt(splitted[1]),
                            Integer.parseInt(splitted[0]),
                            start.getActualMaximum(Calendar.DAY_OF_MONTH));
                } catch (Exception e) {
                    sendMessage(chat, "Wrong date format. Try dd.mm.yyyy", null);
                    return;
                }
            }
            sendMessage(chat, "Sum for your period: " + deciderService.sumForPeriod(from, start, end), null);
            sendMessage(chat, "Make sure that there is menu for each day", null);
        } else {
            throw new IllegalArgumentException(text);
        }
    }


    private void handleDecisionForDay(long from, long chat, String text) {
        String day = "\\/day?(\\s+(?<date>\\d+\\.\\d+.\\d+))?\\s*";

        Pattern pattern = Pattern.compile(day);
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            Calendar calendar = new GregorianCalendar();
            if (matcher.group("date") != null) {
                String[] splitted = matcher.group("date").split("\\.");
                try {
                    calendar = new GregorianCalendar(Integer.parseInt(splitted[2]), Integer.parseInt(splitted[1]) - 1, Integer.parseInt(splitted[0]));
                } catch (Exception e) {
                    sendMessage(chat, "Wrong date format. Try dd.mm.yyyy", null);
                    return;
                }
            }
            if (!menuService.contains(calendar)) {
                sendMessage(chat, "No menu found for your date", null);
            } else {
                List<MenuItem> items = deciderService.getForDate(from, calendar);
                sendMessage(chat, ToStrings.menuItemsToString(items, 0, 10), null);
                sendMessage(chat, "Sum:" + items.stream().mapToDouble(MenuItem::getPrice).sum(), null);
            }

        } else {
            throw new IllegalArgumentException(text);
        }

    }

    private void handleMenu(long chat, String text) {
        String menu = "\\/menu?(\\s+(?<date>\\d+\\.\\d+.\\d+))?\\s*";
        Pattern pattern = Pattern.compile(menu);
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            Calendar calendar = new GregorianCalendar();
            if (matcher.group("date") != null) {
                String[] splitted = matcher.group("date").split("\\.");
                try {
                    calendar = new GregorianCalendar(Integer.parseInt(splitted[2]), Integer.parseInt(splitted[1]) - 1, Integer.parseInt(splitted[0]));
                } catch (Exception e) {
                    sendMessage(chat, "Wrong date format. Try dd.mm.yyyy", null);
                    return;
                }
            }
            if (!menuService.contains(calendar)) {
                sendMessage(chat, "No menu found for your date", null);
            } else {
                Menu menu1 = menuService.getForDate(calendar);
                sendMessage(chat, ToStrings.menuItemsToString(menu1.getItemsCopy(), 0, 100), null);
            }
        } else {
            throw new IllegalArgumentException(text);
        }
    }

    private void handleList(long chat, String text) {
        String list = "\\/list(\\s+(?<regex>-r))?\\s+(?<name>.*?)?\\s*";
        Pattern pattern = Pattern.compile(list);
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {

            String name = ".*";

            if (matcher.group("name") != null) {
                name = matcher.group("name");
            }

            if (matcher.group("regex") == null) {
                name = ".*" + name + ".*";
            }

            List<MenuItem> items = menuService.getForNameRegex(name);
            if (items.size() == 0) {
                sendMessage(chat, "I haven't found such dishes in my database", null);

            }
            sendMessage(chat, ToStrings.menuItemsToString(items, 0, 20), null);
        } else {
            throw new IllegalArgumentException(text);
        }
    }

    @Override
    public void sendHelp(long id) {
        sendMessage(id, HELP, null);
    }

    public void handleStart(long chat) {
        sendHelp(chat);
    }

    //ПРОБЛЕМА!!! НЕ ПОЛУЧАЕТСЯ ЗАГРУЖАТЬ МЕНЮШКИ НА СЕРВЕРЕ, СЛЕТАЕТ КОДИРОВКА, ПОЛУЧАЕМ КУЧУ ВОПРОСОВ.
    //НАДО ФИКСИТЬ
    /*
        Надо:
            1. Если сообщение из конфы, к каждому ответу прикреплять упоминание человека, который написал
            2. Обрабатывать инлайн запросы
            3. Хендлить ok:false в запросах
            4. Организовать по-человечески
            5. Хэндлить исключения!!!
        Команды:
            /list [-r] [-n <MEAL_NAME>] [-p <PRICE> | <LOWER_PRICE>, <UPPER_PRICE>]
            /rand
            /test
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
