package ru.compscicenter.projects.lunch.web.service;

import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.web.exception.MenuDuplicateException;
import ru.compscicenter.projects.lunch.web.exception.MenuUploadingException;
import ru.compscicenter.projects.lunch.web.model.MenuItemDBModel;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.regex.PatternSyntaxException;

public interface MenuService {
    void saveMenu(Menu menu) throws MenuDuplicateException;

    List<Menu> getAll();

    void saveAll(Collection<? extends Menu> coll) throws MenuDuplicateException;

    List<Menu> getAllForDates(Calendar start, Calendar end);

    @Nullable
    Menu getForDate(Calendar day);

    @Nullable
    MenuItem getForName(String name);

    List<MenuItem> getForNameRegex(String regex) throws PatternSyntaxException;

    boolean contains(Calendar day);

    List<MenuItem> getAllItems();

    List<MenuItemDBModel> getAllDBItems();

    Menu upload(InputStream stream) throws MenuUploadingException, MenuDuplicateException;
}
