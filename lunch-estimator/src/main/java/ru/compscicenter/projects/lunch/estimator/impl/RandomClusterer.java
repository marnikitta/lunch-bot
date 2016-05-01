package ru.compscicenter.projects.lunch.estimator.impl;

import ru.compscicenter.projects.lunch.estimator.Clusterer;
import ru.compscicenter.projects.lunch.model.MenuItem;

import java.util.*;

public class RandomClusterer implements Clusterer {
    private static final Random rd = new Random();
    private static final int clusters = 5;

    @Override
    public Map<MenuItem, Integer> doCluster(List<? extends MenuItem> list) {
        Map<MenuItem, Integer> result = new HashMap<>();

        for (MenuItem menuItem : list) {
            result.putIfAbsent(menuItem, rd.nextInt(5));
        }
        return result;
    }
}
