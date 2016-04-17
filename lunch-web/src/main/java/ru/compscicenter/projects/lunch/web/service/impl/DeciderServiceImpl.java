package ru.compscicenter.projects.lunch.web.service.impl;

import ru.compscicenter.projects.lunch.estimator.Decider;
import ru.compscicenter.projects.lunch.estimator.ServingDecider;
import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.model.MenuKnowledge;
import ru.compscicenter.projects.lunch.model.User;
import ru.compscicenter.projects.lunch.web.exception.NoMenuForDateException;
import ru.compscicenter.projects.lunch.web.exception.NoSuchUserException;
import ru.compscicenter.projects.lunch.web.service.DeciderService;
import ru.compscicenter.projects.lunch.web.service.MenuService;
import ru.compscicenter.projects.lunch.web.service.UserService;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeciderServiceImpl implements DeciderService {

    private Decider decider;
    private UserService userService;
    private MenuService menuService;
    private ServingDecider servingDecider;
    private static Logger logger = Logger.getLogger(DeciderServiceImpl.class.getName());

    public void setServingDecider(ServingDecider servingDecider) {
        this.servingDecider = servingDecider;
    }

    public void setDecider(Decider decider) {
        this.decider = decider;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    @Override
    @Transactional
    public List<MenuItem> getForDate(long userId, Calendar date) {
        logger.log(Level.FINE, "Getting for date");

        List<Menu> data = menuService.getAllForDates(new GregorianCalendar(1996, 1, 1), date);
        if (data.size() == 0) {
            logger.warning("Knowledge base is empty for " + date.toString());
            throw new NoMenuForDateException("Knowledge base is empty for " + date.toString());
        }
        MenuKnowledge knowledge = new MenuKnowledge(data);

        User user = userService.getUserById(userId);
        if (user == null) {
            throw new NoSuchUserException("No such user  " + userId);
        }

        Menu menu = menuService.getForDate(date);
        if (menu == null) {
            logger.warning("There is no menu for date " + date);
            throw new NoMenuForDateException("There is no menu for date " + date);
        }
        List<MenuItem> items = menu.getItemsCopy();
        decider.range(items, knowledge, user);

        return servingDecider.serve(items);
    }
}
