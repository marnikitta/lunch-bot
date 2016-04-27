package ru.compscicenter.projects.lunch.web.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.model.User;
import ru.compscicenter.projects.lunch.web.dao.MenuDAO;
import ru.compscicenter.projects.lunch.web.dao.UserDAO;
import ru.compscicenter.projects.lunch.web.exception.NoSuchUserException;
import ru.compscicenter.projects.lunch.web.model.MenuItemDBModel;
import ru.compscicenter.projects.lunch.web.model.UserDBModel;
import ru.compscicenter.projects.lunch.web.service.UserService;
import ru.compscicenter.projects.lunch.web.util.ModelConverter;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserDAO userDAO;
    private MenuDAO menuDAO;

    public void setMenuDAO(MenuDAO menuDAO) {
        this.menuDAO = menuDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public User getUserById(final long id) {
        if (exists(id)) {
            UserDBModel userDBModel = userDAO.getById(id);
            return ModelConverter.dbUserToUser(userDBModel);
        } else {
            logger.debug("There is no user for id = " + id);
            return null;
        }
    }

    @Override
    @Transactional
    public void makeRandomUser(final long id) {
        if (exists(id)) {
            logger.debug("Already has user with id=" + id);
            throw new DuplicateKeyException("Already has user with id=" + id);
        }

        List<MenuItemDBModel> menuList = menuDAO.getAllItems();
        Collections.shuffle(menuList);

        UserDBModel user = new UserDBModel();
        user.setId(id);
        int max = Math.min(menuList.size(), 10);
        user.setLoveList(menuList.subList(0, max));
        user.setHateList(new ArrayList<>());

        userDAO.saveOrUpdate(user);
    }

    @Override
    @Transactional
    public void createUser(long id) {
        if (!exists(id)) {
            User user = new User(id);
            userDAO.saveOrUpdate(ModelConverter.userToDBUser(user));
        } else {
            reset(id);
        }
    }

    @Override
    @Transactional
    public boolean exists(final long id) {
        return userDAO.contains(id);
    }

    @Override
    @Transactional
    public void reset(long id) {
        if (exists(id)) {
            UserDBModel user = userDAO.getById(id);
            user.getLoveList().clear();
            user.getHateList().clear();
            userDAO.saveOrUpdate(user);
        } else {
            throw new NoSuchUserException("No user with id = " + id);
        }
    }

    @Override
    public MenuItem addForNameAndPrice(long id, String name, double price, int type) {
        return null;
    }

    @Override
    public List<MenuItem> addForNameAndPriceRegex(long id, String regex, double lower, double upper, int type) {
        return null;
    }
}
