package ru.compscicenter.projects.lunch.db.dao;

import ru.compscicenter.projects.lunch.db.model.UserDBModel;

public interface UserDAO {
    public UserDBModel getById(long id);

    public void saveOrUpdate(UserDBModel userDBModel);

    public boolean contains(long id);
}
