package ru.compscicenter.projects.lunch.estimator.impl;

import ru.compscicenter.projects.lunch.estimator.Decider;
import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.model.MenuKnowledge;
import ru.compscicenter.projects.lunch.model.User;

import java.util.Collections;
import java.util.List;

public class RandomDecider implements Decider {
    @Override
    public List<? extends MenuItem> range(List<? extends MenuItem> sample, MenuKnowledge knowledge, User user) {
        Collections.shuffle(sample);
        return sample;
    }
}
