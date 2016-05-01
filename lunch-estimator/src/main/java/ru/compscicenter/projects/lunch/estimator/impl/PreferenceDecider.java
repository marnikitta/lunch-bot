package ru.compscicenter.projects.lunch.estimator.impl;


import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import ru.compscicenter.projects.lunch.estimator.Decider;
import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.model.MenuKnowledge;
import ru.compscicenter.projects.lunch.model.User;
import ru.compscicenter.projects.lunch.tagger.MenuItemClustering;
import ru.compscicenter.projects.lunch.tagger.MenuItemLocation;
import ru.compscicenter.projects.lunch.tagger.MinDfException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PreferenceDecider implements Decider {

    private final DistanceMeasure distanceMeasure = new EuclideanDistance();

    private static final int NUMBER_OF_CLUSTERS = 10;

    private static final int NUMBER_OF_ITERATIONS = 10000;

    private static final int MIN_DF = 0;

    @Override
    public List<MenuItem> range(final List<? extends MenuItem> sample, final MenuKnowledge knowledge, final User user) {

        MenuItemClustering clusterer = new MenuItemClustering(NUMBER_OF_CLUSTERS, NUMBER_OF_ITERATIONS, MIN_DF);
        List<MenuItem> sortSample = null;


        try {

            clusterer.fit(new ArrayList<>(knowledge.getList()));

            List<Integer> preferenceClusters = new ArrayList<>();
            List loveSet = user.getLoveList();
            for (int clusterInd = 0; clusterInd < clusterer.size(); ++clusterInd) {
                for (MenuItemLocation itemLocation : clusterer.getPoints(clusterInd)) {
                    if (loveSet.contains(itemLocation.getMenuItem())) {
                        preferenceClusters.add(clusterInd);
                    }
                }
            }


            List<Double> distances = clusterer.clusterDistance(sample, preferenceClusters);

            List<Integer> indecies = new ArrayList<>();
            for (int i = 0; i < distances.size(); ++i) {
                indecies.add(i);
            }

            Collections.sort(indecies, (o1, o2) -> distances.get(o1).compareTo(distances.get(o2)));


            sortSample = new ArrayList<>();
            for (int i = 0; i < sample.size(); ++i) {
                sortSample.add(sample.get(indecies.get(i)));
            }


        } catch (MinDfException e) {
            e.printStackTrace();
        }

        return sortSample;

    }
}
