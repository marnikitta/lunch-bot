package ru.compscicenter.projects.lunch.estimator;

import ru.compscicenter.projects.lunch.model.Menu;

import java.io.File;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Main {
    public static void main(String[] args) throws Exception {


        final File folder = new File("xml");
        ArrayList<Menu> menus = new ArrayList<Menu>();

        for (final File fileEntry : folder.listFiles()) {
            menus.addAll(MenuXmlParser.parseMenu(fileEntry.getPath()));
        }


        String[] types = {"garnish", "main course", "soup", "salad"};
        GregorianCalendar from = new GregorianCalendar(2015, 2, 1);
        GregorianCalendar to = new GregorianCalendar(2017, 2, 29);


        PriceEstimator estimator = new PriceEstimator(menus, from, to);
        for (String type : types) {
            System.out.println(estimator.getMeanValue(type));
        }

    }
}
