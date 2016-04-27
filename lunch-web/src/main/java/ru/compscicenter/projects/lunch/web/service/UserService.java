package ru.compscicenter.projects.lunch.web.service;

import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.model.User;

import java.util.List;

public interface UserService {
    public User getUserById(long id);

    public void makeRandomUser(long id);

    public void createUser(long id);

    public boolean exists(long id);

    public void reset(long id);

    public MenuItem addForNameAndPrice(long id, String name, double price, int type);

    public List<MenuItem> addForNameAndPriceRegex(long id, String regex, double lower, double upper, int type);
}
