package ru.compscicenter.projects.lunch.tagger;


import org.apache.commons.math3.ml.clustering.Clusterable;

public class MenuItemLocation implements Clusterable {

    private final String name;
    private final double price;
    private final double weight;
    private final double[] point;

    public MenuItemLocation(double[] tfidf, double price, double weight, String name) {
        point = new double[tfidf.length + 2];

        System.arraycopy(tfidf, 0, point, 0, tfidf.length);

        point[tfidf.length] = price;
        point[tfidf.length + 1] = weight;

        this.name = name;
        this.price = price;
        this.weight = weight;
    }

    @Override
    public double[] getPoint() {
        return point;
    }

    public String getName(){
        return name;
    }

    public double getPrice(){
        return price;
    }

    public double getWeight(){
        return weight;
    }
}
