package ru.compscicenter.projects.lunch.estimator;

import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.model.MenuKnowledge;
import ru.compscicenter.projects.lunch.model.User;

import java.util.List;


public interface Decider {
    /**
     * Сортирует список блюд на основании предпочтений пользователя и базы знаний.
     * По убыванию предпочтения
     *
     * @param sample    список блюд, который необходимо отсортировать
     * @param knowledge база знаний
     * @param user      пользователь, на предпочтениях которого основывается алгоритм
     * @return sorted sample list
     */
    public List<MenuItem> range(final List<? extends MenuItem> sample, final MenuKnowledge knowledge, final User user);
}
