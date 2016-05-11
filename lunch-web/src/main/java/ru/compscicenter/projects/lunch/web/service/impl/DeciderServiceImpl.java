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

public class DeciderServiceImpl implements DeciderService {


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
    public double sumForPeriod(final long userId, final Calendar start, final Calendar end) throws NoSuchUserException {
        if (!userService.exists(userId)) {
            throw new NoSuchUserException("No such user  " + userId);
        }

        User user = userService.getUserById(userId);

        List<Menu> data = menuService.getAllForDates(new GregorianCalendar(1996, 1, 1), end);
        if (data.size() == 0) {
            return 0;
        }
        MenuKnowledge knowledge = new MenuKnowledge(data);

        double result = 0;

        Calendar it = (Calendar) start.clone();

        while (!it.getTime().after(end.getTime())) {
            if (menuService.contains(it)) {
                Menu menu = menuService.getForDate(it);
                assert menu != null;
                List<MenuItem> list = decider.range(menu.getItems(), knowledge, user);
                result += servingDecider.serve(list).stream().mapToDouble(MenuItem::getPrice).sum();
            }
            it.add(Calendar.DAY_OF_MONTH, 1);
        }
        return result;
    }

    @Override
    @Transactional
    public List<MenuItem> getForDate(final long userId, final Calendar date)
            throws NoMenuForDateException, NoSuchUserException {
        if (!menuService.contains(date)) {
            throw new NoMenuForDateException("There is no menuPattern for date " + date);
        }

        if (!userService.exists(userId)) {
            throw new NoSuchUserException("No such user  " + userId);
        }

        Menu menu = menuService.getForDate(date);
        User user = userService.getUserById(userId);

        List<Menu> data = menuService.getAllForDates(new GregorianCalendar(1996, 1, 1), date);
        MenuKnowledge knowledge = new MenuKnowledge(data);

        assert menu != null;
        assert user != null;

        List<MenuItem> result = decider.range(menu.getItems(), knowledge, user);

        return servingDecider.serve(result);
    }
}
