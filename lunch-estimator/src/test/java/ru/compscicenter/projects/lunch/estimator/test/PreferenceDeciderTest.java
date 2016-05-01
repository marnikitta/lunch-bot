package ru.compscicenter.projects.lunch.estimator.test;

import org.testng.annotations.Test;
import ru.compscicenter.projects.lunch.estimator.MenuXmlParser;
import ru.compscicenter.projects.lunch.estimator.impl.PreferenceDecider;
import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.model.MenuKnowledge;
import ru.compscicenter.projects.lunch.model.User;
import ru.compscicenter.projects.lunch.tagger.MenuItemClustering;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;


public class PreferenceDeciderTest {

    @Test
    public void testRange() throws Exception {

        final File folder = new File("xml");
        List<Menu> menus = new ArrayList<>();
        MenuItemClustering clusterer = new MenuItemClustering(10, 100000, 0);
        List<MenuItem> loveList = new ArrayList<>();
        List<MenuItem> sample = new ArrayList<>();
        List<MenuItem> sampleSort;

        for (final File fileEntry : folder.listFiles()) {
            menus.addAll(MenuXmlParser.parseMenu(new FileInputStream(fileEntry)));
        }

        MenuKnowledge knowledge = new MenuKnowledge(menus);
        clusterer.fit(new ArrayList(knowledge.getList()));

//        for (int i = 0; i < clusterer.size(); i++) {
//            System.out.println("Cluster " + i);
//            for (MenuItemLocation menuItem : clusterer.getPoints(i)) {
//                System.out.println(menuItem.getMenuItem().getName() + " "
//                                + menuItem.getMenuItem().getPrice() + " "
//                                + menuItem.getMenuItem().getWeight());
//            }
//            System.out.println();
//        }

        int[] preferenceClusters = {1, 5, 9};
        System.out.println("Love List");
        for (int clusterInd : preferenceClusters) {
            loveList.add(clusterer.getPoints(clusterInd).get(1).getMenuItem());

            System.out.println(clusterer.getPoints(clusterInd).get(1).getMenuItem().getName());
        }
        System.out.println();

        User user = new User(1, loveList, new ArrayList<>());

        System.out.println("Sample");
        for (int clusterInd = 1; clusterInd < clusterer.size(); ++clusterInd) {
            sample.add(clusterer.getPoints(clusterInd).get(2).getMenuItem());

            System.out.println(String.valueOf(clusterInd) + ": " +
                    clusterer.getPoints(clusterInd).get(2).getMenuItem().getName());
        }
        System.out.println();

        PreferenceDecider preferenceDecider = new PreferenceDecider();
        sampleSort = preferenceDecider.range(sample, knowledge, user);

        for (MenuItem item : sampleSort) {
            System.out.println(item.getName());
        }

        List<MenuItem> expected = new ArrayList<>();
        for (int clusterInd : preferenceClusters) {
            expected.add(sample.get(clusterInd - 1));
        }

        List<MenuItem> actual = new ArrayList<>();
        for (int i = 0; i < preferenceClusters.length; ++i) {
            actual.add(sampleSort.get(i));
        }

        assert (actual.containsAll(expected));

    }

}