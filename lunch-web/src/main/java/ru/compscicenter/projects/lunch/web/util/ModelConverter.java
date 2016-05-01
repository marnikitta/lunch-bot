package ru.compscicenter.projects.lunch.web.util;

import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.model.User;
import ru.compscicenter.projects.lunch.web.model.MenuDBModel;
import ru.compscicenter.projects.lunch.web.model.MenuItemDBModel;
import ru.compscicenter.projects.lunch.web.model.UserDBModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class ModelConverter {

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
                new ArrayList<>(menuItemDBModel.getComposition())); //Костыль. Иначе equals стремно работает
    }

    public static MenuItemDBModel menuItemToDBMenuItem(final MenuItem menuItem) {
        return new MenuItemDBModel(menuItem.getType(),
                menuItem.getTags(),
                menuItem.getName(),
                menuItem.getWeight(),
                menuItem.getCalorie(),
                menuItem.getPrice(),
                new ArrayList<>(menuItem.getComposition())); // Аналогичный костыль
    }

    public static UserDBModel userToDBUser(final User user) {
        UserDBModel userDBModel = new UserDBModel();
        userDBModel.setId(user.getId());

        userDBModel.setLoveList(user.getLoveList().stream().map(ModelConverter::menuItemToDBMenuItem).collect(Collectors.toList()));
        userDBModel.setHateList(user.getHateList().stream().map(ModelConverter::menuItemToDBMenuItem).collect(Collectors.toList()));

        return userDBModel;
    }

    public static User dbUserToUser(final UserDBModel userDBModel) {
        User user = new User(userDBModel.getId());

        userDBModel.getLoveList().stream().map(ModelConverter::dbMenuItemToMenuItem).forEach(user.getLoveList()::add);
        userDBModel.getHateList().stream().map(ModelConverter::dbMenuItemToMenuItem).forEach(user.getHateList()::add);

        return user;
    }
}
