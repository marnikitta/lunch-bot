package ru.compscicenter.projects.lunch.web.util;

import ru.compscicenter.projects.lunch.model.MenuItem;

import java.util.Collections;
import java.util.List;

public class ToStrings {
    private ToStrings() {
    }

    public static String menuItems(List<? extends MenuItem> collection) {
        StringBuilder stringBuilder = new StringBuilder();
        Collections.shuffle(collection);
        for (int i = 0; i < Math.min(collection.size(), 10); ++i) {
            stringBuilder.append(menuItem(collection.get(i)) + "\n");
        }
        if (collection.size() > 10) {
            stringBuilder.append("...showing only first 10 rows...\n");
        }
        return stringBuilder.toString();
    }

    public static String menuItem(MenuItem menuItem) {
        return menuItem.getName() + " - " + menuItem.getPrice();
    }
}
