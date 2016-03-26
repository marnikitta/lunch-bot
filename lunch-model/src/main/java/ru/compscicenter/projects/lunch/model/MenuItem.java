package ru.compscicenter.projects.lunch.model;

import java.util.List;

public class MenuItem {

    private final String type;
    private final String tags;
    private final String name;
    private final double weight;
    private final double calorie;
    private final double price;
    private final List<String> composition;


    public MenuItem(final String type,
                    final String tags,
                    final String name,
                    final double weight,
                    final double calorie,
                    final double price,
                    final List<String> composition) {
        this.type = type;
        this.tags = tags;
        this.name = name;
        this.weight = weight;
        this.calorie = calorie;
        this.price = price;
        this.composition = composition;
    }

    public String getType() {
        return type;
    }

    public String getTags() {
        return tags;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public double getCalorie() {
        return calorie;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getComposition() {
        return composition;
    }
}
