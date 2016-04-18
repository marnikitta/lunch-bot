package ru.compscicenter.projects.lunch.web.dao;

import ru.compscicenter.projects.lunch.web.model.MenuDBModel;
import ru.compscicenter.projects.lunch.web.model.MenuItemDBModel;

import java.util.Calendar;
import java.util.List;

public interface MenuDAO {
    public void saveOrUpdate(final MenuDBModel menu);

    public List<MenuDBModel> getAll();

    public List<MenuDBModel> getAllForDates(final Calendar start, final Calendar end);

    public MenuDBModel getForDate(final Calendar day);

    public List<MenuItemDBModel> getAllItems();

    public boolean contains(Calendar calendar);
}
