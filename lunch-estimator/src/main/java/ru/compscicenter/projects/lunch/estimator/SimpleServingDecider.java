package ru.compscicenter.projects.lunch.estimator;

import ru.compscicenter.projects.lunch.model.MenuItem;

import java.util.List;

public class SimpleServingDecider implements ServingDecider {

    private int n;

    public SimpleServingDecider(final int n) {
        this.n = n;
    }

    @Override
    public List<MenuItem> serve(final List<MenuItem> menuItems) {
        return menuItems.subList(0, Math.min(n, menuItems.size()));
    }
}
