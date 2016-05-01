package ru.compscicenter.projects.lunch.web.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class DeciderServiceImpl implements DeciderService {


    private static Logger logger = LoggerFactory.getLogger(DeciderServiceImpl.class);
    private Decider decider;
    private UserService userService;
    private MenuService menuService;
    private ServingDecider servingDecider;

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
    public double sumForPeriod(final long userId, final Calendar start, final Calendar end) {
        double result = 0;

        Calendar it = (Calendar) start.clone();
        while (!it.getTime().after(end.getTime())) {
            try {
                List<MenuItem> menuItems = getForDate(userId, it);
                result += menuItems.stream().mapToDouble(MenuItem::getPrice).sum();
            } catch (NoMenuForDateException e) {
                logger.debug("Summing", e);
            }
            it.add(Calendar.DAY_OF_MONTH, 1);
        }
        return result;
    }

    @Override
    @Transactional
    public List<MenuItem> getForDate(final long userId, final Calendar date) {
        if (date == null) {
            throw new NullPointerException("date is null");
        }

        Menu menu = menuService.getForDate(date);
        if (menu == null) {
            throw new NoMenuForDateException("There is no menu for date " + date);
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            throw new NoSuchUserException("No such user  " + userId);
        }

        List<Menu> data = menuService.getAllForDates(new GregorianCalendar(1996, 1, 1), date);
        if (data.size() == 0) {
            throw new NoMenuForDateException("Knowledge base is empty for " + date.toString());
        }
        MenuKnowledge knowledge = new MenuKnowledge(data);

        List<MenuItem> result = decider.range(menu.getItemsCopy(), knowledge, user);

        return servingDecider.serve(result);
    }
}
