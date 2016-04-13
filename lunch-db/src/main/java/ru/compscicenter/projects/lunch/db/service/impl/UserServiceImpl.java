package ru.compscicenter.projects.lunch.db.service.impl;

import org.springframework.dao.DuplicateKeyException;
import ru.compscicenter.projects.lunch.db.dao.UserDAO;
import ru.compscicenter.projects.lunch.db.model.MenuItemDBModel;
import ru.compscicenter.projects.lunch.db.model.UserDBModel;
import ru.compscicenter.projects.lunch.db.service.MenuService;
import ru.compscicenter.projects.lunch.db.service.UserService;
import ru.compscicenter.projects.lunch.db.util.ModelConverter;
import ru.compscicenter.projects.lunch.model.User;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {
    private static Logger logger = Logger.getLogger(MenuServiceImpl.class.getName());

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
    public User getUserById(long id) {
        if (userDAO.contains(id)) {
            UserDBModel userDBModel = userDAO.getById(id);
            User user = ModelConverter.dbUserToUser(userDBModel);
            return user;
        } else {
            logger.warning("no user for id = " + id);
            return null;
        }
    }

    @Override
    @Transactional
    public void makeRandomUser(long id) {
        if (userDAO.contains(id)) {
            logger.severe("Already has user with id=" + id);
            throw new DuplicateKeyException("Already has user with id=" + id);
        }
        logger.info("Making random user");
        List<MenuItemDBModel> menuList = menuService.getAllItems();
        Collections.shuffle(menuList);

        UserDBModel user = new UserDBModel();
        user.setId(id);
        int max = Math.min(menuList.size(), 10);
        user.setLoveSet(new HashSet<>(menuList.subList(0, max)));
        user.setHateSet(new HashSet<>());

        userDAO.saveOrUpdate(user);
    }
}
