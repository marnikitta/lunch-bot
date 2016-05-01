package ru.compscicenter.projects.lunch.web.model;

import ru.compscicenter.projects.lunch.model.MenuItem;

import java.util.List;

public class TestMenuDBModel extends MenuItem {

    public TestMenuDBModel(String type, String tags, String name, double weight, double calorie, double price, List<String> composition) {
        super(type, tags, name, weight, calorie, price, composition);
    }
}
