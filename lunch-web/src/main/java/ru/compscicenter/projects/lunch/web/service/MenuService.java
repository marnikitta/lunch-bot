package ru.compscicenter.projects.lunch.web.service;

import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.web.exception.MenuDuplicateException;
import ru.compscicenter.projects.lunch.web.exception.MenuUploadingException;
import ru.compscicenter.projects.lunch.web.model.MenuItemDBModel;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.regex.PatternSyntaxException;

public interface MenuService {
    public void saveMenu(Menu menu) throws MenuDuplicateException;

    public List<Menu> getAll();

    public void saveAll(Collection<? extends Menu> coll) throws MenuDuplicateException;

    public List<Menu> getAllForDates(Calendar start, Calendar end);

    public Menu getForDate(Calendar day);

    public MenuItem getForName(String name);

    public List<MenuItem> getForNameRegex(String regex) throws PatternSyntaxException;

    public boolean contains(Calendar day);

    public List<MenuItem> getAllItems();

    public List<MenuItemDBModel> getAllDBItems();

    public Menu upload(InputStream stream) throws MenuUploadingException, MenuDuplicateException;
}
