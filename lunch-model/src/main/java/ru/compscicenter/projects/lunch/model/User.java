package ru.compscicenter.projects.lunch.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class User {
    private final long id;

    private final Set<MenuItem> loveSet;
    private final Set<MenuItem> hateSet;

    public User(final long id, final Collection<? extends MenuItem> loveCol, final Collection<? extends MenuItem> hateCol) {
        this.id = id;
        this.loveSet = new HashSet<>(loveCol);
        this.hateSet = new HashSet<>(hateCol);
    }

    public User(final long id) {
        this.id = id;
        this.hateSet = new HashSet<>();
        this.loveSet = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public Set<MenuItem> getLoveSet() {
        return loveSet;
    }

    public Set<MenuItem> getHateSet() {
        return hateSet;
    }
}
