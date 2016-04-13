package ru.compscicenter.projects.lunch.db;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.compscicenter.projects.lunch.db.service.MenuService;
import ru.compscicenter.projects.lunch.db.service.UserService;
import ru.compscicenter.projects.lunch.estimator.MenuXmlParser;
import ru.compscicenter.projects.lunch.model.Menu;

import java.io.BufferedInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        MenuService menuService = context.getBean(MenuService.class);

        Path path = Paths.get("xml");

        Files.walk(path, 1).forEach(filePath -> {
            if (Files.isRegularFile(filePath)) {
                try {
                    Menu menu = MenuXmlParser.parseMenu(new BufferedInputStream(Files.newInputStream(filePath))).get(0);
                    menuService.saveMenu(menu);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        UserService userService = context.getBean(UserService.class);

        userService.makeRandomUser(1);
        userService.makeRandomUser(2);
        userService.makeRandomUser(3);
    }
}
