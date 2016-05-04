package ru.compscicenter.projects.lunch.model;

import java.util.*;
import java.util.stream.Collectors;

public class MenuKnowledge {
    private List<Menu> menus;

    public MenuKnowledge(final Collection<? extends Menu> collection) {
        menus = new ArrayList<>();
        menus.addAll(collection);
    }

    public MenuKnowledge() {
        menus = new ArrayList<>();
    }

    public Set<MenuItem> getList() {
        return menus.stream().
                flatMap(Menu::stream).
                collect(Collectors.toSet());
    }

    public Set<MenuItem> getList(final Calendar begin, final Calendar end) {
        return menus.stream().
                filter(m -> m.getDate().after(begin) && m.getDate().before(end)).
                flatMap(Menu::stream).
                collect(Collectors.toSet());
    }
}
