package ru.compscicenter.projects.lunch.db.dao;

import ru.compscicenter.projects.lunch.db.model.MenuDBModel;

public interface MenuDAO {
    public void saveOrUpdate(MenuDBModel menu);

    public MenuDBModel load(long id);
}
