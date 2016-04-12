package ru.compscicenter.projects.lunch.db.util;

import ru.compscicenter.projects.lunch.db.model.MenuDBModel;
import ru.compscicenter.projects.lunch.db.model.MenuItemDBModel;
import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class ModelConverter {
    private ModelConverter() {

    }

    public static Menu dbMenuToMenu(final MenuDBModel menuDBModel) {
        Menu.Builder builder = new Menu.Builder();
        builder.setDate(menuDBModel.getDate());

        List<MenuItem> items = new ArrayList<>();
        menuDBModel.getItems().forEach(item -> items.add(dbMenuItemToMenuItem(item)));
        builder.addAll(items );

        return builder.build();
    }

    public static MenuDBModel menuToDBMenu(final Menu menu) {
        MenuDBModel menuDBModel = new MenuDBModel();
        List<MenuItemDBModel> items = new ArrayList<>();
        menu.stream().forEach(item -> {
            MenuItemDBModel it = menuItemToDBMenuItem(item);
            items.add(it);
        });
        menuDBModel.setDate(menu.getDate());
        menuDBModel.setItems(items);
        return menuDBModel;
    }

    public static MenuItem dbMenuItemToMenuItem(final MenuItemDBModel menuItemDBModel) {
        return new MenuItem(
                menuItemDBModel.getType(),
                menuItemDBModel.getTags(),
                menuItemDBModel.getName(),
                menuItemDBModel.getWeight(),
                menuItemDBModel.getCalorie(),
                menuItemDBModel.getPrice(),
                menuItemDBModel.getComposition());
    }

    public static MenuItemDBModel menuItemToDBMenuItem(final MenuItem menuItem) {
        return new MenuItemDBModel(menuItem.getType(),
                menuItem.getTags(),
                menuItem.getName(),
                menuItem.getWeight(),
                menuItem.getCalorie(),
                menuItem.getPrice(),
                menuItem.getComposition());
    }
}
