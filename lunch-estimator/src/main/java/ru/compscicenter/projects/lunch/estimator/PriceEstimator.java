package ru.compscicenter.projects.lunch.estimator;

import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.model.MenuItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


public class PriceEstimator {

    private ArrayList<Menu> menuArrayList;

    PriceEstimator(ArrayList<Menu> menuArrayList, Calendar from, Calendar to) {
        this.menuArrayList = new ArrayList<Menu>();

        for (Menu menu : menuArrayList) {
            Calendar menuDate = menu.getDate();
            if (menuDate.after(from) && menuDate.before(to)) {
                this.menuArrayList.add(menu);
            }
        }
    }

    public double getMeanValue(String type) {
        ArrayList<Double> pricesByType = new ArrayList<Double>();

        for (Menu menu : menuArrayList) {
            for (int i = 0; i < menu.size(); ++i) {
                MenuItem menuItem = menu.getItem(i);
                if (Objects.equals(menuItem.getType(), type)) {
                    pricesByType.add(menuItem.getPrice());
                }
            }
        }

        pricesByType.sort(Double::compareTo);

        double quartile_25 = pricesByType.get((int) (pricesByType.size() * 0.25 + 1));
        double quartile_75 = pricesByType.get((int) (pricesByType.size() * 0.75 + 1));

        double leftBorder = quartile_25 - 1.5 * (quartile_75 - quartile_25);
        double rightBorder = quartile_75 + 1.5 * (quartile_75 - quartile_25);

        double sum = 0;
        int numberOfSum = 0;
        for (double price : pricesByType) {
            if (price > leftBorder && price < rightBorder && price > 0) {
                sum += price;
                ++numberOfSum;
            }
        }
        return sum / numberOfSum;
    }
}
