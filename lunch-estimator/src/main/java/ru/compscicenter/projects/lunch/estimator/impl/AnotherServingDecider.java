package ru.compscicenter.projects.lunch.estimator.impl;

import ru.compscicenter.projects.lunch.estimator.ServingDecider;
import ru.compscicenter.projects.lunch.model.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnotherServingDecider implements ServingDecider {
    @Override
    public List<MenuItem> serve(List<MenuItem> menuItems) {
        Map<String, MenuItem> kinds = new HashMap<>();
        menuItems.forEach(menuItem -> kinds.merge(menuItem.getType(), menuItem, (o, n) -> o));
        return new ArrayList<>(kinds.values());
    }
}
