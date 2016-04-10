package ru.compscicenter.projects.lunch.model;

import java.util.*;

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
        Set<MenuItem> result = new HashSet<>();
        for (Menu m : menus) {
            m.stream().forEach(result::add);
        }
        return result;
    }

    public Set<MenuItem> getList(final Calendar begin, final Calendar end) {
        Set<MenuItem> result = new HashSet<>();
        for (Menu m : menus) {
            if (m.getDate().after(begin) && m.getDate().before(end)) {
                m.stream().forEach(result::add);
            }
        }
        return result;
    }
}
