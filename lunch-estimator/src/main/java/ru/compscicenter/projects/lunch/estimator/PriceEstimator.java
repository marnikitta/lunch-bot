package ru.compscicenter.projects.lunch.estimator;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.model.MenuItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


public class PriceEstimator {

    private List<Menu> menuArrayList;

    PriceEstimator(final List<Menu> menuArrayList, final Calendar from, final Calendar to) {
        this.menuArrayList = new ArrayList<>();

        for (Menu menu : menuArrayList) {
            final Calendar menuDate = menu.getDate();
            if (menuDate.after(from) && menuDate.before(to)) {
                this.menuArrayList.add(menu);
            }
        }
    }

    public double getMeanValue(final String type) {
        final List<Double> pricesByType = new ArrayList<>();

        for (Menu menu : menuArrayList) {
            for (int i = 0; i < menu.size(); ++i) {
                final MenuItem menuItem = menu.getItem(i);
                if (Objects.equals(menuItem.getType(), type)) {
                    pricesByType.add(menuItem.getPrice());
                }
            }
        }

        double[] prices_array = new double[pricesByType.size()];
        for (int i = 0; i < pricesByType.size(); ++i) {
            prices_array[i] = pricesByType.get(i);
        }

        Percentile percentile = new Percentile();
        percentile.setData(prices_array);

        final double quartile_25 = percentile.evaluate(25);
        final double quartile_75 = percentile.evaluate(75);

        final double leftBorder = quartile_25 - 1.5 * (quartile_75 - quartile_25);
        final double rightBorder = quartile_75 + 1.5 * (quartile_75 - quartile_25);

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
