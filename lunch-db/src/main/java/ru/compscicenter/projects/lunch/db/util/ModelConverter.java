package ru.compscicenter.projects.lunch.db.util;

import ru.compscicenter.projects.lunch.db.model.MenuDBModel;
import ru.compscicenter.projects.lunch.db.model.MenuItemDBModel;
import ru.compscicenter.projects.lunch.db.model.UserDBModel;
import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModelConverter {
    private ModelConverter() {

    }

    public static Menu dbMenuToMenu(final MenuDBModel menuDBModel) {
        Menu.Builder builder = new Menu.Builder();
        builder.setDate(menuDBModel.getDate());

        List<MenuItem> items = new ArrayList<>();
        menuDBModel.getItems().forEach(item -> items.add(dbMenuItemToMenuItem(item)));
        builder.addAll(items);

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

    public static UserDBModel userToDBUser(final User user) {
        UserDBModel userDBModel = new UserDBModel();
        userDBModel.setId(user.getId());

        Set<MenuItemDBModel> dbSet = new HashSet<>();
        user.getLoveSet().stream().forEach(item -> dbSet.add(menuItemToDBMenuItem(item)));
        userDBModel.setLoveSet(dbSet);

        dbSet.clear();
        user.getHateSet().stream().forEach(item -> dbSet.add(menuItemToDBMenuItem(item)));
        userDBModel.setHateSet(dbSet);

        return userDBModel;
    }

    public static User dbUserToUser(final UserDBModel userDBModel) {
        User user = new User(userDBModel.getId());
        Set<MenuItem> set = new HashSet<>();

        userDBModel.getLoveSet().stream().forEach(item -> set.add(dbMenuItemToMenuItem(item)));
        user.getLoveSet().addAll(set);

        set.clear();
        userDBModel.getHateSet().stream().forEach(item -> set.add(dbMenuItemToMenuItem(item)));
        user.getHateSet().addAll(set);

        return user;
    }
}
