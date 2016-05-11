package ru.compscicenter.projects.lunch.web.dao;

import ru.compscicenter.projects.lunch.web.model.UserDBModel;

public interface UserDao {

    /***
     * @return proxy object, can throw ObjectNotFoundException
     */
    UserDBModel getById(long id);

    void saveOrUpdate(UserDBModel userDBModel);

    boolean contains(long id);

    void delete(long id);
}
