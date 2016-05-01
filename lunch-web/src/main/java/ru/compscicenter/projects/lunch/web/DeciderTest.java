package ru.compscicenter.projects.lunch.web;

import ru.compscicenter.projects.lunch.estimator.Decider;
import ru.compscicenter.projects.lunch.estimator.MenuXmlParser;
import ru.compscicenter.projects.lunch.estimator.impl.PreferenceDecider;
import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.model.MenuKnowledge;
import ru.compscicenter.projects.lunch.model.User;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeciderTest {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("xml");
        List<MenuItem> list = new ArrayList<>();
        List<Menu> menus = new ArrayList<>();

        Files.walk(path, 1).forEach(filePath -> {
            if (Files.isRegularFile(filePath)) {
                try {
                    Menu menu = MenuXmlParser.parseMenu(new BufferedInputStream(Files.newInputStream(filePath))).get(0);
                    list.addAll(menu.getItemsCopy());
                    menus.add(menu);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        MenuKnowledge menuKnowledge = new MenuKnowledge(menus);
        User user = new User(12);
        user.getLoveList().addAll(list.subList(0, 20));

        long start = System.nanoTime();
        Random rd = new Random();
        Decider decider = new PreferenceDecider();
        for (int i = 0; i < 30; ++i) {
            List<MenuItem> result = decider.range(menus.get(rd.nextInt(menus.size())).getItemsCopy(), menuKnowledge, user);
            result.stream().limit(5).map(MenuItem::getName).forEach(System.out::println);
        }
        System.out.println((System.nanoTime() - start) / 1e9);
    }
}
