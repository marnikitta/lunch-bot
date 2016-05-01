package ru.compscicenter.projects.lunch.web.service;

import ru.compscicenter.projects.lunch.estimator.DeciderException;
import ru.compscicenter.projects.lunch.web.model.MenuItemDBModel;

import java.util.Map;

public interface CacheService {
    public Map<MenuItemDBModel, Integer> getClusters() throws DeciderException;

    public void forceUpdate() throws DeciderException;
}
