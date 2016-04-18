package ru.compscicenter.projects.lunch.web.service;

import ru.compscicenter.projects.lunch.model.User;

public interface UserService {
    public User getUserById(long id);

    public void makeRandomUser(long id);

    public boolean exists(long id);
}
