package ru.compscicenter.projects.lunch.web.service;

import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.web.exception.MenuDuplicateException;
import ru.compscicenter.projects.lunch.web.exception.MenuUploadingException;

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

    public MenuItem getForNameAndPrice(String name, double price);

    public List<MenuItem> getForNameAndPriceRegex(String regex, double lower, double upper) throws PatternSyntaxException;

    public boolean contains(Calendar day);

    public List<MenuItem> getAllItems();

    public Menu upload(InputStream stream) throws MenuUploadingException, MenuDuplicateException;
}
