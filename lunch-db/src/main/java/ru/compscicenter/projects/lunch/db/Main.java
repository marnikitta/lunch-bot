package ru.compscicenter.projects.lunch.db;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.compscicenter.projects.lunch.db.service.MenuService;
import ru.compscicenter.projects.lunch.estimator.MenuXmlParser;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("hibernate-context.xml");
        MenuService menuService = (MenuService) context.getBean(MenuService.class);

        final File folder = new File("xml");

        for (final File fileEntry : folder.listFiles()) {
            menuService.saveMenu(MenuXmlParser.parseMenu(fileEntry.getPath()).get(0));
        }
    }
}
