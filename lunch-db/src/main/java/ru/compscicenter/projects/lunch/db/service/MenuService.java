package ru.compscicenter.projects.lunch.db.service;

import ru.compscicenter.projects.lunch.db.model.MenuItemDBModel;
import ru.compscicenter.projects.lunch.model.Menu;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public interface MenuService {
    public void saveMenu(Menu menu);

    public List<Menu> getAll();

    public void saveAll(Collection<? extends Menu> coll);

    public List<Menu> getAllForDates(Calendar start, Calendar end);

    public Menu getForDate(Calendar day);

    public List<MenuItemDBModel> getAllItems();
}
