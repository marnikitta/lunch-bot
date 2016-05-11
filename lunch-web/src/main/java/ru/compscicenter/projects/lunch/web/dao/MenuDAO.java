package ru.compscicenter.projects.lunch.web.dao;

import ru.compscicenter.projects.lunch.web.model.MenuDBModel;
import ru.compscicenter.projects.lunch.web.model.MenuItemDBModel;

import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.List;

public interface MenuDao {
    void saveOrUpdate(final MenuDBModel menu);

    List<MenuDBModel> getAll();

    List<MenuDBModel> getAllForDates(final Calendar start, final Calendar end);

    @Nullable
    MenuDBModel getForDate(final Calendar day);

    List<MenuItemDBModel> getAllItems();

    boolean contains(Calendar calendar);

    @Nullable
    MenuItemDBModel getForName(String name);
}
