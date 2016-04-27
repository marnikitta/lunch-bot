package ru.compscicenter.projects.lunch.web.util;

import ru.compscicenter.projects.lunch.model.MenuItem;

import java.util.List;

public class ToStrings {
    private ToStrings() {
    }

    public static String menuItemsToString(List<? extends MenuItem> collection, int offset, int limit) {
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

    public static String meuItemToString(MenuItem menuItem) {
        return menuItem.getName() + " - " + menuItem.getPrice();
    }
}
