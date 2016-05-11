package ru.compscicenter.projects.lunch.web.service.impl;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.compscicenter.projects.lunch.estimator.DeciderException;
import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.web.exception.GameUpdatingException;
import ru.compscicenter.projects.lunch.web.exception.NoMenuForDateException;
import ru.compscicenter.projects.lunch.web.exception.NoSuchUserException;
import ru.compscicenter.projects.lunch.web.model.Game;
import ru.compscicenter.projects.lunch.web.service.*;
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
    private GameService gameService;

    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

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

    private static final String HELP =
            "/help - show help" + "\n"
                    + "/list [-r] [meal name | regex] - show meals that matches regex" + "\n"
                    + "/menu [dd.mm.yyyy] - show menu for today [or date]" + "\n"
                    + "/day [dd.mm.yyyy] - suggest serving for today [or date]" + "\n"
                    + "/play - improve decider algorithm" + "\n"
                    + "/dates - get avalible dates" + "\n"
                    + "/cancel - cancel current operation" + "\n"
                    + "/sum [mm.yyyy] - calculate sum from beginning of month till now [month]" + "\n\n"
                    + "Examples:" + "\n"
                    + "\t/list карто" + "\n"
                    + "\t/menu 15.02.2016" + "\n"
                    + "\t/day 15.02.2016" + "\n"
                    + "\t/sum 02.2016";

    private static final String listPattern = "\\/list(\\s+(?<regex>-r))?(\\s+(?<name>.*))?\\s*";
    private static final String menuPattern = "\\/menu?(\\s+(?<date>\\d+\\.\\d+.\\d+))?\\s*";
    private static final String dayPattern = "\\/day?(\\s+(?<date>\\d+\\.\\d+.\\d+))?\\s*";
    private static final String sumPatter = "\\/sum?(\\s+(?<date>\\d+.\\d+))?\\s*";
    private static final String playPattern = "\\/play\\s*";
    private static final String resetPattern = "\\/reset\\s*";
    private static final String datesPattern = "\\/dates\\s*";
    private static final String votePattern = ".*\\(\\$(?<word>\\w*=*)\\)";
    private static final String cancelPattern = "\\/cancel\\s*";

    private String getToken() {
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

    private void sendMessage(final long id, final String message, final Map<String, String> params) {
        Map<String, String> map = new HashMap<>();

        if (params != null) {
            map.putAll(params);
        }

        map.put("chat_id", id + "");
        map.put("text", message);

        try {
            String response = TelegramMethodExecutor.doMethod("sendMessage", map, getToken());
            logger.info("Response: " + response);
        } catch (IOException e) {
            logger.error("Error during method execution", e);
        }
    }

    private void registerUser(final long id) {
        if (!userService.exists(id)) {
            userService.createUser(id);
        }
    }

    @Override
    public void handleUpdate(final String json) {
        logger.info(json);
        sendMessage(169022871, json, null);

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
                sendMessage(chat, "Ooops... Something went wrong", null);
                throw new Exception(e);
            }

        } catch (Exception e) {
            logger.error("Sth went wrong", e);
            sendMessage(169022871, "Exception: " + e.toString(), null);
        }
    }


    private void handleTextQuery(final long from, final long chat, final String text)
            throws NoMenuForDateException, NoSuchUserException, GameUpdatingException {
        String start = "\\/start";
        String help = "\\/help";

        if (text.matches(listPattern)) {
            handleList(chat, text);
        } else if (text.matches(menuPattern)) {
            handleMenu(chat, text);
        } else if (text.matches(dayPattern)) {
            handleDecisionForDay(from, chat, text);
        } else if (text.matches(sumPatter)) {
            handleSum(from, chat, text);
        } else if (text.matches(playPattern)) {
            handlePlay(chat, from);
        } else if (text.matches(start)) {
            handleStart(chat, from);
        } else if (text.matches(cancelPattern)) {
            sendHelp(chat);
        } else if (text.matches(resetPattern)) {
            handleReset(from, chat);
        } else if (text.matches(help)) {
            sendHelp(chat);
        } else if (text.matches(datesPattern)) {
            handleDates(chat);
        } else if (text.matches(votePattern)) {
            handleVote(from, chat, text);
        } else {
            sendMessage(chat, "No such command " + text, null);
            sendHelp(chat);
        }
    }

    private void handleDates(final long chat) {
        List<Menu> menus = menuService.getAll();
        StringBuilder sb = new StringBuilder();
        for (Menu menu : menus) {
            sb.append(ToStrings.dateToString(menu.getDate())).append("; ");
        }
        sendMessage(chat, sb.toString(), null);
    }

    private void handleReset(final long from, final long chat) throws NoSuchUserException {
        userService.reset(from);
        sendMessage(chat, "Reset was done", null);
    }

    private void handleVote(final long from, final long chat, final String text)
            throws GameUpdatingException {
        Pattern pattern = Pattern.compile(votePattern);
        Matcher matcher = pattern.matcher(text);

        if (matcher.matches()) {
            String info = matcher.group("word");

            String[] ids = ToStrings.decode(info).split("#");
            long gameId = Long.parseLong(ids[0]);
            long winnerId = Long.parseLong(ids[1]);
            gameService.setResult(from, gameId, winnerId);
            handlePlay(chat, from);
        } else {
            throw new IllegalStateException(text);
        }
    }

    private void handleSum(final long from, final long chat, final String text)
            throws NoSuchUserException {
        Pattern pattern = Pattern.compile(sumPatter);
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            Calendar start = new GregorianCalendar();
            Calendar end = new GregorianCalendar();
            start.set(Calendar.DAY_OF_MONTH, 1);

            if (matcher.group("date") != null) {
                String[] splitted = matcher.group("date").split("\\.");
                try {
                    start = new GregorianCalendar(Integer.parseInt(splitted[1]),
                            Integer.parseInt(splitted[0]) - 1, 1);
                    end = new GregorianCalendar(Integer.parseInt(splitted[1]),
                            Integer.parseInt(splitted[0]) - 1,
                            start.getActualMaximum(Calendar.DAY_OF_MONTH));
                } catch (Exception e) {
                    sendMessage(chat, "Wrong date format. Try dd.mm.yyyy", null);
                    return;
                }
            }
            sendMessage(chat, "I'm thinking, please wait...", null);

            String period = ToStrings.dateToString(start) + " - " + ToStrings.dateToString(end);
            sendMessage(chat, "Period: " + period + "\nSum: " + deciderService.sumForPeriod(from, start, end), null);
        } else {
            throw new IllegalArgumentException(text);
        }
    }


    private void handleDecisionForDay(final long from, final long chat, final String text)
            throws NoMenuForDateException, NoSuchUserException {
        Pattern pattern = Pattern.compile(dayPattern);
        Matcher matcher = pattern.matcher(text);

        if (matcher.matches()) {
            Calendar calendar = new GregorianCalendar();
            if (matcher.group("date") != null) {
                String[] splitted = matcher.group("date").split("\\.");
                try {
                    calendar = new GregorianCalendar(Integer.parseInt(splitted[2]),
                            Integer.parseInt(splitted[1]) - 1, Integer.parseInt(splitted[0]));
                } catch (Exception e) {
                    sendMessage(chat, "Wrong date format. Try dd.mm.yyyy", null);
                    return;
                }
            }
            if (!menuService.contains(calendar)) {
                sendMessage(chat, "No menu found for " + ToStrings.dateToString(calendar), null);
            } else {
                String date = "Date: " + ToStrings.dateToString(calendar);
                List<MenuItem> items = deciderService.getForDate(from, calendar);
                sendMessage(chat, date + "\n\n" + ToStrings.menuItemsToString(items, 0, 10), null);
                sendMessage(chat, "Sum:" + items.stream().mapToDouble(MenuItem::getPrice).sum(), null);
            }
        } else {
            throw new IllegalArgumentException(text);
        }
    }

    private void handleMenu(final long chat, final String text) {
        Pattern pattern = Pattern.compile(menuPattern);
        Matcher matcher = pattern.matcher(text);

        if (matcher.matches()) {
            Calendar calendar = new GregorianCalendar();
            if (matcher.group("date") != null) {
                String[] splitted = matcher.group("date").split("\\.");
                try {
                    calendar = new GregorianCalendar(Integer.parseInt(splitted[2]),
                            Integer.parseInt(splitted[1]) - 1, Integer.parseInt(splitted[0]));
                } catch (Exception e) {
                    sendMessage(chat, "Wrong date format. Try dd.mm.yyyy", null);
                    return;
                }
            }
            if (!menuService.contains(calendar)) {
                sendMessage(chat, "No menu found for " + ToStrings.dateToString(calendar), null);
            } else {
                Menu menu1 = menuService.getForDate(calendar);
                String date = "Date: " + ToStrings.dateToString(calendar);

                assert menu1 != null;
                sendMessage(chat, date + "\n\n" + ToStrings.menuItemsToString(menu1.getItems(), 0, 100), null);
            }
        } else {
            throw new IllegalArgumentException(text);
        }
    }

    private void handleList(final long chat, final String text) {
        Pattern pattern = Pattern.compile(listPattern);
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

    public void handlePlay(final long chat, final long user) {
        try {
            Game g = gameService.getNextGame(user);
            sendMessage(chat, ToStrings.gameToString(g), new HashMap<String, String>() {
                        {
                            put("reply_markup", ToStrings.gameToKeyBoard(g));
                        }
                    }
            );
        } catch (DeciderException e) {
            sendMessage(chat, "Sth went wrong...", null);
        }
    }

    private void sendHelp(final long id) {
        sendMessage(id, HELP, null);
    }

    private void handleStart(final long chat, final long from) {
        sendHelp(chat);
        handlePlay(chat, from);
    }

    //Проблема с кодировкой на сервере, надо фиксить
}
