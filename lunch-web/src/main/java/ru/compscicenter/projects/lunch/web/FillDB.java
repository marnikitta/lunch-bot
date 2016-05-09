package ru.compscicenter.projects.lunch.web;

import ru.compscicenter.projects.lunch.estimator.MenuXmlParser;
import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.model.MenuItem;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FillDB {
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
    }
}
