package ru.compscicenter.projects.lunch.web.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.web.model.Game;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;

public final class ToStrings {
    private ToStrings() {
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    public static String dateToString(final Calendar calendar) {
        return sdf.format(calendar.getTime());
    }

    public static String menuItemsToString(final List<? extends MenuItem> collection, final int offset, final int limit) {
        StringBuilder stringBuilder = new StringBuilder();
        if (collection.size() <= offset) {
            return "";
        }
        for (int i = offset; i < offset + Math.min(collection.size(), limit); ++i) {
            stringBuilder.append(meuItemToString(collection.get(i))).append("\n");
        }
        if (collection.size() > limit) {
            stringBuilder.append("...showing only first ").append(limit).append(" rows...\n");
        }
        return stringBuilder.toString();
    }

    public static String gameToString(final Game game) {
        return "1. " + game.getFirst().getName() + " -" + game.getFirst().getPrice() + "\n"
                + "2. " + game.getSecond().getName() + " -" + game.getSecond().getPrice();
    }


    @SuppressWarnings("unchecked")
    public static String gameToKeyBoard(final Game game) {
        JSONObject jsonObject = new JSONObject();

        JSONArray row1 = new JSONArray();
        row1.add(game.getFirst().getName() + " ($" + encode(game.getId() + "#" + game.getFirst().getId()) + ")");
        JSONArray row2 = new JSONArray();
        row2.add(game.getSecond().getName() + " ($" + encode(game.getId() + "#" + game.getSecond().getId()) + ")");
        JSONArray row3 = new JSONArray();
        row3.add("/cancel");

        JSONArray keyboard = new JSONArray();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        jsonObject.put("keyboard", keyboard);
        jsonObject.put("resize_keyboard", true);
        jsonObject.put("one_time_keyboard", true);
        jsonObject.put("selective", true);
        return jsonObject.toJSONString();
    }

    public static String meuItemToString(final MenuItem menuItem) {
        return menuItem.getName() + " - " + menuItem.getPrice();
    }

    public static String encode(final String string) {
        return Base64.getEncoder().encodeToString(string.getBytes());
    }

    public static String decode(final String decode) {
        return new String(Base64.getDecoder().decode(decode));
    }
}
