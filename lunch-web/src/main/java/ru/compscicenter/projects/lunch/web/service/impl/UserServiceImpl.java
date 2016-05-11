package ru.compscicenter.projects.lunch.web.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.compscicenter.projects.lunch.model.User;
import ru.compscicenter.projects.lunch.web.dao.MenuDao;
import ru.compscicenter.projects.lunch.web.dao.UserDao;
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

    private UserDao userDao;
    private MenuDao menuDao;

    public void setMenuDao(MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public User getUserById(final long id) {
        if (exists(id)) {
            UserDBModel userDBModel = userDao.getById(id);
            return ModelConverter.dbUserToUser(userDBModel);
        } else {
            logger.debug("There is no user for id = " + id);
            return null;
        }
    }

    @Override
    @Transactional
    public void makeRandomUser(final long id) {
        UserDBModel user = new UserDBModel();
        user.setId(id);

        if (exists(id)) {
            logger.warn("Already has user with id=" + id);
            user = userDao.getById(id);
        }

        List<MenuItemDBModel> menuList = menuDao.getAllItems();
        Collections.shuffle(menuList);

        int max = Math.min(menuList.size(), 10);
        user.setLoveList(menuList.subList(0, max));
        user.setHateList(new ArrayList<>());

        userDao.saveOrUpdate(user);
    }

    @Override
    @Transactional
    public void createUser(final long id) {
        if (!exists(id)) {
            User user = new User(id);
            userDao.saveOrUpdate(ModelConverter.userToDBUser(user));
        } else {
            try {
                reset(id);
            } catch (NoSuchUserException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @Transactional
    public boolean exists(final long id) {
        return userDao.contains(id);
    }

    @Override
    @Transactional
    public void reset(final long id) throws NoSuchUserException {
        if (exists(id)) {
            UserDBModel user = userDao.getById(id);
            user.getLoveList().clear();
            user.getHateList().clear();
            userDao.saveOrUpdate(user);
        } else {
            throw new NoSuchUserException("No user with id = " + id);
        }
    }
}
