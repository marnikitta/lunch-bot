package ru.compscicenter.projects.lunch.web.service;

import ru.compscicenter.projects.lunch.web.model.MenuItemDBModel;

import java.util.Map;

public interface CacheService {
    Map<MenuItemDBModel, Integer> getClusters();

    void forceUpdate();
}
