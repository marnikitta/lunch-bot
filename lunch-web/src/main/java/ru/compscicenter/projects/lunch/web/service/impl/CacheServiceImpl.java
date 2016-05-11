package ru.compscicenter.projects.lunch.web.service.impl;

import ru.compscicenter.projects.lunch.estimator.impl.MeanClusterer;
import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.web.model.MenuItemDBModel;
import ru.compscicenter.projects.lunch.web.service.CacheService;
import ru.compscicenter.projects.lunch.web.service.MenuService;
import ru.compscicenter.projects.lunch.web.util.ModelConverter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheServiceImpl implements CacheService {


    private static double LIFE_TIME = 8e7;
    private Map<MenuItemDBModel, Integer> cached = null;
    private long lastUpdate = -1;

    private MenuService menuService;

    public void setMenuService(final MenuService menuService) {
        this.menuService = menuService;
    }

    @Override
    public Map<MenuItemDBModel, Integer> getClusters() {
        if (cached == null || System.nanoTime() - lastUpdate > LIFE_TIME) {
            update();
        }
        return cached;
    }

    @Override
    public synchronized void forceUpdate() {
        lastUpdate = -1;
        update();
    }

    private synchronized void update() {
        if (!(cached == null || System.currentTimeMillis() - lastUpdate > LIFE_TIME)) {
            return;
        }
        Map<MenuItemDBModel, Integer> result = new HashMap<>();

        List<MenuItem> items = menuService.getAllItems();
        Map<MenuItem, Integer> map = new MeanClusterer().doCluster(items);

        List<MenuItemDBModel> allMenuItems = menuService.getAllDBItems();
        for (MenuItemDBModel menuItemDBModel : allMenuItems) {
            MenuItem key = ModelConverter.dbMenuItemToMenuItem(menuItemDBModel);

            if (map.containsKey(key)) {
                int cls = map.get(key);
                result.putIfAbsent(menuItemDBModel, cls);
            } else {
                throw new IllegalStateException("Map should contain this key");
            }
        }

        lastUpdate = System.currentTimeMillis();
        cached = Collections.unmodifiableMap(result);
    }
}
