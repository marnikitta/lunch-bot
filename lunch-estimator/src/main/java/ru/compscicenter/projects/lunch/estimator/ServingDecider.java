package ru.compscicenter.projects.lunch.estimator;

import ru.compscicenter.projects.lunch.model.MenuItem;

import java.util.List;

public interface ServingDecider {
    /**
     * Выбирает из ранжированного списка набор блюд для единовременного потребления, о как.
     *
     * @param menuItems Ранжированный (сортированнаый список по убыванию предпочтения)
     * @return Набор блюд(~ 4-6)
     */
    public List<MenuItem> serve(final List<MenuItem> menuItems);
}
