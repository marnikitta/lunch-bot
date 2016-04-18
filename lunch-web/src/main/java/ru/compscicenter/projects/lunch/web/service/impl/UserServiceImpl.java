package ru.compscicenter.projects.lunch.web.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import ru.compscicenter.projects.lunch.model.User;
import ru.compscicenter.projects.lunch.web.dao.UserDAO;
import ru.compscicenter.projects.lunch.web.model.MenuItemDBModel;
import ru.compscicenter.projects.lunch.web.model.UserDBModel;
import ru.compscicenter.projects.lunch.web.service.MenuService;
import ru.compscicenter.projects.lunch.web.service.UserService;
import ru.compscicenter.projects.lunch.web.util.ModelConverter;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private MenuService menuService;

    private UserDAO userDAO;

    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
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
        if (userDAO.contains(id)) {
            logger.debug("Already has user with id=" + id);
            throw new DuplicateKeyException("Already has user with id=" + id);
        }
        List<MenuItemDBModel> menuList = menuService.getAllItems();
        Collections.shuffle(menuList);

        UserDBModel user = new UserDBModel();
        user.setId(id);
        int max = Math.min(menuList.size(), 10);
        user.setLoveSet(new HashSet<>(menuList.subList(0, max)));
        user.setHateSet(new HashSet<>());

        userDAO.saveOrUpdate(user);
    }

    @Override
    @Transactional
    public boolean exists(final long id) {
        return userDAO.contains(id);
    }
}
