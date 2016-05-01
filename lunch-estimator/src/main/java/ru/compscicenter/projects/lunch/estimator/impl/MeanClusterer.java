package ru.compscicenter.projects.lunch.estimator.impl;

import ru.compscicenter.projects.lunch.estimator.Clusterer;
import ru.compscicenter.projects.lunch.estimator.DeciderException;
import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.tagger.MenuItemClustering;
import ru.compscicenter.projects.lunch.tagger.MenuItemLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MeanClusterer implements Clusterer {

    private static final int NUMBER_OF_CLUSTERS = 10;

    private static final int NUMBER_OF_ITERATIONS = 10000;

    private static final int MIN_DF = 0;

    @Override
    public Map<MenuItem, Integer> doCluster(List<? extends MenuItem> sample) throws DeciderException {

        MenuItemClustering clusterer = new MenuItemClustering(NUMBER_OF_CLUSTERS, NUMBER_OF_ITERATIONS, MIN_DF);
        Map<MenuItem, Integer> clusterMap = null;

        try {
            clusterer.fit(sample);

            clusterMap = new HashMap<>();
            for (int i = 0; i < clusterer.size(); ++i) {
                for (MenuItemLocation menuItemLocation : clusterer.getPoints(i)) {
                    clusterMap.put(menuItemLocation.getMenuItem(), i);
                }
            }

        } catch (Exception e) {
            throw new DeciderException(e);
        }
        return clusterMap;
    }
}
