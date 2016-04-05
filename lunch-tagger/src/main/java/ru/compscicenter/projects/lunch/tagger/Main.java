package ru.compscicenter.projects.lunch.tagger;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import ru.compscicenter.projects.lunch.estimator.MenuXmlParser;
import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.model.MenuItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) throws Exception {
        final File folder = new File("xml");

        List<Menu> menus = new ArrayList<>();
        List<MenuItem> menuItems = new ArrayList<>();

        for (final File fileEntry : folder.listFiles()) {
            menus.addAll(MenuXmlParser.parseMenu(fileEntry.getPath()));
        }

        for (Menu menu : menus) {
            for (int j = 0; j < menu.size(); ++j) {
                menuItems.add(menu.getItem(j));
            }
        }

        MenuItemClustering clusterer = new MenuItemClustering(menuItems, 0);

        List<CentroidCluster<MenuItemLocation>> clusterResults = clusterer.clusterRun(10, 10000);

        for (int i = 0; i < clusterResults.size(); i++) {
            System.out.println("Cluster " + i);
            for (MenuItemLocation menuItem : clusterResults.get(i).getPoints()) {
                System.out.println(menuItem.getName() + " " + menuItem.getPrice() + " " + menuItem.getWeight());
            }
            System.out.println();
        }
    }
}
