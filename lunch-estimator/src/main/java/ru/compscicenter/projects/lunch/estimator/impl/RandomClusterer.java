package ru.compscicenter.projects.lunch.estimator.impl;

import ru.compscicenter.projects.lunch.estimator.Clusterer;
import ru.compscicenter.projects.lunch.model.MenuItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomClusterer implements Clusterer {
    private static final Random rd = new Random();
    private static final int clusters = 5;

    @Override
    public Map<MenuItem, Integer> doCluster(Collection<? extends MenuItem> collection) {
        Map<MenuItem, Integer> result = new HashMap<>();

        for (MenuItem menuItem : collection) {
            result.putIfAbsent(menuItem, rd.nextInt(5));
        }
        return result;
    }
}
