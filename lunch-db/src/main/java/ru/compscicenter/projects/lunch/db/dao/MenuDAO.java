package ru.compscicenter.projects.lunch.db.dao;

import ru.compscicenter.projects.lunch.db.model.MenuDBModel;

import java.util.Calendar;
import java.util.List;

public interface MenuDAO {
    public void saveOrUpdate(final MenuDBModel menu);

    public MenuDBModel load(final long id);

    public List<MenuDBModel> getAll();

    public List<MenuDBModel> getAllForDates(final Calendar start, final Calendar end);
}
