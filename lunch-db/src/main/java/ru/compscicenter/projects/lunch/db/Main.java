package ru.compscicenter.projects.lunch.db;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.compscicenter.projects.lunch.db.service.DeciderService;
import ru.compscicenter.projects.lunch.db.service.MenuService;
import ru.compscicenter.projects.lunch.db.service.UserService;
import ru.compscicenter.projects.lunch.estimator.MenuXmlParser;
import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.model.MenuItem;

import java.io.BufferedInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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
        DeciderService deciderService = context.getBean(DeciderService.class);

        userService.makeRandomUser(1);
        userService.makeRandomUser(2);
        userService.makeRandomUser(3);

        final long id = 1;
        final Calendar calendar = new GregorianCalendar(2016, 2, 2);
        List<MenuItem> items = deciderService.getForDate(id, calendar);
        System.out.println(items);
    }
}
