package ru.compscicenter.projects.lunch.web.service;

import ru.compscicenter.projects.lunch.model.User;
import ru.compscicenter.projects.lunch.web.exception.NoSuchUserException;

import javax.annotation.Nullable;

public interface UserService {

    @Nullable
    User getUserById(long id);

    void makeRandomUser(long id);

    void createUser(long id);

    boolean exists(long id);

    void reset(long id) throws NoSuchUserException;
}
