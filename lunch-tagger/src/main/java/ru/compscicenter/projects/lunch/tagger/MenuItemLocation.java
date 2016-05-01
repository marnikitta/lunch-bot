package ru.compscicenter.projects.lunch.tagger;


import org.apache.commons.math3.ml.clustering.Clusterable;
import ru.compscicenter.projects.lunch.model.MenuItem;

public class MenuItemLocation implements Clusterable {

    private final MenuItem menuItem;
    private final double[] point;

    public MenuItemLocation(double[] tfidf, MenuItem menuItem) {
        point = new double[tfidf.length + 2];

        System.arraycopy(tfidf, 0, point, 0, tfidf.length);

        point[tfidf.length] = menuItem.getPrice();
        point[tfidf.length + 1] = menuItem.getWeight();

        this.menuItem = menuItem;
    }

    @Override
    public double[] getPoint() {
        return point;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }
}
