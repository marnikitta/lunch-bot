package ru.compscicenter.projects.lunch.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final long id;

    private final List<MenuItem> loveList;
    private final List<MenuItem> hateList;

    public User(final long id, final List<? extends MenuItem> loveCol, final List<? extends MenuItem> hateCol) {
        this.id = id;
        this.loveList = new ArrayList<>(loveCol);
        this.hateList = new ArrayList<>(hateCol);
    }

    public User(final long id) {
        this.id = id;
        this.hateList = new ArrayList<>();
        this.loveList = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public List<MenuItem> getLoveList() {
        return loveList;
    }

    public List<MenuItem> getHateList() {
        return hateList;
    }
}
